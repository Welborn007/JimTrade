package jimtrade.com.jimtrade;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import jimtrade.com.jimtrade.Categories.Categories;
import jimtrade.com.jimtrade.Mails.Receive;
import jimtrade.com.jimtrade.Mails.Sent;
import jimtrade.com.jimtrade.OTP_Process.OTP;
import jimtrade.com.jimtrade.Rewards.Reward_points;

public class My_JimTrade extends Activity
{


    RelativeLayout relativeLayout;
    TextView button, verify;
    TextView rewards;

    private static final String username = "support@jimtrade.com";
    private static final String password = "Jupiter@336";

    TextView textView,textView1,textView2,textView3,textView4,textView5;

    TextView d1;

    TextView sent,recieve,Mobile,file,change,contactdetails,sample;

    RelativeLayout relmobile;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String MyPREFERENCES1 = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    SharedPreferences.Editor editor1;

    Dialog dialog;

    private static final String TAG_MEMBER_CONFIRMATION = "memconfirmed";
    private static final String TAG_INQUIRIES_SENT = "meminquiriesdone";
    private static final String TAG_INQUIRIES_RECEIVED = "meminquiriesreceived";
    private static final String TAG_MEMBER_POINTS = "mempoints";
    private static final String TAG_MEMBER_SUBSCRIBED = "memsubscribed";
    private static final String TAG_MEMBER_DIFFERENTIATOR = "memsupplier";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_my__jim_trade);

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

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        try {
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String s3 = sharedpreferences.getString("mem_id", null);
            new LoadData().execute("http://m.jimtrade.com/mobileapp.svc/myjimtrade/" + s3);
        }catch (NullPointerException npe)
        {

        }

        button = (TextView) findViewById(R.id.btn1);
        verify = (TextView) findViewById(R.id.btn2);

        sent = (TextView) findViewById(R.id.txt3);
        recieve = (TextView) findViewById(R.id.txt2);
        Mobile = (TextView) findViewById(R.id.mobinum);
        file = (TextView) findViewById(R.id.sample);
        change = (TextView) findViewById(R.id.changenum);
        textView5 = (TextView) findViewById(R.id.txt4);
        contactdetails = (TextView) findViewById(R.id.contactdetails);
        sample = (TextView) findViewById(R.id.sample_text);
        relmobile = (RelativeLayout) findViewById(R.id.mobileotp);
        rewards = (TextView) findViewById(R.id.txt5);

        String confirm = sharedpreferences.getString("confirm",null);
        String email = sharedpreferences.getString("Email", null);

        if(confirm.contains("1"))
        {
            d1 = (TextView) findViewById(R.id.txt1);
            d1.setText("Verified Email ID:");
            TextView address_conf = (TextView) findViewById(R.id.confirm_email12);
            address_conf.setVisibility(View.VISIBLE);
            address_conf.setText(email);
            button.setVisibility(View.GONE);
        }


        try {

            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String mobile = sharedpreferences.getString("verifiedno", null);
            String validation = sharedpreferences.getString("status", null);
            String country = sharedpreferences.getString("country", null);


            if(country.contains("India"))
            {
                relmobile.setVisibility(View.VISIBLE);
            }

            if(mobile.matches("[0-9]+"))
            {
                Mobile.setText(mobile);
                Mobile.setVisibility(View.VISIBLE);
                file.setVisibility(View.VISIBLE);
                change.setVisibility(View.VISIBLE);
            }

            if(validation.contains("Verfied") || validation.contains("Success"))
            {
                verify.setVisibility(View.GONE);
                sample.setVisibility(View.GONE);
            }

            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(My_JimTrade.this, OTP.class);
                    startActivity(intent);
                }
            });

            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(My_JimTrade.this, OTP.class);
                    startActivity(intent);
                    sharedpreferences1 = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
                    editor1 = sharedpreferences1.edit();

                    String identifier = "home";
                    editor1.putString("identifier", identifier);
                    editor1.commit();
                }
            });

            sent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(My_JimTrade.this, Sent.class);
                    startActivity(intent);
                }
            });

            contactdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(My_JimTrade.this, Contact_Details.class);
                    startActivity(intent);
                }
            });

            recieve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(My_JimTrade.this, Receive.class);
                    startActivity(intent);
                }
            });

            textView5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(My_JimTrade.this, Categories.class);
                    startActivity(intent);
                }
            });

            rewards.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // make a HTTP request
                    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    String contact_person = sharedpreferences.getString("contact_person", null);
                    String encrypt = sharedpreferences.getString("encrpyt", null);
                    String email = sharedpreferences.getString("Email", null);
                    String password = sharedpreferences.getString("Password", null);

                    final String email1 = "machadowelborn@gmail.com";
                    final String subject = "Your Registration at JimTrade.com";

                    final String message = "<html>\n" +
                            "       <head>\n" +
                            "       <meta content='text/html; charset=iso-8859-1' http-equiv='Content-Type'>\n" +
                            "       <title>India Business Directory, Indian Suppliers Directory, Indian Products Directory from India</title>\n" +
                            "       <style type='text/css'>\n" +
                            "       <!--\n" +
                            "       body,td,p,th{font-size:13px;color:#000000;font-family:Arial, Helvetica, sans-serif;line-height:130%;}\n" +
                            "       -->\n" +
                            "       </style>\n" +
                            "       </head>\n" +
                            "       <body bgcolor='#ffffff'>\n" +
                            "       <table width='600' cellspacing='0' border='0' cellpadding='0' align='center' style='margin-bottom:6px;'>\n" +
                            "       <tr> <td width='600' align='center' style='font-size:15;color:#0F54AF;font-family:Arial, Helvetica, sans-serif;'>\n" +
                            "       <a href='http://www.jimtrade.com'><img src='http://www.jimtrade.com/images_new/jimtrade_logo.jpg' width='250' height='80' border='0' title='JimTrade.com : India`s Largest Online Business Directory' alt='JimTrade.com : India`s Largest Online Business Directory'></a><br>\n" +
                            "       <b>India's Largest Online Business Directory</b>\n" +
                            "       </td></tr></table>\n" +
                            "\n" +
                            "\n" +
                            "       <table width='600' cellspacing='0' border='0' cellpadding='0' align='center'>\n" +
                            "<tr>" + "<td valign='top'  bgcolor='#E7EFF8'  width='600' height='10' style='\n" +
                            "       border-left:solid #6DA6D6 1px;border-right:solid #6DA6D6 1px;border-top:solid #6DA6D6 1px;word-break:break-all;'>&nbsp;\n" +
                            "       </td>\n" +
                            "       </tr>\n" +
                            "       <td valign='top'   bgcolor='#E7EFF8' width='600' height='41' style='border-left:solid #6DA6D6 1px; border-right:solid #6DA6D6 1px; padding:0 0; word-break:break-all;'>\n" +
                            "       <div align='center' style='padding:6px 0px; font-size:22px; font-family:Arial, Helvetica, sans-serif;line-height:22px; color:#206D9F;'>Your registration at JimTrade.com-Please Confirm</div>\n" +
                            "       </td>\n" +
                            "       </tr>\n" +
                            "       <tr>\n" +
                            "       <td width='600px' style='border-left:solid #6DA6D6 1px; border-right:solid #6DA6D6 1px; padding:12px;'>\n" +
                            "\n" +
                            "      \n" +
                            "           \n" +
                            "      \n" +
                            "           <div style='font-size:13px; font-family:Arial, Helvetica, sans-serif;'>Dear " + contact_person + ",</strong>\n" +
                            "     \n" +
                            "       <br /><br />Welcome to the JimTrade.com, India's largest online business directory, with over 3,00,000 product reviews. We are delighted to have you as a Free Member.\n" +
                            "\n" +
                            "       <br><table width='100%' cellspacing='6' border='0' cellpadding='2' style='border-bottom:1px solid #6DA6D6;'>\n" +
                            "    \n" +
                            "       <tr>\n" +
                            "       <td colspan='2'  style='font-size:13px; font-family:Arial, Helvetica, sans-serif;'>Click here to <a href='http://www.jimtrade.com/memberconfirmation.aspx?mem_id=" + encrypt + "'>confirm your email address</a><br> \n" +
                            "       </td>\n" +
                            "       </tr>\n" +
                            "\n" +
                            "       <tr><td colspan='2' bgcolor='#E7EFF8'><div style='font-size:14px;color:#206D9F;'><strong>&nbsp;Your login details are as under : </strong></div></td></tr>\n" +
                            "       <tr>\n" +
                            "       <td align='left' width='10%' valign='top' style='padding-top:10px;font-size:13px; font-family:Arial, Helvetica, sans-serif;'>Login Id:</td>\n" +
                            "       <td style='font-family: Arial, Helvetica, sans-serif;font-size: 13px;font-style: normal;color: #2761AC;text-decoration: underline;font-weight: none;'>" + email + "</td>\n" +
                            "       </tr>\n" +
                            "       <tr>\n" +
                            "       <td align='left' width='10%' valign='top' style='padding-top:10px;font-size:13px; font-family:Arial, Helvetica, sans-serif;'>Password:</td>\n" +
                            "       <td style='padding-top:10px;font-size:13px; font-family:Arial, Helvetica, sans-serif;'>" + password + "\n" +
                            "       </td>\n" +
                            "       </tr>\n" +
                            "       <tr>\n" +
                            "       <td align='left' valign='top' colspan='2'><a href='http://www.jimtrade.com/login/default.aspx?source=changepassword' style='font-family: Arial, Helvetica, sans-serif;font-size: 13px;font-style: normal;color: #2761AC;text-decoration: underline;font-weight: none;'>Change Your Password</a></td>\n" +
                            "       </tr>\n" +
                            "       </table>\n" +
                            "       <br />\n" +
                            "       As a registered member, you are eligible to enjoy a number of privileges at <a href='http://www.jimtrade.com/login/default.aspx' style='font-family: Arial, Helvetica, sans-serif;font-size: 13px;font-style: normal;color: #2761AC;text-decoration: underline;font-weight: none;'>My Jimtrade</a> : <br />\n" +
                            "       1. You will get e-newsletters related to products of your interest.<br />\n" +
                            "       2. You can manage categories for newsletter.<br />\n" +
                            "       3. You can manage your inquiries.<br />\n" +
                            "       4. Create your profile and send it with every inquiry just using login & password.<br />\n" +
                            "        If country.ToLower = \"india\" Then\n" +
                            "           5. You can win rewards of your choice by redeeming reward points earned from inquiries. <br/> <a href='http://www.jimtrade.com/corporate/rewardscheme.htm' style='font-family: Arial, Helvetica, sans-serif;font-size: 13px;font-style: normal;color: #2761AC;text-decoration: underline;font-weight: none;'>Know more about JimTrade Rewards.</a>\n" +
                            "        End If\n" +
                            "\n" +
                            "       6.\tYou can win rewards of your choice after you redeem your points earned from inquiry. To find out visit <a href='http://www.jimtrade.com/login/default.aspx?source=rewards'>My Reward Points</a>. \n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "       <br /><br />Wishing you the best of online business,\n" +
                            "\n" +
                            "       <br /><br />JimTrade.com\n" +
                            "       <br />India's Largest Online Business Directory<br /><br />\n" +
                            "\n" +
                            "       </div>\n" +
                            "\n" +
                            "       <span style='font-size:11px;font-family:Verdana;color:#7a7a7a;'>JimTrade.com shall not be liable for any lost profits or incidental, consequential or other damages arising out of or in connection with this message, our web site content, our services or the activities of any of the users of our web site.</span>\n" +
                            "       \n" +
                            "\n" +
                            "       </td>\n" +
                            "       </tr>\n" +
                            "       <tr>\n" +
                            "       <td valign='top'   width='600' height='10' style='\n" +
                            "       border-left:solid #6DA6D6 1px;border-right:solid #6DA6D6 1px;border-bottom:solid #6DA6D6 1px;word-break:break-all;'>&nbsp;\n" +
                            "       </td>\n" +
                            "       </tr>\n" +
                            "       </table>\n" +
                            "\n" +
                            "\n" +
                            "       <table width='600px' cellspacing='0' border='0' cellpadding='0' align='center' style='margin-top:12px;'>\n" +
                            "       <tr>\n" +
                            "       <td align='center' style='text-align:center'>\n" +
                            "       <div style='text-align:center;padding:3px; font-size:10px;font-family:Verdana;color:#7a7a7a'>\n" +
                            "       This email has been sent to: <a href='mailto:\" " + email + " \"'>\" " + email + " \"</a><br>\n" +
                            "       <hr style='color:#d3d4d5' size='1'>\n" +
                            "       Refer to our <a href='http://www.jimtrade.com/corporate/privacypolicy.htm' target='_blank' style='text-decoration:none;'>Privacy Policy</a> and <a href='http://www.jimtrade.com/corporate/terms.htm' target='_blank' style='text-decoration:none;'>Terms of Use</a>.<br />\n" +
                            "       Copyright © 2005-2009 Jupiter Infomedia Ltd. <br>\n" +
                            "       This email has been sent by Jimtrade.com. \n" +
                            "       For further assistance, please contact us at:<br>\n" +
                            "       Customer Care Team, 336, Laxmi Plaza,  \n" +
                            "       Laxmi Industrial Estate, Laxmi Industrial Estate, New Link Road, Andheri(West), Mumbai - 400 053, Maharashtra, India.<br> Tel. (91)22-2634 1691/2/3; Fax (91)22-2634 1693; <br />\n" +
                            "       </div>\n" +
                            "       </td>\n" +
                            "       </tr>\n" +
                            "       </table>\n" +
                            "       </body>\n" +
                            "       </html>";

                    sendMail(email1, subject, message);
                }
            });

        }catch (NullPointerException ne)
        {
            Toast.makeText(My_JimTrade.this,"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
        }
    }

    class LoadData extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // custom dialog
            dialog = new Dialog(My_JimTrade.this);
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
                if (isNetworkStatusAvialable (My_JimTrade.this)) {

                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray != null) {
                        // looping through All categories

                        for (int i = 0; i <= jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);
                            String inquiry_receive = c.getString(TAG_INQUIRIES_RECEIVED);
                            String inquiry_sent = c.getString(TAG_INQUIRIES_SENT);
                            String Subscribed_category = c.getString(TAG_MEMBER_SUBSCRIBED);
                            String rewards = c.getString(TAG_MEMBER_POINTS);
                            String confirmation = c.getString(TAG_MEMBER_CONFIRMATION);
                            String diffrentiator = c.getString(TAG_MEMBER_DIFFERENTIATOR);

                            relativeLayout = (RelativeLayout) findViewById(R.id.rl4);

                            if (confirmation.contains("1"))
                            {
                                /*
                                relativeLayout.setVisibility(View.GONE);
                                relativeLayout.setPadding(0, -1 * relativeLayout.getHeight(), 0, 0);
                                */
                            }

                            if(rewards.isEmpty())
                            {
                                TextView textView5 = (TextView) findViewById(R.id.txt5);
                                textView5.setVisibility(textView5.GONE);
                                textView5.setPadding(0, -1 * textView5.getHeight(), 0, 0);
                            }


                            textView = (TextView) findViewById(R.id.txt7);
                            textView1 = (TextView) findViewById(R.id.txt8);
                            textView2 = (TextView) findViewById(R.id.txt9);
                            textView3 = (TextView) findViewById(R.id.txt10);
                            textView4 = (TextView) findViewById(R.id.txt2);

                            textView.setText(inquiry_receive);
                            textView1.setText(inquiry_sent);
                            textView2.setText(Subscribed_category);
                            textView3.setText(rewards);

                            if(diffrentiator.contains("0"))
                            {
                                textView.setVisibility(View.GONE);
                                textView.setPadding(0, -1 * textView.getHeight(), 0, 0);

                                textView4.setVisibility(View.GONE);
                                textView4.setPadding(0, -1 * textView4.getHeight(), 0, 0);
                            }


                        }
                    } else {

                        Log.d("Categories: ", "null");
                    }
                }
                else
                {
                    Toast.makeText(My_JimTrade.this, "Check Internet Connectivity!!", Toast.LENGTH_LONG).show();
                    return;
                }

            } catch (JSONException e) {
                Log.d("Test", "Couldn't successfully parse the JSON response!");
            }
            catch (NullPointerException ne)
            {
                Toast.makeText(My_JimTrade.this, "Check Internet Connectivity!!", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("support@jimtrade.com", "Jim Trade"));

        String[] recipientList = email.split(",");
        InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
        int counter = 0;
        for (String recipient : recipientList) {
            recipientAddress[counter] = new InternetAddress(recipient.trim());
            counter++;
        }
        message.setRecipients(Message.RecipientType.TO, recipientAddress);
        message.setContent(messageBody,"text/html" );
        message.setSubject(subject);

        return message;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "mail.jimtrade.com");
        properties.put("mail.smtp.port", "25");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private class SendMailTask extends AsyncTask<Message, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // custom dialog
            dialog = new Dialog(My_JimTrade.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progressdialog);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"Verification mail sent!!",Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
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
}