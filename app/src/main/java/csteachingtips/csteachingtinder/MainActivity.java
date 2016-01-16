package csteachingtips.csteachingtinder;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements CardModel.OnCardDimissedListener, View.OnClickListener {

    Random rand = new Random();
    TextView instructions;
    CardContainer tipContainer;
    TipStackAdapter adapter;
    WebView webView;
    MainActivity activity;
    ImageButton helpButton;
    private static final String PREFS = "prefs";
    private boolean saved = false;
    private int numDownloads = 0;
    private String date = "";
    ArrayList<Integer> likesSoFar;
    ArrayList<Integer> viewsSoFar;
    ArrayList<String> tipsSoFar; //All the tips we've seen so far; Depending on how long we need to store
    // info, we could only save these for one session, save these forever, save them for a certain amount
    //of time, or even get rid of tipsSoFar all together if we can update to the site in real time.
    int tipsLeft = 5; //Currently, the app loads 5 tips to start, then doesn't load any more.  I added a
    //to load new tips when the tips currently displayed run out.  Not sure if this is the best way to go
    //about it.  It would probably be ideal if a new tip loaded at the back after each swipe.
    int group = 0; //This is used to specify a single group of 10 tips to be displayed at a time.
    AlertDialog alert;
    TipSorter t;
    TextView tenTips;
    private static final int REQUEST_EXTERNAL_STORAGE = 1; //Must ask user for permission to create files.
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    static final String randomTipUrl = "http://csteachingtips.org/random-tip";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    static boolean ready = true;










    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create an action bar with our logo
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.combined_logo);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#AEE8C1")));

        //Get the screen size, and adjust the size of the text on the bottom to match.
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        Float instructionSize = (float) (screenSize.x * .045);
        if (instructionSize < 30) {
            instructionSize = (float) 30;
        } else if (instructionSize > 60) {
            instructionSize = (float) 60;
        }


        //Create the help button in the top right corner.
        helpButton = (ImageButton) findViewById(R.id.help_button);
        helpButton.setOnClickListener(this);


        //Create the line of text with the instructions on the bottom of the screen.
        instructions = (TextView) findViewById(R.id.instructions);
        instructions.setTextSize(TypedValue.COMPLEX_UNIT_PX, instructionSize);

        //Update tipsSoFar with the tips saved from previous times.
        tipsSoFar = new ArrayList<String>();
        viewsSoFar = new ArrayList<Integer>();
        likesSoFar = new ArrayList<Integer>();





        ////////////////////////////////////////////////////////////////////////////////////////////


        final ValueCallback v = new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String jsonTip) {
                Tip newTip = new Tip(removeQuotes(jsonTip), activity);
                adapter.add(newTip);
                // Tips don't seem to be moveable if the adapter is not re-set
                // after they are added. We _should_ be able to set
                // the adapter once and not have to worry about it.
                // Setting the adapter after a tip has been removed
                // seems to put the tip back on top of the stack,
                // which is very much undesirable.
                tipContainer.setAdapter(adapter);
                System.out.print("ADAPTER: ");
                System.out.println(adapter.getCount());
                if (adapter.getCount() < 5) {
                    System.out.println("TOO FEW METHOD CALLED");
                    webView.reload(); //Probably problems
                }
            }
        };


        activity = this;
        adapter = new TipStackAdapter(this);
        tipContainer = (CardContainer) findViewById(R.id.tips);
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClient wvc = new WebViewClient() {

            @Override
            public void onPageFinished(final WebView myView, String url) {

                System.out.println("PAGE FINISHED METHOD CALLED");

                // Inject Javascript to extract tip
                myView.evaluateJavascript(
                        "var div = document.createElement('div');"
                                + "div.innerHTML = document.getElementsByClassName('tipspace')[0].innerHTML;"
                                + "div.textContent.trim() || div.innerText.trim() || '';",v);

                ready = true;
            }
        };
        webView.setWebViewClient(wvc);





        ////////////////////////////////////////////////////////////////////////////////////////////


        //for (int i = 0; i < 5; i++) {
            loadTips();
        //}

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }









    //Grab five tips from website, display them
    void loadTips() {

        System.out.println("LOAD TIPS METHOD CALLED");

        //while(!ready) {} //Probs here

        webView.loadUrl(randomTipUrl);
        //ready = false;
    }



    //Take the quotes off of the start and end of each tip.
    public String removeQuotes(String jsonString) {
        jsonString = fixSpecialCharacters(jsonString);
        return jsonString.substring(1, jsonString.length() - 1);
    }



    //Change weird symbols which don't show up properly in normal text.
    private String fixSpecialCharacters(String s) {
        return s.replace("\\u003C", "<")
                .replace("\\u003E", ">")
                .replace("\\\"", "\"");

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
            case R.id.view_results:
                // When you click "View Results," you get a popup with the tips sorted in order of popularity.
                t = new TipSorter(this, tipsSoFar, likesSoFar, viewsSoFar);
                goodTipsPopUp(t);
                return true;

            case R.id.download_results:
                // When you click "Download Results," it downloads data as a csv.
                t = new TipSorter(this, tipsSoFar, likesSoFar, viewsSoFar);
                saved = true;
                verifyStoragePermissions(this);
                downloadMessage(t.newFile(versionNum()));
                return true;

            case R.id.clear_data:
                // When you click "Clear Data" you a popup asking you if you really want to delete the data.
                areYouSure();
                return true;

            case R.id.settings:
                // When you click "Settings," nothing happens (yet).
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    //Figures out the version number which will appear in the filename of any downloaded file.
    //The version number is "" if it is the first time today a file has been downloaded.  Otherwise,
    //the version number is "_1", "_2", "_3" etc.
    String versionNum() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String newDate = dateFormat.format(cal.getTime());
        if (date.equals(newDate)) {
            numDownloads++;
            return "_" + numDownloads;
        }
        date = newDate;
        numDownloads = 0;
        return "";
    }










    //We save the data on the tips viewed so far (and other important data) in SharedPreferences
    // whenever we navigate away from the app or close it.
    public boolean saveData()
    {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.putInt("NumTips", tipsSoFar.size());
        e.putBoolean("Saved", saved);
        e.putInt("NumDownloads", numDownloads);
        e.putString("Date", date);

        for (int i = 0; i < tipsSoFar.size(); i++) {
            e.remove("Tip" + i);
            e.putString("Tip" + i, tipsSoFar.get(i));
            e.remove("Like" + i);
            e.putInt("Like" + i, likesSoFar.get(i));
            e.remove("View" + i);
            e.putInt("View" + i, viewsSoFar.get(i));
        }
        return e.commit();
    }



    //When we start or come back to the app, reload the saved data.
    public void loadData()
    {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        tipsSoFar.clear(); //Clear all the tip-storage arrays
        likesSoFar.clear();
        viewsSoFar.clear();
        int size = sp.getInt("NumTips", 0); //Find out how many tips there should be
        saved = sp.getBoolean("Saved", true);
        numDownloads = sp.getInt("NumDownloads", 0);
        date = sp.getString("Date", "");

        for (int i = 0; i < size; i++) {
            tipsSoFar.add(sp.getString("Tip" + i, null));
            likesSoFar.add(sp.getInt("Like" + i, 0));
            viewsSoFar.add(sp.getInt("View" + i, 0));     //All tip-storage arrays are now loaded with old data
        }

    }



    //Clear SharedPreferences and the data storage ArrayLists.
    private void clearData() {
        System.out.println("Clearing Data!!!");
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        sp.edit().clear().apply();
        tipsSoFar = new ArrayList<String>();
        likesSoFar = new ArrayList<Integer>();
        viewsSoFar = new ArrayList<Integer>();

    }



    //Ask the user if they want to delete the data.
    private void areYouSure() {
        AlertDialog.Builder popup = new AlertDialog.Builder(this);
        popup.setTitle("Are You Sure You Want To Clear Data?");
        String str = "The data cannot be recovered once deleted.";
        if (unsaved()) {
            str = "This data has not been downloaded yet.\n" + str;
        }
        popup.setMessage(str);
        popup.setPositiveButton("Delete Away",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whatever) {
                        clearData();
                        //Then close the popup
                    }
                });
        popup.setNegativeButton("No, Wait!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whatever) {
                        // Just close the popup
                    }
                });
        AlertDialog popupReal = popup.create();
        popupReal.show();
    }



    //Check whether the data has been downloaded yet.
    private boolean unsaved() {
        return !saved;
    }










    //Open the help popup when you click on the info button
    @Override
    public void onClick(View v) {
        showAboutPopUp();
        printTips(); //Take this out later, but it's useful now for debugging.
        //System.out.println("_______________________________________");    <== These lines are useful for debugging.
        //adapter.print();
    }


    //"About this app" Popup opens; closes when the user clicks the "Close" button
    private void showAboutPopUp() {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("About This App");
        helpBuilder.setMessage("Please swipe to the right if you think a tip is useful to an educator teaching " +
                "computer science or swipe left if you think the tip is not useful.\n\n" +
                "This app displays tips created as part of the CSTeachingTips project, which aims to develop a set of " +
                "CS teaching tips to help teachers anticipate students’ difficulties and build upon their strengths.\n\n " +
                "To find more of these tips, please visit csteachingtips.org.");
        helpBuilder.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whatever) {
                        // Just close the dialog
                    }
                });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }









    //Brings up a popup with the tips (sorted by popularity).  There are three buttons: two to
    //navigate through the tips, and one to close the popup.
    void goodTipsPopUp(TipSorter tipSorter) {
        //Create popup
        alert = new AlertDialog.Builder(this).create();
        t = tipSorter;
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.my_alert_dialog, (ViewGroup) findViewById(R.id.tip_popup));
        alert.setTitle("Top Tips");
        int group = 0;
        tenTips = (TextView) view.findViewById(R.id.ten_tips);
        tenTips.setText(t.fiveGroup(group));  //Text is the top 10 tips.
        Button backButton = (Button) view.findViewById(R.id.backward_button);
        Button forwardButton = (Button) view.findViewById(R.id.forward_button);
        Button closeButton = (Button) view.findViewById(R.id.close_button);

        //Specifies what happens when you click the "Back" button.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity main = MainActivity.this;
                int group = main.getGroup();
                if (group > 0) { //If possible, go to the previous set of 5 tips.
                    main.incrementGroup(-1);
                    main.tenTips.setText(main.t.fiveGroup(group - 1));
                }
            }
        });

        //Specifies what happens when you click the "Forward" button.
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity main = MainActivity.this;
                int group = main.getGroup();
                if (group < (tipsSoFar.size() - 1) / 5) {  //If possible, go to the next set of 5 tips.
                    System.out.println("NEW DATA SET");
                    System.out.println(tipsSoFar.size());
                    System.out.println(group);
                    System.out.println((tipsSoFar.size() - 1) / 5);
                    main.incrementGroup(1);
                    main.tenTips.setText(main.t.fiveGroup(group + 1));
                }
            }
        });

        //Specifies what happens when you click the "Close" button.
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.alert.dismiss();   //Close popup.
            }
        });

        alert.setView(view);
        alert.show();
    }




    //Return the current group (the current set of 10 tips being viewed).
    public int getGroup(){
        return group;
    }



    //Change "group" by a certain amount.
    public void incrementGroup(int num){
        group += num;
    }









    //Find and return the index of a certain tip
    int findTip(String text) {
        //Loop through previously created tips, see if this tip is already there.
        for (int i = 0; i < tipsSoFar.size(); i++) {
            if (text.equals(tipsSoFar.get(i))) {
                return i;             //Returns the index of the matching tip (if there is one).
            }
        }
        return -1;  //If there's no match, return -1.

    }


    //Stores the view and (possibly) like of the current tip in the 3 data storage ArrayLists.
    void recordTip(Tip currTip, int like) {
        String text = currTip.getDescription();
        int tipIndex = findTip(text);
        if (tipIndex == -1) {
            tipsSoFar.add(text);
            likesSoFar.add(like);
            viewsSoFar.add(1);
        } else {
            viewsSoFar.set(tipIndex, viewsSoFar.get(tipIndex) + 1);
            likesSoFar.set(tipIndex, likesSoFar.get(tipIndex) + like);
        }
    }




    @Override
    public void onLike() {
        recordTip(adapter.getCurrTip(), 0);
        adapter.pop();
        //webView.loadUrl(randomTipUrl);
        tipsLeft--;
        saved = false;
        checkNum(); //Take this out later.
    }


    @Override
    public void onDislike() {
        recordTip(adapter.getCurrTip(), 1);
        adapter.pop();
        //webView.loadUrl(randomTipUrl);
        tipsLeft--;
        saved = false;
        checkNum(); //Take this out later.
    }



    void checkNum() { //Take this out later; it's good for debugging since it says how many tips are (or should be) left.
        System.out.print("Tips Left:");
        System.out.println(tipsLeft);
        System.out.println("CHECK NUM METHOD CALLED");
        loadTips();
        if (tipsLeft == 0) {
            System.out.println("Out of tips!");
            //loadTips(); //Important!
            tipsLeft = 5;
        }
    }









    //Popup appears, telling the user where the data has been saved.
    private void downloadMessage(String fileName) {
        AlertDialog.Builder popup = new AlertDialog.Builder(this);
        if (fileName.equals("Error: File not created.")) {
            popup.setTitle("Download Not Completed :(");
            popup.setMessage("There must have been been some problem.  The file couldn't be created.");
        } else {
            popup.setTitle("Download Complete!");
            String str = "A csv file named " + fileName + " has been saved to your Downloads folder.";
            popup.setMessage(str);
        }
        popup.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whatever) {
                        //Then close the popup
                    }
                });
        AlertDialog popupReal = popup.create();
        popupReal.show();
    }











    //Save arrays every time our usage of the app gets interrupted (another window comes in front,
    //the user navigates away from the app, etc.
    @Override
    protected void onPause() {
        super.onPause();
        saveData();

    }



    //Load the arrays every time the user comes back to the app.
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }




    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://csteachingtips.csteachingtinder/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }




    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://csteachingtips.csteachingtinder/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    //Called whenever the screen orientation changes
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //Change the text size depending on the current width of the screen
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        Float instructionSize = (float) (screenSize.x * .045);
        if (instructionSize < 30) {
            instructionSize = (float) 30;
        } else if (instructionSize > 60) {
            instructionSize = (float) 60;
        }
        //Create the line of text with the instructions on the bottom of the screen.
        instructions.setTextSize(TypedValue.COMPLEX_UNIT_PX, instructionSize);
    }








     // Checks if the app has permission to write to device storage
     // If the app does not have permission then the user will be prompted to grant permissions
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have file reading/writing permissions already
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission, so ask the user for them
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }








    //Get rid of this later; this is just helpful for now in to see what elements tipsSoFar holds.
    void printTips() {
        for (int i = 0; i < tipsSoFar.size(); i++) {
            System.out.print(tipsSoFar.get(i));
            System.out.print("  -  ");
            System.out.print(likesSoFar.get(i));
            System.out.print("/");
            System.out.println(viewsSoFar.get(i));
            System.out.println();
        }
    }





}