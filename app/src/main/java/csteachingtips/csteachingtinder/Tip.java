package csteachingtips.csteachingtinder;

import android.graphics.drawable.Drawable;

import com.andtinder.model.CardModel;

public class Tip extends CardModel implements Comparable<Tip> {

    int views = 0;
    int likes = 0;


    public Tip(String description, OnCardDimissedListener listener) {
        super(null, description, (Drawable) null);
        setOnCardDimissedListener(listener);
    }


    public Tip(String description, int likes, int views) {
        super(null, description, (Drawable) null);
        this.views = views;
        this.likes = likes;
    }

    public int getViews() {
        return views;
    }

    public int getLikes() {
        return likes;
    }


    public double getRatio() {
        return (1.0 * likes) / views;
    }


    @Override
    public int compareTo(Tip other) {
        //Tip is "lower" if it has a higher likes/views ratio.
        //If the ratios are the same, the tip with more views is "lower."
        double ratioDifference = getRatio() - other.getRatio();
        if (ratioDifference > 0) {
            return -1;
        } else if (ratioDifference < 0) {
            return 1;
        } else {
            return other.getViews() - getViews();

        }


    }



    @Override
    public String toString() {
        return likes + "/" + views + " people liked " + '"' + getDescription() + '"';
    }




}
