package jimtrade.com.jimtrade.Mails;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import jimtrade.com.jimtrade.AboutUs;
import jimtrade.com.jimtrade.ContactUs;
import jimtrade.com.jimtrade.FAQs;
import jimtrade.com.jimtrade.Feedback;
import jimtrade.com.jimtrade.HomeScreen;
import jimtrade.com.jimtrade.LoginActivity;
import jimtrade.com.jimtrade.My_JimTrade;
import jimtrade.com.jimtrade.R;
import jimtrade.com.jimtrade.RegisterNew;

public class Receive_body extends Activity {

    TextView name1,subject1,message1,date1,product1,info;

    TextView address1,designation1,email1,telephone1,contact_person;

    View view;
    RelativeLayout relativeLayout,Holder;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String MyPREFERENCES1 = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    SharedPreferences.Editor editor1;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_receive_body);

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

        getActionBar().setTitle(null);
        getActionBar().setDisplayShowHomeEnabled(true);

        try {
            String name = getIntent().getStringExtra("name");
            String subject = getIntent().getStringExtra("subject");
            String message = getIntent().getStringExtra("message");
            String date = getIntent().getStringExtra("date");
            String product = getIntent().getStringExtra("product");

            String address = getIntent().getStringExtra("address");
            String designation = getIntent().getStringExtra("design");
            String email = getIntent().getStringExtra("email");
            String telephone = getIntent().getStringExtra("tele");
            String contatc_person = getIntent().getStringExtra("cont_per");

            name1 = (TextView) findViewById(R.id.name);
            subject1 = (TextView) findViewById(R.id.subject_body);
            message1 = (TextView) findViewById(R.id.message_body);
            date1 = (TextView) findViewById(R.id.time);
            product1 = (TextView) findViewById(R.id.product_cont);
            view = (View) findViewById(R.id.view2);
            relativeLayout = (RelativeLayout) findViewById(R.id.rel3);
            info = (TextView) findViewById(R.id.info);
            Holder = (RelativeLayout) findViewById(R.id.details_holder);

            address1 = (TextView) findViewById(R.id.address);
            designation1 = (TextView) findViewById(R.id.designation);
            email1 = (TextView) findViewById(R.id.email);
            telephone1 = (TextView) findViewById(R.id.tele);
            contact_person = (TextView) findViewById(R.id.Contact_Person_);

            address1.setText(address);
            designation1.setText(designation);
            email1.setText(email);
            telephone1.setText(telephone);
            contact_person.setText(contatc_person);

            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Holder.getVisibility() == View.GONE)
                    {
                        Holder.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Holder.setVisibility(View.GONE);
                    }
                }
            });

            if (product.isEmpty()) {
                relativeLayout.setVisibility(relativeLayout.GONE);
                relativeLayout.setPadding(0, -1 * relativeLayout.getHeight(), 0, 0);
                view.setVisibility(view.GONE);
                view.setPadding(0, -1 * view.getHeight(), 0, 0);
            }

            name1.setText(name);
            subject1.setText(subject);
            message1.setText(message);
            date1.setText(date);
            product1.setText(product);
        }catch (NullPointerException ne)
        {
            Toast.makeText(getApplicationContext(),"Check Internet Connectivity!!",Toast.LENGTH_LONG).show();
        }
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

            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;

            case R.id.action_home:
                Intent intent2 = new Intent(this, HomeScreen.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
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
