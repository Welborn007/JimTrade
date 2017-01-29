package jimtrade.com.jimtrade.Supplier_contents;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.SubCategoryActivity;


public class Supplier_Categories_Fragment extends ListFragment
{
    private static String title;
    private static int page;
    View view;

    // ArrayList for Listview
    ArrayList<HashMap<String, String>> titlesArray;

    TextView textView;

    Dialog dialog;
    private static final String TAG_SUPPLIER_CATEGORY = "suppliercat";
    private static final String TAG_CAT_INDEX = "catindex";
    private static final String TAG_SUPPLIER_NAME = "suppliername";
    private static final String TAG_CAT_COUNT = "catcount";
    private static final String TAG_PRODUCT_COUNT = "productcount";
    ListView lv;

    public static Supplier_Categories_Fragment newInstance(int i, String s) {
        Supplier_Categories_Fragment fragmentThird = new Supplier_Categories_Fragment();
        Bundle args = new Bundle();
        args.putInt("3", page);
        args.putString("no.3", title);
        fragmentThird.setArguments(args);
        return fragmentThird;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("3", 0);
        title = getArguments().getString("no.3");
        titlesArray = new ArrayList<HashMap<String, String>>();
        try {
            String s3 = getActivity().getIntent().getStringExtra("supplierid");
            new LoadCategories().execute("http://m.jimtrade.com/mobileapp.svc/suppliercat/" + s3);
        }
        catch (NullPointerException npe)
        {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_supplier_categories, container, false);
        return view;
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
            hidePDialog();
            super.onPostExecute(response);

                    try {

                        if (isNetworkStatusAvialable (getActivity())) {


                                JSONArray categories = new JSONArray(response);
                                titlesArray = new ArrayList();

                                if (categories != null) {
                                    // looping through All categories

                                    for (int i = 0; i <= categories.length(); i++) {

                                        JSONObject c = categories.getJSONObject(i);

                                        // Storing each json item values in variable

                                        String name = c.getString(TAG_SUPPLIER_CATEGORY);
                                        String index = c.getString(TAG_CAT_INDEX);
                                        String supplier_name = c.getString(TAG_SUPPLIER_NAME);
                                        String count = c.getString(TAG_PRODUCT_COUNT);
                                        final String cat_count = c.getString(TAG_CAT_COUNT);

                                        // creating new HashMap
                                        HashMap<String, String> map = new HashMap<String, String>();

                                        map.put(TAG_SUPPLIER_CATEGORY, name);
                                        map.put(TAG_CAT_INDEX, index);
                                        map.put(TAG_PRODUCT_COUNT,count);
                                        map.put(TAG_CAT_COUNT,cat_count);

                                        titlesArray.add(map);

                                        ListAdapter adapter = new SimpleAdapter(
                                                getActivity(), titlesArray, R.layout.grid, new String[]{
                                                TAG_SUPPLIER_CATEGORY, TAG_CAT_INDEX}, new int[]{
                                                R.id.item1, R.id.indexdemo});

                                        // updating listview
                                        setListAdapter(adapter);
                                        lv = getListView();


                                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition(position);
                                                String categories = map.get(TAG_SUPPLIER_CATEGORY);
                                                String index = map.get(TAG_CAT_INDEX);
                                                String cat_count = map.get(TAG_CAT_COUNT);
                                                String product_count = map.get(TAG_PRODUCT_COUNT);
                                                Intent intent = new Intent(getActivity(), SubCategoryActivity.class);

                                                intent.putExtra("categories", categories);
                                                intent.putExtra("index", index);
                                                intent.putExtra("count",product_count);
                                                intent.putExtra("catcount",cat_count);
                                                startActivity(intent);
                                            }

                                        });
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

    private void hidePDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
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
