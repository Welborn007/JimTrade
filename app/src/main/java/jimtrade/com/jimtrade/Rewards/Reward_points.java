package jimtrade.com.jimtrade.Rewards;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;

import jimtrade.com.jimtrade.AboutUs;
import jimtrade.com.jimtrade.ContactUs;
import jimtrade.com.jimtrade.FAQs;
import jimtrade.com.jimtrade.Feedback;
import jimtrade.com.jimtrade.HomeScreen;
import jimtrade.com.jimtrade.LoginActivity;
import jimtrade.com.jimtrade.My_JimTrade;
import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.RegisterNew;
import jimtrade.com.jimtrade.SubCategoryActivity;

public class Reward_points extends ListActivity {

    // ALL JSON node names
    private static final String TAG_ACTIVITY = "activity";
    private static final String TAG_BALANCE_POINTS = "balancepoints";
    private static final String TAG_POINTS = "points";
    private static final String TAG_REDEEM_POINTS = "redeemedpoints";
    private static final String TAG_TOTAL_POINTS = "totalpoints";

    ListView lv;
    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;
    Dialog dialog;

    TextView s1,s2,s3;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String MyPREFERENCES1 = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    SharedPreferences.Editor editor1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_reward_points);

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

        s1 = (TextView) findViewById(R.id.Tv2);
        s2 = (TextView) findViewById(R.id.Tv4);
        s3 = (TextView) findViewById(R.id.Tv6);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String s3 = sharedpreferences.getString("mem_id", null);

        productList = new ArrayList<HashMap<String, String>>();
        new LoadRewards().execute("http://m.jimtrade.com/mobileapp.svc/memberactivitypoints/" + s3);
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

    class LoadRewards extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // custom dialog
            dialog = new Dialog(Reward_points.this);
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

                        for (int i = 0; i <= categories.length(); i++) {

                            JSONObject c = categories.getJSONObject(i);

                            // Storing each json item values in variable
                            String Activity = c.getString(TAG_ACTIVITY);
                            String points = c.getString(TAG_POINTS);
                            String balance_points = c.getString(TAG_BALANCE_POINTS);
                            String redeem_points = c.getString(TAG_REDEEM_POINTS);
                            String total_points = c.getString(TAG_TOTAL_POINTS);

                            s1.setText(total_points);
                            s2.setText(redeem_points);
                            s3.setText(balance_points);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_ACTIVITY, Activity);
                            map.put(TAG_POINTS, points);


                            // adding HashList to ArrayList
                            productList.add(map);

                            /**
                             * Updating parsed JSON data into ListView
                             * */
                            ListAdapter adapter = new SimpleAdapter(
                                    Reward_points.this, productList, R.layout.rewards_list, new String[]{
                                    TAG_ACTIVITY, TAG_POINTS}, new int[]{
                                    R.id.item1, R.id.item2});

                            // updating listview
                            setListAdapter(adapter);
                            lv = getListView();
                            lv.setEnabled(false);
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
