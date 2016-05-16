package csteachingtips.csteachingtinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardStackAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TipStackAdapter extends CardStackAdapter implements View.OnClickListener {

    protected List<CardModel> mData;
    Button extendButton;
    ArrayList<String> longTips;

    public TipStackAdapter(Context mContext) {
        super(mContext);
        mData = Collections.synchronizedList(new ArrayList<CardModel>());
        longTips = new ArrayList<String>();
    }

    //Create and display the tip card.
    @Override
    protected View getCardView(int position, CardModel model, View convertView, ViewGroup parent) {
        Tip tipModel = (Tip) model;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.tip, parent, false);
            assert convertView != null;
        }

        ((TextView) convertView.findViewById(R.id.tip)).setText(model.getTitle());
        longTips.add(model.getDescription());
        (convertView.findViewById(R.id.tip_container)).setBackgroundColor(tipModel.getColor()[0]);
        extendButton = (Button) (convertView.findViewById(R.id.expand));
        extendButton.setBackgroundColor(tipModel.getColor()[1]);
        extendButton.setOnClickListener(this);
        return convertView;
    }




    /**
     * When the "Extend" button is clicked, go to the extended tips page.
     */@Override
    public void onClick(View v){
        Intent intent = new Intent(getContext(), ExtendedTip.class);
        //Currently, we also pass the extended tip text.  Ideally, I would like
        //to change this to passing the tip's URL so we can load the actual
        //extended tips page
        intent.putExtra("longTip", getCurrTip().getDescription());
        Context c = getContext();
        c.startActivity(intent);
    }




    private void printLongTips(){
        for (String tip : longTips) {
            System.out.println(longTips);
        }
    }


    //I feel like this could be part of the reason why the adapter needs to be reset every time a
    // new tip is added.  In the original library class, notifyDataSetChanged() is called, which
    // should reload the view, but unfortunately when this is added, you can't swipe at all.
    @Override
    public void add(CardModel item) {
        mData.add(item);
        //notifyDataSetChanged();
    }



    @Override
    public CardModel pop() {
        CardModel model = mData.remove(0);
        return model;
    }




    @Override
    public CardModel getCardModel(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public Tip getCurrTip() {
        return (Tip) mData.get(0);
    }



    //Take out later; currently useful for debugging
    private void print() {
        for (CardModel cm : mData) {
            System.out.println(cm.getDescription());
        }
    }
}

