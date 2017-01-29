package jimtrade.com.jimtrade.Mails;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jimtrade.com.jimtrade.AboutUs;
import jimtrade.com.jimtrade.ContactUs;
import jimtrade.com.jimtrade.FAQs;
import jimtrade.com.jimtrade.Feedback;
import jimtrade.com.jimtrade.HomeScreen;
import jimtrade.com.jimtrade.LoginActivity;
import jimtrade.com.jimtrade.My_JimTrade;
import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.RegisterNew;
import jimtrade.com.jimtrade.adapter.MailAdapter;
import jimtrade.com.jimtrade.app.AppController;
import jimtrade.com.jimtrade.model.Mail_model;

public class Receive extends Activity {

    private static final String TAG = Sent.class.getSimpleName();

    private static final String TAG_BUYER_NAME = "buyer";
    private static final String TAG_SUBJECT = "subject";
    private static final String TAG_MESSAGE = "inquirymsg";
    private static final String TAG_DATE = "inquirydate";
    private static final String TAG_PRODUCT_NAME = "product";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_DESIGNATION = "designation";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_TELEPHONE = "tel";
    private static final String TAG_CONTACT_PERSON = "contactperson";

    Dialog dialog;
    private List<Mail_model> receiveList = new ArrayList<Mail_model>();
    private ListView listView;
    private MailAdapter adapter;

    private int current_page = 0;
    int mPreLast;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String MyPREFERENCES1 = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    SharedPreferences.Editor editor1;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_receive);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        actionBar.setCustomView(cView);

        cView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getActionBar().setTitle(null);
        getActionBar().setDisplayShowHomeEnabled(true);

        try {
            listView = (ListView) findViewById(R.id.list1);
            adapter = new MailAdapter(this, receiveList);
            listView.setAdapter(adapter);

            int currentPosition = listView.getFirstVisiblePosition();
            listView.setSelectionFromTop(currentPosition + 1, 0);

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int lastItem = firstVisibleItem + visibleItemCount;
                    if (lastItem == totalItemCount) {
                        if (mPreLast != lastItem) {
                            mPreLast = lastItem;
                            new LoadInquiries().execute();
                        }
                    }
                }
            });

            new LoadInquiries().execute();
        }catch (NullPointerException npe)
        {

        }
    }

    class LoadInquiries extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // custom dialog
            dialog = new Dialog(Receive.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progressdialog);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
        }


        protected String doInBackground(String... args) {

            URL url;
            HttpURLConnection urlConnection = null;
            String response = new String();

            try {
                url = new URL(args[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    String responseString = readStream(urlConnection.getInputStream());
                    Log.v("CatalogClient", responseString);
                    response = new String(responseString);
                }else{
                    Log.v("CatalogClient", "Response code:"+ responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }

            return response;
        }


        protected void onPostExecute(final String response) {

            super.onPostExecute(response);

            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String supplierId = sharedpreferences.getString("differentiator", null);

            current_page += 1;
            final String url = "http://m.jimtrade.com/mobileapp.svc/getmemberinbox/" + supplierId + "," + current_page;

            JsonArrayRequest movieReq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {


                            try
                            {
                                if (isNetworkStatusAvialable (getApplicationContext())) {

                                    // Parsing json
                                    for (int i = 0; i < response.length() && response.length() != 0; i++)
                                    {
                                        JSONObject c = response.getJSONObject(i);

                                        final Mail_model receive = new Mail_model();

                                        receive.setName(c.getString(TAG_BUYER_NAME));
                                        receive.setAddress(c.getString(TAG_ADDRESS));
                                        receive.setDate(c.getString(TAG_DATE));
                                        receive.setContactperson(c.getString(TAG_CONTACT_PERSON));
                                        receive.setEmail(c.getString(TAG_EMAIL));
                                        receive.setMessage(c.getString(TAG_MESSAGE));
                                        receive.setSubject(c.getString(TAG_SUBJECT));
                                        receive.setTelephone(c.getString(TAG_TELEPHONE));
                                        receive.setProduct(c.getString(TAG_PRODUCT_NAME));
                                        receive.setDesignation(c.getString(TAG_DESIGNATION));

                                        receiveList.add(receive);

                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent intent = new Intent(getApplicationContext(), Receive_body.class);

                                                Mail_model receive = receiveList.get(i);

                                                String name = receive.getName().toString();
                                                String subject = receive.getSubject().toString();
                                                String message = receive.getMessage().toString();
                                                String date = receive.getDate().toString();
                                                String product = receive.getProduct().toString();
                                                String address = receive.getAddress().toString();
                                                String designation = receive.getDesignation().toString();
                                                String email = receive.getEmail().toString();
                                                String telephone = receive.getTelephone().toString();
                                                String contatc_person = receive.getContactperson().toString();

                                                intent.putExtra("name",name);
                                                intent.putExtra("subject",subject);
                                                intent.putExtra("message",message);
                                                intent.putExtra("date",date);
                                                intent.putExtra("product",product);
                                                intent.putExtra("address",address);
                                                intent.putExtra("design",designation);
                                                intent.putExtra("email",email);
                                                intent.putExtra("tele",telephone);
                                                intent.putExtra("cont_per",contatc_person);

                                                startActivity(intent);
                                            }
                                        });
                                    }

                                }
                                else
                                {
                                    Toast.makeText(getBaseContext(), "Check Internet Connectivity!!", Toast.LENGTH_LONG).show();
                                    return;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (NullPointerException ne) {
                                Toast.makeText(getBaseContext(), "Check Internet Connectivity!!", Toast.LENGTH_LONG).show();
                            }

                            dialog.dismiss();

                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    dialog.dismiss();
                }
            });

            movieReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(movieReq);
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String s1 = sharedpreferences.getString("Email",null);

        if(s1 == null) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_login, menu);
        }
        else
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_logout, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.MyJimtrade:
                // create intent to perform web search for this planet
                Intent intent = new Intent(this, My_JimTrade.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("hello","move");
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                editor = sharedpreferences.edit();
                editor.putString("hello","move");
                editor.commit();
                startActivity(intent);
                return true;

            case R.id.aboutus:
                Intent intent9 = new Intent(this,AboutUs.class);
                intent9.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent9);
                return true;

            case R.id.feedback:
                Intent intent5 = new Intent(this,Feedback.class);
                intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent5);
                return true;

            case  R.id.contact:
                Intent intent6 = new Intent(this,ContactUs.class);
                intent6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent6);
                return true;

            case R.id.faq:
                Intent intent7 = new Intent(this,FAQs.class);
                intent7.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent7);
                return true;

            case R.id.action_home:
                Intent intent2 = new Intent(this, HomeScreen.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                return true;

            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;

            case R.id.Login:

                Intent intent1 = new Intent(this, LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);

                sharedpreferences1 = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
                editor1 = sharedpreferences1.edit();

                String identifier = "Jim";
                editor1.putString("identifier", identifier);
                editor1.commit();
                return true;

            case R.id.Register:

                Intent intent3 = new Intent(this, RegisterNew.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent3);
                return true;

            case R.id.Logout:

                // create intent to perform web search for this planet
                Intent intent4 = new Intent(this, HomeScreen.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent4);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                editor.apply();

                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }


    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
                if(netInfos.isConnected())
                    return true;
        }
        return false;
    }

}
