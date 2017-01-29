package jimtrade.com.jimtrade;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;


public class WelcomeScreen extends Activity {

    // Create Preference to check if application is going to be called first
    // time.
    SharedPreferences appPref;
    boolean isFirstTime = true;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String MyPREFERENCES1 = "MyPrefs1" ;
    SharedPreferences sharedpreferences1;
    SharedPreferences.Editor editor1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome_screen);

        // Get preference value to know that is it first time application is
        // being called.
        appPref = getSharedPreferences("isFirstTime", 0);
        isFirstTime = appPref.getBoolean("isFirstTime", true);

        if (isFirstTime) {
            // Create explicit intent which will be used to call Our application
            // when some one clicked on short cut
            Intent shortcutIntent = new Intent(getApplicationContext(),
                    WelcomeScreen.class);
            shortcutIntent.setAction(Intent.ACTION_MAIN);
            Intent intent = new Intent();

            // Create Implicit intent and assign Shortcut Application Name, Icon
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "JimTrade");
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(
                            getApplicationContext(), R.mipmap.ic_launcher));
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(intent);

            // Set preference to inform that we have created shortcut on
            // Homescreen
            SharedPreferences.Editor editor = appPref.edit();
            editor.putBoolean("isFirstTime", false);
            editor.commit();
        }

        Thread background = new Thread()
        {// Create Thread that will sleep for 5 seconds
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(5*1000);

                    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    String s1 = sharedpreferences.getString("Email",null);

                    if(s1 == null) {
                        // After 5 seconds redirect to another intent
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        editor = sharedpreferences.edit();
                        editor.putString("welcome", "welcome");
                        editor.commit();
                        startActivity(intent);
                    }
                    else
                    {
                        // After 5 seconds redirect to another intent
                        Intent intent = new Intent(getBaseContext(), HomeScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();

        sharedpreferences1 = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
        editor1 = sharedpreferences1.edit();

        String identifier = "home";
        editor1.putString("identifier", identifier);
        editor1.commit();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

}
