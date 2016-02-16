package csteachingtips.csteachingtinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ExtendedTip extends AppCompatActivity {

    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_tip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //Maybe change this????

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "What is this thing??????????", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        t = (TextView) findViewById(R.id.extendedTip);
        Intent intent = getIntent();
        String message = intent.getStringExtra("longTip");//MyActivity.EXTRA_MESSAGE);
        t.setText(message);
        t.setTextSize(40);

        //RelativeLayout layout = (RelativeLayout) findViewById(R.id.content);
        //layout.addView(textView);
    }

}
