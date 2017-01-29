package jimtrade.com.jimtrade.Send_Inquiry;

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
import android.widget.CheckBox;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import jimtrade.com.jimtrade.AboutUs;
import jimtrade.com.jimtrade.ContactUs;
import jimtrade.com.jimtrade.FAQs;
import jimtrade.com.jimtrade.Feedback;
import jimtrade.com.jimtrade.HomeScreen;
import jimtrade.com.jimtrade.LoginActivity;
import jimtrade.com.jimtrade.My_JimTrade;
import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.RegisterNew;

public class Send_Inquiry_Product extends Activity {


    private static final String username = "support@jimtrade.com";
    private static final String password = "Jupiter@336";

    Dialog dialog;

   Dialog dialog2;

    CheckBox box1,box2;
    private TextView Supplier_content, Product_content;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String MyPREFERENCES1 = "MyPrefs1" ;
    SharedPreferences sharedpreferences,sharedpreferences1;
    SharedPreferences.Editor editor,editor1;

    private static final String TAG_SUCCESS = "success";
    String success;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_send__inquiry);

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
        sharedpreferences1 = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);

        try {
            String s1 = sharedpreferences.getString("Email", null);
            final String s2 = sharedpreferences.getString("company", null);
            final String s3 = sharedpreferences.getString("contact_person", null);
            final String s4 = sharedpreferences.getString("designation", null);
            final String s5 = sharedpreferences.getString("address", null);
            final String s6 = sharedpreferences.getString("telephone", null);
            final String s7 = sharedpreferences.getString("mem_id", null);

            Product_content = (TextView) findViewById(R.id.Product_Content);
            Supplier_content = (TextView) findViewById(R.id.Supplier_Content);

            final String z1 = sharedpreferences1.getString("prod_name", null);
            final String z2 = sharedpreferences1.getString("supp_name", null);
            String z3 = sharedpreferences1.getString("supp_email", null);
            final String z4 = sharedpreferences1.getString("prod_id", null);
            final String z5 = sharedpreferences1.getString("supp_id", null);
            final String z6 = sharedpreferences1.getString("supp_address", null);
            final String z7 = sharedpreferences1.getString("supp_phone", null);

            Product_content.setText(z1);
            Supplier_content.setText(z2);

            Button sendButton = (Button) findViewById(R.id.send);

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        box1 = (CheckBox) findViewById(R.id.check1);

                        if (box1.isChecked()) {
                            String chkBestResponse = "1";
                            EditText edt1 = (EditText) findViewById(R.id.message1);
                            if (isEmptyField(edt1)) return;
                            String edit_message = edt1.getText().toString();

                            byte[] data = edit_message.getBytes("UTF-8");
                            String Inquiry_dat = Base64.encodeToString(data, Base64.NO_WRAP);

                            final String sample = s7 + "," + z5 + "," + z4 + "," + Inquiry_dat + "," + chkBestResponse;
                            new Senddata().execute("http://m.jimtrade.com/mobileapp.svc/memberinquiries/" + sample);
                        } else {
                            String chkBestResponse = "0";
                            EditText edt1 = (EditText) findViewById(R.id.message1);
                            if (isEmptyField(edt1)) return;
                            String edit_message = edt1.getText().toString();

                            byte[] data = edit_message.getBytes("UTF-8");
                            String Inquiry_dat = Base64.encodeToString(data, Base64.NO_WRAP);

                            final String sample = s7 + "," + z5 + "," + z4 + "," + Inquiry_dat + "," + chkBestResponse;
                            new Senddata().execute("http://m.jimtrade.com/mobileapp.svc/memberinquiries/" + sample);
                        }


                    } catch (UnsupportedEncodingException e) {

                    }
                }
            });

        }catch (NullPointerException ne)
        {
            Toast.makeText(getApplicationContext(),"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
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
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();

                // create intent to perform web search for this planet
                Intent intent4 = new Intent(this, HomeScreen.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent4);

                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private boolean isEmptyField (EditText editText){
        boolean result = editText.getText().toString().length() <= 0;
        if (result)
            Toast.makeText(getApplicationContext(), "Please fill your requirements", Toast.LENGTH_SHORT).show();
        return result;
    }

    class Senddata extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // custom dialog
            dialog = new Dialog(Send_Inquiry_Product.this);
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

                            success = c.getString(TAG_SUCCESS);


                            if (success.contains("success"))
                            {
                                String edit_message = ((EditText)findViewById(R.id.message1)).getText().toString();
                                box2 = (CheckBox) findViewById(R.id.check2);

                                String email = sharedpreferences.getString("Email", null);
                                final String s2 = sharedpreferences.getString("company", null);
                                final String s3 = sharedpreferences.getString("contact_person",null);
                                final String s4 = sharedpreferences.getString("designation",null);
                                final String s5 = sharedpreferences.getString("address",null);
                                final String s6 = sharedpreferences.getString("telephone",null);
                                String s7 = sharedpreferences.getString("mem_id", null);

                                final String z1 = sharedpreferences1.getString("prod_name",null);
                                final String z2 = sharedpreferences1.getString("supp_name", null);
                                final String z6 = sharedpreferences1.getString("supp_address",null);
                                final String z7 = sharedpreferences1.getString("supp_phone", null);
                                String supplier_email = sharedpreferences1.getString("supp_email", null);



                                Calendar cal = Calendar.getInstance();

                                int seconds = cal.get(Calendar.SECOND);
                                int minutes = cal.get(Calendar.MINUTE);
                                int hour = cal.get(Calendar.HOUR_OF_DAY);
                                final String time = hour+":"+minutes+":"+seconds;


                                int day = cal.get(Calendar.DAY_OF_MONTH);
                                int month = cal.get(Calendar.MONTH)+ 1;
                                int year = cal.get(Calendar.YEAR);
                                final String date = day+"/"+month+"/"+year;

                                String html = "<html>\n" +
                                        "<head>\n" +
                                        "<meta content='text/html; charset=iso-8859-1' http-equiv='Content-Type'>\n" +
                                        "<title>\n" +
                                        "India Business Directory, Indian Suppliers Directory, Indian Products Directory from India</title>\n" +
                                        "<style type='text/css'>\n" +
                                        "<!--body,td,p,th{font-size:13px;color:#000000;font-family:Arial, Helvetica, sans-serif;line-height:130%;}-->\n" +
                                        "</style>\n" +
                                        "</head>\n" +
                                        "<body bgcolor='#ffffff'>\n" +
                                        "<table width='600' cellspacing='0' border='0' cellpadding='0' align='center' style='margin-bottom:6px;'>\n" +
                                        "<tr>\n" +
                                        " <td width='600' align='center' style='font-size:15;color:#0F54AF;font-family:Arial, Helvetica, sans-serif;'>\n" +
                                        "<a href='http://www.jimtrade.com'>\n" +
                                        "<img src='http://www.jimtrade.com/images_new/jimtrade_logo.jpg' width='250' height='80' border='0' title='JimTrade.com : India`s Largest Online Business Directory' alt='JimTrade.com : India`s Largest Online Business Directory'>\n" +
                                        "</a>\n" +
                                        "<br>\n" +
                                        "<b>\n" +
                                        "India's Largest Online Business Directory</b>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "</table>\n" +
                                        "<table width='600' cellspacing='0' border='0' cellpadding='0' align='center'>\n" +
                                        "<tr>\n" +
                                        "<td valign='top'  bgcolor='#E7EFF8' width='600' height='41' style='text-align:center;border-left:solid #6DA6D6 1px; border-right:solid #6DA6D6 1px; border-top:solid #6DA6D6 1px; padding:0 0; word-break:break-all;'>\n" +
                                        "<div align='center' style='height:41;padding:6px 0px; font-size:22px; font-family:Arial, Helvetica, sans-serif;line-height:22px; color:#206D9F;text-align:center'>\n" +
                                        "Inquiry from Buyer for your product</div>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td width='600' style='border-left:solid #6DA6D6 1px; border-right:solid #6DA6D6 1px; padding:12px;'>\n" +
                                        "<div style='font-size:13px; font-family:Arial, Helvetica, sans-serif;'>\n" +
                                        "Dear\n" +
                                        "<b>Sir/Madam,</b>" +
                                        "<br>\n" +
                                        "<br>\n" +
                                        "You have received the following inquiry from 'jupiter' through a Premium Listing on JimTrade.com.  \n" +
                                        "<br>\n" +
                                        "<br>\n" +
                                        "<table width='100%' cellspacing='0' border='0' cellpadding='0' >\n" +
                                        "<tr>\n" +
                                        "<td colspan='2' bgcolor='#E7EFF8'>\n" +
                                        "<div style='font-size:13px; font-family:Arial, Helvetica, sans-serif;color:#206D9F;'>\n" +
                                        "<strong>\n" +
                                        "&nbsp;Product Inquiry : </strong>\n" +
                                        "</div>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td width='25%'  align='left' valign='top' style='padding-top:10px;padding-left:10px;font-size:13px; font-family:Arial, Helvetica, sans-serif;'>\n" +
                                        "<b>\n" +
                                        "Product : </b>\n" +
                                        "</td>\n" +
                                        "<td style='padding-top:10px;padding-left:10px;'>\n" +
                                        z1 +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        " <tr>\n" +
                                        "<td  width='25%' align='left' valign='top' style='padding-top:10px;padding-left:10px;font-size:13px; font-family:Arial, Helvetica, sans-serif;' >\n" +
                                        "<b>\n" +
                                        "Date : </b>\n" +
                                        "</td>\n" +
                                        "<td style='padding-top:10px;padding-left:10px;font-size:13px; font-family:Arial, Helvetica, sans-serif;'>\n" + date + "  " + time +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td  width='25%'  align='left' valign='top' style='padding-top:10px;padding-left:10px;font-size:13px; font-family:Arial, Helvetica, sans-serif;' >\n" +
                                        "<b>\n" +
                                        "Message : </b>\n" +
                                        "</td>\n" +
                                        "<td  align='left' style='padding-top:10px;padding-left:10px;font-size:13px; font-family:Arial, Helvetica, sans-serif;'>\n" + edit_message +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        " </table>\n" +
                                        "<br>\n" +
                                        "<table width='100%'  border='0'  style='border-bottom:1px solid #6DA6D6;font-size:13px; font-family:Arial, Helvetica, sans-serif;'>\n" +
                                        "<tr>\n" +
                                        "<td colspan='2' bgcolor='#E7EFF8'>\n" +
                                        "<div style='font-size:14px;color:#206D9F;'>\n" +
                                        "<strong>\n" +
                                        "&nbsp;Buyer details : </strong>\n" +
                                        "</div>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td height='10px'>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td align='left' valign='top' style='width:25%;font-size:13px; font-family:Arial, Helvetica, sans-serif;padding-left:10px;' nowrap>\n" +
                                        "<b>\n" +
                                        "<b>\n" +
                                        "Company : </b>\n" +
                                        "</b>\n" +
                                        "</td>\n" +
                                        s2 +
                                        "</tr>\n" +
                                        " <tr>\n" +
                                        "<td height='5px'>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td align='left' valign='top' style='width:25%;font-size:13px; font-family:Arial, Helvetica, sans-serif;padding-left:10px;' nowrap>\n" +
                                        "<b>\n" +
                                        "Contact Person : </b>\n" +
                                        "</td>\n" +
                                        s3 +
                                        "</tr>\n" +
                                        " <tr>\n" +
                                        "<td height='5px'>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td align='left' valign='top' style='font-size:13px; font-family:Arial, Helvetica, sans-serif;width:25%;padding-left:10px;' nowrap>\n" +
                                        "<b>\n" +
                                        "Designation : </b>\n" +
                                        "</td>\n" +
                                        s4 +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td height='5px'>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td align='left' valign='top' style='font-size:13px; font-family:Arial, Helvetica, sans-serif;width:25%;padding-left:10px;' nowrap>\n" +
                                        "<b>\n" +
                                        "Address : </b>\n" +
                                        "</td>\n" +
                                        s5 +
                                        "<tr>\n" +
                                        "<td height='5px'>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td align='left' valign='top' style='font-size:13px; font-family:Arial, Helvetica, sans-serif;width:25%;padding-left:10px;' nowrap>\n" +
                                        "<b>\n" +
                                        "Telephone : </b>\n" +
                                        "</td>\n" +
                                        s6 +
                                        "<tr>" +
                                "<td align='left' valign='top' style='padding-top:10px;font-size:13px; font-family:Arial, Helvetica, sans-serif;width:25%;padding-left:10px;' nowrap>" +
                                "<b>" +
                                        "Email : </b>" +
                                "</td>" +
                                "<td style='width:75%'>" +
                                "<a href='mailto:" + email + "?bcc=support@jimtrade.com&subject=Reply to Inquiry through JimTrade.com' style='font-size:13px; font-family:Arial, Helvetica, sans-serif;width:25%;'>" +
                                ""+ email + "</a>" +

                                "</td>" +
                                "</tr>" +
                                "<tr>" +
                                "<td height='15px'>" +
                                "</tr>" +
                                "<tr>" +
                                "<td align='left' valign='top' style='font-size:13px; font-family:Arial, Helvetica, sans-serif;width:25%' nowrap>" +
                                "</td>" +
                                "<td style='width:75%;height:30px'>" +
                                "&nbsp;&nbsp;<a href='mailto:" + email + "?bcc=support@jimtrade.com&subject=Reply to Inquiry through JimTrade.com' style='font-family:Arial, Helvetica, sans-serif; font-size:18px; text-align:center; background:#DFEBF9; border:1px solid #DFEBF9; border-radius:3px;text-decoration:none; color:#000000;padding:10px;'>" +
                                "Reply to Inquiry</a>" +
                                " </td>" +
                                "</tr>" +
                                        "<tr>" +
                                        "<td height='15px'>" +
                                        "</tr>" +
                                    "</table>" +

                                        "<br>\n" +
                                        "<a href='http://www.jimtrade.com/login/default.aspx?source=inbox' style='font-family: Arial, Helvetica, sans-serif;font-size: 13px;font-style: normal;color: #2761AC;text-decoration: underline;font-weight: none;'>\n" +
                                        "View all inquires</a>\n" +
                                        "<br>\n" +
                                        "<br>\n" +
                                        "<a href='http://www.jimtrade.com/password_reminder.aspx' style='font-family: Arial, Helvetica, sans-serif;font-size: 13px;font-style: normal;color: #2761AC;text-decoration: underline;font-weight: none;'>\n" +
                                        "Forget Password</a>\n" +
                                        "<br>\n" +
                                        "<br>\n" +
                                        "Be sure to regularly visit JimTrade.com to get more inquiries and response. <br>\n" +
                                        "<br>\n" +
                                        "Wishing you the best of online business,<br>\n" +
                                        "<br>\n" +
                                        "JimTrade.com<br>\n" +
                                        "India's Largest Online Business Directory<br>\n" +
                                        "<br>\n" +
                                        "</div>\n" +
                                        "<span style='font-size:11px;font-family:Verdana;color:#7a7a7a;'>\n" +
                                        "JimTrade.com shall not be liable for any lost profits or incidental, consequential or other damages arising out of or in connection with this message, our web site content, our services or the activities of any of the users of our web site.</span>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td valign='top'   width='600' height='10' style='border-left:solid #6DA6D6 1px;border-right:solid #6DA6D6 1px;border-bottom:solid #6DA6D6 1px;word-break:break-all;'>\n" +
                                        "&nbsp;</td>\n" +
                                        "</tr>\n" +
                                        "</table>\n" +
                                        "<table width='600' cellspacing='0' border='0' cellpadding='0' align='center' style='text-align:center;margin-top:12px;' >\n" +
                                        "<tr>\n" +
                                        "<hr style='color:#d3d4d5' size='1'>\n" +
                                        "Refer to our <a href='http://www.jimtrade.com/corporate/privacypolicy.htm' target='_blank' style='text-decoration:none;'>\n" +
                                        "Privacy Policy</a>\n" +
                                        " and <a href='http://www.jimtrade.com/corporate/terms.htm' target='_blank' style='text-decoration:none;'>\n" +
                                        "Terms of Use</a>\n" +
                                        ".<br>\n" +
                                        "Copyright © 2005-2009 Jupiter Infomedia Ltd. <br>\n" +
                                        "This email has been sent by Jimtrade.com. For further assistance, please contact us at:<br>\n" +
                                        "Customer Care Team, 336, Laxmi Plaza,  Laxmi Industrial Estate, Laxmi Industrial Estate, New Link Road, Andheri(West), Mumbai - 400 053, Maharashtra, India.<br>\n" +
                                        " Tel. (91)22-2634 1691/2/3; Fax (91)22-2634 1693; <br>\n" +
                                        "</div>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "</table>\n" +
                                        "</body>\n" +
                                        "</html>\n";


                                html = html.replace("Hello!!",edit_message);
                                String Html_message = html;
                                final String email1 = "machadowelborn@gmail.com";
                                final String subject = z2 + "  Product Inquiry through JimTrade.com";
                                final String message = Html_message;
                                final String subject1 = "Confirmation of Product Inquiry sent by you";

                                final String message1= "<html>\n" +
                                        "<head>\n" +
                                        "<meta content='text/html; charset=iso-8859-1' http-equiv='Content-Type'>\n" +
                                        "<title>\n" +
                                        "India Business Directory, Indian Suppliers Directory, Indian Products Directory from India</title>\n" +
                                        "<style type='text/css'>\n" +
                                        "<!--body,td,p,th{font-size:13px;color:#000000;font-family:Arial, Helvetica, sans-serif;line-height:130%;}-->\n" +
                                        "</style>\n" +
                                        "</head>\n" +
                                        "<body bgcolor='#ffffff'>\n" +
                                        "<table width='600' cellspacing='0' border='0' cellpadding='0' align='center' style='margin-bottom:6px;'>\n" +
                                        "<tr>\n" +
                                        "<td>\n" +
                                        "<a href='http://www.jimtrade.com/' target='_blank'>\n" +
                                        "<img src='http://www.jimtrade.com/images_new/jimtrade_inner_logo_small.jpg' width='175' height='50' alt='www.jimtrade.com' vspace='6' border='0'>\n" +
                                        "</a>\n" +
                                        "</td>\n" +
                                        "<td valign='bottom' align='right'>\n" +
                                        "<div style='padding:0 0 5px 0;font-size:11px;font-family:Arial, Helvetica, sans-serif; color:#7a7a7a'>\n" +
                                        "<a href='http://products.jimtrade.com/' target='_blank' style='text-decoration:none; color:#7a7a7a'>\n" +
                                        "Indian Products</a>\n" +
                                        " | <a href='http://directory.jimtrade.com/' target='_blank' style='text-decoration:none; color:#7a7a7a'>\n" +
                                        "Indian Suppliers</a>\n" +
                                        " | <a href='http://tradefair.jimtrade.com/' target='_blank' style='text-decoration:none; color:#7a7a7a'>\n" +
                                        "Trade Fairs</a>\n" +
                                        " | <a href='http://www.jimtrade.com/login/' target='_blank' style='text-decoration:none; color:#7a7a7a'>\n" +
                                        "My JimTrade</a>\n" +
                                        "</div>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "</table>\n" +
                                        "<table width='600' cellspacing='0' border='0' cellpadding='0' align='center'>\n" +
                                        "<tr>\n" +
                                        "<td valign='top'  bgcolor='#E7EFF8' width='600' height='41' style='text-align:center;border-left:solid #6DA6D6 1px; border-right:solid #6DA6D6 1px; border-top:solid #6DA6D6 1px; padding:0 0; word-break:break-all;'>\n" +
                                        "<div align='center' style='height:41;padding:6px 0px; font-size:22px; font-family:Arial, Helvetica, sans-serif;line-height:22px; color:#206D9F;text-align:center'>\n" +
                                        "Confirmation of Product Inquiry sent by you</div>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td width='600' style='border-left:solid #6DA6D6 1px; border-right:solid #6DA6D6 1px; padding:12px;'>\n" +
                                        "<div style='font-size:13px; font-family:Arial, Helvetica, sans-serif;'>\n" +
                                        "Dear <strong>\n" +
                                        s3 +
                                        ",</strong> <br>\n" +
                                        "<br>\n" +
                                        "Thank you for using JimTrade.com for your requirement. Following is the content of Inquiry sent by you for your information only.<br>\n" +
                                        "<br>\n" +
                                        "<table width='100%' cellspacing='6' border='0' cellpadding='2' style='border-bottom:1px solid #6DA6D6;font-size:13px; font-family:Arial, Helvetica, sans-serif;'>\n" +
                                        "<tr>\n" +
                                        "<td colspan='2' bgcolor='#E7EFF8'>\n" +
                                        "<div style='font-size:14px;color:#206D9F;'>\n" +
                                        "<strong>\n" +
                                        "&nbsp;Product Inquiry : </strong>\n" +
                                        "</div>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td align='left' valign='top' nowrap>\n" +
                                        "<b>\n" +
                                        "Supplier:</b>\n" +
                                        "</td>\n" +
                                         z2 +
                                        "<br>\n" +
                                         z6 +
                                        "<br>\n" +
                                        z7 +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td align='left' valign='top' style='padding-top:10px;'>\n" +
                                        "<b>\n" +
                                        "Product:</b>\n" +
                                        "</td>\n" +
                                        "<td style='padding-top:10px;'>\n" +
                                         z1 +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        " <tr>\n" +
                                        "<td align='left' valign='top' >\n" +
                                        "<b>\n" +
                                        "Date:</b>\n" +
                                        "</td>\n" +
                                        "<td >\n" +
                                         date + "   " + time +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td width='10%' align='left' valign='top'>\n" +
                                        "<b>\n" +
                                        "Message:</b>\n" +
                                        "</td>\n" +
                                        "<td>\n" +
                                        edit_message +
                                        "</tr>\n" +
                                        " </table>\n" +
                                        "\t<br>\n" +
                                        "To manage all your inquires, please login with your user id and password. If you do not remember your password, <a href='http://www.jimtrade.com/password_reminder.aspx' border='0' align='absmiddle' target='_blank' style='text-decoration:underline; color:#0033FF'>\n" +
                                        "click here</a>\n" +
                                        " to get your password.&nbsp;<br>\n" +
                                        "<br>\n" +
                                        "More Services for the Buyer :<br>\n" +
                                        "<a href='http://products.jimtrade.com/' border='0' align='absmiddle' target='_blank' style='text-decoration:underline; color:#0033FF'>\n" +
                                        "Indian Products</a>\n" +
                                        " Directory with more than 3,00,000 product reviews<br>\n" +
                                        "<a href='http://directory.jimtrade.com/' border='0' align='absmiddle' target='_blank' style='text-decoration:underline; color:#0033FF'>\n" +
                                        "Indian Suppliers</a>\n" +
                                        " Directory with manufacturers and distributors listings<br>\n" +
                                        "<a href='http://tradefair.jimtrade.com/' border='0' align='absmiddle' target='_blank' style='text-decoration:underline; color:#0033FF'>\n" +
                                        "Trade Fairs</a>\n" +
                                        " displaying information related to Trade Fairs & Exhibitions in India<br>\n" +
                                        "<br>\n" +
                                        "Wishing you the best of online business,<br>\n" +
                                        "<br>\n" +
                                        "JimTrade.com<br>\n" +
                                        "India's Largest Online Business Directory<br>\n" +
                                        "<br>\n" +
                                        "</div>\n" +
                                        "<span style='font-size:11px;font-family:Verdana;color:#7a7a7a;'>\n" +
                                        "JimTrade.com shall not be liable for any lost profits or incidental, consequential or other damages arising out of or in connection with this message, our web site content, our services or the activities of any of the users of our web site.</span>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td valign='top'   width='600' height='10' style='border-left:solid #6DA6D6 1px;border-right:solid #6DA6D6 1px;border-bottom:solid #6DA6D6 1px;word-break:break-all;'>\n" +
                                        "&nbsp;</td>\n" +
                                        "</tr>\n" +
                                        "</table>\n" +
                                        "<table width='600' cellspacing='0' border='0' cellpadding='0' align='center' style='text-align:center;margin-top:12px;' >\n" +
                                        "<tr>\n" +
                                        "<td align='center' style='text-align:center'>\n" +
                                        "<div style='text-align:center;padding:3px;font-size:10px;font-family:Verdana;color:#7a7a7a'>\n" +
                                        "This email has been sent to: <strong>\n" +
                                        ""+ email + "</strong>\n" +
                                        "<div style='text-align:center;padding:3px;font-size:10px;font-family:Verdana;color:#7a7a7a'>\n" +
                                        "<hr style='color:#d3d4d5' size='1'>\n" +
                                        "Refer to our <a href='http://www.jimtrade.com/corporate/privacypolicy.htm' target='_blank' style='text-decoration:none;'>\n" +
                                        "Privacy Policy</a>\n" +
                                        " and <a href='http://www.jimtrade.com/corporate/terms.htm' target='_blank' style='text-decoration:none;'>\n" +
                                        "Terms of Use</a>\n" +
                                        ".<br>\n" +
                                        "Copyright © 2005-2009 Jupiter Infomedia Ltd. <br>\n" +
                                        "This email has been sent by Jimtrade.com. For further assistance, please contact us at:<br>\n" +
                                        "Customer Care Team, 336, Laxmi Plaza, Laxmi Industrial Estate, Laxmi Industrial Estate, New Link Road, Andheri(West), Mumbai - 400 053, Maharashtra, India.<br>\n" +
                                        " Tel. (91)22-2634 1691/2/3; Fax (91)22-2634 1693; <br>\n" +
                                        "</div>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "</table>\n" +
                                        "</body>\n" +
                                        "</html>\n";




                                if(box2.isChecked())
                                {
                                    sendMail(email1, subject, message, email1, subject1, message1);
                                }
                                else {
                                    sendMail(email1, subject, message,"", "", "");
                                }
                            }
                            else if (success.contains("Fail"))
                            {
                                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"You have reached the Maximum Inquiries allowed",Toast.LENGTH_LONG).show();
                            }


                        }
                    } else {

                        Log.d("Categories: ", "null");
                    }
                }

                else
                {
                    Toast.makeText(getApplication(),"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                Log.d("Test", "Couldn't successfully parse the JSON response!");
            } catch (NullPointerException ne) {
                Toast.makeText(getApplicationContext(), "Check Internet Connectivity!!", Toast.LENGTH_LONG).show();
            }

            dialog.dismiss();
        }
    }




    private void sendMail(String email, String subject, String messageBody,String email1,String subject1,String message1) {
        Session session = createSessionObject();
        Session session1 = createSessionObject();

        try {


            Message message = createMessage(email, subject, messageBody, session);

            new SendMailTask().execute(message);

            if(!email.isEmpty()){
                Message message12 = createMessage(email1, subject1, message1, session1);
                new SendMailTask1().execute(message12);
            }

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void sendMail1(String email, String subject, String messageBody) {
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
        message.setContent(messageBody, "text/html");
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
        Dialog dialog1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // custom dialog
            dialog1 = new Dialog(Send_Inquiry_Product.this);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(R.layout.progressdialog);
            dialog1.setCancelable(false);
            dialog1.setCanceledOnTouchOutside(false);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog1.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                // custom dialog


                dialog2 = new Dialog(Send_Inquiry_Product.this);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.setContentView(R.layout.dialog_inquiry);

                TextView dialogButton = (TextView) dialog2.findViewById(R.id.dialogButtonHOME);
                TextView dialogButton1 = (TextView) dialog2.findViewById(R.id.dialogButtonEXIT);

                dialog2.show();

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent2 = new Intent(getApplicationContext(), HomeScreen.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                    }
                });

                dialogButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        System.exit(0);
                    }
                });


                dialog1.dismiss();

                if(box2.isChecked()){
                    dialog2.dismiss();
                }

            }catch (NullPointerException npe)
            {

            }
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

    private class SendMailTask1 extends AsyncTask<Message, Void, Void> {
        Dialog dialog1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // custom dialog
            dialog1 = new Dialog(Send_Inquiry_Product.this);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(R.layout.progressdialog);
            dialog1.setCancelable(false);
            dialog1.setCanceledOnTouchOutside(false);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog1.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                // custom dialog
                dialog2 = new Dialog(Send_Inquiry_Product.this);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.setContentView(R.layout.dialog_inquiry);

                TextView dialogButton = (TextView) dialog2.findViewById(R.id.dialogButtonHOME);
                TextView dialogButton1 = (TextView) dialog2.findViewById(R.id.dialogButtonEXIT);

                dialog2.show();

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent2 = new Intent(getApplicationContext(), HomeScreen.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                    }
                });

                dialogButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        System.exit(0);
                    }
                });


                dialog1.dismiss();

            }catch (NullPointerException npe)
            {

            }
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
    public void onBackPressed() {
        finish();
            }
}

