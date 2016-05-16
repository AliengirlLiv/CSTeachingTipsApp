package csteachingtips.csteachingtinder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


//This class creates the ActionBar which is used on every screen.
public class BaseActivity extends AppCompatActivity {

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.combined_logo);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#AEE8C1")));
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }




    public ActionBar getActionbar(){
        return actionBar;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the menu in the Action Bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Specify what happens when each menu item is pressed.
        switch (item.getItemId()) {

            case R.id.settings:
                goToSettings();
                return true;

            case android.R.id.home:
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private void goToSettings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }


}
