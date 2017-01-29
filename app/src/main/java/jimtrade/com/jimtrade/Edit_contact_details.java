package jimtrade.com.jimtrade;

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
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Edit_contact_details extends Activity {

    EditText e1,e2,e3,e4,e5,e6,e7,e8,e9,e10,e11,e12;

    Button submit;

    Dialog dialog;

    // Set the limit of objects to show
    private int i = 0;

    public static final String MyPREFERENCES_DETAILS = "MyPrefsDetails" ;
    SharedPreferences sharedpreferences_details;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String MyPREFERENCES1 = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    SharedPreferences.Editor editor1;

    // ALL JSON node names
    private static final String TAG_NOTIFY = "editdetails";

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_edit_contact_details);

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

            sharedpreferences_details = getSharedPreferences(MyPREFERENCES_DETAILS, Context.MODE_PRIVATE);
        String name = sharedpreferences_details.getString("person", null);
        String title = sharedpreferences_details.getString("title", null);
        String company = sharedpreferences_details.getString("comp", null);
        String address = sharedpreferences_details.getString("adds", null);
        String city = sharedpreferences_details.getString("city", null);
        String state = sharedpreferences_details.getString("stat", null);
        String zip = sharedpreferences_details.getString("zip", null);
        final String country = sharedpreferences_details.getString("country", null);
        String tele = sharedpreferences_details.getString("tele", null);
        String mobnumber = sharedpreferences_details.getString("mobile", null);
        String faxnumber = sharedpreferences_details.getString("fax", null);
        String webs = sharedpreferences_details.getString("web", null);

        e1 = (EditText) findViewById(R.id.cont_person_name);
        e2 = (EditText) findViewById(R.id.title_job);
        e3 = (EditText) findViewById(R.id.comp_name);
        e4 = (EditText) findViewById(R.id.addr);
        e5 = (EditText) findViewById(R.id.city);
        e6 = (EditText) findViewById(R.id.state);
        e7 = (EditText) findViewById(R.id.pin);
        e8 = (EditText) findViewById(R.id.country);
        e9 = (EditText) findViewById(R.id.tel);
        e10 = (EditText) findViewById(R.id.mobnum);
        e11 = (EditText) findViewById(R.id.faxnum);
        e12 = (EditText) findViewById(R.id.website);

        submit = (Button) findViewById(R.id.submit);

        if(name.isEmpty())
        {
            e1.setText("NA");
        }
        else
        {
            e1.setText(name);
        }

        if(title.isEmpty())
        {
            e2.setText("NA");
        }
        else
        {
            e2.setText(title);
        }

        if(company.isEmpty())
        {
            e3.setText("NA");
        }
        else
        {
            e3.setText(company);
        }

        if(address.isEmpty())
        {
            e4.setText("NA");
        }
        else
        {
            e4.setText(address);
        }

        if(city.isEmpty())
        {
            e5.setText("NA");
        }
        else
        {
            e5.setText(city);
        }

        if(state.isEmpty())
        {
            e6.setText("NA");
        }
        else
        {
            e6.setText(state);
        }

        if(zip.isEmpty())
        {
            e7.setText("NA");
        }
        else
        {
            e7.setText(zip);
        }

        if(country.isEmpty())
        {
            e8.setText("NA");
        }
        else
        {
            e8.setText(country);
        }

        if(tele.isEmpty())
        {
        e9.setText("NA");
        }
        else
        {
            e9.setText(tele);
        }

        if(mobnumber.isEmpty())
        {
            e10.setText("NA");
        }
        else
        {
            e10.setText(mobnumber);
        }

        if(faxnumber.isEmpty())
        {
            e11.setText("NA");
        }
        else
        {
            e11.setText(faxnumber);
        }

        if(webs.isEmpty())
        {
            e12.setText("NA");
        }
        else
        {
            e12.setText(webs);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    final String member_id = sharedpreferences.getString("mem_id", null);

                    String person_name = e1.getText().toString();
                    String designation = e2.getText().toString();
                    String company_name = e3.getText().toString();
                    String addre = e4.getText().toString();
                    String city_name = e5.getText().toString();
                    String state_name = e6.getText().toString();
                    String pincode = e7.getText().toString();
                    String country_name = e8.getText().toString();
                    String tele_no = e9.getText().toString();
                    String mobile_no = e10.getText().toString();
                    String fax_number = e11.getText().toString();
                    String website_name = e12.getText().toString();

                    byte[] data1 = person_name.getBytes("UTF-8");
                    String a1 = Base64.encodeToString(data1, Base64.NO_WRAP);

                    byte[] data2 = designation.getBytes("UTF-8");
                    String a2 = Base64.encodeToString(data2, Base64.NO_WRAP);

                    byte[] data3 = company_name.getBytes("UTF-8");
                    String a3 = Base64.encodeToString(data3, Base64.NO_WRAP);

                    byte[] data4 = addre.getBytes("UTF-8");
                    String a4 = Base64.encodeToString(data4, Base64.NO_WRAP);

                    byte[] data5 = city_name.getBytes("UTF-8");
                    String a5 = Base64.encodeToString(data5, Base64.NO_WRAP);

                    byte[] data6 = state_name.getBytes("UTF-8");
                    String a6 = Base64.encodeToString(data6, Base64.NO_WRAP);

                    byte[] data7 = pincode.getBytes("UTF-8");
                    String a7 = Base64.encodeToString(data7, Base64.NO_WRAP);

                    byte[] data8 = country_name.getBytes("UTF-8");
                    String a8 = Base64.encodeToString(data8, Base64.NO_WRAP);

                    byte[] data9 = tele_no.getBytes("UTF-8");
                    String a9 = Base64.encodeToString(data9, Base64.NO_WRAP);

                    byte[] data10 = mobile_no.getBytes("UTF-8");
                    String a10 = Base64.encodeToString(data10, Base64.NO_WRAP);

                    byte[] data11 = fax_number.getBytes("UTF-8");
                    String a11 = Base64.encodeToString(data11, Base64.NO_WRAP);

                    byte[] data12 = website_name.getBytes("UTF-8");
                    String a12 = Base64.encodeToString(data12, Base64.NO_WRAP);

                    String args = member_id + "," + a3 + "," + a1 + "," + a2 + "," + a4 + "," + a5 + "," + a6 + "," + a7 + "," + a8 + "," + a9 + "," + a10 + "," + a11 + "," + a12 ;

                    new EditDetails().execute("http://m.jimtrade.com/mobileapp.svc/membereditcontactdetails/" + args);
                }
                catch (UnsupportedEncodingException e) {

                }
            }
        });

        }catch(NullPointerException ne)
        {
            Toast.makeText(getApplicationContext(), "Check Internet Connectivity!!", Toast.LENGTH_LONG).show();
        }
    }

    class EditDetails extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // custom dialog
            dialog = new Dialog(Edit_contact_details.this);
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
            dialog.dismiss();

            super.onPostExecute(response);

            try {
                if (isNetworkStatusAvialable (getApplicationContext())) {

                    JSONArray categories = new JSONArray(response);

                    if (categories != null) {
                        // looping through All categories

                        for (i = 0; i <= categories.length(); i++) {

                            JSONObject c = categories.getJSONObject(i);

                            // Storing each json item values in variable
                            String notifier = c.getString(TAG_NOTIFY);

                            if (notifier.contains("success"))
                            {
                                Intent intent = new Intent(Edit_contact_details.this,Contact_Details.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                                startActivity(intent);
                            }
                        }
                    } else {

                        Log.d("Categories: ", "null");
                    }
                }

                else
                {
                    Toast.makeText(getApplication(), "Check Internet Connectivity!!", Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                Log.d("Test", "Couldn't successfully parse the JSON response!");
            } catch (NullPointerException ne) {
                Toast.makeText(getApplicationContext(), "Check Internet Connectivity!!", Toast.LENGTH_LONG).show();
            }

            dialog.dismiss();

        }
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
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                editor.apply();

                // create intent to perform web search for this planet
                Intent intent4 = new Intent(this, HomeScreen.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent4);

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
}
