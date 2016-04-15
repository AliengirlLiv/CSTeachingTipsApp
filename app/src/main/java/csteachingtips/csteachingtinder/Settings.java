package csteachingtips.csteachingtinder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;


public class Settings extends AppCompatActivity implements View.OnClickListener {
    RadioButton personalUser;
    RadioButton conferenceMode;
    Button newUser;
    Button downloadData;
    Button uploadData;
    Button viewFavorites;
    Button clearStoredData;
    CheckBox autoUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent intent = getIntent();

        //Create an action bar with our logo
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.combined_logo);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#AEE8C1")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        personalUser = (RadioButton) findViewById(R.id.personal_user);
        conferenceMode = (RadioButton) findViewById(R.id.conference_mode);
        newUser = (Button) findViewById(R.id.new_user);
        downloadData = (Button) findViewById(R.id.download_data);
        uploadData = (Button) findViewById(R.id.upload_data);
        viewFavorites = (Button) findViewById(R.id.view_favorites);
        clearStoredData = (Button) findViewById(R.id.clear_stored_data);
        autoUpload = (CheckBox) findViewById(R.id.auto_upload);



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
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.new_user:
                //Take to a new page (or maybe just a popup).
                break;
            case R.id.download_data:
                //Some sort of return thing??
                break;
            case R.id.upload_data:
                //Nothing here yet.
                break;
            case R.id.view_favorites:
                //Popup, or maybe some sort of return thing.
                break;
            case R.id.clear_stored_data:
                //Some sort of return thing?
                break;
        }
    }


    public void goToSettings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }






   /* // When you click "View Results," you get a popup with the tips sorted in order of popularity.
    t = new TipSorter(this, MainActivity.tipsSoFar, extendedTipsSoFar, likesSoFar, viewsSoFar);
    goodTipsPopUp(t);
    return true;

    case R.id.download_results:
    // When you click "Download Results," it downloads data as a csv.
    t = new TipSorter(this, tipsSoFar, extendedTipsSoFar, likesSoFar, viewsSoFar);
    saved = true;
    verifyStoragePermissions(this);
    downloadMessage(t.newFile(versionNum()));
    return true;

    case R.id.clear_data:
    // When you click "Clear Data" you a popup asking you if you really want to delete the data.
    areYouSure();
    return true;*/









}





