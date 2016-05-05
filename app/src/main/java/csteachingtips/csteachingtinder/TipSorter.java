package csteachingtips.csteachingtinder;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.util.Date;


public class TipSorter {
    Context context;
    ArrayList<String> tips;
    ArrayList<String> extendedTips;
    ArrayList<Integer> likes;
    ArrayList<Integer> views;
    Tip[] sortedTips;
    TipSorter activity;


    //Save the three data storage ArrayLists.
    public TipSorter(Context context, ArrayList<String> tips, ArrayList<String> extendedTips, ArrayList<Integer> likes, ArrayList<Integer> views) {
        this.context = context;
        this.tips = tips;
        this.extendedTips = extendedTips;
        this.likes = likes;
        this.views = views;
        sortedTips = new Tip[tips.size()];
        makeTipList();
        activity = this;
    }








    //Creates a new CSV file with the tips, listed in order of popularity
    String newFile(String versionNum) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            Date date = new Date();
            String fileName = "csTeachingTips-" + dateFormat.format(date) + versionNum + ".csv";
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            FileOutputStream fos = new FileOutputStream(file);
            String myInputText = "";
            for (int i = 0; i < sortedTips.length; i++) {
                Tip tip = sortedTips[i];
                String tipText =  tip.getTitle() + "," + tip.getDescription();
                //Take out all weird characters (e.g. smart quotes) b/c they show up wrong in Excel
                tipText = tipText.replaceAll("[\\u2018\\u2019\\u201b\\u2032]", "'")
                        .replaceAll("[\\u201C\\u201D\\u201e\\u2033]", "\"")
                        .replaceAll("[\\u2013\\u2014\\u2015]", "-")
                        .replaceAll("[\\u2017]", "_")
                        .replaceAll("[\\u2026]", "...")
                        .replaceAll("[\\u201a]", ",")
                        .replace("\\u003C", "<")
                        .replace("\\u003E", ">")
                ;


                //Add quotes at beginning and end, and make quotes within tips double up ("").
                tipText = "\"" + tipText.replace("\"", "\"\"") + "\"";



                String tipLong = tip.getLong().replaceAll("[\\u2018\\u2019\\u201b\\u2032]", "'")
                        .replaceAll("[\\u201C\\u201D\\u201e\\u2033]", "\"")
                        .replaceAll("[\\u2013\\u2014\\u2015]", "-")
                        .replaceAll("[\\u2017]", "_")
                        .replaceAll("[\\u2026]", "...")
                        .replaceAll("[\\u201a]", ",")
                        .replace("\\u003C", "<")
                        .replace("\\u003E", ">")
                        ;

                //Add quotes at beginning and end, and make quotes within tips double up ("").
                tipLong = "\"" + tipLong.replace("\"", "\"\"") + "\"";

                int likes = tip.getLikes();
                int views = tip.getViews();
                myInputText = myInputText + tipText + "," + tipLong + "," + likes + "," + views + "\n";
            }
            fos.write(myInputText.getBytes());
            fos.close();
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: File not created.";
        }

    }





    //Make an array of Tips.  Sort the tips.  The tips with the greatest likes/views ratios are first.
    //When there's a tie, the tip with more views wins.
    public void makeTipList() {
        for (int i = 0; i < tips.size(); i++) {
            sortedTips[i] = new Tip(tips.get(i), extendedTips.get(i), likes.get(i), views.get(i));
        }
        Arrays.sort(sortedTips);
    }



    //Returns a certain set of 5 tips
    public String fiveGroup(int set) {
        String str = "";
        if (sortedTips.length==0){
            str = "Sorry, it looks like you don't have any tips saved. \n\n After you like some tips, they will appear here.\n\n\n";
        } else {
            System.out.print("FIRST: ");
            System.out.println(sortedTips[0]);
            System.out.print("LAST: ");
            System.out.print(sortedTips[sortedTips.length - 1]);

            int start = 5 * set;

            for (int i = start; i < Math.min(5 + start, sortedTips.length); i++) {
                str = str + "Tip #" + (i + 1) + ": " + sortedTips[i] + "\n\n";
            }
        }
        return str;
    }

}





