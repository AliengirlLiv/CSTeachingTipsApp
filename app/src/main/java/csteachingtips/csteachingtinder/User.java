package csteachingtips.csteachingtinder;

/**
 * Created by Heather on 3/18/2016.
 */
public class User {

    /*public static final int PRIMARILY_CS_TEACHER = 1;
    public static final int PRIMARILY_NON_CS_TEACHER = 2;
    public static final int NON_CS_TEACHER = 3;
    public static final int CS_PROFESSIONAL = 4;
    public static final int CS_STUDENT = 5;
    public static final int OTHER_USER = 6;
    private static final int ELEMENTARY = 7;
    private static final int JUNIOR_HIGH = 8;
    private static final int HIGH_SCHOOL = 9;
    private static final int COLLEGE = 10;
    private static final int OTHER_GRADE = 11;*/
    private String username;
    private String user;
    private String otherUser;
    private String grade;
    private String otherGrade;

    public User (String username, String user, String otherUser, String grade, String otherGrade){
        this.username = username;
        this.user = user;
        this.otherUser = otherUser;
        this.grade = grade;
        this.otherGrade = otherGrade;
    }

    public String getUsername(){
        return username;
    }

    public String getUser(){
        return user;
    }

    public String getOtherUser(){
        return otherUser;
    }

    public String getGrade(){
        return grade;
    }

    public String getOtherGrade(){
        return otherGrade;
    }




}
