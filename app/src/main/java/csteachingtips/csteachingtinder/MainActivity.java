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

import java.util.Random;


public class MainActivity extends AppCompatActivity implements CardModel.OnCardDimissedListener, View.OnClickListener /* View.OnClickListener*/ {

    Random rand= new Random();
    TextView instructions;
    ///ActionBar actionBar;
    CardContainer tipContainer;
    TipStackAdapter adapter;
    WebView webView;
    MainActivity activity;
    ImageButton helpButton;

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
                                adapter.add(new Tip(removeQuotes(jsonTip), activity));

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

    // onDislike*
    @Override
    public void onLike() {
        adapter.pop();
        //webView.loadUrl(randomTipUrl);
    }


    //Open the help popup when you click on the info button
    @Override
    public void onClick(View v) {
        showSimplePopUp();
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





    // onLike*
    @Override
    public void onDislike() {
        adapter.pop();
        //webView.loadUrl(randomTipUrl);
    }
}