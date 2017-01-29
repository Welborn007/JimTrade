package jimtrade.com.jimtrade.adapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

import jimtrade.com.jimtrade.Categories.Categories;
import jimtrade.com.jimtrade.Categories.Categories_Sliding_Fragments;
import jimtrade.com.jimtrade.HomeScreen;
import jimtrade.com.jimtrade.Inquiry.Inquiry_Login;
import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.Second_Level_Category;
import jimtrade.com.jimtrade.Send_Inquiry.Send_Inquiry_Product;
import jimtrade.com.jimtrade.SubCategoryActivity;
import jimtrade.com.jimtrade.app.AppController;
import jimtrade.com.jimtrade.model.Category;
import jimtrade.com.jimtrade.model.Products;

public class Category_Adapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Category> categoryItems;

    Dialog dialog;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    private static final String TAG = Categories_Sliding_Fragments.class.getSimpleName();

    public Category_Adapter(FragmentActivity activity,List<Category> categoryItems) {
        this.activity = activity;
        this.categoryItems = categoryItems;
    }

    @Override
    public int getCount() {
        return categoryItems.size();
    }

    @Override
    public Object getItem(int location) {
        return categoryItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView category_name,Category_id;
        TextView delete;

        try {

            if (inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) convertView = inflater.inflate(R.layout.category_adapter_xml, null);

            category_name = (TextView) convertView.findViewById(R.id.category_name);
            Category_id = (TextView) convertView.findViewById(R.id.category_id);
            delete = (TextView) convertView.findViewById(R.id.delete);

            // getting products data for the row
            final Category m = categoryItems.get(position);

            category_name.setText(String.valueOf(m.getCategory_name() + " (" + m.getProduct_count() + ")"));
            Category_id.setText(String.valueOf(m.getCategory_id()));

            sharedpreferences = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            final String z1 = sharedpreferences.getString("mem_id",null);

            // custom dialog
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_delete_cat);

            category_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, SubCategoryActivity.class);
                    intent.putExtra("categories",m.getCategory_name());
                    intent.putExtra("index",m.getCategory_index());
                    intent.putExtra("count",m.getProduct_count());
                    intent.putExtra("catcount",m.getCategory_count());
                    activity.startActivity(intent);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                dialog.show();

                }
            });

            TextView dialogButton = (TextView) dialog.findViewById(R.id.dialogButtonHOME);
            TextView dialogButton1 = (TextView) dialog.findViewById(R.id.dialogButtonEXIT);

            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    new DeleteCategory().execute("http://m.jimtrade.com/mobileapp.svc/deletemembercategories/" + z1 + "," + String.valueOf(m.getCategory_id()));

                }
            });

            dialogButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });


        }catch (NullPointerException npe){
            npe.printStackTrace();
        }

        return convertView;
    }


    class DeleteCategory extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // custom dialog
            dialog = new Dialog(activity);
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
            Intent intent = new Intent(activity, Categories.class);
            activity.finish();
            activity.startActivity(intent);
            dialog.dismiss();
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
