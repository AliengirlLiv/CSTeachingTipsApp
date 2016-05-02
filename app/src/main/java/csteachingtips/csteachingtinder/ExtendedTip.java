package csteachingtips.csteachingtinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class ExtendedTip extends AppCompatActivity implements View.OnClickListener {

    WebView w;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FIGURE OUT HOW TO MAKE THIS APPLY TO ALL SCREENS
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.combined_logo);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#AEE8C1")));
        actionBar.setDisplayHomeAsUpEnabled(true);


        setContentView(R.layout.activity_extended_tip);
        w = (WebView)findViewById(R.id.webview);
        w.getSettings().setJavaScriptEnabled(true);
        w.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageCommitVisible(WebView view, String url)//, Bitmap favicon)
            {
               w.loadUrl("javascript: (function() { " +
                       "document.getElementsByClassName('container-fluid')[0].style.display = 'none';" +
                       "document.getElementsByClassName('socialmediabuttons')[0].style.display = 'none';" +
                       "document.getElementsByClassName('sources')[0].style.display = 'none';" +
                       "document.getElementsByClassName('tipside')[0].style.display = 'none';" +
                       "document.getElementsByClassName('tipside')[1].style.display = 'none';" +
                       "document.getElementById('disqus_thread').style.display = 'none'; " +
                       "document.getElementById('footer').style.display = 'none'; " +
                       "})()");
            }
        });
       // <div class="field field-name-field-tags field-type-taxonomy-term-reference field-label-above"><div class="field-label">Tags:&nbsp;</div><div class="field-items"><div class="field-item even"><a href="/browse-all?field_tags%5B%5D=423&amp;search_api_views_fulltext=&amp;sort_by=created">Exploring Computer Science (ECS)</a></div><div class="field-item odd"><a href="/browse-all?field_tags%5B%5D=387&amp;search_api_views_fulltext=&amp;sort_by=created">Meaningful and Relevant Content</a></div></div></div>
        w.loadUrl("http://csteachingtips.org/tip/teach-students-combine-critical-thinking-skills-and-smart-searching-techniques-so-they-can");
        Intent intent = getIntent();
        String message = intent.getStringExtra("longTip");
        b = (Button) findViewById(R.id.returnButton);
        b.setOnClickListener(this);
    }


    public void onClick(View v) {
            Intent intent = new Intent(this, MainActivity.class);
            String message = "goodbye";
            intent.putExtra("longTip", "hello");
            startActivity(intent);
    }

    //FIND A WAY TO REUSE THIS CODE
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Specify what happens when each menu item is pressed.
        switch (item.getItemId()) {

            case R.id.settings:
                goToSettings();
                return true;

            case android.R.id.home:
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    //FIND A WAY TO REUSE THIS CODE
    public void goToSettings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }



}
