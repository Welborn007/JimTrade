package jimtrade.com.jimtrade.Products_contents;

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
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
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

import jimtrade.com.jimtrade.Inquiry.Inquiry_Login;
import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.Send_Inquiry.Send_Inquiry_Product;
import jimtrade.com.jimtrade.SupplierScreenFragments;
import jimtrade.com.jimtrade.adapter.Products_info_Adapter;


public class Supplier_details_Fragment_Product_Screen extends Fragment
{
    private static String title;
    private static int page;
    View view,view1,view2,view3;
    private static final String TAG_PRODUCT_NAME = "productname";
    private static final String TAG_IMAGEPATH = "productimage";
    private static final String TAG_SUPPLIER_EMAIL = "supplieremail";
    private static final String TAG_PRODUCT_ID = "iproductid";
    private static final String TAG_PRODUCT_SUPPLIER = "productsupplier";
    private static final String TAG_SUPPLIER_ADDR = "supplieraddress";
    private static final String TAG_SUPPLIER_FAX = "supplierfax";
    private static final String TAG_SUPPLIER_PHONE = "supplierphone";
    private static final String TAG_SUPPLIER_ID = "supplierid";

    TextView textView,textView1,textView2,textView3,textView4,textView5,textView6,textView7;
    Dialog dialog;

    ImageView img1,img2,img3,img4,img5;
    TableLayout tb1;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String MyPREFERENCES1 = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    SharedPreferences.Editor editor1;

    Button inquiry;

