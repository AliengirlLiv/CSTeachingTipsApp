package csteachingtips.csteachingtinder;

import java.util.ArrayList;

public class User implements java.io.Serializable {

    private String username = "Anonymous/Conference Mode";
    private boolean anonymous = false;
    private ArrayList<String[]> questions;


    public User (){
        anonymous = true;
        questions = new ArrayList<String[]>();
        String[] anonArray = {"Anonymous/conference mode.","No personal questions available."};
        questions.add(anonArray);
    }



    public User (String username, ArrayList<String[]> questionResponses){
        this.username = username;
        questions = new ArrayList<String[]>();
        for (String[] q: questionResponses){
            questions.add(q);
        }

    }


    //Returns the user's answers to the personal questions.
    public ArrayList<String[]> getQuestions(){
        return questions;
    }


    //Returns a boolean telling whether or not the user is anonymous.
    public Boolean getAnonymous(){ return anonymous; }

    //Returns the username (or "Anonymous/Conference Mode" if there is no user)
    public String getUsername(){
        return username;
    }

}
