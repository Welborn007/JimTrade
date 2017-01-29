package jimtrade.com.jimtrade.Products_contents;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jimtrade.com.jimtrade.Inquiry.Inquiry_Login;
import jimtrade.com.jimtrade.ProductScreenFragments;
import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.Send_Inquiry.Send_Inquiry_Product;
import jimtrade.com.jimtrade.adapter.Products_info_Adapter;
import jimtrade.com.jimtrade.app.AppController;
import jimtrade.com.jimtrade.model.Products;



public class Product_Info_Activity extends Fragment {

    private static final String TAG = ProductScreenFragments.class.getSimpleName();
    private static String title;
    View view;
    private List<Products> catList = new ArrayList<Products>();
    private ListView listView;
    private Products_info_Adapter adapter;
    private static int page;
    private static final String TAG_PRODUCT_NAME = "productname";
    private static final String TAG_IMAGEPATH = "productimage";
    private static final String TAG_SUPPLIER_NAME = "productsupplier";
    private static final String TAG_SUPPLIER_EMAIL = "supplieremail";
    private static final String TAG_SUPPLIER_ID = "supplierid";
    private static final String TAG_PRODUCT_ID = "iproductid";
    private static final String TAG_SUPPLIER_ADDR = "supplieraddress";
    private static final String TAG_SUPPLIER_PHONE = "supplierphone";
    private static final String TAG_CATEGORY_NAME = "productcat";
    private static final String TAG_INDEX = "productcatindex";
    private static final String TAG_CAT_COUNT = "catcount";
    private static final String TAG_PRODUCT_COUNT = "productcount";
    private static final String TAG_PRODUCT_FEATURES = "productspec";

    public static final String MyPREFERENCES = "MyPrefs" ;

    SharedPreferences sharedpreferences;

    public static final String MyPREFERENCES1 = "MyPrefs1" ;

    SharedPreferences sharedpreferences1;
    SharedPreferences.Editor editor1;

    int id_count = 0;
    Dialog dialog;

    private static final String url = "http://m.jimtrade.com/mobileapp.svc/getproductdetails/";

    public static Product_Info_Activity newInstance(int i, String s) {
        Product_Info_Activity fragmentFirst = new Product_Info_Activity();
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

    public Product_Info_Activity()
    {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_product__info_, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {

            adapter = new Products_info_Adapter(getActivity(), catList);
            listView = (ListView) getView().findViewById(R.id.list2);
            listView.setAdapter(adapter);
            sharedpreferences1 = getActivity().getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
            editor1 = sharedpreferences1.edit();

            fetch();
        }catch (NullPointerException ne)
        {
            Toast.makeText(getActivity(),"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
        }

    }


    private void fetch() {

        // custom dialog
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressdialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

                // Creating volley request obj
                String s3 = getActivity().getIntent().getStringExtra("productid");
                JsonArrayRequest movieReq = new JsonArrayRequest(url + s3,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());
                                if (response == null) {
                                    Toast.makeText(getActivity(), "Empty data", Toast.LENGTH_LONG).show();
                                } else {
                                    // Parsing json
                                    for (int i = 0; i < response.length(); i++) {
                                        try {
                                            if (isNetworkStatusAvialable (getActivity())) {

                                                    JSONObject obj = response.getJSONObject(i);

                                                    String product_name = obj.getString(TAG_PRODUCT_NAME);
                                                    String supplier_name = obj.getString(TAG_SUPPLIER_NAME);
                                                    String Supplier_email = obj.getString(TAG_SUPPLIER_EMAIL);
                                                    String address = obj.getString(TAG_SUPPLIER_ADDR);
                                                    String phone = obj.getString(TAG_SUPPLIER_PHONE);
                                                    String product_id = obj.getString(TAG_PRODUCT_ID);
                                                    String supplier_id = obj.getString(TAG_SUPPLIER_ID);
                                                    String Category = obj.getString(TAG_CATEGORY_NAME);
                                                    String Index = obj.getString(TAG_INDEX);
                                                    String Count = obj.getString(TAG_PRODUCT_COUNT);
                                                    String Feat = obj.getString(TAG_PRODUCT_FEATURES);
                                                    String cat_count = obj.getString(TAG_CAT_COUNT);
                                                    String identifier = "0";

                                                    editor1.putString("prod_name",product_name);
                                                    editor1.putString("supp_name",supplier_name);
                                                    editor1.putString("supp_email",Supplier_email);
                                                    editor1.putString("prod_id",product_id);
                                                    editor1.putString("supp_id",supplier_id);
                                                    editor1.putString("supp_address", address);
                                                    editor1.putString("supp_phone", phone);
                                                    editor1.putString("identifier", identifier);
                                                    editor1.commit();

                                                    Products products = new Products();
                                                    id_count = id_count + 1;
                                                    products.setProductName(obj.getString(TAG_PRODUCT_NAME));
                                                    products.setThumbnailUrl(obj.getString(TAG_IMAGEPATH));
                                                    products.setCategoryName(obj.getString(TAG_CATEGORY_NAME));
                                                    products.setCategoryIndex(obj.getString(TAG_INDEX));
                                                    products.setCount(obj.getString(TAG_PRODUCT_COUNT));
                                                    products.setFeatures(obj.getString(TAG_PRODUCT_FEATURES));
                                                    products.setCategory_Count(obj.getString(TAG_CAT_COUNT));

                                                    // adding movie to movies array
                                                    catList.add(products);
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
                        String error1 =  error.toString();
                        Toast.makeText(getActivity(), error1, Toast.LENGTH_SHORT).show();
                    }
                });

        movieReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
                AppController.getInstance().addToRequestQueue(movieReq);
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

}

