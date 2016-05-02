package csteachingtips.csteachingtinder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class Settings extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    RadioGroup radioGroup;
    RadioButton personalUser;
    RadioButton conferenceMode;
    Button newUser;
    Button downloadData;
    Button uploadData;
    Button viewFavorites;
    Button clearStoredData;
    CheckBox autoUpload;
    TextView questionsAndAnswers;
    Spinner availableUsers;
    private String username;
    List<String> personalQuestions;
    ArrayList<User> users;
    ArrayList<String> usernames;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("RELOADING PAGE!!!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent intent = getIntent();
        currentUser = (User) intent.getSerializableExtra("CURRENT_USER");
        users = (ArrayList<User>) intent.getSerializableExtra("USERS");



        //Create an action bar with our logo
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.combined_logo);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#AEE8C1")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        personalUser = (RadioButton) findViewById(R.id.personal_user);
        radioGroup = (RadioGroup) findViewById(R.id.r_group);
        conferenceMode = (RadioButton) findViewById(R.id.conference_mode);
        newUser = (Button) findViewById(R.id.new_user);
        newUser.setOnClickListener(this);
        downloadData = (Button) findViewById(R.id.download_data);
        uploadData = (Button) findViewById(R.id.upload_data);
        viewFavorites = (Button) findViewById(R.id.view_favorites);
        clearStoredData = (Button) findViewById(R.id.clear_stored_data);
        autoUpload = (CheckBox) findViewById(R.id.auto_upload);
        questionsAndAnswers = (TextView) findViewById(R.id.personal_questions);
        availableUsers = (Spinner) findViewById(R.id.available_users);
        availableUsers.setOnItemSelectedListener(this);
        loadPersonalQuestions();



        if (currentUser.getAnonymous()){
            radioGroup.check(R.id.conference_mode);
            questionsAndAnswers.setText("Conference mode - no personal questions.");
        } else {
            radioGroup.check(R.id.personal_user);
            questionsAndAnswers.setText(formatQandA(currentUser.getQuestions()));
        }

        usernames = new ArrayList<String>();
        for (User user: users){
            usernames.add(user.getUsername());
            System.out.print("NEW USER:");
            System.out.println(user.getUsername());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, usernames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        availableUsers.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the menu in the Action Bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Specify what happens when each menu item is pressed.
        switch (item.getItemId()) {

            case R.id.settings:
                return true;

            case android.R.id.home:
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.new_user:
                createNewUser();
                break;
            case R.id.download_data:
                //Some sort of return thing??
                break;
            case R.id.upload_data:
                //Nothing here yet.
                break;
            case R.id.view_favorites:
                //Popup, or maybe some sort of return thing.
                break;
            case R.id.clear_stored_data:
                //Some sort of return thing?
                break;
        }
    }



   /* private void updateUsernames(){
        usernames.add(currentUser.getUsername());
        availableUsers.setSelection(availableUsers.getAdapter().getCount()-1);
        System.out.println("UPDATED USERNAMES!!!");
    }
*/
    /**
     * NEXT STEPS:
     * - when you click "create new user," a popup (or maybe a new window) appears
     *   with the name and questions
     * - Have the new users appear places (e.g. in the top of both screens and in the
     *   dropdown)
     * - Make sure info getting stored has usernmae data
     * - Make sure personal/anonymous bump back and forth properly
     * - Make sure personal information bups back
     *
     *
     *
     */


    private String formatQandA(ArrayList<String[]> QandA){
        String result = "";
        for (String[] q : QandA){
            result += q[0] + ": " + q[1] + "\n\n";
        }
        return result;
    }

    private void loadPersonalQuestions(){
        personalQuestions = Arrays.asList("Username", "Which best describes your role?", "If you are an educator, what grades you typically teach?");
    }






    private void createNewUser(){
        AlertDialog.Builder createUserBuilder = new AlertDialog.Builder(this);
        createUserBuilder.setTitle("Create New User");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.create_user_dialog, (ViewGroup) findViewById(R.id.new_user_popup));
        LinearLayout popupLayout = new LinearLayout(this);
        popupLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.WRAP_CONTENT);
        final ArrayList<EditText> editTexts = new ArrayList<EditText>();
        ArrayList<TextView> questions = new ArrayList<TextView>();
        for (String question: personalQuestions){
            editTexts.add(new EditText(this));
            editTexts.get(editTexts.size()-1).setLayoutParams(new android.app.ActionBar.LayoutParams(params));
            questions.add(new TextView(this));
            questions.get(questions.size()-1).setText(question);
            popupLayout.addView(questions.get(questions.size() - 1));
            popupLayout.addView(editTexts.get(editTexts.size()-1));

        }
        createUserBuilder.setView(popupLayout);

        createUserBuilder.setPositiveButton("Create", new CreateNewUser(users) {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("CLICKED THE POSITIVE BUTTON");
                ArrayList<String[]> QandA = new ArrayList<String[]>();
                for (int i = 1; i < personalQuestions.size(); i++){
                    QandA.add(new String[] {personalQuestions.get(i), editTexts.get(i).getText().toString()});
                }
                User newUser = new User(editTexts.get(0).getText().toString(), QandA);
                users.add(newUser);
                for (User user: users){
                    System.out.print("ONE USER: ");
                    System.out.println(user.getUsername());
                }
                usernames.add(newUser.getUsername());
                switchUser(newUser);
                //updateUsernames();

            }
        });
        createUserBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        createUserBuilder.show();
    }

    private void switchUser(User newCurrentUser){
        currentUser = newCurrentUser;
        availableUsers.setSelection(usernames.indexOf(currentUser.getUsername()));
        questionsAndAnswers.setText(formatQandA(currentUser.getQuestions()));
    }







   /* // When you click "View Results," you get a popup with the tips sorted in order of popularity.
    t = new TipSorter(this, MainActivity.tipsSoFar, extendedTipsSoFar, likesSoFar, viewsSoFar);
    goodTipsPopUp(t);
    return true;


    CURRENT PLAN OF ACTION:
    - find out why users isn't getting recognized
    Save the popup results as a real user.





    case R.id.download_results:
    // When you click "Download Results," it downloads data as a csv.
    t = new TipSorter(this, tipsSoFar, extendedTipsSoFar, likesSoFar, viewsSoFar);
    saved = true;
    verifyStoragePermissions(this);
    downloadMessage(t.newFile(versionNum()));
    return true;

    case R.id.clear_data:
    // When you click "Clear Data" you a popup asking you if you really want to delete the data.
    areYouSure();
    return true;*/


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        String username = usernames.get(position).toString();
        for (User u : users){
            if (Objects.equals(u.getUsername(),username)){
                switchUser(u);
                return;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parentView){}







}





