package csteachingtips.csteachingtinder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;

/**
 * Created by owatkins on 5/4/2016.
 */
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
        //actionBar.setTitle("hiya");
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

    //FIND A WAY TO REUSE THIS CODE
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




    public void goToSettings(){
        Intent intent = new Intent(this, Settings.class);
        /*intent.putExtra("CURRENT_USER", (Serializable) currentUser);
        intent.putExtra("USERS", (Serializable) users);*/
        //!// intent.putExtra("ADAPTER", (Serializable) adapter);
        startActivity(intent);
    }


}
