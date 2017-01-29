package jimtrade.com.jimtrade.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.model.Mail_model;


public class MailAdapter extends BaseAdapter
{
    private Activity activity;
    private LayoutInflater inflater;
    private List<Mail_model> receiveItems;

    public MailAdapter(Activity activity, List<Mail_model> receiveItems) {
        this.activity = activity;
        this.receiveItems = receiveItems;
    }

    @Override
    public int getCount() {
        return receiveItems.size();
    }

    @Override
    public Object getItem(int location) {
        return receiveItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView name,timing,subject;

        try {
            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.mail_frame, null);

            name = (TextView) convertView.findViewById(R.id.mail_name);
            timing = (TextView) convertView.findViewById(R.id.timing);
            subject = (TextView) convertView.findViewById(R.id.subject);

            Mail_model m = receiveItems.get(position);


            name.setText(String.valueOf(m.getName()));
            timing.setText(String.valueOf(m.getDate()));
            subject.setText(String.valueOf(m.getSubject()));
        }catch (NullPointerException npe)
        {

        }

        return convertView;
    }

}
