package com.example.android.haqdarshak_akshay_android_test;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.haqdarshak_akshay_android_test.database.databaseContract;
import com.example.android.haqdarshak_akshay_android_test.database.usedDbHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.android.haqdarshak_akshay_android_test.database.databaseContract.usersEntry.TABLE_NAME;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameField;
    private EditText ageField;
    private EditText emailIdField;
    private EditText mobileField;
    private EditText passwordField;
    private Spinner sexField;
    private usedDbHelper mDbHelper;
    private Boolean dataOkToSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameField = (EditText) findViewById(R.id.nameField);
        ageField = (EditText) findViewById(R.id.ageField);
        emailIdField = (EditText) findViewById(R.id.emailIdField);
        mobileField = (EditText) findViewById(R.id.mobNoField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        sexField = (Spinner) findViewById(R.id.sexSelector);
        mDbHelper = new usedDbHelper(this);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataOkToSubmit = true;
                long insetId = 0;
                // check name field
                final String name = nameField.getText().toString();
                if (!isValidName(name)){
                    dataOkToSubmit = false;
                    nameField.setError(getResources().getString(R.string.invalidName).toString());
                }

                //check age field
                final String age = ageField.getText().toString();
                if(age != null){
                    if (age.length() == 0){
                        dataOkToSubmit = false;
                        ageField.setError(getResources().getString(R.string.emptyFiled).toString());
                    }
                }else{
                    dataOkToSubmit = false;
                    ageField.setError(getResources().getString(R.string.emptyFiled).toString());
                }

                //check email validations
                final String emailId = emailIdField.getText().toString();
                if (!isValidEmail(emailId)){
                    dataOkToSubmit = false;
                    emailIdField.setError(getResources().getString(R.string.invalidEmail).toString());
                }

                //check mobile no
                final String mobNo = mobileField.getText().toString();
                if (!isValidPhone(mobNo)){
                    dataOkToSubmit = false;
                    mobileField.setError(getResources().getString(R.string.invalidMobile).toString());
                }

                //check password
                final String password = passwordField.getText().toString();
                if(password != null){
                    if (password.length() < 6){
                        dataOkToSubmit = false;
                        passwordField.setError(getResources().getString(R.string.invalidPassword).toString());
                    }
                }else{
                    dataOkToSubmit = false;
                    passwordField.setError(getResources().getString(R.string.invalidPassword).toString());
                }

                final String sex = sexField.getSelectedItem().toString();
                
                if(dataOkToSubmit){
                    ContentValues cv = new ContentValues();
                    cv.put(databaseContract.usersEntry.COLUMN_MOBILE, mobNo);
                    cv.put(databaseContract.usersEntry.COLUMN_NAME, name);
                    cv.put(databaseContract.usersEntry.COLUMN_SEX, sex);
                    cv.put(databaseContract.usersEntry.COLUMN_AGE, age);
                    cv.put(databaseContract.usersEntry.COLUMN_EMAIL, emailId);
                    cv.put(databaseContract.usersEntry.USER_PASSWORD, password);

                    insetId = db.insert(TABLE_NAME, null, cv);
                    db.close();
                    if (insetId>0){
                        Toast.makeText(SignUpActivity.this, getResources().getString(R.string.successAddedDb).toString()
                                , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        mobileField.setError(getResources().getString(R.string.usedMobileNo).toString());
                    }
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isValidPhone(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() == 10) {
                return true;
            }else {return false;}
        }else {return false;}
    }

    private boolean isValidName(String name) {
        if (Pattern.matches("[a-zA-Z[ ]]+", name)) {
            return true;
        }
        else {return false;}
    }
}
