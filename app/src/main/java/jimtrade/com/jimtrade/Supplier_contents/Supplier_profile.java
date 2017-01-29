package jimtrade.com.jimtrade.Supplier_contents;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import jimtrade.com.jimtrade.R;


public class Supplier_profile extends Fragment
{
    private static String title;
    View view;
    private static int page;
    private static final String TAG_SUPPLIER_NAME = "suppliername";
    private static final String TAG_SUPPLIER_PROFILE = "supplierprofile";

    TextView textView,textView1,txt1;

    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;

    public static Supplier_profile newInstance(int i, String s)
    {
        Supplier_profile fragmentFirst = new Supplier_profile();
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
        productList = new ArrayList<HashMap<String, String>>();

        try {
            String s3 = getActivity().getIntent().getStringExtra("supplierid");
            new LoadSupplierProfile().execute("http://m.jimtrade.com/mobileapp.svc/supplierdetails/" + s3);
        }catch (NullPointerException npe)
        {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_supplier_profile, container, false);
        txt1 = (TextView) view.findViewById(R.id.suppcat);
        return view;
    }


    class LoadSupplierProfile extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                    try {
                        if (isNetworkStatusAvialable (getActivity())) {

                                JSONArray jsonArray = new JSONArray(response);

                                if (jsonArray != null) {
                                    // looping through All categories

                                    for (int i = 0; i <= jsonArray.length(); i++) {

                                        JSONObject c = jsonArray.getJSONObject(i);

                                        // Storing each json item values in variable

                                        String name = c.getString(TAG_SUPPLIER_NAME);
                                        String profile = c.getString(TAG_SUPPLIER_PROFILE);
                                        textView1 = (TextView) view.findViewById(R.id.profilesupp);

                                        profile = profile.replace("<Br>", "");
                                        profile = profile.replace("<font style=text-transform:none;>", "");
                                        profile = profile.replace("<br>", "");
                                        profile = profile.replace("</font>", "");
                                        profile = profile.replace("<b>", "");
                                        profile = profile.replace("</b", "");
                                        profile = profile.replace("<li>", "");
                                        profile = profile.replace("</li>", "");

                                        String str1 = profile;
                                        textView1.setText(str1);
                                    }
                                } else {

                                    Log.d("Categories: ", "null");
                                }

                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
                            return;
                        }

                    } catch (JSONException e) {
                        Log.d("Test", "Couldn't successfully parse the JSON response!");
                    } catch (NullPointerException ne) {
                        Toast.makeText(getActivity(), "Check Internet Connectivity!!", Toast.LENGTH_LONG).show();
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


