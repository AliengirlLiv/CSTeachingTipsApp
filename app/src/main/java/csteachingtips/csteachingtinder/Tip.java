package csteachingtips.csteachingtinder;

import android.graphics.drawable.Drawable;

import com.andtinder.model.CardModel;

public class Tip extends CardModel {

    int views = 0;
    int likes = 0;


    public Tip(String description, OnCardDimissedListener listener) {
        super(null, description, (Drawable) null);
        setOnCardDimissedListener(listener);
    }

    public int getViews() {
        return views;
    }

    public int getLikes() {
        return likes;
    }

    public void likeTip() {
        likes += 1;
        views +=1;
    }

    public void dislikeTip() {
        views += 1;
    }




    //Returns true if the descriptions (i.e. the text of the tip) are the same
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tip tip = (Tip) o;

        return getDescription().equals(tip.getDescription());

    }

}
