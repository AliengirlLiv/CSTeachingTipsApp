package csteachingtips.csteachingtinder;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.andtinder.model.CardModel;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainActivity extends BaseActivity implements CardModel.OnCardDimissedListener, View.OnClickListener {

    CardPile tipContainer;
    TipStackAdapter adapter;
    WebView webView;
    MainActivity activity;
    ImageButton helpButton;
    private static final String PREFS = "prefs";
    private boolean saved = false;
    ArrayList<Integer> likesSoFar;
    ArrayList<Integer> viewsSoFar;
    ArrayList<String> tipsSoFar; //All the tips we've seen so far; Depending on how long we need to store
    // info, we could only save these for one session, save these forever, save them for a certain amount
    //of time, or even get rid of tipsSoFar all together if we can update to the site in real time.
    ArrayList<String> extendedTipsSoFar;
    int tipsLeft = 5;


    static final String randomTipUrl = "http://csteachingtips.org/random-tip";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    static boolean ready = true;
    private User currentUser;
    private ArrayList<User> users;
    int numTips = 5;
    TextView loggedIn;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStuffUp();
    }

    public void setStuffUp() {

        setContentView(R.layout.activity_main);

        //Create an action bar with our logo


        //Create and display new user
        User anonymousUser = new User(true);
        users = new ArrayList<User>();
        users.add(anonymousUser);
        currentUser = anonymousUser;
        loggedIn = (TextView) findViewById(R.id.logged_in);


        //Create the help button in the top right corner.
        helpButton = (ImageButton) findViewById(R.id.help_button);
        helpButton.setOnClickListener(this);


        //Update tipsSoFar with the tips saved from previous times.
        tipsSoFar = new ArrayList<String>();
        extendedTipsSoFar= new ArrayList<String>();
        viewsSoFar = new ArrayList<Integer>();
        likesSoFar = new ArrayList<Integer>();


        getActionbar().setDisplayHomeAsUpEnabled(false);

        //!//
       /* Intent myIntent = getIntent();
        System.out.println("INTENT: " + myIntent);
        adapter = (TipStackAdapter) myIntent.getSerializableExtra("ADAPTER");
        if (adapter == null){
            adapter = new TipStackAdapter(this);
        }*/











        ////////////////////////////////////////////////////////////////////////////////////////////


        final ValueCallback v = new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String jsonTip) {
                System.out.print("JSON TIP!!!!!!!!!!!");
                if (jsonTip.equals("null")) {
                    new AlertDialog.Builder(MainActivity.this).setMessage("Couldn't load tip.  Are you connected to the internet?").setNeutralButton("Close", null).show();
                    Button b = (Button) findViewById(R.id.check_connection);
                    b.setVisibility(View.VISIBLE);
                    b.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            setStuffUp();
                        }

                    });
                    return;
                }
                int index = jsonTip.indexOf("\\n\\n");
                System.out.println(index);
                String title = jsonTip.substring(0, index);
                String description = jsonTip.substring(index + 4);
                Tip newTip = new Tip(removeQuotes(title, 1), removeQuotes(description, 0), activity);
                adapter.add(newTip);
                tipContainer.setAdapter(adapter);
                System.out.print("ADAPTER: ");
                System.out.println(adapter.getCount());
                if (adapter.getCount() < numTips) {
                    System.out.println("TOO FEW METHOD CALLED");
                    webView.reload(); //Probably problems
                }
            }
        };


        activity = this;
        adapter = new TipStackAdapter(this);