    public static Supplier_details_Fragment_Product_Screen newInstance(int i, String s) {
        Supplier_details_Fragment_Product_Screen fragmentFourth = new Supplier_details_Fragment_Product_Screen();
        Bundle args = new Bundle();
        args.putInt("3", page);
        args.putString("no.3", title);
        fragmentFourth.setArguments(args);
        return fragmentFourth;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("3", 0);
        title = getArguments().getString("no.3");
        try {
            String s3 = getActivity().getIntent().getStringExtra("productid");
            new Loadsupplierdetails().execute("http://m.jimtrade.com/mobileapp.svc/getproductdetails/" + s3);
        }catch (NullPointerException npe)
        {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_supplier_details__fragment__product__screen, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences1 = getActivity().getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
        final String s1 = sharedpreferences.getString("Email",null);
        editor1 = sharedpreferences1.edit();

        Button inquire = (Button) getView().findViewById(R.id.inquiry);

        inquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s1 == null) {
                    Intent intent = new Intent(getActivity(), Inquiry_Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), Send_Inquiry_Product.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intent);
                }
            }
        });

    }


    class Loadsupplierdetails extends AsyncTask<String, String, String> {

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

                                JSONArray jsonArray = new JSONArray(response);

                                if (jsonArray != null) {
                                    // looping through All categories

                                    for (int i = 0; i <= jsonArray.length(); i++) {

                                        JSONObject c = jsonArray.getJSONObject(i);

                                        // Storing each json item values in variable

                                        final String supplier_name = c.getString(TAG_PRODUCT_SUPPLIER);
                                        String address = c.getString(TAG_SUPPLIER_ADDR);
                                        String fax = c.getString(TAG_SUPPLIER_FAX);
                                        String phone = c.getString(TAG_SUPPLIER_PHONE);
                                        String Id = c.getString(TAG_SUPPLIER_ID);
                                        String product_name = c.getString(TAG_PRODUCT_NAME);
                                        String supp_email = c.getString(TAG_SUPPLIER_EMAIL);
                                        String product_id = c.getString(TAG_PRODUCT_ID);
                                        String identifier = "0";


                                        textView = (TextView) view.findViewById(R.id.suppname);
                                        TextView textView00 = (TextView) view.findViewById(R.id.name);
                                        textView1 = (TextView) view.findViewById(R.id.suppadd);
                                        TextView textView11 = (TextView) view.findViewById(R.id.address);
                                        textView2 = (TextView) view.findViewById(R.id.faxno);
                                        TextView textView21 = (TextView) view.findViewById(R.id.fax);
                                        textView3 = (TextView) view.findViewById(R.id.phoneno1);
                                        textView4 = (TextView) view.findViewById(R.id.phoneno2);
                                        textView5 = (TextView) view.findViewById(R.id.phoneno3);
                                        textView6 = (TextView) view.findViewById(R.id.phoneno4);
                                        textView7 = (TextView) view.findViewById(R.id.phoneno5);

                                        TextView textView31 = (TextView) view.findViewById(R.id.phone);

                                        img1 = (ImageView) view.findViewById(R.id.phone_icon1);
                                        img2 = (ImageView) view.findViewById(R.id.phone_icon2);
                                        img3 = (ImageView) view.findViewById(R.id.phone_icon3);
                                        img4 = (ImageView) view.findViewById(R.id.phone_icon4);
                                        img5 = (ImageView) view.findViewById(R.id.phone_icon5);

                                        final String str4 = Id;

                                        String str = supplier_name;

                                        final String str1 = address;
                                        String str2 = fax;
                                        final String str3 = phone;

                                        textView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getActivity(),SupplierScreenFragments.class);

                                                intent.putExtra("supplierid",str4);

                                                sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                                editor = sharedpreferences.edit();
                                                editor.putString("supplier_header", supplier_name);
                                                editor.commit();

                                                startActivity(intent);
                                            }
                                        });

                                        view1 = (View) view.findViewById(R.id.view1);
                                        view2 = (View) view.findViewById(R.id.view2);
                                        view3 = (View) view.findViewById(R.id.view3);

                                        tb1= (TableLayout) view.findViewById(R.id.phone_holder);


                                        // Hide if empty Json Object
                                        if (str.isEmpty()) {
                                            textView00.setVisibility(view.GONE);
                                            textView00.setPadding(0, -1 * textView00.getHeight(), 0, 0);
                                            textView.setVisibility(view.GONE);
                                            textView.setPadding(0, -1 * textView.getHeight(), 0, 0);
                                            view1.setVisibility(view.GONE);
                                            view1.setPadding(0, -1 * view1.getHeight(), 0, 0);
                                        }

                                        // Hide if empty Json Object
                                        if (str1.isEmpty()) {
                                            textView11.setVisibility(view.GONE);
                                            textView11.setPadding(0, -1 * textView11.getHeight(), 0, 0);
                                            textView1.setVisibility(view.GONE);
                                            textView1.setPadding(0, -1 * textView1.getHeight(), 0, 0);
                                            view2.setVisibility(view.GONE);
                                            view2.setPadding(0, -1 * view2.getHeight(), 0, 0);
                                        }

                                        // Hide if empty Json Object
                                        if (str2.isEmpty()) {
                                            textView21.setVisibility(view.GONE);
                                            textView21.setPadding(0, -1 * textView21.getHeight(), 0, 0);
                                            textView2.setVisibility(view.GONE);
                                            textView2.setPadding(0, -1 * textView2.getHeight(), 0, 0);
                                            view3.setVisibility(view.GONE);
                                            view3.setPadding(0, -1 * view3.getHeight(), 0, 0);
                                        }

                                        // Hide if empty Json Object
                                        if (str3.isEmpty()) {
                                            tb1.setVisibility(View.GONE);
                                            textView31.setVisibility(view.GONE);
                                            textView31.setPadding(0, -1 * textView31.getHeight(), 0, 0);
                                            textView3.setVisibility(view.GONE);
                                            textView3.setPadding(0, -1 * textView3.getHeight(), 0, 0);
                                            view3.setVisibility(view.GONE);
                                            view3.setPadding(0, -1 * view3.getHeight(), 0, 0);
                                        }

                                        if(str1.isEmpty() && str2.isEmpty() && str3.isEmpty())
                                        {
                                            textView11.setVisibility(view.GONE);
                                            textView11.setPadding(0, -1 * textView11.getHeight(), 0, 0);
                                            textView1.setVisibility(view.GONE);
                                            textView1.setPadding(0, -1 * textView1.getHeight(), 0, 0);
                                            view1.setVisibility(view.GONE);
                                            view1.setPadding(0, -1 * view1.getHeight(), 0, 0);
                                            view2.setVisibility(view.GONE);
                                            view2.setPadding(0, -1 * view2.getHeight(), 0, 0);

                                            textView21.setVisibility(view.GONE);
                                            textView21.setPadding(0, -1 * textView21.getHeight(), 0, 0);
                                            textView2.setVisibility(view.GONE);
                                            textView2.setPadding(0, -1 * textView2.getHeight(), 0, 0);
                                            view3.setVisibility(view.GONE);
                                            view3.setPadding(0, -1 * view3.getHeight(), 0, 0);

                                            tb1.setVisibility(View.GONE);
                                            textView31.setVisibility(view.GONE);
                                            textView31.setPadding(0, -1 * textView31.getHeight(), 0, 0);
                                            textView3.setVisibility(view.GONE);
                                            textView3.setPadding(0, -1 * textView3.getHeight(), 0, 0);
                                        }

                                        if(str1.isEmpty() && str2.isEmpty())
                                        {
                                            textView11.setVisibility(view.GONE);
                                            textView11.setPadding(0, -1 * textView11.getHeight(), 0, 0);
                                            textView1.setVisibility(view.GONE);
                                            textView1.setPadding(0, -1 * textView1.getHeight(), 0, 0);
                                            view2.setVisibility(view.GONE);
                                            view2.setPadding(0, -1 * view2.getHeight(), 0, 0);

                                            textView21.setVisibility(view.GONE);
                                            textView21.setPadding(0, -1 * textView21.getHeight(), 0, 0);
                                            textView2.setVisibility(view.GONE);
                                            textView2.setPadding(0, -1 * textView2.getHeight(), 0, 0);
                                            view3.setVisibility(view.GONE);
                                            view3.setPadding(0, -1 * view3.getHeight(), 0, 0);
                                        }

                                        if(str2.isEmpty() && str3.isEmpty())
                                        {
                                            textView21.setVisibility(view.GONE);
                                            textView21.setPadding(0, -1 * textView21.getHeight(), 0, 0);
                                            textView2.setVisibility(view.GONE);
                                            textView2.setPadding(0, -1 * textView2.getHeight(), 0, 0);
                                            view2.setVisibility(view.GONE);
                                            view2.setPadding(0, -1 * view2.getHeight(), 0, 0);
                                            view3.setVisibility(view.GONE);
                                            view3.setPadding(0, -1 * view3.getHeight(), 0, 0);

                                            tb1.setVisibility(View.GONE);
                                            textView31.setVisibility(view.GONE);
                                            textView31.setPadding(0, -1 * textView31.getHeight(), 0, 0);
                                            textView3.setVisibility(view.GONE);
                                            textView3.setPadding(0, -1 * textView3.getHeight(), 0, 0);
                                        }

                                        if(str1.isEmpty() && str3.isEmpty())
                                        {
                                            textView11.setVisibility(view.GONE);
                                            textView11.setPadding(0, -1 * textView11.getHeight(), 0, 0);
                                            textView1.setVisibility(view.GONE);
                                            textView1.setPadding(0, -1 * textView1.getHeight(), 0, 0);
                                            view2.setVisibility(view.GONE);
                                            view2.setPadding(0, -1 * view2.getHeight(), 0, 0);

                                            tb1.setVisibility(View.GONE);
                                            textView31.setVisibility(view.GONE);
                                            textView31.setPadding(0, -1 * textView31.getHeight(), 0, 0);
                                            textView3.setVisibility(view.GONE);
                                            textView3.setPadding(0, -1 * textView3.getHeight(), 0, 0);
                                            view3.setVisibility(view.GONE);
                                            view3.setPadding(0, -1 * view3.getHeight(), 0, 0);
                                        }

                                        textView.setText(str);
                                        textView1.setText(str1);
                                        textView2.setText(str2);

                                        editor1.putString("prod_name",product_name);
                                        editor1.putString("supp_name",supplier_name);
                                        editor1.putString("supp_email",supp_email);
                                        editor1.putString("prod_id",product_id);
                                        editor1.putString("supp_id",Id);
                                        editor1.putString("supp_address",address);
                                        editor1.putString("supp_phone", phone);
                                        editor1.putString("identifier", identifier);
                                        editor1.commit();

                                        String[] parts = str3.split(",", -1);

                                        boolean tabletSize = getResources().getBoolean(R.bool.tablet);

                                        for(int j = 0; j < parts.length ; j++) {

                                            if ( parts[j] != "" || parts[j] != null || parts[j].isEmpty())
                                            {
                                                final String part1 = parts[j];
                                                if (j == 0) {
                                                    textView3.setText(part1);
                                                    textView3.setVisibility(View.VISIBLE);
                                                    img1.setVisibility(View.VISIBLE);
                                                    if (!tabletSize) {
                                                        textView3.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                                                callIntent.setData(Uri.parse("tel:" + part1));
                                                                startActivity(callIntent);
                                                            }
                                                        });
                                                    }
                                                }
                                                if (j == 1) {
                                                    textView4.setText(part1);
                                                    textView4.setVisibility(View.VISIBLE);
                                                    img2.setVisibility(View.VISIBLE);
                                                    if (!tabletSize) {
                                                        textView4.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                                                callIntent.setData(Uri.parse("tel:" + part1));
                                                                startActivity(callIntent);
                                                            }
                                                        });
                                                    }
                                                }
                                                if (j == 2) {
                                                    textView5.setText(part1);
                                                    textView5.setVisibility(View.VISIBLE);
                                                    img3.setVisibility(View.VISIBLE);
                                                    if (!tabletSize) {
                                                        textView5.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                                                callIntent.setData(Uri.parse("tel:" + part1));
                                                                startActivity(callIntent);
                                                            }
                                                        });
                                                    }
                                                }
                                                if (j == 3) {
                                                    textView6.setText(part1);
                                                    textView6.setVisibility(View.VISIBLE);
                                                    img4.setVisibility(View.VISIBLE);
                                                    if (!tabletSize) {
                                                        textView6.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                                                callIntent.setData(Uri.parse("tel:" + part1));
                                                                startActivity(callIntent);
                                                            }
                                                        });
                                                    }
                                                }
                                                if (j == 4) {
                                                    textView7.setText(part1);
                                                    textView7.setVisibility(View.VISIBLE);
                                                    img5.setVisibility(View.VISIBLE);
                                                    if (!tabletSize) {
                                                        textView7.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                                                callIntent.setData(Uri.parse("tel:" + part1));
                                                                startActivity(callIntent);
                                                            }
                                                        });
                                                    }
                                                }
                                            }

                                        }

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

    private void hidePDialog()
    {
        if (dialog != null && dialog.isShowing())
        {
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




