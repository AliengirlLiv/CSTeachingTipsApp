package csteachingtips.csteachingtinder;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements CardModel.OnCardDimissedListener /* View.OnClickListener*/ {

    Button swipeButton;
    Random rand= new Random();
    TextView tip;
    TextView instructions;
    ActionBar actionBar;
    CardContainer tipContainer;
    TipStackAdapter adapter;
    WebView webView;
    MainActivity activity;

    static final String randomTipUrl = "http://csteachingtips.org/random-tip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create an action bar with our logo and the title "CS Teaching Tips"
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("CS Teaching Tips!!!");
        //actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setIcon(R.drawable.logo_best);
        //actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#AEE8C1"))); //#3E9865
        tip = (TextView) findViewById(R.id.tip_location);
        instructions = (TextView) findViewById(R.id.instructions);
        activity = this;

        adapter = new TipStackAdapter(this);
        tipContainer = (CardContainer) findViewById(R.id.tips);
        //tipContainer.setAdapter(adapter);
        swipeButton = (Button) findViewById(R.id.swipe_button);
        //swipeButton.setOnClickListener(this);

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

    // onLike*
    @Override
    public void onDislike() {
        adapter.pop();
        //webView.loadUrl(randomTipUrl);
    }
}