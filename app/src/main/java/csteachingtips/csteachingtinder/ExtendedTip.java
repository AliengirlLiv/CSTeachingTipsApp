package csteachingtips.csteachingtinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class ExtendedTip extends AppCompatActivity implements View.OnClickListener {

    WebView w;
    //TextView t;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_tip);

        //t = (TextView) findViewById(R.id.extendedTip);
        Intent intent = getIntent();
        String message = intent.getStringExtra("longTip");
        //t.setText(message);

        b = (Button) findViewById(R.id.returnButton);
        b.setOnClickListener(this);

    }


    public void onClick(View v) {
            Intent intent = new Intent(this, MainActivity.class);
            String message = "goodbye";
            intent.putExtra("longTip", "hello");
            startActivity(intent);
    }




}
