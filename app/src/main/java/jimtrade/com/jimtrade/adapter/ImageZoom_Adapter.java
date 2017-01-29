package jimtrade.com.jimtrade.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.app.AppController;
import jimtrade.com.jimtrade.model.Products;

public class ImageZoom_Adapter extends BaseAdapter
{

    private Activity activity;
    private LayoutInflater inflater;
    private List<Products> productItems;
    private ImageLoader imageLoader;

    public ImageZoom_Adapter(Activity activity,List<Products> productItems) {
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

        try {
            if (inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.zoom_row, null);

            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            NetworkImageView thumbNail = (NetworkImageView) convertView
                    .findViewById(R.id.image);

            // getting products data for the row
            Products m = productItems.get(position);

            // thumbnail image
            thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        }catch (NullPointerException npe)
        {
            npe.printStackTrace();
        }
        return convertView;
    }
}

