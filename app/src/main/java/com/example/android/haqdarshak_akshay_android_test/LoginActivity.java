package com.example.android.haqdarshak_akshay_android_test;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.haqdarshak_akshay_android_test.database.databaseContract;
import com.example.android.haqdarshak_akshay_android_test.database.usedDbHelper;

import static com.example.android.haqdarshak_akshay_android_test.database.databaseContract.usersEntry.TABLE_NAME;

public class LoginActivity extends AppCompatActivity {
    private usedDbHelper mDbHelper;
    private EditText enterMob;
    private EditText enterPassword;
    private int wrongLoginCount = 0;
    private Button signInButton;
    private TextView pastAttempsErrorView;
    private final int WRONG_ATTEMPT_WAIT_LENGTH = 100000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            wrongLoginCount = savedInstanceState.getInt("wrongLoginCount");
        }
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDbHelper = new usedDbHelper(this);
        enterMob = (EditText) findViewById(R.id.mobileNoLogin);
        enterPassword = (EditText) findViewById(R.id.passwordLogin);
        signInButton = (Button) findViewById(R.id.signInButton);
        pastAttempsErrorView = (TextView) findViewById(R.id.pastAttempsError);
    }

    public void signUp(View view){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void signIn(View view) {
        if (wrongLoginCount < 2) {
            String mobile = enterMob.getText().toString();
            String password = enterPassword.getText().toString();
            if (mobile != null && mobile.length() == 10) {
                if (password != null) {
                    boolean successLoggingIn = checkUsernamePassword(mobile, password);
                    if (successLoggingIn) {
                        wrongLoginCount = 0;
                        Toast.makeText(this, "userVerified", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        enterPassword.setError(getResources().getString(R.string.invalidUsernamePassword).toString());
                        wrongLoginCount = wrongLoginCount + 1;
                    }
                }
            } else {
                enterMob.setError(getResources().getString(R.string.invalidMobile));
            }
        }else{
            signInButton.setEnabled(false);
            pastAttempsErrorView.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    signInButton.setEnabled(true);
                    pastAttempsErrorView.setVisibility(View.GONE);
                    wrongLoginCount = 0;
                }
            }, WRONG_ATTEMPT_WAIT_LENGTH);
        }
    }
    private boolean checkUsernamePassword(String mob, String password){
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = databaseContract.usersEntry.COLUMN_MOBILE + " = ?" + " AND " + databaseContract.usersEntry.USER_PASSWORD +" = ?";
        Cursor cursor = db.query(TABLE_NAME,
                null,
                selection,
                new String[]{mob, password},
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("wrongLoginCount", wrongLoginCount);
        super.onSaveInstanceState(outState);
    }
}
