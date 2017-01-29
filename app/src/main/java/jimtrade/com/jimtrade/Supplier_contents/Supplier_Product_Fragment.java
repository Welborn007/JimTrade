package jimtrade.com.jimtrade.Supplier_contents;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

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
import java.util.List;

import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.SubCategoryActivity;
import jimtrade.com.jimtrade.adapter.SupplierProductAdapter;
import jimtrade.com.jimtrade.app.AppController;
import jimtrade.com.jimtrade.model.SupplierProducts;


public class Supplier_Product_Fragment extends Fragment
{
    View view;

    private static String title;
    private static int page;
    private static final String TAG = SubCategoryActivity.class.getSimpleName();
    private static final String TAG_IMAGEPATH = "imagepath";
    private static final String TAG_PRODUCTID = "productid";
    private static final String TAG_PRODUCTNAME = "productname";
    private static final String TAG_CATNAME = "catname";
    private static final String TAG_CATEGORY_ID = "catgeoryid";
    private static final String TAG_PRODUCT_COUNT = "productcount";
    private static final String TAG_SUPPLIER_PRODUCT_COUNT = "supplierproductcount";
    private static final String TAG_CAT_COUNT = "catcount";
    private static final String TAG_CATEGORY_INDEX = "categoryindex";
    private static final String TAG_SUPPLIER_NAME = "suppliername";
    private static final String TAG_SUPPLIER_EMAIL = "supplieremail";
    private static final String TAG_SUPPLIER_ADDR = "supplieraddress";
    private static final String TAG_SUPPLIER_PHONE = "supplierphone";
    private static final String TAG_SUPPLIERID = "supplierid";


    Dialog dialog;
    private List<SupplierProducts> catList = new ArrayList<SupplierProducts>();
    private ListView listView;
    private SupplierProductAdapter adapter;
    int id_count = 0;

    public Supplier_Product_Fragment()
    {}

    public static Supplier_Product_Fragment newInstance(int i, String s)
    {
        Supplier_Product_Fragment fragmentSecond = new Supplier_Product_Fragment();
        Bundle args = new Bundle();
        args.putInt("1", page);
        args.putString("no.1", title);
        fragmentSecond.setArguments(args);
        return fragmentSecond;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("1", 0);
        title = getArguments().getString("no.1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_supplier__product_, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {

            listView = (ListView) getView().findViewById(R.id.list1);
            adapter = new SupplierProductAdapter(getActivity(), catList);
            listView.setAdapter(adapter);
            new LoadProduct().execute();
        }catch (NullPointerException npe)
        {

        }
    }

    private void hidePDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    class LoadProduct extends AsyncTask<String, String, String> {

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

            super.onPostExecute(response);
            // Creating volley request obj
            String s1 = getActivity().getIntent().getStringExtra("supplierid");

            final String url = "http://m.jimtrade.com/mobileapp.svc/supplierproduct/" + s1;

                    JsonArrayRequest movieReq = new JsonArrayRequest(url,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    Log.d(TAG, response.toString());


                                    // Parsing json
                                    for (int i = 0; i < response.length(); i++) {
                                        try {

                                            if (isNetworkStatusAvialable (getActivity())) {

                                                    JSONObject obj = response.getJSONObject(i);
                                                    SupplierProducts products = new SupplierProducts();
                                                    id_count = id_count + 1;
                                                    products.setProductName(obj.getString(TAG_PRODUCTNAME));
                                                    products.setThumbnailUrl(obj.getString(TAG_IMAGEPATH));
                                                    products.setProductID(obj.getInt(TAG_PRODUCTID));
                                                    products.setCatname(obj.getString(TAG_CATNAME));
                                                    products.setCategoryID(obj.getInt(TAG_CATEGORY_ID));
                                                    String count = obj.getString(TAG_SUPPLIER_PRODUCT_COUNT);
                                                    products.setCategoryIndex(obj.getString(TAG_CATEGORY_INDEX));
                                                    products.setCount(obj.getString(TAG_PRODUCT_COUNT));
                                                    products.setCategory_count(obj.getString(TAG_CAT_COUNT));
                                                    products.setSupplier_product_count(obj.getString(TAG_SUPPLIER_PRODUCT_COUNT));
                                                    products.setSupplierName(obj.getString(TAG_SUPPLIER_NAME));
                                                    products.setSupp_addr(obj.getString(TAG_SUPPLIER_ADDR));
                                                    products.setSupp_email(obj.getString(TAG_SUPPLIER_EMAIL));
                                                    products.setSupp_phone(obj.getString(TAG_SUPPLIER_PHONE));
                                                    products.setSupplierID(obj.getString(TAG_SUPPLIERID));

                                                    String supplier_name = obj.getString(TAG_SUPPLIER_NAME);

                                                    // adding movie to movies array
                                                    catList.add(products);

                                                    TextView textView = (TextView) getView().findViewById(R.id.ListofProducts);
                                                    textView.setText(String.valueOf("Total " + count + " Products"));


                                            }
                                            else
                                            {
                                                Toast.makeText(getActivity(),"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
                                                return;
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (NullPointerException ne) {
                                            Toast.makeText(getActivity(), "Check Internet Connectivity!!", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                    hidePDialog();

                                    // notifying list adapter about data changes
                                    // so that it renders the list view with updated data
                                    adapter.notifyDataSetChanged();

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            hidePDialog();

                        }
                    });

            movieReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    // Adding request to request queue
                    AppController.getInstance().addToRequestQueue(movieReq);

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
