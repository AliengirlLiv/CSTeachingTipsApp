package csteachingtips.csteachingtinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.andtinder.view.CardContainer;

import java.util.Random;


public class MainActivity extends AppCompatActivity /*implements View.OnClickListener*/ {

    String[] tipList = {"First tip", "Second tip", "Third tip", "Fourth tip", "Fifth tip"};
    Button swipeButton;
    Random rand = new Random();
    CardContainer tipContainer;
    TipStackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new TipStackAdapter(this);
        for (String tip : tipList) {
            adapter.add(new Tip(tip));
        }
        tipContainer = (CardContainer) findViewById(R.id.tips);
        tipContainer.setAdapter(adapter);

        swipeButton = (Button) findViewById(R.id.swipe_button);
        //swipeButton.setOnClickListener(this);
    }

}