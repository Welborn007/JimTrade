package jimtrade.com.jimtrade;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
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


public class Subcategories_Fragment extends ListFragment {
    View view;

    Dialog dialog;

    ListView lv;

    RelativeLayout r1;

    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;

    TextView btnLoadMore,heading;

    // Set the limit of objects to show
    private int i = 0;
    private int j = 2;

    // ALL JSON node names
    private static final String TAG_ID = "categoryid";
    private static final String TAG_INDEX = "catgeoryindex";
    private static final String TAG_NAME = "directoryname";
    private static final String TAG_COUNT = "productcount";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_subcategories_, container, false);
        r1 = (RelativeLayout) view.findViewById(R.id.category_content);
        heading = (TextView) view.findViewById(R.id.heading);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            productList = new ArrayList<HashMap<String, String>>();
            String s1 = getActivity().getIntent().getStringExtra("index");

            new LoadCategories().execute("http://m.jimtrade.com/mobileapp.svc/GetSubcategory/" + s1);
        }catch (NullPointerException ne)
        {
            Toast.makeText(getActivity(),"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
        }

    }

    class LoadCategories extends AsyncTask<String, String, String> {

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

                        if (categories != null) {
                            // looping through All categories

                            for (i = 0; i <= j; i++) {

                                JSONObject c = categories.getJSONObject(i);

                                // Storing each json item values in variable
                                String id = c.getString(TAG_ID);
                                String index = c.getString(TAG_INDEX);
                                String name = c.getString(TAG_NAME);
                                String count = c.getString(TAG_COUNT);


                                // creating new HashMap
                                HashMap<String, String> map = new HashMap<String, String>();

                                // adding each child node to HashMap key => value
                                map.put(TAG_ID, id);
                                map.put(TAG_INDEX, index);
                                map.put(TAG_NAME, name);
                                map.put(TAG_COUNT, count);

                                // adding HashList to ArrayList
                                productList.add(map);

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

            try {
                /**
                 * Updating parsed JSON data into ListView
                 * */
                ListAdapter adapter = new SimpleAdapter(
                        getActivity(), productList, R.layout.grid, new String[]{
                        TAG_NAME, TAG_COUNT}, new int[]{
                        R.id.item1, R.id.item2});


                // updating listview
                setListAdapter(adapter);
                lv = getListView();

                // LoadMore button
                btnLoadMore = (TextView) view.findViewById(R.id.load_more);


                String s3 = getActivity().getIntent().getStringExtra("count");
                String s4 = getActivity().getIntent().getStringExtra("catcount");
                btnLoadMore.setText(s3 + " Products in " + s4 + " Categories");

                String s9 = getActivity().getIntent().getStringExtra("categories");
                heading.setText(s9);

                if (productList.isEmpty()) {
                    r1.setVisibility(view.GONE);
                    r1.setPadding(0, -1 * r1.getHeight(), 0, 0);
                }

                btnLoadMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productList.clear();
                        String s1 = getActivity().getIntent().getStringExtra("index");
                        String s2 = getActivity().getIntent().getStringExtra("categories");

                        Intent intent = new Intent(getActivity(), Second_Level_Category.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("index", s1);
                        intent.putExtra("categories", s2);
                        startActivity(intent);
                    }
                });

            }catch (NullPointerException npe)
            {
                npe.printStackTrace();
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



