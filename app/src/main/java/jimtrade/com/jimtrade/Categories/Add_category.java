package jimtrade.com.jimtrade.Categories;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.adapter.Category_Adapter;
import jimtrade.com.jimtrade.adapter.Category_add_Adapter;
import jimtrade.com.jimtrade.model.Category;


public class Add_category extends Fragment {

    private static final String TAG = Categories_Sliding_Fragments.class.getSimpleName();

    private static final String TAG_CATEGORY_NAME = "categoryname";
    private static final String TAG_CATEGORY_ID = "categoryid";
    private static final String TAG_CATEGORY_INDEX = "categoryindex";
    private static final String TAG_PRODUCT_COUNT = "productcount";
    private static final String TAG_CAT_COUNT = "catcount";

    Dialog dialog;

    private static String title;
    View view;
    private static int page;

    private List<Category> catList = new ArrayList<Category>();
    private ListView listView;
    private Category_add_Adapter adapter;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    EditText search;
    Button button;

    public static Add_category newInstance(int i, String s)
    {
        Add_category fragmentSecond = new Add_category();
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            adapter = new Category_add_Adapter(getActivity(), catList);

            listView = (ListView) getView().findViewById(R.id.list2);
            listView.setAdapter(adapter);
        }catch (NullPointerException ne)
        {
            Toast.makeText(getActivity(),"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_category, container, false);


        try {

            search = (EditText) view.findViewById(R.id.autoComplete);
            button = (Button)view.findViewById(R.id.search);
            String empty = search.getText().toString();
            final RelativeLayout content = (RelativeLayout) view.findViewById(R.id.rl1);
            final TextView textView = (TextView) view.findViewById(R.id.Categories);

            final View view1 = (View) view.findViewById(R.id.view);
            final View view2 = (View) view.findViewById(R.id.view1);

            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (isEmptyField(search)) return;

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);

                    content.setVisibility(view.VISIBLE);
                    textView.setVisibility(view.VISIBLE);
                    view1.setVisibility(view.VISIBLE);
                    view2.setVisibility(view.VISIBLE);

                    String s2 = search.getText().toString();

                    s2 = s2.replace(" ", "%20");
                    s2 = s2.replace("  ", "%20");
                    s2 = s2.replace("   ", "%20");

                    sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    final String z1 = sharedpreferences.getString("mem_id", null);

                    new AddCategory().execute("http://m.jimtrade.com/mobileapp.svc/membercatsearch/" + s2 + "," + z1);

                }
            });
        }catch (NullPointerException ne)
        {
            Toast.makeText(getActivity(),"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private boolean isEmptyField (EditText editText){
        boolean result = editText.getText().toString().length() <= 0;
        if (result)
            Toast.makeText(getActivity(), "Please Enter Keyword!", Toast.LENGTH_SHORT).show();
        return result;
    }

    class AddCategory extends AsyncTask<String, String, String> {

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
            dialog.dismiss();

            super.onPostExecute(response);

            try {

                if (isNetworkStatusAvialable (getActivity())) {

                    JSONArray categories = new JSONArray(response);

                    if (categories != null) {
                        // looping through All categories


                        for (int i = 0; i < categories.length() && categories.length() != 0; i++) {
                            JSONObject obj = categories.getJSONObject(i);

                            Category category = new Category();

                            category.setCategory_id(obj.getString(TAG_CATEGORY_ID));
                            category.setCategory_name(obj.getString(TAG_CATEGORY_NAME));
                            category.setCategory_index(obj.getString(TAG_CATEGORY_INDEX));
                            category.setProduct_count(obj.getString(TAG_PRODUCT_COUNT));
                            category.setCategory_count(obj.getString(TAG_CAT_COUNT));

                            // adding movie to movies array
                            catList.add(category);
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

                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.d("Test", "Couldn't successfully parse the JSON response!");
            } catch (NullPointerException ne) {
                Toast.makeText(getActivity(), "No Category Found!!!", Toast.LENGTH_LONG).show();
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
