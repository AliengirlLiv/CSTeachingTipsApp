package csteachingtips.csteachingtinder;

import java.util.ArrayList;

/**
 * Created by Heather on 3/18/2016.
 */
public class User implements java.io.Serializable {


    private String username = "Anonymous/Conference Mode";
    /*private String myRole;
    private String otherRole;
    private String grade;
    private String otherGrade;*/
    private boolean anonymous = false;
    private ArrayList<String[]> questions;


    public User (boolean anonymous){
        this.anonymous = anonymous;
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


    public ArrayList<String[]> getQuestions(){
        return questions;
    }

    public void addQuestion(String[] newQuestion){
        questions.add(newQuestion);
    }




    public Boolean getAnonymous(){ return anonymous; }

    public String getUsername(){
        return username;
    }














}
