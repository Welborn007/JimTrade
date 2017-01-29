package jimtrade.com.jimtrade.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import jimtrade.com.jimtrade.Inquiry.Inquiry_Login;
import jimtrade.com.jimtrade.ProductScreenFragments;
import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.Second_Level_Category;
import jimtrade.com.jimtrade.Send_Inquiry.Send_Inquiry_Product;
import jimtrade.com.jimtrade.SubCategoryActivity;
import jimtrade.com.jimtrade.SupplierScreenFragments;
import jimtrade.com.jimtrade.app.AppController;
import jimtrade.com.jimtrade.model.Products;
import jimtrade.com.jimtrade.model.SupplierProducts;


public class SupplierProductAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<SupplierProducts> productItems;
    private ImageLoader imageLoader;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String MyPREFERENCES1 = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    SharedPreferences.Editor editor1;

    public SupplierProductAdapter(FragmentActivity activity,List<SupplierProducts> productItems) {
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

        TextView name,id,catname,cat_id,category_index,count;

        Button inquiry_product;

        try {

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.supplier_list_row, null);

            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            NetworkImageView thumbNail = (NetworkImageView) convertView
                    .findViewById(R.id.thumbnail);
            name = (TextView) convertView.findViewById(R.id.product_name);
            name.setTag(R.id.product_name, position);

            id = (TextView) convertView.findViewById(R.id.product_id);

            cat_id = (TextView) convertView.findViewById(R.id.category_id);

            category_index = (TextView) convertView.findViewById(R.id.catindex);
            category_index.setTag(R.id.catindex, position);

            catname = (TextView) convertView.findViewById(R.id.cat_name);
            catname.setTag(R.id.cat_name, position);

            count = (TextView) convertView.findViewById(R.id.count123);

            inquiry_product = (Button) convertView.findViewById(R.id.inquiry);


            // getting products data for the row
            final SupplierProducts m = productItems.get(position);

            // thumbnail image
            thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

            // product name
            name.setText(String.valueOf(m.getProductName()));

            // products id
            id.setText("Product ID: " + String.valueOf(m.getProductID()));

            // Category ID
            cat_id.setText("Category ID" + String.valueOf(m.getCategoryID()));

            // supplier name
            catname.setText(String.valueOf(m.getCatname()));

            // Category Index
            category_index.setText(String.valueOf(m.getCategoryIndex()));


            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ProductScreenFragments.class);
                    int position = (Integer) v.getTag(R.id.product_name);
                    // getting products data for the row
                    SupplierProducts m = productItems.get(position);

                    intent.putExtra("name", String.valueOf(m.getProductName()));
                    intent.putExtra("productid", String.valueOf(m.getProductID()));
                    intent.putExtra("categoryname", String.valueOf(m.getCatname()));
                    intent.putExtra("categoryid", String.valueOf(m.getCategoryID()));
                    activity.startActivity(intent);

                    sharedpreferences = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    editor = sharedpreferences.edit();
                    editor.putString("product_header", String.valueOf(m.getProductName()));
                    editor.commit();
                }
            });

            catname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(activity, SubCategoryActivity.class);
                    int position = (Integer) v.getTag(R.id.cat_name);
                    // getting products data for the row
                    SupplierProducts m = productItems.get(position);

                    intent1.putExtra("categories", String.valueOf(m.getCatname()));
                    intent1.putExtra("index", String.valueOf(m.getCategoryIndex()));
                    intent1.putExtra("count",String.valueOf(m.getCount()));
                    intent1.putExtra("catcount", String.valueOf(m.getCategory_count()));
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
                    editor1.putString("supp_email", String.valueOf(m.getSupp_email()));
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