package csteachingtips.csteachingtinder;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements CardModel.OnCardDimissedListener, View.OnClickListener {

    Random rand = new Random();
    TextView instructions;
    CardContainer tipContainer;
    TipStackAdapter adapter;
    WebView webView;
    MainActivity activity;
    ImageButton helpButton;
    private static final String PREFS = "prefs";
    Set<String> tipSet;
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






    static final String randomTipUrl = "http://csteachingtips.org/random-tip";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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

        //Create the help button in the top right corner.
        helpButton = (ImageButton) findViewById(R.id.help_button);
        helpButton.setOnClickListener(this);

        //Create the line of text with the instructions on the bottom of the screen.
        instructions = (TextView) findViewById(R.id.instructions);

        //Update tipsSoFar with the tips saved from previous times.
        tipsSoFar = new ArrayList<String>();
        viewsSoFar = new ArrayList<Integer>();
        likesSoFar = new ArrayList<Integer>();

        loadTips();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    void loadTips() {
        System.out.println("LOADING TIPS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        activity = this;
        adapter = new TipStackAdapter(this);
        tipContainer = (CardContainer) findViewById(R.id.tips);
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, String url) {

                // Inject Javascript to extract tip
                view.evaluateJavascript(
                        "var div = document.createElement('div');"
                                + "div.innerHTML = document.getElementsByClassName('tipspace')[0].innerHTML;"
                                + "div.textContent.trim() || div.innerText.trim() || '';",
                        new ValueCallback<String>() {
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
                                if (adapter.getCount() < 5) {
                                    view.reload();
                                }
                            }
                        });
            }
        });
        webView.loadUrl(randomTipUrl);
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
                t = new TipSorter(tipsSoFar, likesSoFar, viewsSoFar);
                goodTipsPopUp(t);
                return true;

            case R.id.download_results:
                // When you click "Download Results," nothing happens (yet).
                t = new TipSorter(tipsSoFar, likesSoFar, viewsSoFar);
                //t.downloadExcel();


                return true;

            case R.id.clear_data:
                // When you click "Clear Data" you a popup asking you if you really want to delete the data.
                areYouSure();
                return true;

            case R.id.settings:
                // When you click "View Results," nothing happens (yet).
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }











    public String removeQuotes(String jsonString) {
        return jsonString.substring(1, jsonString.length() - 1);
    }


    public boolean saveArrays()  //We save the data on the tips viewed so far in SharedPreferences whenever we navigate away from the app or close it.
    {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.putInt("NumTips", tipsSoFar.size());

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


    public void loadArrays() //When we start or come back to the app, reload the saved data.
    {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        tipsSoFar.clear(); //Clear all the tip-storage arrays
        likesSoFar.clear();
        viewsSoFar.clear();
        int size = sp.getInt("NumTips", 0); //Find out how many tips there should be

        for (int i = 0; i < size; i++) {
            tipsSoFar.add(sp.getString("Tip" + i, null));
            likesSoFar.add(sp.getInt("Like" + i, 0));
            viewsSoFar.add(sp.getInt("View" + i, 0));     //All tip-storage arrays are now loaded with old data
        }

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
                "To find more of these tips, please visit csteachingtips.com.");
        helpBuilder.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whatever) {
                        // Just close the dialog
                    }
                });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }






    //Return the current group (the current set of 10 tips being viewed).
    public int getGroup(){
        return group;
    }



    //Change "group" by a certain amount.
    public void incrementGroup(int num){
        group += num;
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
        tenTips.setText(t.tenGroup(group));  //Text is the top 10 tips.
        Button backButton = (Button) view.findViewById(R.id.backward_button);
        Button forwardButton = (Button) view.findViewById(R.id.forward_button);
        Button closeButton = (Button) view.findViewById(R.id.close_button);

        //Specifies what happens when you click the "Back" button.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity main = MainActivity.this;
                int group = main.getGroup();
                if (main.getGroup() > 0) { //If possible, go to the previous set of 10 tips.
                    main.incrementGroup(-1);
                    main.tenTips.setText(main.t.tenGroup(group - 1));
                }
            }
        });

        //Specifies what happens when you click the "Forward" button.
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity main = MainActivity.this;
                int group = main.getGroup();
                if (main.getGroup() < tipsSoFar.size() / 10) {  //If possible, go to the next set of 10 tips.
                    main.incrementGroup(1);
                    main.tenTips.setText(main.t.tenGroup(group + 1));
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
        checkNum(); //Take this out later.
    }


    @Override
    public void onDislike() {
        recordTip(adapter.getCurrTip(), 1);
        adapter.pop();
        //webView.loadUrl(randomTipUrl);
        tipsLeft--;
        checkNum(); //Take this out later.
    }


    void checkNum() { //Take this out later; it's good for debugging since it says how many tips are (or should be) left.
        System.out.print("Tips Left:");
        System.out.println(tipsLeft);
        if (tipsLeft == 0) {
            System.out.println("Out of tips!");
            loadTips();
            tipsLeft = 5;
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



    private boolean unsaved() { //Once we have a way to download the data, change this to check whether the data has been downloaded yet.
        return true;
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






    //Clear SharedPreferences and the data storage ArrayLists.
    private void clearData() {
        System.out.println("Clearing Data!!!");
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        sp.edit().clear().apply();
        tipsSoFar = new ArrayList<String>();
        likesSoFar = new ArrayList<Integer>();
        viewsSoFar = new ArrayList<Integer>();

    }







    //Save arrays every time our usage of the app gets interrupted (another window comes in front,
    //the user navigates away from the app, etc.
    @Override
    protected void onPause() {
        super.onPause();
        saveArrays();

    }


    //Load the arrays every time the user comes back to the app.
    @Override
    protected void onResume() {
        super.onResume();
        loadArrays();
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
}