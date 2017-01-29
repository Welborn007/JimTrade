package jimtrade.com.jimtrade.OTP_Process;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.concurrent.TimeUnit;

import jimtrade.com.jimtrade.AboutUs;
import jimtrade.com.jimtrade.ContactUs;
import jimtrade.com.jimtrade.FAQs;
import jimtrade.com.jimtrade.Feedback;
import jimtrade.com.jimtrade.HomeScreen;
import jimtrade.com.jimtrade.LoginActivity;
import jimtrade.com.jimtrade.My_JimTrade;
import jimtrade.com.jimtrade.Post_Inquiry;
import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.RegisterNew;
import jimtrade.com.jimtrade.Send_Inquiry.Send_Inquiry_Product;
import jimtrade.com.jimtrade.Send_Inquiry.Send_Inquiry_Supplier;


public class OTPverify extends OTP {

    Button send, skip, resend;
    EditText Otp;

    TextView counter,number;

    IntentFilter filter1;
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    Dialog dialog;

    private static final String TAG_OTP_VALIDATION = "mobileotpverification";
    private static final String TAG_MOBILE = "mobileno";

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor,editor1;

    public static final String MyPREFERENCES1 = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;

    int i = 10; //declare this globally

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otpverify);

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

        send = (Button) findViewById(R.id.sendOTP);
        skip = (Button) findViewById(R.id.skip);
        Otp = (EditText) findViewById(R.id.otp);
        resend = (Button) findViewById(R.id.resendOTP);
        counter = (TextView) findViewById(R.id.counter);
        number = (TextView) findViewById(R.id.mobinumber);

        try {

            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            final String member_id = sharedpreferences.getString("mem_id", null);
            final String mobile = sharedpreferences.getString("mobilenumber", null);

            sharedpreferences1 = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
            final String z1 = sharedpreferences1.getString("identifier", null);

            number.setText(mobile);

            new CountDownTimer(61000, 1000) { // adjust the milli seconds here

                public void onTick(long millisUntilFinished) {
                    counter.setText("OTP will be received within : " + String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }

                public void onFinish() {
                    counter.setText("click on 'Resend OTP'");
                }
            }.start();


            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (isEmptyField(Otp)) return;

                    String otp = Otp.getText().toString().trim();

                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                    new OTPsend().execute("http://m.jimtrade.com/mobileapp.svc/membermobilenootpvarification/" + member_id + "," + mobile + "," + otp);
                }
            });

            resend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(OTPverify.this, "OTP re-send to number successfully", Toast.LENGTH_SHORT).show();
                    new OTPNumber().execute("http://m.jimtrade.com/mobileapp.svc/membermobilenootp/" + member_id + "," + mobile);
                }
            });

            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (z1.contains("0")) {
                        Intent intent = new Intent(OTPverify.this, Send_Inquiry_Product.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    } else if (z1.contains("1")) {
                        Intent intent = new Intent(OTPverify.this, Send_Inquiry_Supplier.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    } else if (z1.contains("2")){
                        Intent intent = new Intent(OTPverify.this, Post_Inquiry.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    }
                    else if (z1.contains("3")){
                        Intent intent = new Intent(OTPverify.this, My_JimTrade.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(OTPverify.this, HomeScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    }

                }
            });

            filter1 = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            registerReceiver(myReceiver, filter1);
        }catch (NullPointerException npe)
        {

        }

    }

    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();

            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            final String member_id = sharedpreferences.getString("mem_id",null);
            final String mobile = sharedpreferences.getString("mobilenumber", null);

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();

                        Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);


                        String otp = message.replaceAll("[^0-9]", "");

                        if(!otp.isEmpty())
                        {
                            new OTPsend().execute("http://m.jimtrade.com/mobileapp.svc/membermobilenootpvarification/" + member_id + "," + mobile + "," + otp);
                        }

                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" +e);

            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);

    }

    private boolean isEmptyField (EditText editText){
        boolean result = editText.getText().toString().length() <= 0;
        if (result)
            Toast.makeText(OTPverify.this, "Please Enter all details!", Toast.LENGTH_SHORT).show();
        return result;
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

    class OTPsend extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // custom dialog
            dialog = new Dialog(OTPverify.this);
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
                if (isNetworkStatusAvialable (OTPverify.this)) {

                    JSONArray categories = new JSONArray(response);

                    if (categories != null) {
                        // looping through All categories

                        for (int i = 0; i <= categories.length(); i++) {

                            JSONObject c = categories.getJSONObject(i);

                            // Storing each json item values in variable
                            String validation = c.getString(TAG_OTP_VALIDATION);
                            String mobilenumber = c.getString(TAG_MOBILE);

                            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            editor = sharedpreferences.edit();
                            editor.putString("verifiedno",mobilenumber);
                            editor.putString("status", validation);
                            editor.commit();


                            if(validation.contains("Success"))
                            {
                                sharedpreferences1 = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
                                final String z1 = sharedpreferences1.getString("identifier",null);

                                if(z1.contains("0"))
                                {
                                    Toast.makeText(OTPverify.this, "Mobile Number Verified", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(OTPverify.this,Send_Inquiry_Product.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    finish();
                                    startActivity(intent);
                                }
                                else if(z1.contains("1"))
                                {
                                    Toast.makeText(OTPverify.this, "Mobile Number Verified", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(OTPverify.this,Send_Inquiry_Supplier.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    finish();
                                    startActivity(intent);
                                }
                                else if (z1.contains("2")){
                                    Toast.makeText(OTPverify.this, "Mobile Number Verified", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(OTPverify.this, Post_Inquiry.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    finish();
                                    startActivity(intent);
                                }
                                else if (z1.contains("3")){
                                    Toast.makeText(OTPverify.this, "Mobile Number Verified", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(OTPverify.this, My_JimTrade.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    finish();
                                    startActivity(intent);
                                }
                                else if(z1.contains("home"))
                                {
                                    Toast.makeText(OTPverify.this, "Mobile Number Verified", Toast.LENGTH_LONG).show();
                                    Intent intent1 = new Intent(OTPverify.this, HomeScreen.class);
                                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    finish();
                                    startActivity(intent1);
                                }
                                else
                                {
                                    Toast.makeText(OTPverify.this, "Failure", Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    } else {

                    }
                }

                else
                {
                    Toast.makeText(OTPverify.this,"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Log.d("Test", "Couldn't successfully parse the JSON response!");
            } catch (NullPointerException ne) {
                Toast.makeText(OTPverify.this, "The field is empty", Toast.LENGTH_LONG).show();
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