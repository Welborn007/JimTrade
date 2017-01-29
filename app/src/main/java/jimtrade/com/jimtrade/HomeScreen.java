package jimtrade.com.jimtrade;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;

import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeScreen extends Activity
{

    ImageView img1,img2,img3,img4,img5,img6;

    TextView headerimage;
    private int counter = 0;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String MyPREFERENCES1 = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    SharedPreferences.Editor editor1;

    private boolean _doubleBackToExitPressedOnce    = false;

    //Ice-cream sandwich API suppport fro navigation drawer
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_home_screen);

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

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        sharedpreferences1 = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
        editor1 = sharedpreferences1.edit();

        getActionBar().setTitle(null);
        getActionBar().setDisplayHomeAsUpEnabled(false);

        img1 = (ImageView) findViewById(R.id.find_products1);
        img2 = (ImageView) findViewById(R.id.find_products2);
        img3 = (ImageView) findViewById(R.id.inquiry_1);
        img4 = (ImageView) findViewById(R.id.inquiry_2);
        img5 = (ImageView) findViewById(R.id.MyJimtrade_1);
        img6 = (ImageView) findViewById(R.id.MyJimtrade_2);

        FindProducts listener1 = new FindProducts();
        img1.setOnClickListener(listener1);
        img2.setOnClickListener(listener1);

        PostInquiry listener2 = new PostInquiry();
        img3.setOnClickListener(listener2);
        img4.setOnClickListener(listener2);

        JimTrade listener3 = new JimTrade();
        img5.setOnClickListener(listener3);
        img6.setOnClickListener(listener3);
    }


    class FindProducts implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(HomeScreen.this,search_item.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    class PostInquiry implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            sharedpreferences1 = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);

            editor.putString("navigator","post_inquiry");
            editor1.putString("identifier","2");
            editor.commit();
            editor1.commit();

                Intent intent = new Intent(HomeScreen.this,Post_Inquiry.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

        }
    }

    class JimTrade implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            sharedpreferences1 = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
            String s1 = sharedpreferences.getString("Email",null);
            editor.putString("navigator","jimtrade");
            editor1.putString("identifier","3");
            editor.commit();
            editor1.commit();

            if(s1 == null) {
                Intent intent = new Intent(HomeScreen.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(HomeScreen.this,My_JimTrade.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
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

                case R.id.Login:
                    // create intent to perform web search for this planet
                    Intent intent1 = new Intent(this, LoginActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    sharedpreferences1 = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
                    editor1 = sharedpreferences1.edit();

                    String identifier = "Jim";
                    editor1.putString("identifier", identifier);
                    editor1.commit();
                    return true;

                case R.id.aboutus:
                    Intent intent4 = new Intent(this,AboutUs.class);
                    intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent4);
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

                case R.id.Register:
                    // create intent to perform web search for this planet
                    Intent intent2 = new Intent(this, RegisterNew.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    return true;

                case R.id.Logout:
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.commit();

                    // create intent to perform web search for this planet
                    Intent intent3 = new Intent(this, HomeScreen.class);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent3);

                    Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();

                default:
                    return super.onOptionsItemSelected(item);
            }

    }


    @Override
    public void onBackPressed() {


        if (_doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this._doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to quit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                _doubleBackToExitPressedOnce = false;

            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String s1 = sharedpreferences.getString("Email",null);

        if(s1 == null) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.home_login, menu);
        }
        else
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.home_logout, menu);
        }
        return true;
    }


}

