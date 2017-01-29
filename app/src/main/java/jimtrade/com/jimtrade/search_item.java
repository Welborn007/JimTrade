package jimtrade.com.jimtrade;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;


public class search_item extends ListActivity {

    EditText search;
    Button button;

    String empty;
    ListView lv2;
    Dialog dialog;

    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;

    // ALL JSON node names
    private static final String TAG_ID = "categoryid";
    private static final String TAG_INDEX = "catgeoryindex";
    private static final String TAG_NAME = "directoryname";
    private static final String TAG_COUNT = "productcount";
    private static final String TAG_CAT_COUNT = "catcount";

    private static final String TAG_CATEGORY_COUNT = "catcount";
    private static final String TAG_CATEGORY_ID = "catid";
    private static final String TAG_CATEGORY_INDEX = "catindex";
    private static final String TAG_CATEGORY_NAME = "catname";

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String MyPREFERENCES1 = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    SharedPreferences.Editor editor1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_search_item);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        actionBar.setCustomView(cView);

        cView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getActionBar().setTitle("");
        getActionBar().setDisplayShowHomeEnabled(true);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();


        productList = new ArrayList<HashMap<String, String>>();
        search = (EditText) findViewById(R.id.autoComplete);
        button = (Button)findViewById(R.id.search);
        empty = search.getText().toString();
        final RelativeLayout content = (RelativeLayout) findViewById(R.id.rl1);
        final TextView textView = (TextView) findViewById(R.id.Categories);
        final TextView textView1 = (TextView) findViewById(R.id.Products);
        final View view1 = (View) findViewById(R.id.view);
        final View view2 = (View) findViewById(R.id.view1);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);

        new LoadMainCategories().execute("http://m.jimtrade.com/cat.json");

        try {

            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (isEmptyField(search)) return;

                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);

                    content.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    textView1.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                    lv2.setVisibility(View.GONE);

                    String s2 = search.getText().toString();

                    s2 = s2.replace(" ", "%20");
                    s2 = s2.replace("  ", "%20");
                    s2 = s2.replace("   ", "%20");

                    new LoadCategories().execute("http://m.jimtrade.com/mobileapp.svc/catsearch/" + s2);
                    productList.clear();
                }
            });

        }catch (NullPointerException npe)
        {

        }
    }


    private boolean isEmptyField (EditText editText){
        boolean result = editText.getText().toString().length() <= 0;
        if (result)
            Toast.makeText(search_item.this, "Please Enter Keyword!", Toast.LENGTH_SHORT).show();
        return result;
    }

    class LoadMainCategories extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // custom dialog
            dialog = new Dialog(search_item.this);
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
                if (isNetworkStatusAvialable(getApplicationContext())) {

                    JSONArray categories = new JSONArray(response);

                    if (categories != null) {
                        // looping through All categories

                        for (int i = 0; i <= categories.length(); i++) {

                            JSONObject c = categories.getJSONObject(i);

                            // Storing each json item values in variable
                            String id = c.getString(TAG_CATEGORY_ID);
                            String index = c.getString(TAG_CATEGORY_INDEX);
                            String name = c.getString(TAG_CATEGORY_NAME);
                            String count = c.getString(TAG_CATEGORY_COUNT);


                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_CATEGORY_ID, id);
                            map.put(TAG_CATEGORY_INDEX, index);
                            map.put(TAG_CATEGORY_NAME, name);
                            map.put(TAG_CATEGORY_COUNT, count);

                            // adding HashList to ArrayList
                            productList.add(map);

                            /**
                             * Updating parsed JSON data into ListView
                             * */


                            final SimpleAdapter adapter = new SimpleAdapter(search_item.this, productList, R.layout.grid, new String[]{
                                    TAG_CATEGORY_NAME, TAG_CATEGORY_COUNT}, new int[]{
                                    R.id.item1, R.id.item2});

                            lv2 = (ListView) findViewById(R.id.list1);
                            lv2.setAdapter(adapter);

                            lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    HashMap<String,String> map =(HashMap<String,String>)adapter.getItem(i);
                                    String categories = map.get(TAG_CATEGORY_NAME);
                                    String index = map.get(TAG_CATEGORY_INDEX);

                                    Intent intent = new Intent(search_item.this, Second_Level_Category.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("categories",categories);
                                    intent.putExtra("index",index);
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
                    Toast.makeText(getApplication(),"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                Log.d("Test", "Couldn't successfully parse the JSON response!");
            } catch (NullPointerException ne) {
                Toast.makeText(getApplicationContext(), "Check Internet Connectivity!!", Toast.LENGTH_LONG).show();
            }

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
            dialog = new Dialog(search_item.this);
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

        protected void onPostExecute(String response) {

            // dismiss the dialog after getting all categories
            dialog.dismiss();

            super.onPostExecute(response);
                    try {

                        if (isNetworkStatusAvialable (search_item.this)) {

                                JSONArray categories = new JSONArray(response);

                        if (categories != null) {
                            // looping through All categories


                            for (int i = 0; i < categories.length() && categories.length() != 0; i++) {
                                JSONObject c = categories.getJSONObject(i);

                                // Storing each json item values in variable
                                String id = c.getString(TAG_ID);
                                String index = c.getString(TAG_INDEX);
                                String name = c.getString(TAG_NAME);
                                String count = c.getString(TAG_COUNT);
                                String cat_count = c.getString(TAG_CAT_COUNT);

                                // creating new HashMap
                                HashMap<String, String> map = new HashMap<String, String>();

                                // adding each child node to HashMap key => value
                                map.put(TAG_ID, id);
                                map.put(TAG_INDEX, index);
                                map.put(TAG_NAME, name);
                                map.put(TAG_COUNT, count);
                                map.put(TAG_CAT_COUNT, cat_count);


                                // adding HashList to ArrayList
                                productList.add(map);

                               ListView lv1 = (ListView) findViewById(android.R.id.list);
                                final SimpleAdapter adapter = new SimpleAdapter(
                                        search_item.this, productList, R.layout.grid, new String[]{
                                        TAG_NAME, TAG_COUNT,TAG_CAT_COUNT}, new int[]{
                                        R.id.item1, R.id.item2,R.id.item3});

                                // updating listview
                                lv1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        } else {
                            Log.d("Categories: ", "null");
                        }

                        }
                        else
                        {
                            Toast.makeText(search_item.this,"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
                            return;
                        }

                    } catch (JSONException e) {
                        Log.d("Test", "Couldn't successfully parse the JSON response!");
                    } catch (NullPointerException ne) {
                        Toast.makeText(search_item.this, "No Product Found!!!", Toast.LENGTH_LONG).show();
                    }

        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id)
    {
        super.onListItemClick(l, v, pos, id);
        HashMap<String,String> map =(HashMap<String,String>)l.getItemAtPosition(pos);
        String categories = map.get(TAG_NAME);
        String index = map.get(TAG_INDEX);
        String count = map.get(TAG_COUNT);
        String cat_count = map.get(TAG_CAT_COUNT);

        Intent intent = new Intent(search_item.this,SubCategoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("categories", categories);
        intent.putExtra("index",index);
        intent.putExtra("count",count);
        intent.putExtra("catcount",cat_count);
        startActivity(intent);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String s1 = sharedpreferences.getString("Email",null);

        if(s1 == null) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_login, menu);
        }
        else
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_logout, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.MyJimtrade:
                // create intent to perform web search for this planet
                Intent intent = new Intent(this, My_JimTrade.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("hello","move");
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                editor = sharedpreferences.edit();
                editor.putString("hello","move");
                editor.commit();
                startActivity(intent);
                return true;

            case R.id.aboutus:
                Intent intent9 = new Intent(this,AboutUs.class);
                intent9.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent9);
                return true;

            case R.id.feedback:
                Intent intent5 = new Intent(this,Feedback.class);
                intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent5);
                return true;

            case  R.id.contact:
                Intent intent6 = new Intent(this,ContactUs.class);
                intent6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent6);
                return true;

            case R.id.faq:
                Intent intent7 = new Intent(this,FAQs.class);
                intent7.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent7);
                return true;

            case R.id.action_home:
                Intent intent2 = new Intent(this, HomeScreen.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                return true;

            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;

            case R.id.Login:

                Intent intent1 = new Intent(this, LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);

                sharedpreferences1 = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
                editor1 = sharedpreferences1.edit();

                String identifier = "Jim";
                editor1.putString("identifier", identifier);
                editor1.commit();
                return true;

            case R.id.Register:

                Intent intent3 = new Intent(this, RegisterNew.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent3);
                return true;

            case R.id.Logout:
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                editor.apply();

                // create intent to perform web search for this planet
                Intent intent4 = new Intent(this, HomeScreen.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent4);

                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
