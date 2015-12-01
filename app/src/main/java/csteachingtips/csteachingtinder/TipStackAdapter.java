package csteachingtips.csteachingtinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardStackAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TipStackAdapter extends CardStackAdapter {

    protected List<CardModel> mData;

    public TipStackAdapter(Context mContext) {
        super(mContext);
        mData = Collections.synchronizedList(new ArrayList<CardModel>());
    }

    @Override
    protected View getCardView(int position, CardModel model, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.tip, parent, false);
            assert convertView != null;
        }

        ((TextView) convertView.findViewById(R.id.tip)).setText(model.getDescription());
        return convertView;
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
    public void print() {
        for (CardModel cm : mData) {
            System.out.println(cm.getDescription());
        }
    }

}


