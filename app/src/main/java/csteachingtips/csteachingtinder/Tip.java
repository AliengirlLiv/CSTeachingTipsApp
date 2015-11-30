package csteachingtips.csteachingtinder;

import android.graphics.drawable.Drawable;

import com.andtinder.model.CardModel;

public class Tip extends CardModel {

    public Tip(String description, OnCardDimissedListener listener) {
        super(null, description, (Drawable) null);
        setOnCardDimissedListener(listener);
    }
}
