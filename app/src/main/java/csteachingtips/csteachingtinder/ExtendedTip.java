package csteachingtips.csteachingtinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class ExtendedTip extends BaseActivity implements View.OnClickListener {
    WebView w;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_tip);
        w = (WebView)findViewById(R.id.webview);
        w.getSettings().setJavaScriptEnabled(true);
        w.setWebViewClient(new WebViewClient());
        /**
         * PROBLEMS HERE!!!
         *
         * Currently, this just loads a generic extended tip instead of the right one.
         * Ideally, the extended tip would be replaced by the url for the tip.
         * Then the url string would contain the url to be loaded rather than what it currently
         * contains, the extended tip text.
         */
        Intent intent = getIntent();
        String url = intent.getStringExtra("longTip");
        w.loadUrl("http://csteachingtips.org/tip/teach-students-combine-critical-thinking-skills-and-smart-searching-techniques-so-they-can");
        b = (Button) findViewById(R.id.returnButton);
        b.setOnClickListener(this);
    }




    //When you click the "go back" button, go back to the main activity.
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}