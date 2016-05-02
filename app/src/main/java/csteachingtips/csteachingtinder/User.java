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

   /* public User (String username, String user, String otherUser, String grade, String otherGrade){
        this.username = username;
        this.myRole = user;
        this.otherRole = otherUser;
        this.grade = grade;
        this.otherGrade = otherGrade;
    }

    public String getMyRole(){
        return myRole;
    }

    public String getOtherRole(){
        return otherRole;
    }

    public String getGrade(){
        return grade;
    }

    public String getOtherGrade(){
        return otherGrade;
    }*/

    public User (boolean anonymous){
        this.anonymous = anonymous;
        questions = new ArrayList<String[]>();
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
