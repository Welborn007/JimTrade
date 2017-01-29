package jimtrade.com.jimtrade;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jimtrade.com.jimtrade.Inquiry.ForgotPassword;
import jimtrade.com.jimtrade.OTP_Process.OTP;


public class LoginActivity extends Activity {


    TextView forgot,new_member;

    private Button mSubmit, Skip;

    Dialog dialog;

    String s1,s2,base64,base642;

    private static final String TAG_EMAIL = "mem_email";
    private static final String TAG_ADDRESS = "md_address";
    private static final String TAG_COMPANY = "md_company";
    private static final String TAG_CONTACT_PERSON = "md_contactperson";
    private static final String TAG_COUNTRY = "md_country";
    private static final String TAG_DESIGNATION = "md_designation";
    private static final String TAG_MOBILE = "md_mobile";
    private static final String TAG_PHONE = "md_phonne";
    private static final String TAG_ID = "mem_id";
    private static final String TAG_PASSWORD = "mem_password";
    private static final String TAG_POINTS = "mem_points";
    private static final String TAG_MAIL_ENCRYPT = "mem_encryptemail";
    private static final String TAG_DIFFERENTIATOR = "mem_supplier";
    private static final String TAG_VERIFIED_NUMBER = "mem_mobileno";
    private static final String TAG_NUMBER_STATUS = "mem_mobilenoverified";
    private static final String TAG_EMAIL_CONFIRM = "emailconfirmed";

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
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_login);

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

        Skip = (Button) findViewById(R.id.skip);

        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, HomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        forgot = (TextView) findViewById(R.id.forgotpassword);
        new_member = (Button) findViewById(R.id.new_mem);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        new_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterNew.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        mSubmit = (Button) findViewById(R.id.button1);

        final EditText user = (EditText) findViewById(R.id.edt1);
        final EditText pass = (EditText) findViewById(R.id.edt2);

        try {
            mSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    try {

                        if (isEmptyField(user)) return;
                        if (isEmptyField(pass)) return;

                        // make a HTTP request
                        s1 = user.getText().toString().trim();
                        s2 = pass.getText().toString().trim();

                        if (!isEmailValid(s1)) return;


                        // Sending side
                        byte[] data = s1.getBytes("UTF-8");
                        byte[] data1 = s2.getBytes("UTF-8");
                        base64 = Base64.encodeToString(data, Base64.NO_WRAP);
                        base642 = Base64.encodeToString(data1, Base64.NO_WRAP);

                        new AttemptLogin().execute("http://m.jimtrade.com/mobileapp.svc/memberdetails/" + base64 + "," + base642);

                    } catch (UnsupportedEncodingException e) {

                    }


                }
            });

        }catch (NullPointerException ne)
        {
            Toast.makeText(LoginActivity.this,"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
        }
    }

    public boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;


        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;

        }
        else {
            Toast.makeText(LoginActivity.this, "Enter proper email-id", Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String s1 = sharedpreferences.getString("welcome", null);

        if(s1 == "welcome") {

            editor = sharedpreferences.edit();
            editor.remove("welcome");
            editor.commit();

            Skip.setVisibility(View.VISIBLE);

            getActionBar().setTitle(null);
            getActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(false);

            getMenuInflater().inflate(R.menu.search, menu);
        }
        else
        {
            getMenuInflater().inflate(R.menu.search, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_home:
                // create intent to perform web search for this planet
                Intent intent = new Intent(this,HomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    private boolean isEmptyField (EditText editText){
        boolean result = editText.getText().toString().length() <= 0;
        if (result)
            Toast.makeText(LoginActivity.this, "Please Enter all details!", Toast.LENGTH_SHORT).show();
        return result;
    }

    class AttemptLogin extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // custom dialog
            dialog = new Dialog(LoginActivity.this);
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
                if (isNetworkStatusAvialable (LoginActivity.this)) {

                    JSONArray categories = new JSONArray(response);

                    if(categories.length() <= 0 )
                    {
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                    }

                    if (categories != null) {
                        // looping through All categories

                        for (int i = 0; i <= categories.length(); i++) {

                            JSONObject c = categories.getJSONObject(i);

                            // Storing each json item values in variable
                            String email = c.getString(TAG_EMAIL);
                            String password = c.getString(TAG_PASSWORD);
                            String company = c.getString(TAG_COMPANY);
                            String contact_person = c.getString(TAG_CONTACT_PERSON);
                            String designation = c.getString(TAG_DESIGNATION);
                            String address = c.getString(TAG_ADDRESS);
                            String tel = c.getString(TAG_PHONE);
                            String member_id = c.getString(TAG_ID);
                            String encrypt = c.getString(TAG_MAIL_ENCRYPT);
                            String points = c.getString(TAG_POINTS);
                            String differentiator = c.getString(TAG_DIFFERENTIATOR);
                            String verified_no = c.getString(TAG_VERIFIED_NUMBER);
                            String status_no = c.getString(TAG_NUMBER_STATUS);
                            String country = c.getString(TAG_COUNTRY);
                            String email_confirmation = c.getString(TAG_EMAIL_CONFIRM);

                            String Country_small = country.substring(0,1).toUpperCase() + country.substring(1).toLowerCase();

                            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            sharedpreferences1 = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
                            final String z1 = sharedpreferences1.getString("identifier",null);


                            if(status_no.contains("Verfied") && z1.contains("3"))
                            {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this,My_JimTrade.class);
                                finish();
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else if(status_no.contains("Unverfied") && Country_small.contains("India"))
                            {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this,OTP.class);
                                finish();
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else if(status_no.contains("Verfied") && z1.contains("2"))
                            {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this,Post_Inquiry.class);
                                finish();
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else if(status_no.contains("Verfied") && z1.contains("home"))
                            {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                Intent intent1 = new Intent(LoginActivity.this, HomeScreen.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                                startActivity(intent1);
                            }
                            else if(status_no.contains("Verfied") && z1.contains("Jim"))
                            {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                Intent intent1 = new Intent(LoginActivity.this, My_JimTrade.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                                startActivity(intent1);
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this,My_JimTrade.class);
                                finish();
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }


                            editor.putString("Email",email);
                            editor.putString("Password",password);
                            editor.putString("company",company);
                            editor.putString("contact_person",contact_person);
                            editor.putString("designation",designation);
                            editor.putString("address",address);
                            editor.putString("telephone",tel);
                            editor.putString("mem_id",member_id);
                            editor.putString("encrpyt",encrypt);
                            editor.putString("points",points);
                            editor.putString("differentiator",differentiator);
                            editor.putString("verifiedno",verified_no);
                            editor.putString("status",status_no);
                            editor.putString("country",Country_small);
                            editor.putString("confirm",email_confirmation);
                            editor.apply();
                            editor.commit();

                        }
                    } else {

                    }
                }

                else
                {
                    Toast.makeText(LoginActivity.this,"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Log.d("Test", "Couldn't successfully parse the JSON response!");
            } catch (NullPointerException ne) {
                Toast.makeText(LoginActivity.this, "The Email or password field is empty", Toast.LENGTH_LONG).show();
            }

            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(LoginActivity.this,HomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
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
