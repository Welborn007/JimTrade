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

import jimtrade.com.jimtrade.HomeScreen;
import jimtrade.com.jimtrade.OTP_Process.OTP;
import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.Send_Inquiry.Send_Inquiry_Product;
import jimtrade.com.jimtrade.Send_Inquiry.Send_Inquiry_Supplier;


public class Already_Registered extends Fragment
{

    private static String title;
    private static int page;
    View view;

    TextView forgot;

    private Button mSubmit;

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

    public static Already_Registered newInstance(int i, String s) {
        Already_Registered fragmentFirst = new Already_Registered();
        Bundle args = new Bundle();
        args.putInt("1", page);
        args.putString("no.1", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("1", 0);
        title = getArguments().getString("no.1");
    }

    public Already_Registered()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_already__registered, container, false);

        forgot = (TextView) view.findViewById(R.id.Forgot_inquiry);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ForgotPassword.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        mSubmit = (Button) view.findViewById(R.id.Send_Enquiry);

        final EditText user = (EditText) view.findViewById(R.id.login_id);
        final EditText pass = (EditText) view.findViewById(R.id.Password_inquiry);

        try {
            mSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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

    class AttemptLogin extends AsyncTask<String, String, String> {
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
                        Toast.makeText(getActivity(), "Email Id and Password Doesn't Match", Toast.LENGTH_LONG).show();
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

                            if(status_no.contains("Verfied"))
                            {
                                if(z1.contains("0"))
                                {
                                    Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity(),Send_Inquiry_Product.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    getActivity().finish();
                                    startActivity(intent);
                                }
                                else if(z1.contains("1"))
                                {
                                    Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_LONG).show();
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
                                Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(),OTP.class);
                                getActivity().finish();
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else
                            {
                                if(z1.contains("0"))
                                {
                                    Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity(),Send_Inquiry_Product.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    getActivity().finish();
                                    startActivity(intent);
                                }
                                else if(z1.contains("1"))
                                {
                                    Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_LONG).show();
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

                        }
                    } else {


                    }
                }

                else
                {
                    Toast.makeText(getActivity(),"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                Log.d("Test", "Couldn't successfully parse the JSON response!");
            } catch (NullPointerException ne) {
                Toast.makeText(getActivity(), "The Email or password field is empty", Toast.LENGTH_LONG).show();
            }

            dialog.dismiss();

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
