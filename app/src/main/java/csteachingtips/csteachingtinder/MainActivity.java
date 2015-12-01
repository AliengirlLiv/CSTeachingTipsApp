package csteachingtips.csteachingtinder;

import android.content.DialogInterface;
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


public class MainActivity extends AppCompatActivity implements CardModel.OnCardDimissedListener, View.OnClickListener {

    Random rand= new Random();
    TextView instructions;
    ///ActionBar actionBar;
    CardContainer tipContainer;
    TipStackAdapter adapter;
    WebView webView;
    MainActivity activity;
    ImageButton helpButton;
    ArrayList<Tip> tipsSoFar; //All the tips we've seen so far; Depending on how long we need to store
   // info, we could only save these for one session, save these forever, save them for a certain amount
    //of time, or even get rid of tipsSoFar all together if we can update to the site in real time.


    static final String randomTipUrl = "http://csteachingtips.org/random-tip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create an action bar with our logo
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.combined_logo);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#AEE8C1")));

        //Create the help button in the top right corner.
        helpButton = (ImageButton) findViewById(R.id.help_button);
        helpButton.setOnClickListener(this);

        //Create the line of text with the instructions on the bottom of the screen.
        instructions = (TextView) findViewById(R.id.instructions);

        activity = this;
        adapter = new TipStackAdapter(this);
        tipContainer = (CardContainer) findViewById(R.id.tips);
        tipsSoFar = new ArrayList<Tip>();

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





    //Open the help popup when you click on the info button
    @Override
    public void onClick(View v) {
        showSimplePopUp();
        //printTips();
        //System.out.println("_______________________________________");    <== These lines are useful for debugging.
        //adapter.print();
    }


    //Popup opens; closes when the user clicks the "Close" button
    private void showSimplePopUp() {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("About This App");
        helpBuilder.setMessage("Insert brilliant help message.\nTalk about how the app works, the purpose behind the app, and so on.  Make this app cool!");
        helpBuilder.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whatever) {
                        // Just close the dialog
                    }
                });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }


    Tip recordNewTip(Tip newTip) {
     //Loop through previously created tips, see if this tip is already there.  If not, add it.
        for (int i = 0; i < tipsSoFar.size(); i++) {
            if (newTip.equals(tipsSoFar.get(i))) {
                return tipsSoFar.get(i);             //Returns the matching tip (if there is one).
            }
        }
        tipsSoFar.add(newTip);
        return newTip;  //If there's no match, add the current tip to the list and return it.

    }



//Save tip in list.  Increment both # of likes and # of views.
    void likeTip(Tip currTip) {
        Tip tipInList = recordNewTip(currTip);
        tipInList.likeTip();
    }


    //Save tip in list.  Increment only # of views.
    void dislikeTip(Tip currTip) {
        System.out.println("I DON'T LIKE THIS TIP!!!!!");
        Tip tipInList = recordNewTip(currTip);
        tipInList.dislikeTip();
    }



//Earlier, this was onLike.  I changed it so the swipe direction would be right.
    //To change it, just make this method onLike'.
    // onDislike*
    @Override
    public void onDislike() {
        likeTip(adapter.getCurrTip());
        adapter.pop();
        //webView.loadUrl(randomTipUrl);
    }


    //Earlier, this was onDisike.  I changed it so the swipe direction would be right.
    //To change it, just make this method onDisike'.
    // onLike*
    @Override
    public void onLike() {
        dislikeTip(adapter.getCurrTip());
        adapter.pop();
        //webView.loadUrl(randomTipUrl);
    }




    //Get rid of this later; this is just helpful for now in seeing what elements tipsSoFar holds.
    void printTips() {
        for (Tip t : tipsSoFar) {
            System.out.print(t.getDescription());
            System.out.print("  -  ");
            System.out.print(t.getLikes());
            System.out.print("/");
            System.out.println(t.getViews());
            System.out.println();
        }
    }




}