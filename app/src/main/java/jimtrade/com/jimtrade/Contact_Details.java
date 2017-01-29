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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
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

public class Contact_Details extends Activity {

    TextView c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12;

    TextView z1,z2,z3,z4,z5,z6,z7,z8,z9,z10,z11,z12;

    ImageButton imageButton;

    Dialog dialog;

    // Set the limit of objects to show
    private int i = 0;

    // ALL JSON node names
    private static final String TAG_CONTACT_PERSON = "md_contactperson";
    private static final String TAG_DESIGNATION = "md_designation";
    private static final String TAG_COMPANY_NAME = "md_company";
    private static final String TAG_ADDRESS = "md_address";
    private static final String TAG_CITY = "md_city";
    private static final String TAG_STATE = "md_state";
    private static final String TAG_PIN_CODE = "md_pin";
    private static final String TAG_COUNTRY = "md_country";
    private static final String TAG_TELE = "md_phonne";
    private static final String TAG_MOBILE = "md_mobile";
    private static final String TAG_FAX = "md_fax";
    private static final String TAG_WEBSITE = "md_website";

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String MyPREFERENCES1 = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    SharedPreferences.Editor editor1;

    public static final String MyPREFERENCES_DETAILS = "MyPrefsDetails" ;
    SharedPreferences sharedpreferences_details;
    SharedPreferences.Editor editor_details;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_contact__details);

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

        c1 = (TextView) findViewById(R.id.cont_person);
        c2 = (TextView) findViewById(R.id.designation_title);
        c3 = (TextView) findViewById(R.id.company_name);
        c4 = (TextView) findViewById(R.id.Address);
        c5 = (TextView) findViewById(R.id.City);
        c6 = (TextView) findViewById(R.id.State);
        c7 = (TextView) findViewById(R.id.pincode);
        c8 = (TextView) findViewById(R.id.Country);
        c9 = (TextView) findViewById(R.id.Telephone_No);
        c10 = (TextView) findViewById(R.id.Mobile_No);
        c11 = (TextView) findViewById(R.id.Fax);
        c12 = (TextView) findViewById(R.id.Website);

        z1 = (TextView) findViewById(R.id.c1);
        z2 = (TextView) findViewById(R.id.c2);
        z3 = (TextView) findViewById(R.id.c3);
        z4 = (TextView) findViewById(R.id.c4);
        z5 = (TextView) findViewById(R.id.c5);
        z6 = (TextView) findViewById(R.id.c6);
        z7 = (TextView) findViewById(R.id.c7);
        z8 = (TextView) findViewById(R.id.c8);
        z9 = (TextView) findViewById(R.id.c9);
        z10 = (TextView) findViewById(R.id.c10);
        z11 = (TextView) findViewById(R.id.c11);
        z12 = (TextView) findViewById(R.id.c12);

        try {

            imageButton = (ImageButton) findViewById(R.id.editcontact);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Contact_Details.this, Edit_contact_details.class);
                    startActivity(intent);
                }
            });

            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String member_id = sharedpreferences.getString("mem_id", null);

            new LoadContactDetails().execute("http://m.jimtrade.com/mobileapp.svc/membercontactdetails/" + member_id);
        }catch (NullPointerException npe)
        {

        }
    }

    class LoadContactDetails extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // custom dialog
            dialog = new Dialog(Contact_Details.this);
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
                            String con_name = c.getString(TAG_CONTACT_PERSON);
                            String desig = c.getString(TAG_DESIGNATION);
                            String company = c.getString(TAG_COMPANY_NAME);
                            String addrs = c.getString(TAG_ADDRESS);
                            String city = c.getString(TAG_CITY);
                            String state = c.getString(TAG_STATE);
                            String zip = c.getString(TAG_PIN_CODE);
                            String county = c.getString(TAG_COUNTRY);
                            String telephone = c.getString(TAG_TELE);
                            String mob = c.getString(TAG_MOBILE);
                            String fax = c.getString(TAG_FAX);
                            String website = c.getString(TAG_WEBSITE);

                            c1.setText(con_name);
                            c2.setText(desig);
                            c3.setText(company);
                            c4.setText(addrs);
                            c5.setText(city);
                            c6.setText(state);
                            c7.setText(zip);
                            c8.setText(county);
                            c9.setText(telephone);
                            c10.setText(mob);
                            c11.setText(fax);
                            c12.setText(website);

                            sharedpreferences_details = getSharedPreferences(MyPREFERENCES_DETAILS, Context.MODE_PRIVATE);
                            editor_details = sharedpreferences_details.edit();
                            editor_details.putString("person",con_name);
                            editor_details.putString("title",desig);
                            editor_details.putString("comp",company);
                            editor_details.putString("adds",addrs);
                            editor_details.putString("city",city);
                            editor_details.putString("stat",state);
                            editor_details.putString("zip",zip);
                            editor_details.putString("country",county);
                            editor_details.putString("tele",telephone);
                            editor_details.putString("mobile",mob);
                            editor_details.putString("fax",fax);
                            editor_details.putString("web",website);
                            editor_details.commit();

                            if (con_name.isEmpty())
                            {
                                z1.setVisibility(View.GONE);
                                c1.setVisibility(View.GONE);
                            }

                            if (desig.isEmpty())
                            {
                                z2.setVisibility(View.GONE);
                                c2.setVisibility(View.GONE);
                            }

                            if (company.isEmpty())
                            {
                                z3.setVisibility(View.GONE);
                                c3.setVisibility(View.GONE);
                            }

                            if (addrs.isEmpty())
                            {
                                z4.setVisibility(View.GONE);
                                c4.setVisibility(View.GONE);
                            }

                            if (city.isEmpty())
                            {
                                z5.setVisibility(View.GONE);
                                c5.setVisibility(View.GONE);
                            }

                            if (state.isEmpty())
                            {
                                z6.setVisibility(View.GONE);
                                c6.setVisibility(View.GONE);
                            }

                            if (zip.isEmpty())
                            {
                                z7.setVisibility(View.GONE);
                                c7.setVisibility(View.GONE);
                            }

                            if (county.isEmpty())
                            {
                                z8.setVisibility(View.GONE);
                                c8.setVisibility(View.GONE);
                            }

                            if (telephone.isEmpty())
                            {
                                z9.setVisibility(View.GONE);
                                c9.setVisibility(View.GONE);
                            }

                            if (mob.isEmpty())
                            {
                                z10.setVisibility(View.GONE);
                                c10.setVisibility(View.GONE);
                            }

                            if (fax.isEmpty())
                            {
                                z11.setVisibility(View.GONE);
                                c11.setVisibility(View.GONE);
                            }

                            if (website.isEmpty())
                            {
                                z12.setVisibility(View.GONE);
                                c12.setVisibility(View.GONE);
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
