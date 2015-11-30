package csteachingtips.csteachingtinder;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
    TextView instructions;
    ActionBar actionBar;

//Greens
    //light #69BD46
    //dark  #13A54A
    //light-dark #13A54A
    //dark^2     #11A44A
    //light^2    #68BD45
    //light^2dark #3EAE49
    //dark^2light #3EAE49
    //Other dark

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


       // # <-- LIGHT GREEN
        //  actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#879f38")));

        tip = (TextView) findViewById(R.id.tip_location);
        instructions = (TextView) findViewById(R.id.instructions);
        swipeButton = (Button) findViewById(R.id.swipe_button);
        swipeButton.setOnClickListener(this);

      //  getActionBar().setIcon(R.drawable.ic_launcher);!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
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