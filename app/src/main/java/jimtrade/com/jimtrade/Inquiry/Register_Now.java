package jimtrade.com.jimtrade.Inquiry;

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

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import jimtrade.com.jimtrade.HomeScreen;
import jimtrade.com.jimtrade.OTP_Process.OTP;
import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.Send_Inquiry.Send_Inquiry_Product;
import jimtrade.com.jimtrade.Send_Inquiry.Send_Inquiry_Supplier;


public class Register_Now extends Fragment
{

    private static final String username = "support@jimtrade.com";
    private static final String password = "Jupiter@336";

    private static String title;
    private static int page;
    View view;

    Dialog dialog;

    public static final String MyPREFERENCES = "MyPrefs" ;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String MyPREFERENCES1 = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;

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

    public static Register_Now newInstance(int i, String s) {
        Register_Now fragmentSecond = new Register_Now();
        Bundle args = new Bundle();
        args.putInt("2", page);
        args.putString("no.2", title);
        fragmentSecond.setArguments(args);
        return fragmentSecond;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("2", 1);
        title = getArguments().getString("no.2");
    }

    public Register_Now()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register__now, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        Button sendButton = (Button) view.findViewById(R.id.button_register);
        Button resetButton = (Button) view.findViewById(R.id.button_reset);

        final EditText e1 = (EditText) view.findViewById(R.id.Edt_Company);
        final EditText e2 = (EditText) view.findViewById(R.id.Contact_Person_Edt);
        final EditText e3 = (EditText) view.findViewById(R.id.Design_edt);
        final EditText e4 = (EditText) view.findViewById(R.id.email_edt);
        final EditText e5 = (EditText) view.findViewById(R.id.pass_edt);
        final EditText e6 = (EditText) view.findViewById(R.id.cont_no_edt);
        final EditText e7 = (EditText) view.findViewById(R.id.address_edt);

        final Spinner mySpinner=(Spinner) view.findViewById(R.id.spinner1);

