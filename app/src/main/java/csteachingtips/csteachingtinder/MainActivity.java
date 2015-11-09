package csteachingtips.csteachingtinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String[] tipList =  {"First tip", "Second tip", "Third tip", "Fourth tip", "Fifth tip"};
    Button swipeButton;
    Random rand= new Random();
    TextView tip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tip = (TextView) findViewById(R.id.tip_location);
        swipeButton = (Button) findViewById(R.id.swipe_button);
        swipeButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        displayTip ();
    }


    private void displayTip() {
        int index = rand.nextInt(tipList.length);
        tip.setText(tipList[index]);
    }
}