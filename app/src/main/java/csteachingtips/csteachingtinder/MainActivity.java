package csteachingtips.csteachingtinder;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.andtinder.view.CardContainer;

import java.util.Random;


public class MainActivity extends AppCompatActivity  /*implements View.OnClickListener*/ {

    Button swipeButton;
    Random rand = new Random();
    CardContainer tipContainer;
    TipStackAdapter adapter;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new TipStackAdapter(this);
        tipContainer = (CardContainer) findViewById(R.id.tips);
        tipContainer.setAdapter(adapter);

        swipeButton = (Button) findViewById(R.id.swipe_button);
        //swipeButton.setOnClickListener(this);

        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new TipExtractor(this), "TipExtractor");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                // Inject Javascript to extract tip
                webView.loadUrl("javascript:window.TipExtractor.extractTip(" +
                        "document.getElementsByClassName('tipspace')[0].innerHTML.trim());");
            }
        });

        webView.loadUrl("http://csteachingtips.org/random-tip");
        for (int i = 0; i < 5; i++) {
            webView.reload();
        }

    }

    private class TipExtractor {

        private Activity ctx;
        public String tip_;

        TipExtractor(Activity ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void extractTip(String tip) {
            tip_ = tip;
            ctx.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.add(new Tip(tip_));
                }
            });
        }
    }
}