        try {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        if (isEmptyField(e1)) return;
                        if (isEmptyField(e2)) return;
                        if (isEmptyField(e3)) return;
                        if (isEmptyField(e4)) return;
                        if (isEmptyField(e5)) return;
                        if (isEmptyField(e6)) return;

                        String company = e1.getText().toString();
                        String contact_person = e2.getText().toString();
                        String designation = e3.getText().toString();
                        String email = e4.getText().toString();
                        String password = e5.getText().toString();
                        String contact_no = e6.getText().toString();
                        String address = e7.getText().toString();

                        if (!isEmailValid(email)) return;

                        String country = mySpinner.getSelectedItem().toString();

                        byte[] data3 = email.getBytes("UTF-8");
                        String a1 = Base64.encodeToString(data3, Base64.NO_WRAP);

                        byte[] data4 = password.getBytes("UTF-8");
                        String a2 = Base64.encodeToString(data4, Base64.NO_WRAP);

                        byte[] data1 = contact_person.getBytes("UTF-8");
                        String a3 = Base64.encodeToString(data1, Base64.NO_WRAP);

                        byte[] data2 = designation.getBytes("UTF-8");
                        String a4 = Base64.encodeToString(data2, Base64.NO_WRAP);

                        byte[] data = company.getBytes("UTF-8");
                        String a5 = Base64.encodeToString(data, Base64.NO_WRAP);

                        byte[] data6 = address.getBytes("UTF-8");
                        String a6 = Base64.encodeToString(data6, Base64.NO_WRAP);

                        byte[] data5 = contact_no.getBytes("UTF-8");
                        String a7 = Base64.encodeToString(data5, Base64.NO_WRAP);

                        byte[] data7 = country.getBytes("UTF-8");
                        String a8 = Base64.encodeToString(data7, Base64.NO_WRAP);


                        String sample = a1 + "," + a2 + "," + a3 + "," + a4 + "," + a5 + "," + a6 + "," + a7 + "," + a8;

                        /*
                        String sample2 = email + "," + password + "," + contact_person + "," + designation + "," + company + "," + address + "," + contact_no + "," + country ;
                        Toast.makeText(getActivity(),sample,Toast.LENGTH_LONG).show();
                        */

                        new Senddata().execute("http://m.jimtrade.com/mobileapp.svc/memberinsert/" + sample);


                    } catch (UnsupportedEncodingException e) {

                    }
                }
            });


            resetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    e1.setText("");
                    e2.setText("");
                    e3.setText("");
                    e4.setText("");
                    e5.setText("");
                    e6.setText("");
                    e7.setText("");
                    mySpinner.setSelection(0);
                }
            });

        }catch (NullPointerException ne)
        {
            Toast.makeText(getActivity(),"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
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
            Toast.makeText(getActivity(), "Enter proper email-id", Toast.LENGTH_SHORT).show();
        }
        return isValid;
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
            Toast.makeText(getActivity(), "Please Enter all details!", Toast.LENGTH_SHORT).show();
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
            dialog = new Dialog(getActivity());
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
                if (isNetworkStatusAvialable (getActivity())) {

                    JSONArray categories = new JSONArray(response);

                    sharedpreferences1 = getActivity().getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
                    final String z1 = sharedpreferences1.getString("identifier",null);

                    if(categories.length() <= 0 )
                    {
                        Toast.makeText(getActivity(), "Email Id Already Registered", Toast.LENGTH_LONG).show();
                    }

                    if (categories != null) {
                        // looping through All categories

                        for (int i = 0; i <= categories.length(); i++) {

                            JSONObject c = categories.getJSONObject(i);

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

                            if(status_no.contains("Verfied"))
                            {
                                Toast.makeText(getActivity(), "Successfully registered", Toast.LENGTH_LONG).show();

                                if(z1.contains("0"))
                                {
                                    Intent intent = new Intent(getActivity(),Send_Inquiry_Product.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    getActivity().finish();
                                    startActivity(intent);
                                }
                                else if(z1.contains("1"))
                                {
                                    Intent intent = new Intent(getActivity(),Send_Inquiry_Supplier.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    getActivity().finish();
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "None", Toast.LENGTH_LONG).show();
                                }
                            }
                            else if(status_no.contains("Unverfied") && Country_small.contains("India"))
                            {
                                Toast.makeText(getActivity(), "Successfully registered", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(),OTP.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                getActivity().finish();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Successfully registered", Toast.LENGTH_LONG).show();
                                if(z1.contains("0"))
                                {
                                    Intent intent = new Intent(getActivity(),Send_Inquiry_Product.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    getActivity().finish();
                                    startActivity(intent);
                                }
                                else if(z1.contains("1"))
                                {
                                    Intent intent = new Intent(getActivity(),Send_Inquiry_Supplier.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    getActivity().finish();
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "None", Toast.LENGTH_LONG).show();
                                }
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

                            if(!member_id.isEmpty())
                            {

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
                                        "           <div style='font-size:13px; font-family:Arial, Helvetica, sans-serif;'>Dear " + contact_person +",</strong>\n" +
                                        "     \n" +
                                        "       <br /><br />Welcome to the JimTrade.com, India's largest online business directory, with over 3,00,000 product reviews. We are delighted to have you as a Free Member.\n" +
                                        "\n" +
                                        "       <br><table width='100%' cellspacing='6' border='0' cellpadding='2' style='border-bottom:1px solid #6DA6D6;'>\n" +
                                        "    \n" +
                                        "       <tr>\n" +
                                        "       <td colspan='2'  style='font-size:13px; font-family:Arial, Helvetica, sans-serif;'>Click here to <a href='http://www.jimtrade.com/memberconfirmation.aspx?mem_id="+ encrypt +"'>confirm your email address</a><br> \n" +
                                        "       </td>\n" +
                                        "       </tr>\n" +
                                        "\n" +
                                        "       <tr><td colspan='2' bgcolor='#E7EFF8'><div style='font-size:14px;color:#206D9F;'><strong>&nbsp;Your login details are as under : </strong></div></td></tr>\n" +
                                        "       <tr>\n" +
                                        "       <td align='left' width='10%' valign='top' style='padding-top:10px;font-size:13px; font-family:Arial, Helvetica, sans-serif;'>Login Id:</td>\n" +
                                        "       <td style='font-family: Arial, Helvetica, sans-serif;font-size: 13px;font-style: normal;color: #2761AC;text-decoration: underline;font-weight: none;'>"+ email +"</td>\n" +
                                        "       </tr>\n" +
                                        "       <tr>\n" +
                                        "       <td align='left' width='10%' valign='top' style='padding-top:10px;font-size:13px; font-family:Arial, Helvetica, sans-serif;'>Password:</td>\n" +
                                        "       <td style='padding-top:10px;font-size:13px; font-family:Arial, Helvetica, sans-serif;'>"+ password +"\n" +
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
                                        "       </html>" ;

                                sendMail(email1, subject, message);
                            }
                        }
                    } else {

                        Log.d("Categories: ", "null");
                    }
                }

                else
                {
                    Toast.makeText(getActivity(),"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                Log.d("Test", "Couldn't successfully parse the JSON response!");
            } catch (NullPointerException ne) {
                Toast.makeText(getActivity(), "Check Internet Connectivity!!", Toast.LENGTH_LONG).show();
            }

            dialog.dismiss();
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
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

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
