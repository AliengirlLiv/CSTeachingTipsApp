package csteachingtips.csteachingtinder;

import android.content.DialogInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Olivia on 4/29/2016.
 */
public abstract class CreateNewUser implements DialogInterface.OnClickListener{

    ArrayList<User> users;

    public CreateNewUser(ArrayList<User> users){
        this.users = users;
    }

 /*   @Override
    public void onClick(DialogInterface dialog, int which) {
        //dialog.();
        User newUser = new User(editTexts.get(0).getText().toString(), editTexts.get(1).getText().toString(), null, editTexts.get(2).getText().toString(), null);
        users.add(newUser);
        username = "hi"; //= nameInput.getText().toString();
    }
*/
}
