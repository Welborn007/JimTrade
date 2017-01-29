package jimtrade.com.jimtrade;

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
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

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


import jimtrade.com.jimtrade.adapter.ProductsAdapter;


import jimtrade.com.jimtrade.app.AppController;
import jimtrade.com.jimtrade.model.Products;


public class Product_Listing extends Fragment {
    private static final String TAG = SubCategoryActivity.class.getSimpleName();
    private static final String TAG_IMAGEPATH = "imagepath";
    private static final String TAG_PRODUCTID = "productid";
    private static final String TAG_PRODUCTNAME = "productname";
    private static final String TAG_SUPPLIERID = "supplierid";
    private static final String TAG_SUPPLIERNAME = "suppliername";
    private static final String TAG_CATEGORY_ID = "catid";
    private static final String TAG_PRODUCT_COUNT = "productcount";
    private static final String TAG_SUPPLIER_EMAIL = "supplieremail";
    private static final String TAG_SUPPLIER_ADDR = "supplieraddress";
    private static final String TAG_SUPPLIER_PHONE = "supplierphone";

    Dialog dialog;
    private List<Products> catList = new ArrayList<Products>();
    private ListView listView;
    private ProductsAdapter adapter;
    int id_count = 0;

    private int current_page = 0;
    int mPreLast;
    Button footerView;
    View view;

    public Product_Listing() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product__listing, container, false);

        listView = (ListView) view.findViewById(R.id.list1);
        footerView = (Button) view.findViewById(R.id.click);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new ProductsAdapter(getActivity(), catList);
        listView.setAdapter(adapter);
        try {
            int currentPosition = listView.getFirstVisiblePosition();
            listView.setSelectionFromTop(currentPosition + 1, 0);

            footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listView.smoothScrollToPosition(0);
                }
            });

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int lastItem = firstVisibleItem + visibleItemCount;
                    if (lastItem == totalItemCount) {
                        if (mPreLast != lastItem) {
                            mPreLast = lastItem;
                            new LoadProductDesc().execute();
                        }
                    }
                    if (totalItemCount > 20) {
                        footerView.setVisibility(getView().VISIBLE);
                    }

                    if (firstVisibleItem < 10) {
                        footerView.setVisibility(getView().INVISIBLE);
                    }
                }
            });

            new LoadProductDesc().execute();
        }catch(NullPointerException npe)
        {

        }
    }



    private void hidePDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    class LoadProductDesc extends AsyncTask<String, String, String> {

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
            String s1 = getActivity().getIntent().getStringExtra("index");
            current_page += 1;
            final String url = "http://m.jimtrade.com/mobileapp.svc/catproduct/" + s1 + "," + current_page;

                    JsonArrayRequest movieReq = new JsonArrayRequest(url,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    Log.d(TAG, response.toString());

                                    try
                                    {
                                        if (isNetworkStatusAvialable (getActivity())) {

                                                // Parsing json
                                                for (int i = 0; i < response.length() && response.length() != 0; i++)
                                                {
                                                    JSONObject obj = response.getJSONObject(i);
                                                    Products products = new Products();
                                                    id_count = id_count + 1;
                                                    products.setProductName(obj.getString(TAG_PRODUCTNAME));
                                                    products.setThumbnailUrl(obj.getString(TAG_IMAGEPATH));
                                                    products.setProductID(obj.getInt(TAG_PRODUCTID));
                                                    products.setSupplierName(obj.getString(TAG_SUPPLIERNAME));
                                                    products.setSupplierID(obj.getInt(TAG_SUPPLIERID));
                                                    products.setCategoryID(obj.getInt(TAG_CATEGORY_ID));
                                                    products.setSupp_addr(obj.getString(TAG_SUPPLIER_ADDR));
                                                    products.setSupp_email(obj.getString(TAG_SUPPLIER_EMAIL));
                                                    products.setSupp_phone(obj.getString(TAG_SUPPLIER_PHONE));
                                                    String count = obj.getString(TAG_PRODUCT_COUNT);

                                                    catList.add(products);

                                                    TextView textView = (TextView) getView().findViewById(R.id.ListofProducts);
                                                    textView.setText(String.valueOf("Products 1 to " + id_count + " of " + count));
                                                }

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

