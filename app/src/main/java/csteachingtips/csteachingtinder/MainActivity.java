package csteachingtips.csteachingtinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.andtinder.view.CardContainer;


public class MainActivity extends AppCompatActivity  /*implements View.OnClickListener*/ {

    Button swipeButton;
    CardContainer tipContainer;
    TipStackAdapter adapter;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new TipStackAdapter(this);
        tipContainer = (CardContainer) findViewById(R.id.tips);

        swipeButton = (Button) findViewById(R.id.swipe_button);
        //swipeButton.setOnClickListener(this);

        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, String url) {

                // Inject Javascript to extract tip
                view.evaluateJavascript(
                        "document.getElementsByClassName('tipspace')[0].innerHTML.trim();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String tip) {
                                adapter.add(new Tip(tip));

                                // Tips don't seem to be moveable if the adapter is not reset
                                // after they are added. We _should_ be able to set
                                // the adapter once and not have to worry about it.
                                // We should probably look into this eventually,
                                // but it doesn't seem to pose a threat yet.
                                tipContainer.setAdapter(adapter);
                                if (adapter.getCount() < 5) {
                                    view.reload();
                                }
                            }
                        });
            }
        });
        webView.loadUrl("http://csteachingtips.org/random-tip");
    }
}