/**
        for(int i = 0; i < numTips; i++){
            Tip newTip = tipGetter.getNewTip();////WHAT???
            adapter.add(newTip);
            System.out.println("Worked okay at least once");
        }
*/


        tipContainer = (CardPile) findViewById(R.id.tips);
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);



        WebViewClient wvc = new WebViewClient() {

            @Override
            public void onPageFinished(final WebView myView, String url) {

                System.out.println("PAGE FINISHED METHOD CALLED");


                myView.evaluateJavascript(
                        "var div = document.createElement('div');"
                                + "div.innerHTML = (document.getElementsByClassName('tip-title')[0].innerHTML).concat('\\n\\n').concat(document.getElementsByClassName('field-item even')[0].innerHTML);"
                                + "div.textContent.trim() || div.innerText.trim() || '';",v); //On the line above, if there isn't any body, it gets the 1st tag instead

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
        webView.loadUrl(randomTipUrl);
    }



    //Take the quotes off of the start and end of each tip.
    public String removeQuotes(String jsonString, int x) {
        jsonString = fixSpecialCharacters(jsonString);
        return jsonString.substring(x, jsonString.length() - 1 + x);
    }



    //Change weird symbols which don't show up properly in normal text.
    private String fixSpecialCharacters(String s) {
        System.out.println(s);
        return s.replace("\\u003C", "<")
                .replace("\\u003E", ">")
                .replace("\\\"", "\"")
                .replace("\\\'", "\'")
                .replace("\\\\", "\\") //Make sure this doesn't happen multiple times
                .replace("\\n", "\n")
                .replace("\\t", "\t")
                .replace("\\u009B", ""); //This line appeared in the tip which begins: SciGirls Seven tip: “Girls benefit from relationships with role models and mentors.”
// \u2022 = buller point
    }












    //We save the data on the tips viewed so far (and other important data) in SharedPreferences
    // whenever we navigate away from the app or close it.
    public boolean saveData() {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        Gson gson = new Gson();

        Type typeIntArrayList = new TypeToken<ArrayList<Integer>>(){}.getType();
        Type typeStringArrayList = new TypeToken<ArrayList<String>>(){}.getType();
        //:( Type typeAdapter = new TypeToken<TipStackAdapter>(){}.getType();

        e.putString("TipsSoFar", gson.toJson(tipsSoFar, typeStringArrayList));
        e.putString("ExtendedTipsSoFar", gson.toJson(extendedTipsSoFar, typeStringArrayList));
        e.putString("LikesSoFar", gson.toJson(likesSoFar, typeIntArrayList));
        e.putString("ViewsSoFar", gson.toJson(viewsSoFar, typeIntArrayList));
        e.putBoolean("Saved", saved);
        //:(e.putString("Adapter", gson.toJson(adapter, typeAdapter));


        for (int i = 0; i < adapter.getCount(); i++) {
            Tip currTip = adapter.getCurrTip();
            e.remove("ActiveTip" + i);
            e.putString("ActiveTip" + i, currTip.getTitle());
            e.remove("ActiveETip" + i);
            e.putString("ActiveETip" + i, currTip.getDescription());
            e.remove("ActiveLikes" + i);
            e.putInt("ActiveLikes" + i, currTip.getLikes());
            e.remove("ActiveViews" + i);
            e.putInt("ActiveViews" + i, currTip.getViews());
            adapter.pop();
        }
        return e.commit();
    }



    //When we start or come back to the app, reload the saved data.
    public void loadData()
    {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        saved = sp.getBoolean("Saved", true);


        Gson gson = new Gson();
        Type typeIntArrayList = new TypeToken<ArrayList<Integer>>(){}.getType();
        Type typeStringArrayList = new TypeToken<ArrayList<String>>(){}.getType();

        currentUser = gson.fromJson(sp.getString("CurrentUser", "Anonymous/Conference Mode"), User.class);
        loggedIn.setText(currentUser.getUsername());
        tipsSoFar = gson.fromJson(sp.getString("TipsSoFar", "[]"), typeStringArrayList);
        if (tipsSoFar == null){
            tipsSoFar = new ArrayList<>();
            extendedTipsSoFar = new ArrayList<>();
            viewsSoFar = new ArrayList<>();
            likesSoFar = new ArrayList<>();
        } else {
            extendedTipsSoFar = gson.fromJson(sp.getString("ExtendedTipsSoFar", ""), typeStringArrayList);
            viewsSoFar = gson.fromJson(sp.getString("ViewsSoFar", ""), typeIntArrayList);
            likesSoFar = gson.fromJson(sp.getString("LikesSoFar", ""), typeIntArrayList);
        }



        //Save current tips
        for (int i = 0; i < numTips; i++) {
            String active = sp.getString("ActiveTip" + i, null);
            if (active != null){
                String description = sp.getString("ActiveETip" + i, null);
                int likes = sp.getInt("ActiveLikes" + i, 0);
                int views = sp.getInt("ActiveViews" + i,0);
                Tip newTip = new Tip(active, description, likes, views);
                System.out.println("Added a tip from sharedpreferences.");
                System.out.println(active);
                adapter.add(newTip); //Maybe reload webview after one?  After all of them?
            }
        }


    }







    //Open the help popup when you click on the info button
    @Override
    public void onClick(View v) {
        showAboutPopUp();
        adapter.print();
        //!//printTips();
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
        String body = currTip.getDescription();
        String title = currTip.getTitle();
        int tipIndex = findTip(title);
        if (tipIndex == -1) {
            tipsSoFar.add(title);
            extendedTipsSoFar.add(body);
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
   }






    //Get rid of this later; this is just helpful for now in to see what elements tipsSoFar holds.
    void printTips() {
        System.out.println("TIPS SO FAR IS " + tipsSoFar);
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