package jimtrade.com.jimtrade;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
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

import jimtrade.com.jimtrade.adapter.ImageZoom_Adapter;
import jimtrade.com.jimtrade.adapter.Products_info_Adapter;
import jimtrade.com.jimtrade.app.AppController;
import jimtrade.com.jimtrade.model.Products;


public class Image_zoom extends Activity {

    private static final String TAG = ProductScreenFragments.class.getSimpleName();
    View view;
    private List<Products> catList = new ArrayList<Products>();
    private ListView listView;
    private ImageZoom_Adapter adapter;
    private static final String TAG_IMAGEPATH = "productimage";
    Dialog dialog;

    private static final String url = "http://m.jimtrade.com/mobileapp.svc/getproductdetails/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_image_zoom);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        try {
            adapter = new ImageZoom_Adapter(this, catList);

            listView = (ListView) findViewById(R.id.list2);
            ImageButton cancel = (ImageButton) findViewById(R.id.cancel_img);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            listView.setAdapter(adapter);

            fetch();
        }catch (NullPointerException npe)
        {

        }
    }

    private void fetch() {

        // custom dialog
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressdialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        // Creating volley request obj
        String s3 = getIntent().getStringExtra("productid");
        JsonArrayRequest movieReq = new JsonArrayRequest(url + s3,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        if (response == null) {
                            Toast.makeText(Image_zoom.this, "Empty data", Toast.LENGTH_LONG).show();
                        } else {
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    if (isNetworkStatusAvialable (getApplicationContext())) {

                                            JSONObject obj = response.getJSONObject(i);
                                            Products products = new Products();
                                            products.setThumbnailUrl(obj.getString(TAG_IMAGEPATH));
                                            // adding movie to movies array
                                            catList.add(products);
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (NullPointerException ne) {
                                    Toast.makeText(Image_zoom.this, "Check Internet Connectivity!!", Toast.LENGTH_LONG).show();
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
