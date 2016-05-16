package csteachingtips.csteachingtinder;

import android.content.DialogInterface;

import java.util.ArrayList;


public abstract class CreateNewUser implements DialogInterface.OnClickListener{

    ArrayList<User> users;
    public CreateNewUser(ArrayList<User> users){
        this.users = users;
    }

}