package csteachingtips.csteachingtinder;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


public class TipSorter {

    ArrayList<String> tips;
    ArrayList<Integer> likes;
    ArrayList<Integer> views;
    Tip[] sortedTips;
    TipSorter activity;



    //Save the three data storage ArrayLists.
    public TipSorter(ArrayList<String> tips, ArrayList<Integer> likes, ArrayList<Integer> views) {
        this.tips = tips;
        this.likes = likes;
        this.views = views;
        sortedTips = new Tip[tips.size()];
        makeTipList();
        activity = this;
    }



    //Make an array of Tips.  Sort the tips.  The tips with the greatest likes/views ratios are first.
    //When there's a tie, the tip with more views wins.
    public void makeTipList() {
        for (int i=0; i<tips.size(); i++) {
            sortedTips[i] = new Tip(tips.get(i), likes.get(i), views.get(i));
        }
        Arrays.sort(sortedTips);
    }


    //returns a certain set of 10 tips
    public String tenGroup(int set) {
        int start = 10 * set;
        String str = "";
        for (int i = start; i < Math.min(10 + start, sortedTips.length - 1); i++) {
            str = str + "Tip #" + (i+1) + ": " + sortedTips[i] + '\n';
        }
        return str;
    }





        public void downloadExcel() throws IOException, WriteException
        {
            WritableWorkbook workbook = Workbook.createWorkbook(new File("tipList.xls"));
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);

            for (int i = 0; i< sortedTips.length; i++) {
                Tip tip = sortedTips[i];
                Label tipText = new Label(0, i, tip.getDescription());
                Number likes = new Number(1, i, tip.getLikes());
                Number views = new Number(2, i, tip.getViews());
                sheet.addCell(tipText);
                sheet.addCell(likes);
                sheet.addCell(views);
            }
            workbook.write();
            workbook.close();
        }
}


