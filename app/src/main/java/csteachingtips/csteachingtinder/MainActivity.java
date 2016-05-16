package csteachingtips.csteachingtinder;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
    int nextCol = 0;

    static final String randomTipUrl = "http://csteachingtips.org/random-tip";
    private GoogleApiClient client;
    static boolean ready = true;
    private User currentUser;
    private ArrayList<User> users;
    int numTips = 5;
    TextView loggedIn;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("COLORS: " + Color.parseColor("#AEC1E8") + " - " + Color.parseColor("#8A99B8"));
        setStuffUp();
    }

    private void setStuffUp() {

        setContentView(R.layout.activity_main);

        //Create and display new user
        User anonymousUser = new User();
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

        //MainActivity is the only screen that doesn't need a home button.
        getActionbar().setDisplayHomeAsUpEnabled(false);

        activity = this;
        adapter = new TipStackAdapter(this);
        tipContainer = (CardPile) findViewById(R.id.tips);
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);


        /**
         * This gets called after the new randomTip is loaded
         */
        final ValueCallback v = new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String jsonTip) {
                //If there's no tip, it means we're probably not connected to the internet.
                if (jsonTip.equals("null")) {
                    new AlertDialog.Builder(MainActivity.this).setMessage("Couldn't load tip.  Are you connected to the internet?")
                             .setNeutralButton("Close", null).show();
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
                String title = jsonTip.substring(1, index);
                String description = jsonTip.substring(index + 4);
                Tip newTip = new Tip(title, removeQuotes(description, 1), activity, getColor());
                adapter.add(newTip);
                tipContainer.setAdapter(adapter);
                if (adapter.getCount() < numTips) {
                    webView.reload();
                }
            }
        };


        /**
         * When the page is finished, get a tip and the extended tip from the website.
         */
        WebViewClient wvc = new WebViewClient() {

            @Override
            public void onPageFinished(final WebView myView, String url) {

                myView.evaluateJavascript(
                        "var div = document.createElement('div');"
                                + "div.innerHTML = (document.getElementsByClassName('tip-title')[0].innerHTML).concat('\\n\\n').concat(document.getElementsByClassName('field-item even')[0].innerHTML);"
                                + "div.textContent.trim() || div.innerText.trim() || '';", v); //On the line above, if there isn't any body, it gets the 1st tag instead

                ready = true;
            }
        };
        webView.setWebViewClient(wvc);

        loadTips();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * Add tip created by the TipGetter class.
     * (This won't be called, since the TipGetter class is never called.
     * @param tip - the tip created by the TipGetter class
     */
    public void addTip(Tip tip){
        adapter.add(tip);
        tipContainer.setAdapter(adapter);
    }


    /**
     * Grab tips from website, display them
     */
    private void loadTips() {
        webView.loadUrl(randomTipUrl);
    }





    /**
     * Take the quotes off of the start and end of each tip.
     */
    private String removeQuotes(String jsonString, int x) {
        jsonString = fixSpecialCharacters(jsonString);
        return jsonString.substring(x, jsonString.length() - 1 + x);
    }



    //Change weird symbols which don't show up properly in normal text.
    private String fixSpecialCharacters(String s) {
        return s.replace("\\u003C", "<")
                .replace("\\u003E", ">")
                .replace("\\\"", "\"")
                .replace("\\\'", "\'")
                .replace("\\\\", "\\") //Make sure this doesn't happen multiple times
                .replace("\\n", "\n")
                .replace("\\t", "\t")
                .replace("\\u009B", "");
    }






    //We save the data on the tips viewed so far (and other important data) in SharedPreferences
    // whenever we navigate away from the app or close it.
    private boolean saveData() {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        Gson gson = new Gson();

        Type typeIntArrayList = new TypeToken<ArrayList<Integer>>(){}.getType();
        Type typeStringArrayList = new TypeToken<ArrayList<String>>(){}.getType();

        e.putString("TipsSoFar", gson.toJson(tipsSoFar, typeStringArrayList));
        e.putString("ExtendedTipsSoFar", gson.toJson(extendedTipsSoFar, typeStringArrayList));
        e.putString("LikesSoFar", gson.toJson(likesSoFar, typeIntArrayList));
        e.putString("ViewsSoFar", gson.toJson(viewsSoFar, typeIntArrayList));
        e.putBoolean("Saved", saved);


        for (int i = 0; i < Math.max(adapter.getCount(), numTips); i++){
            e.remove("ActiveTip" + i);
            e.remove("ActiveETip" + i);
            e.remove("ActiveLikes" + i);
            e.remove("ActiveViews" + i);
        }
        for (int i = 0; i < adapter.getCount(); i++) {
            Tip currTip = adapter.getCurrTip();
            e.putString("ActiveTip" + i, currTip.getTitle());
            e.putString("ActiveETip" + i, currTip.getDescription());
            e.putInt("ActiveLikes" + i, currTip.getLikes());
            e.putInt("ActiveViews" + i, currTip.getViews());
            adapter.pop();
        }
        return e.commit();
    }



    //When we start or come back to the app, reload the saved data.
    private void loadData()
    {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        saved = sp.getBoolean("Saved", true);


        Gson gson = new Gson();
        Type typeIntArrayList = new TypeToken<ArrayList<Integer>>(){}.getType();
        Type typeStringArrayList = new TypeToken<ArrayList<String>>(){}.getType();

        currentUser = gson.fromJson(sp.getString("CurrentUser", gson.toJson(new User(), User.class)), User.class);
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
                Tip newTip = new Tip(active, description, likes, views, getColor());
                adapter.add(newTip); //Maybe reload webview after one?  After all of them?
            }
        }
        tipContainer.setAdapter(adapter);

    }







    //Open the help popup when you click on the info button
    @Override
    public void onClick(View v) {
        showAboutPopUp();
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
    private int findTip(String text) {
        //Loop through previously created tips, see if this tip is already there.
        for (int i = 0; i < tipsSoFar.size(); i++) {
            if (text.equals(tipsSoFar.get(i))) {
                return i;             //Returns the index of the matching tip (if there is one).
            }
        }
        return -1;  //If there's no match, return -1.

    }





    //Stores the view and (possibly) like of the current tip in the 3 data storage ArrayLists.
    private void recordTip(Tip currTip, int like) {
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


    /**
     * PROBLEMS with OnLike and OnDislike!!!
     * If the user swipes too fast, tips start getting repeated,
     * and in the extreme case where all the tips run out, the app crashes.
     */




    /**
     * This is called when the user swipes RIGHT.
     * (Yeah, I know it's backwards of how I'd expect it to work).
     */
    @Override
    public void onDislike() {
        recordTip(adapter.getCurrTip(), 0);
        adapter.pop();
        tipsLeft--;
        saved = false;
        loadTips();
    }





    /**
     * This is called when the user swipes LEFT.
     */
    @Override
    public void onLike() {
        recordTip(adapter.getCurrTip(), 1);
        adapter.pop();
        tipsLeft--;
        saved = false;
        System.out.println("Boo :(");
        loadTips();
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


    /**
     * Get tip color
     * @return int[] containing a pair of colors: one light and one dark, to be used in the tip
     */
    public int[] getColor(){
        int blue = Color.parseColor("#AEC1E8");
        int teal = Color.parseColor("#AEE8E8");
        int green = Color.parseColor("#D4FADD");
        int blueDark = Color.parseColor("#8A99B8");
        int tealDark = Color.parseColor("#88B3B3");
        int greenDark = Color.parseColor("#A3C4AB");
        int[] colors = {green, teal, blue};
        int[] darkColors = {greenDark, tealDark, blueDark};
        int[] result = {colors[nextCol], darkColors[nextCol]};
        nextCol = (nextCol+1)%3;
        System.out.println("NEXT COLOR: " + nextCol);
        return result;

    }





    //Get rid of this later; this is just helpful for now in to see what elements tipsSoFar holds.
    private void printTips() {
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

