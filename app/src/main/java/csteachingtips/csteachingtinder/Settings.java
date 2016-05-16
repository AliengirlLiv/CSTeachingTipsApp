package csteachingtips.csteachingtinder;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class Settings extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
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
    private static final String PREFS = "prefs";
    private static final int REQUEST_EXTERNAL_STORAGE = 1; //Must ask user for permission to create files.
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private boolean saved = false;
    private int numDownloads = 0;
    private String date = "";
    TipSorter t;
    android.support.v7.app.AlertDialog alert;
    ArrayList<Integer> likesSoFar;
    ArrayList<Integer> viewsSoFar;
    ArrayList<String> tipsSoFar;
    ArrayList<String> extendedTipsSoFar;
    int group = 0; //This is used to specify a single group of 10 tips to be displayed at a time.
    TextView tenTips;
    TipStackAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        personalUser = (RadioButton) findViewById(R.id.personal_user);
        radioGroup = (RadioGroup) findViewById(R.id.r_group);
        conferenceMode = (RadioButton) findViewById(R.id.conference_mode);
        newUser = (Button) findViewById(R.id.new_user);
        newUser.setOnClickListener(this);
        downloadData = (Button) findViewById(R.id.download_data);
        downloadData.setOnClickListener(this);
        uploadData = (Button) findViewById(R.id.upload_data);
        uploadData.setOnClickListener(this);
        viewFavorites = (Button) findViewById(R.id.view_favorites);
        viewFavorites.setOnClickListener(this);
        clearStoredData = (Button) findViewById(R.id.clear_stored_data);
        clearStoredData.setOnClickListener(this);
        autoUpload = (CheckBox) findViewById(R.id.auto_upload);
        questionsAndAnswers = (TextView) findViewById(R.id.personal_questions);
        availableUsers = (Spinner) findViewById(R.id.available_users);
        availableUsers.setOnItemSelectedListener(this);

        loadPersonalQuestions();
        getData();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, usernames);
        availableUsers.setAdapter(adapter);
        switchUser(currentUser);
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
                downloadData();
                break;
            case R.id.upload_data:
                //Nothing here yet.  Someday there should be a way to automatically upload data from
                //the app to the website.
                break;
            case R.id.view_favorites:
                // When you click "View Results," you get a popup with the tips sorted in order of popularity.
                goodTipsPopUp(t);
                break;
            case R.id.clear_stored_data:
                // When you click "Clear Data" you a popup asking you if you really want to delete the data.
                areYouSure();
                break;
        }
    }


    /**
     * @param QandA - The ArrayList of string arrays in the form [question, answer].
     * @return The properly formatted question/answer text.
     */
    private String formatQandA(ArrayList<String[]> QandA){
        String result = "";
        for (String[] q : QandA){
            result += "\n" + q[0] + "    " + q[1] + "\n\n";
        }
        return result;
    }

    //We only have a few personal questions so far, but we could add any questions needed here.
    private void loadPersonalQuestions(){
        personalQuestions = Arrays.asList("Username", "Which best describes your role?", "If you are an educator, what grades you typically teach?");
    }




    /**\
     * A popup appears that allows you to create a new user.
     * That user becomes the current user.
     */
    private void createNewUser(){
        AlertDialog.Builder createUserBuilder = new AlertDialog.Builder(this);
        createUserBuilder.setTitle("Create New User");
        final LayoutInflater inflater = getLayoutInflater();
        clearStoredData = (Button) findViewById(R.id.clear_stored_data);
        final LinearLayout popupLayout = new LinearLayout(this);
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
                ArrayList<String[]> QandA = new ArrayList<String[]>();
                if (!(usernames.indexOf(editTexts.get(0).getText().toString()) == -1)) {
                    View view = inflater.inflate(R.layout.create_user_dialog, (ViewGroup) findViewById(R.id.new_user_popup));
                    popupLayout.addView(view);
                    ///usernameTaken.setVisibility(View.VISIBLE); //Null pointer!!!
                } else{

                    for (int i = 1; i < personalQuestions.size(); i++) {
                        QandA.add(new String[]{personalQuestions.get(i), editTexts.get(i).getText().toString()});
                    }
                User newUser = new User(editTexts.get(0).getText().toString(), QandA);
                users.add(newUser);
                for (User user : users) {
                }
                usernames.add(newUser.getUsername());
                switchUser(newUser);
            }
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


    /**
     * Change the current user
     * @param newCurrentUser The new current user.
     */
    private void switchUser(User newCurrentUser){
        currentUser = newCurrentUser;
        conferenceMode.setChecked(currentUser.getAnonymous());
        personalUser.setChecked(!currentUser.getAnonymous());
        if (!currentUser.getUsername().equals(availableUsers.getSelectedItem())){
            availableUsers.setSelection(usernames.indexOf(currentUser.getUsername()));
        }
        questionsAndAnswers.setText(formatQandA(currentUser.getQuestions()));

    }


    /**
     * When you select a new user, that becomes the current user, and the personal questions
     * get updated to reflect this.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        String username = usernames.get(position).toString();
        for (User u : users){
            if (Objects.equals(u.getUsername(),username)){

                if (! username.equals(currentUser.getUsername())){
                    switchUser(u);
                }
                return;
            }
        }
    }


    /**
     * Don't do anything if someone clicks on the list of users but doesn't select one.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parentView){}


    /**
     * Call saveData to save all the personal data in SharedPreferences if the user closes or leaves the app.
     */
    @Override
    protected void onPause(){
        super.onPause();
        saveData();
    }

    /**
     * Directly save all the personal data in SharedPreferences if the user closes or leaves the app.
     */
    private void saveData(){
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putInt("Personal/Anonymous", radioGroup.getCheckedRadioButtonId());
        Gson gson = new Gson();
        Type listOfTestObject = new TypeToken<ArrayList<User>>(){}.getType();
        e.putString("Users", gson.toJson(users, listOfTestObject));
        e.putString("CurrentUser", gson.toJson(currentUser));
        e.putBoolean("UpdateAutomatically", autoUpload.isChecked());
        e.putBoolean("Saved", saved);
        e.putInt("NumDownloads", numDownloads);
        e.putString("Date", date);

        e.commit();
    }


    /**
     * Load data from SharedPreferences
     */
    private void getData(){
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        radioGroup.check(sp.getInt("Personal/Anonymous", 1));
        numDownloads = sp.getInt("NumDownloads", 0);
        date = sp.getString("Date", "");
        Gson gson = new Gson();
        currentUser = gson.fromJson(sp.getString("CurrentUser", ""), User.class);
        if (currentUser == null){
            currentUser = new User();
        }
        Type listOfTestObject = new TypeToken<ArrayList<User>>(){}.getType();
        Type typeIntArrayList = new TypeToken<ArrayList<Integer>>(){}.getType();
        Type typeStringArrayList = new TypeToken<ArrayList<String>>(){}.getType();
        tipsSoFar = gson.fromJson(sp.getString("TipsSoFar", ""), typeStringArrayList);
        extendedTipsSoFar = gson.fromJson(sp.getString("ExtendedTipsSoFar", ""), typeStringArrayList);
        viewsSoFar = gson.fromJson(sp.getString("ViewsSoFar", ""), typeIntArrayList);
        likesSoFar = gson.fromJson(sp.getString("LikesSoFar", ""), typeIntArrayList);
        t = new TipSorter(this, tipsSoFar, extendedTipsSoFar, likesSoFar, viewsSoFar);

         String usersString = sp.getString("Users", "");
        if (!(usersString.equals(""))){
            users = gson.fromJson(usersString, listOfTestObject);
        } else {
            users = new ArrayList<User>();
            users.add(currentUser);
        }

        usernames = new ArrayList<String>();
        for (User user: users){
            usernames.add(user.getUsername());
        }
        autoUpload.setChecked(sp.getBoolean("UpdateAutomatically", false));

    }







    // When you click "Download Results," it downloads data as a csv.
    private void downloadData(){
        saved = true;
        verifyStoragePermissions(this);
        downloadMessage(t.newFile(versionNum())); //<-- Add TipSorter
    }




    // Checks if the app has permission to write to device storage
    // If the app does not have permission then the user will be prompted to grant permissions
    private static void verifyStoragePermissions(Activity activity) {
        // Check if we have file reading/writing permissions already
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission, so ask the user for them
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }




    //Popup appears, telling the user where the data has been saved.
    private void downloadMessage(String fileName) {
        android.support.v7.app.AlertDialog.Builder popup = new android.support.v7.app.AlertDialog.Builder(this);
        if (fileName.equals("Error: File not created.")) {
            popup.setTitle("Download Not Completed :(");
            popup.setMessage("There must have been been some problem.  The file couldn't be created.");
        } else {
            popup.setTitle("Download Complete!");
            String str = "A csv file named " + fileName + " has been saved to the Downloads folder in your phone's internal storage.";
            popup.setMessage(str);
        }
        popup.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whatever) {
                        //Then close the popup
                    }
                });
        android.support.v7.app.AlertDialog popupReal = popup.create();
        popupReal.show();
    }



    //Figures out the version number which will appear in the filename of any downloaded file.
    //The version number is "" if it is the first time today a file has been downloaded.  Otherwise,
    //the version number is "_1", "_2", "_3" etc.
    private String versionNum() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String newDate = dateFormat.format(cal.getTime());
        if (date.equals(newDate)) {
            numDownloads++;
            return "_" + numDownloads;
        }
        date = newDate;
        numDownloads = 0;
        return "";
    }



    //Check whether the data has been downloaded yet.
    private boolean unsaved() {
        return !saved;
    }



    //Ask the user if they want to delete the data.
    private void areYouSure() {
        android.support.v7.app.AlertDialog.Builder popup = new android.support.v7.app.AlertDialog.Builder(this);
        popup.setTitle("Are You Sure You Want To Clear Data?");
        String str = "The data cannot be recovered once deleted.";
        if (unsaved()) {
            str = "This data has not been downloaded yet.\n" + str;
        }
        popup.setMessage(str);
        popup.setPositiveButton("Delete Away",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whatever) {
                        clearData();
                        //Then close the popup
                    }
                });
        popup.setNegativeButton("No, Wait!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whatever) {
                        // Just close the popup
                    }
                });
        android.support.v7.app.AlertDialog popupReal = popup.create();
        popupReal.show();
    }




    //Clear SharedPreferences and the data storage ArrayLists.
    private void clearData() {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        sp.edit().clear().apply();
        tipsSoFar = new ArrayList<String>();
        extendedTipsSoFar= new ArrayList<String>();
        likesSoFar = new ArrayList<Integer>();
        viewsSoFar = new ArrayList<Integer>();
        e.remove("TipsSoFar");
        e.remove("ExtendedTipsSoFar");
        e.remove("ViewsSoFar");
        e.remove("LikesSoFar");
    }






    //Brings up a popup with the tips (sorted by popularity).  There are three buttons: two to
    //navigate through the tips, and one to close the popup.
    private void goodTipsPopUp(TipSorter tipSorter) {
        //Create popup
        alert = new android.support.v7.app.AlertDialog.Builder(this).create();
        t = tipSorter;
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.my_alert_dialog, (ViewGroup) findViewById(R.id.tip_popup));
        alert.setTitle("Top Tips");
        int group = 0;
        tenTips = (TextView) view.findViewById(R.id.ten_tips);
        tenTips.setText(t.fiveGroup(group));  //Text is the top 10 tips.
        Button backButton = (Button) view.findViewById(R.id.backward_button);
        Button forwardButton = (Button) view.findViewById(R.id.forward_button);
        Button closeButton = (Button) view.findViewById(R.id.close_button);

        //Specifies what happens when you click the "Back" button.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MainActivity main = MainActivity.this;
                Settings main = Settings.this;
                int group = main.getGroup();
                if (group > 0) { //If possible, go to the previous set of 5 tips.
                    main.incrementGroup(-1);
                    main.tenTips.setText(main.t.fiveGroup(group - 1));
                }
            }
        });

        //Specifies what happens when you click the "Forward" button.
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings main = Settings.this;
                int group = main.getGroup();
                if (group < (tipsSoFar.size() - 1) / 5) {  //If possible, go to the next set of 5 tips.
                    main.incrementGroup(1);
                    main.tenTips.setText(main.t.fiveGroup(group + 1));
                }
            }
        });

        //Specifies what happens when you click the "Close" button.
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.this.alert.dismiss();   //Close popup.
            }
        });

        alert.setView(view);
        alert.show();
    }




    //Return the current group (the current set of 10 tips being viewed).
    private int getGroup(){
        return group;
    }



    //Change "group" by a certain amount.
    private void incrementGroup(int num){
        group += num;
    }

}