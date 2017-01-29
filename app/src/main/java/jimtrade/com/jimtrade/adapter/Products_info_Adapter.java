package jimtrade.com.jimtrade.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import jimtrade.com.jimtrade.app.AppController;

import jimtrade.com.jimtrade.model.Products;

public class Products_info_Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Products> productItems;
    private ImageLoader imageLoader;

    public static final String MyPREFERENCES = "MyPrefs" ;

    SharedPreferences sharedpreferences;

    public Products_info_Adapter(FragmentActivity activity,List<Products> productItems) {
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

        TextView category_name,category_index,count;

        Button enquire;

       TextView features1,features2;

        try {

            if (inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) convertView = inflater.inflate(R.layout.product_info_xml, null);

            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.image);

            features1 = (TextView) convertView.findViewById(R.id.features_header);
            features2 = (TextView) convertView.findViewById(R.id.features);

            category_name = (TextView) convertView.findViewById(R.id.category_name);
            count = (TextView) convertView.findViewById(R.id.count12);
            category_index = (TextView) convertView.findViewById(R.id.index);
            category_index.setTag(R.id.index, position);

            // getting products data for the row
            final Products m = productItems.get(position);

            // thumbnail image
            thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

            category_name.setText(String.valueOf(m.getCategoryName()));

            category_index.setText(String.valueOf(m.getCategoryIndex()));

            count.setText(String.valueOf(m.getCount()));

            sharedpreferences = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            final String s1 = sharedpreferences.getString("Email", null);
            enquire = (Button) convertView.findViewById(R.id.inquiry);

            enquire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (s1 == null) {
                        Intent intent = new Intent(activity, Inquiry_Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);
                    } else {
                        Intent intent = new Intent(activity, Send_Inquiry_Product.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);
                    }
                }
            });


            category_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity,SubCategoryActivity.class);


                    intent.putExtra("categories", String.valueOf(m.getCategoryName()));
                    intent.putExtra("count",String.valueOf(m.getCount()));
                    intent.putExtra("index",String.valueOf(m.getCategoryIndex()));
                    intent.putExtra("catcount",String.valueOf(m.getCategory_Count()));
                    activity.startActivity(intent);
                }
            });

            String Feat = String.valueOf(m.getFeatures());
            Feat = Feat.replace("**", "\n\u2022 ");
            String str1 = Feat.trim();

            features2.setText(str1);

            // Hide if empty Json Object
            if (str1.isEmpty() || str1 == "null") {
                features1.setVisibility(convertView.GONE);
                features2.setVisibility(convertView.GONE);
                            }

        }catch (NullPointerException npe){
            npe.printStackTrace();
        }

        return convertView;
    }
}
