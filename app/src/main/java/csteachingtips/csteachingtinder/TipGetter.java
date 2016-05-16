package csteachingtips.csteachingtinder;


import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;




public class TipGetter{

    private RandomList<Integer> availablePages;
    private int totalPages;
    private RandomList<Tip> tipJar;
    TipStackAdapter adapter;
    private Context context;
    private MainActivity mainActivity;




    public TipGetter(TipStackAdapter adapter, MainActivity mainActivity){
        tipJar = new RandomList<Tip>();
        availablePages = new RandomList<Integer>();
        this.context = mainActivity;
        this.mainActivity = mainActivity;
        //When the app first starts, find how many tip pages there are.
        FindNumPages findNumPages = new FindNumPages();
        findNumPages.execute();
    }




    //Take the quotes off of the start and end of each tip.
    private String fixString(String string) {
        string = fixSpecialCharacters(string);
        return string;
    }




    //Change weird symbols which don't show up properly in normal text.
    private String fixSpecialCharacters(String s) {
        return s.replace("\\u003C", "<")
                .replace("\\u003E", ">")
                .replace("\\\"", "\"")
                .replace("\\\'", "\'")
                .replace("\\\\", "\\")
                .replace("\\n", "\n")
                .replace("\\t", "\t")
                .replace("\\u009B", "");
    }





    /**
     * Find the total number of pages we can load tips from.
     */
    public class FindNumPages extends AsyncTask<Void, Void, Integer> {

        /**
         * This loads the "Browse All" page from the website and finds the total number of tip pages.
         *
         * @param whatever //This is a dummy variable
         * @return number of tip pages
         */
        @Override
        protected Integer doInBackground(Void... whatever) { //'whatever' is never used

            try {
                Connection.Response response =  Jsoup.connect("http://csteachingtips.org/browse-all?sort=created&order=asc")
                        .userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                        .referrer("http://www.google.com")
                        .timeout(15000)
                        .execute();
                Document doc = response.parse();
                System.out.println("DOC =" + (doc == null));
                checkConnected(doc.toString());
                Elements lastPage = doc.getElementsByClass("pager-last");
                Element realLastPage = lastPage.get(0);
                Element link = realLastPage.select("a").first();
                String pageURL = link.attr("href");
                String[] split = pageURL.split("page=");
                String pageNumPlusEnd = split[split.length-1];
                String pageNum = "";
                //Chops out the digits which are actually part of the number of pages.
                for (int i = 0; i < pageNumPlusEnd.length(); i++){
                    if (Character.isDigit(pageNumPlusEnd.charAt(i))){
                        pageNum += pageNumPlusEnd.substring(i, i+1);
                    }else{
                        break;
                    }
                }
                totalPages = Integer.parseInt(pageNum);
                return Integer.parseInt(pageNum);
            } catch (IOException | IndexOutOfBoundsException | NullPointerException e) {
                e.printStackTrace();
                return null;
            }
        }



        @Override
        protected void onPostExecute(Integer numPages) {
            super.onPostExecute(numPages);
            if (numPages != null){
                totalPages = numPages;
                newAvailablePageList();
                //Get a new page of tips
                getNewTipPage();
            }

        }





        /**
         * @param doc loaded; if this is null, it means we aren't connected to the internet.
         */
        private void checkConnected(String doc){
            if (doc == "null") {
                new AlertDialog.Builder(context).setMessage("Couldn't load tip.  Are you connected to the internet?").setNeutralButton("Close", null).show();
                return;
            }
        }
    }


    /**
     * Load all of the tips from a single page.
     */
    private class GetTips extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... pageNumber) {

            try {
                //This url is to the one of the "Browse Tips" pages; which one is specified by the "pagenumber" variable
                String url = "http://csteachingtips.org/browse-all?keys=&&&sort_by=created&sort_order=ASC&page=" + pageNumber;
                Connection.Response response =  Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                        .referrer("http://www.google.com")
                        .timeout(15000)
                        .execute();
                Document doc = response.parse();
                Elements tipList = doc.getElementsByClass("views-row");

                //Adds every tip on the page to the tipjar.
                for (Element e: tipList){
                    Element link = e.select("a").first();
                    String tipUrl = link.attr("href");
                    String tipText = link.text();
                    tipJar.add(new Tip(fixString(tipText), tipUrl, 0,0, mainActivity.getColor()));
                }
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            //Keep adding new tips until there are 7 in the adapter.
            while (adapter.getCount() < 7){
                getNewTip();
            }
        }
    }




    /**
     * Pull a tip from the tipJar and add it to the adapter.
     */
    private void getNewTip(){
        Tip newTip = tipJar.removeRandom();
        //If we're low on tips, get a new page
        if (tipJar.size() < 20) {
            getNewTipPage();
        }
        mainActivity.addTip(newTip);
    }





    /**
     * Create a new list of pages from which we can obtain tips.
     */
    private void newAvailablePageList(){
        for (int i = 0; i < totalPages; i++) {
            availablePages.add(i);
        }
    }


    /**
     * Choose a random tip page from the list of available tip pages.
     * Then create a new GetTips object to get all the tips on that page.
     */
    private void getNewTipPage() {
        int nextPage = availablePages.removeRandom();
        GetTips gt = new GetTips();
        gt.execute(nextPage);
    }

}
