package jimtrade.com.jimtrade.Products_contents;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import jimtrade.com.jimtrade.R;

public class Product_details_fragment extends Fragment {

    View view,view1,view2;
    private static String title;
    private static int page;
    private static final String TAG_PRODUCT_DESC = "productdesc";
    private static final String TAG_PRODUCT_NAME = "productname";
    private static final String TAG_PRODUCT_SPECIFICATIONS = "spec";
    TextView textView,textView1;

    public static Product_details_fragment newInstance(int i, String s)
    {
        Product_details_fragment fragmentSecond = new Product_details_fragment();
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
        page = getArguments().getInt("2", 0);
        title = getArguments().getString("no.2");
        try {
            String s3 = getActivity().getIntent().getStringExtra("productid");
            // make a HTTP request
            String s5 = getActivity().getIntent().getStringExtra("productid");
            String s4 = getActivity().getIntent().getStringExtra("categoryid");
            new LoadProductDesc().execute("http://m.jimtrade.com/mobileapp.svc/getproductdetails/" + s3);
            new LoadProductSpec().execute("http://m.jimtrade.com/mobileapp.svc/prodkeyspec/" + s5 + "," + s4);
        }catch (NullPointerException npe)
        {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_product_details, container, false);
        return view;
    }

    class LoadProductDesc extends AsyncTask<String, String, String> {

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

                                            view1 = (View) view.findViewById(R.id.view1);
                                            view2 = (View) view.findViewById(R.id.view2);

                                            String name = c.getString(TAG_PRODUCT_NAME);

                                            String DESC = c.getString(TAG_PRODUCT_DESC);
                                            textView = (TextView) view.findViewById(R.id.product_name2);

                                            DESC = DESC.replace("<font style=text-transform:none;>", "");
                                            DESC = DESC.replace("<br>", "");
                                            DESC = DESC.replace("</font>", "");
                                            DESC = DESC.replace("<b>", "");
                                            DESC = DESC.replace("</b", "");
                                            DESC = DESC.replace("<li>", "");
                                            DESC = DESC.replace("</li>", "");
                                            DESC = DESC.replace("<td>", "");
                                            DESC = DESC.replace("</td>", "");
                                            DESC = DESC.replace("</B>", "");
                                            DESC = DESC.replace("<B>", "");
                                            DESC = DESC.replace("</tr>", "");
                                            DESC = DESC.replace("</li>", "");
                                            DESC = DESC.replace("<tr class=\"fnt\" bgcolor=\"E1E1E1\">", "");
                                            DESC = DESC.replace("<table width=`99%` cellpadding=`3` cellspacing=`1` border=`0` align=`center`>", "");
                                            DESC = DESC.replace("/table", "");
                                            DESC = DESC.replace("<>", "");

                                            String str = DESC.trim();
                                            textView.setText(str);
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
                        }
                        catch (NullPointerException ne)
                        {
                            Toast.makeText(getActivity(), "Check Internet Connectivity!!", Toast.LENGTH_LONG).show();
                        }
                    }

    }

    class LoadProductSpec extends AsyncTask<String, String, String> {

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


                                        view1 = (View) view.findViewById(R.id.view1);

                                        String spec = c.getString(TAG_PRODUCT_SPECIFICATIONS);
                                        spec = spec.replace("***", "\n\u2022 ");
                                        String str1 = spec.trim();
                                        str1 = str1.replace("<br>","");
                                        textView1 = (TextView) view.findViewById(R.id.spec);
                                        TextView textView11 = (TextView) view.findViewById(R.id.specifications);
                                        textView1.setText(str1);

                                        // Hide if empty Json Object
                                        if (str1.isEmpty() || str1 == null || str1 == "null")
                                        {
                                            textView11.setVisibility(view.GONE);
                                            textView11.setPadding(0, -1 * textView11.getHeight(), 0, 0);
                                            textView1.setVisibility(view.GONE);
                                            textView1.setPadding(0, -1 * textView1.getHeight(), 0, 0);
                                            view1.setVisibility(view.GONE);
                                            view1.setPadding(0, -1 * view1.getHeight(), 0, 0);
                                        }

                                    }
                                } else
                                {
                                    Log.d("Categories: ", "null");
                                }

                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e)
                    {
                        Log.d("Test", "Couldn't successfully parse the JSON response!");
                    }
                    catch (NullPointerException ne)
                    {
                        Toast.makeText(getActivity(), "Check Internet Connectivity12!!", Toast.LENGTH_LONG).show();
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
