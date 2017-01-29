package jimtrade.com.jimtrade.Categories;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jimtrade.com.jimtrade.ProductScreenFragments;
import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.adapter.Category_Adapter;
import jimtrade.com.jimtrade.adapter.Products_info_Adapter;
import jimtrade.com.jimtrade.app.AppController;
import jimtrade.com.jimtrade.model.Category;
import jimtrade.com.jimtrade.model.Products;


public class Subscribed extends Fragment {

    private static final String TAG = Categories_Sliding_Fragments.class.getSimpleName();

    private static String title;
    View view;
    private static int page;

    private List<Category> catList = new ArrayList<Category>();
    private ListView listView;
    private Category_Adapter adapter;

    private static final String TAG_CATEGORY_NAME = "categoryname";
    private static final String TAG_CATEGORY_ID = "categoryid";
    private static final String TAG_CATEGORY_INDEX = "categoryindex";
    private static final String TAG_PRODUCT_COUNT = "productcount";
    private static final String TAG_CAT_COUNT = "catcount";

    public static final String MyPREFERENCES = "MyPrefs" ;

    SharedPreferences sharedpreferences;

    Dialog dialog;

    private static final String url = "http://m.jimtrade.com/mobileapp.svc/getmembercategories/";

    public static Subscribed newInstance(int i, String s)
    {
        Subscribed fragmentFirst = new Subscribed();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_subscribed, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            adapter = new Category_Adapter(getActivity(), catList);

            listView = (ListView) getView().findViewById(R.id.list2);
            listView.setAdapter(adapter);
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

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String z1 = sharedpreferences.getString("mem_id",null);

        JsonArrayRequest movieReq = new JsonArrayRequest(url + z1,
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

                                        Category category = new Category();

                                        category.setCategory_id(obj.getString(TAG_CATEGORY_ID));
                                        category.setCategory_name(obj.getString(TAG_CATEGORY_NAME));
                                        category.setCategory_index(obj.getString(TAG_CATEGORY_INDEX));
                                        category.setProduct_count(obj.getString(TAG_PRODUCT_COUNT));
                                        category.setCategory_count(obj.getString(TAG_CAT_COUNT));

                                        // adding movie to movies array
                                        catList.add(category);
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
