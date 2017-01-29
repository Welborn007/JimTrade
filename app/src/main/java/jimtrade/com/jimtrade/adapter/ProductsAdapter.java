package jimtrade.com.jimtrade.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Network;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import jimtrade.com.jimtrade.Image_zoom;
import jimtrade.com.jimtrade.Inquiry.Inquiry_Login;
import jimtrade.com.jimtrade.ProductScreenFragments;
import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.Send_Inquiry.Send_Inquiry_Product;
import jimtrade.com.jimtrade.SupplierScreenFragments;
import jimtrade.com.jimtrade.app.AppController;

import jimtrade.com.jimtrade.model.Products;


public class ProductsAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Products> productItems;
    private ImageLoader imageLoader;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String MyPREFERENCES1 = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    SharedPreferences.Editor editor1;

    public ProductsAdapter(FragmentActivity activity,List<Products> productItems) {
        this.activity = activity;
        this.productItems = productItems;
        imageLoader = AppController.getInstance().getImageLoader();
    }

    @Override
     public int getCount() {
        return productItems.size();
    }

    @Override
    public Object getItem(int location) {
        return productItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView name,id,suppliername,supplierId,cat_id;

        Button inquiry_product;

        try {

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.list_row, null);

            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();



            NetworkImageView thumbNail = (NetworkImageView) convertView
                    .findViewById(R.id.thumbnail);

            name = (TextView) convertView.findViewById(R.id.product_name);
            name.setTag(R.id.product_name, position);

            id = (TextView) convertView.findViewById(R.id.product_id);
            supplierId = (TextView) convertView.findViewById(R.id.supplier_id);

            cat_id = (TextView) convertView.findViewById(R.id.category_id);
            cat_id.setTag(R.id.category_id, position);

            suppliername = (TextView) convertView.findViewById(R.id.supplier_name);
            suppliername.setTag(R.id.supplier_name, position);

            inquiry_product = (Button) convertView.findViewById(R.id.inquiry);

            // getting products data for the row
            final Products m = productItems.get(position);

            // thumbnail image
            thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

            // product name
            name.setText(String.valueOf(m.getProductName()));

            // products id
            id.setText("Product ID: " + String.valueOf(m.getProductID()));

            // supplier id
            supplierId.setText("Supplier ID" + String.valueOf(m.getSupplierID()));

            // Category ID
            cat_id.setText("Category ID" + String.valueOf(m.getCategoryID()));

            // supplier name
            suppliername.setText(String.valueOf(m.getSupplierName()));



            thumbNail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(activity, Image_zoom.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    intent2.putExtra("name", String.valueOf(m.getProductName()));
                    intent2.putExtra("productid", String.valueOf(m.getProductID()));
                    intent2.putExtra("suppliername", String.valueOf(m.getSupplierName()));
                    intent2.putExtra("supplierid", String.valueOf(m.getSupplierID()));
                    intent2.putExtra("categoryid", String.valueOf(m.getCategoryID()));
                    activity.startActivity(intent2);
                }
            });

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity,ProductScreenFragments.class);
                    int position=(Integer) v.getTag(R.id.product_name);
                    // getting products data for the row
                    Products m = productItems.get(position);

                    intent.putExtra("name",String .valueOf(m.getProductName()));
                    intent.putExtra("productid",String.valueOf(m.getProductID()));
                    intent.putExtra("suppliername",String.valueOf(m.getSupplierName()));
                    intent.putExtra("supplierid", String.valueOf(m.getSupplierID()));
                    intent.putExtra("categoryid",String.valueOf(m.getCategoryID()));

                    sharedpreferences = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    editor = sharedpreferences.edit();
                    editor.putString("product_header", String.valueOf(m.getProductName()));
                    editor.commit();

                    activity.startActivity(intent);
                }
            });

            suppliername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(activity, SupplierScreenFragments.class);
                    int position=(Integer) v.getTag(R.id.supplier_name);
                    // getting products data for the row
                    Products m = productItems.get(position);
                    intent1.putExtra("name",String .valueOf(m.getProductName()));
                    
                    intent1.putExtra("productid",String.valueOf(m.getProductID()));
                    intent1.putExtra("suppliername",String.valueOf(m.getSupplierName()));
                    intent1.putExtra("supplierid",String.valueOf(m.getSupplierID()));
                    intent1.putExtra("categoryid", String.valueOf(m.getCategoryID()));

                    sharedpreferences = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    editor = sharedpreferences.edit();
                    editor.putString("supplier_header", String.valueOf(m.getSupplierName()));
                    editor.commit();

                    activity.startActivity(intent1);
                }
            });

            inquiry_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    sharedpreferences = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    final String s1 = sharedpreferences.getString("Email", null);

                    if (s1 == null) {
                        Intent intent = new Intent(activity, Inquiry_Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(activity, Send_Inquiry_Product.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);
                    }


                    sharedpreferences1 = activity.getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
                    editor1 = sharedpreferences1.edit();

                    editor1.putString("prod_name",String .valueOf(m.getProductName()));
                    editor1.putString("supp_name",String.valueOf(m.getSupplierName()));
                    editor1.putString("prod_id",String.valueOf(m.getProductID()));
                    editor1.putString("supp_id",String.valueOf(m.getSupplierID()));
                    editor1.putString("supp_email",String.valueOf(m.getSupp_email()));
                    editor1.putString("supp_address", String.valueOf(m.getSupp_addr()));
                    editor1.putString("supp_phone", String.valueOf(m.getSupp_phone()));
                    String identifier = "0";
                    editor1.putString("identifier", identifier);

                    editor1.apply();
                    editor1.commit();

                }
            });

        }catch (NullPointerException npe)
        {
            npe.printStackTrace();
        }

        return convertView;
    }
}
