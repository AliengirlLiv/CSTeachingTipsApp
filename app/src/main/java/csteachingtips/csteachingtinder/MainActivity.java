package csteachingtips.csteachingtinder;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements CardModel.OnCardDimissedListener, View.OnClickListener {

    Random rand= new Random();
    TextView instructions;
    ///ActionBar actionBar;
    CardContainer tipContainer;
    TipStackAdapter adapter;
    WebView webView;
    MainActivity activity;
    ImageButton helpButton;
    private static final String PREF_NAME = "name"; ///TAKE THIS OUT LATER!!!
    private static final String PREFS = "prefs";
    //SharedPreferences mSharedPreferences;
    Set<String> tipSet;
    ArrayList<Integer> likesSoFar;
    ArrayList<Integer> viewsSoFar;
    ArrayList<String> tipsSoFar; //All the tips we've seen so far; Depending on how long we need to store
   // info, we could only save these for one session, save these forever, save them for a certain amount
    //of time, or even get rid of tipsSoFar all together if we can update to the site in real time.
    int tipsLeft = 5; //Currently, the app loads 5 tips to start, then doesn't load any more.  I added a
    //to load new tips when the tips currently displayed run out.  Not sure if this is the best way to go
    //about it.  It would probably be ideal if a new tip loaded at the back after each swipe.


    static final String randomTipUrl = "http://csteachingtips.org/random-tip";

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
    }



    void loadTips() {
        System.out.println("LOADING TIPS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

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







    public String removeQuotes(String jsonString) {
        return jsonString.substring(1, jsonString.length() - 1);
    }






    public boolean saveArrays()  //We save all of these in SharedPreferences whenever we quit the app (and possibly some other times too).
    {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        //SharedPreferences sp = SharedPreferences.getDefaultSharedPreferences(this);
        //SharedPreferences.Editor mEdit1 = sp.edit();
        SharedPreferences.Editor e = sp.edit();

        e.putInt("NumTips", tipsSoFar.size());

        for(int i=0;i<tipsSoFar.size();i++)
        {
            e.remove("Tip" + i);
            e.putString("Tip" + i, tipsSoFar.get(i));
            e.remove("Like" + i);
            e.putInt("Like" + i, likesSoFar.get(i));
            e.remove("View" + i);
            e.putInt("View" + i, viewsSoFar.get(i));
        }

        System.out.println();
        System.out.println();
        System.out.println("SAVED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println();
        System.out.println();

        return e.commit();
    }



    public void loadArrays() //Context mContext)
    {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);   //CHECK THIS!!!
       // SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(mContext);
        tipsSoFar.clear(); //Clear all the tip-storage arrays
        likesSoFar.clear();
        viewsSoFar.clear();
        int size = sp.getInt("NumTips", 0); //Find out how many tips there should be

        for(int i=0;i<size;i++)
        {
            tipsSoFar.add(sp.getString("Tip" + i, null));
            likesSoFar.add(sp.getInt("Like" + i, 0));
            viewsSoFar.add(sp.getInt("View" + i, 0));     //All tip-storage arrays are now loaded with old data
        }


        System.out.println();
        System.out.println();
        System.out.println("RELOADED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println();
        System.out.println();


    }




    //Open the help popup when you click on the info button
    @Override
    public void onClick(View v) {
        showSimplePopUp();
        printTips();
        //System.out.println("_______________________________________");    <== These lines are useful for debugging.
        //adapter.print();
    }


    //Popup opens; closes when the user clicks the "Close" button
    private void showSimplePopUp() {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("About This App");
        helpBuilder.setMessage("Please swipe to the right if you think a tip is useful to an educator teaching computer science or swipe left if you think the tip is not useful.\n\nThis app displays tips created as part of the CSTeachingTips project, which aims to develop a set of CS teaching tips to help teachers anticipate students’ difficulties and build upon their strengths.\n\n To find more of these tips, please visit csteachingtips.com.");
        helpBuilder.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whatever) {
                        // Just close the dialog
                    }
                });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
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





    void recordTip (Tip currTip, int like) {
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
        checkReload();
    }



    @Override
    public void onDislike() {
        recordTip(adapter.getCurrTip(), 1);
        adapter.pop();
        //webView.loadUrl(randomTipUrl);
        tipsLeft--;
        checkReload();
    }



    void checkReload() {
        System.out.print("Tips Left:");
        System.out.println(tipsLeft);
        if (tipsLeft == 0) {
            System.out.println("Out of tips!");
            loadTips();
            tipsLeft = 5;
        }
    }







    //Get rid of this later; this is just helpful for now in seeing what elements tipsSoFar holds.
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



    @Override
    protected void onPause() {
        super.onPause();
        saveArrays();

    }



    @Override
    protected void onResume() {
        super.onResume();
        loadArrays();
    }







}