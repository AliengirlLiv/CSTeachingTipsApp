package csteachingtips.csteachingtinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardStackAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TipStackAdapter extends CardStackAdapter implements View.OnClickListener {

    protected List<CardModel> mData;
    Button extendButton;
    ArrayList<String> longTips;

    public TipStackAdapter(Context mContext) {
        super(mContext);
        mData = Collections.synchronizedList(new ArrayList<CardModel>());
        longTips = new ArrayList<String>();
    }

    @Override
    protected View getCardView(int position, CardModel model, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.tip, parent, false);
            assert convertView != null;
        }

        ((TextView) convertView.findViewById(R.id.tip)).setText(model.getTitle()); //If we ever want description, put it in here.
        longTips.add(model.getDescription());
        int[] col = getColor();
        (convertView.findViewById(R.id.tip_container)).setBackgroundColor(col[0]);
        ///(convertView.findViewById(R.id.expand)).setBackgroundColor(col[1]);
        extendButton = (Button) (convertView.findViewById(R.id.expand));
        extendButton.setBackgroundColor(col[1]);
        extendButton.setOnClickListener(this);

        return convertView;
    }

    int nextCol = 0;

    private int[] getColor(){
        int blue = Color.parseColor("#AEC1E8");
        //int gray = Color.parseColor("#B3B3B3");
        int teal = Color.parseColor("#AEE8E8");
        int green = Color.parseColor("#D4FADD");

        int blueDark = Color.parseColor("#8A99B8");
        //int gray = Color.parseColor("#B3B3B3");
        int tealDark = Color.parseColor("#88B3B3");
        int greenDark = Color.parseColor("#A3C4AB");
/**
 int pink = Color.parseColor("#FAD4F1");
 int yellow = Color.parseColor("#FAF0D4");

 int blue = Color.parseColor("#D4DEFA");*/
        int[] colors = {green, teal, blue};
        int[] darkColors = {greenDark, tealDark, blueDark};
        //Random rand = new Random();
        int[] result = {colors[nextCol], darkColors[nextCol]};
        nextCol = (nextCol+1)%3;
        return result;

    }


    public void onClick(View v){
        Intent intent = new Intent(getContext(), ExtendedTip.class);
        System.out.print("Long Tip: ");
        printLongTips();
        intent.putExtra("longTip", longTips.get(0));
        Context c = getContext();
        c.startActivity(intent);
    }




    private void printLongTips(){
        for (String tip : longTips) {
            System.out.println(longTips);
        }
    }


    @Override
    public void add(CardModel item) {
        mData.add(item);
        //notifyDataSetChanged();
    }

    /////    @Override
    /////   public CardModel pop() {
    /////       CardModel model = mData.remove(getCount() - 1);
    /////       //notifyDataSetChanged();
    /////      return model;
    /////  }


    @Override
    public CardModel pop() {
        CardModel model = mData.remove(0);
        return model;
    }




    @Override
    public CardModel getCardModel(int position) {
        return mData.get(position); //This line is causing it to crash! IDK WHY!
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public Tip getCurrTip() {
        return (Tip) mData.get(0);
    }

    //Take out later; currently useful for debugging
    public void print() {
        for (CardModel cm : mData) {
            System.out.println(cm.getDescription());
        }
    }
}