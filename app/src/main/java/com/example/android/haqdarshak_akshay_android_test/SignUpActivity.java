package com.example.android.haqdarshak_akshay_android_test;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.haqdarshak_akshay_android_test.database.databaseContract;
import com.example.android.haqdarshak_akshay_android_test.database.usedDbHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.android.haqdarshak_akshay_android_test.database.databaseContract.usersEntry.TABLE_NAME;
/*This activity handles the sign up activity of the app*/
public class SignUpActivity extends AppCompatActivity {
    private EditText nameField;
    private EditText ageField;
    private EditText emailIdField;
    private EditText mobileField;
    private EditText passwordField;
    private Spinner sexField;
    private usedDbHelper mDbHelper;
    private Boolean dataOkToSubmit;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private String mTempPhotoPath;
    private Bitmap mResultsBitmap;
    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.fileprovider1";
    private ImageView mImageView;
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
        mImageView = (ImageView) findViewById(R.id.imageView);
        mDbHelper = new usedDbHelper(this);
        //get a writable version of the database.
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //on click listener for signup button and check validations for fields
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
                //if all fields are valid create entry in the database
                if(dataOkToSubmit){
                    ContentValues cv = new ContentValues();
                    cv.put(databaseContract.usersEntry.COLUMN_MOBILE, mobNo);
                    cv.put(databaseContract.usersEntry.COLUMN_NAME, name);
                    cv.put(databaseContract.usersEntry.COLUMN_SEX, sex);
                    cv.put(databaseContract.usersEntry.COLUMN_AGE, age);
                    cv.put(databaseContract.usersEntry.COLUMN_EMAIL, emailId);
                    cv.put(databaseContract.usersEntry.USER_PASSWORD, password);
                    insetId = db.insert(TABLE_NAME, null, cv);
                    //db.close();
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
    //helper method for email validation
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    //helper method for phone validation
    private boolean isValidPhone(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() == 10) {
                return true;
            }else {return false;}
        }else {return false;}
    }
    //helper method for name validation
    private boolean isValidName(String name) {
        if (Pattern.matches("[a-zA-Z[ ]]+", name)) {
            return true;
        }
        else {return false;}
    }
    // method is called on clicking capture image
    public void captureImage(View v){
        // Check for the external storage permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // If you do not have permission, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            // Launch the camera if the permission exists
            launchCamera();
        }
    }

    private void launchCamera() {
        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = createTempImageFile(this);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                mTempPhotoPath = photoFile.getAbsolutePath();

                // Get the content URI for the image file
                Uri photoURI = FileProvider.getUriForFile(this,
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the image capture activity was called and was successful
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Process the image and set it to the TextView
            processAndSetImage();
        } else {
            // Otherwise, delete the temporary image file
            deleteImageFile(this, mTempPhotoPath);
        }
    }

    private void processAndSetImage() {

        // Resample the saved image to fit the ImageView
        mResultsBitmap = resamplePic(this, mTempPhotoPath);
        // Set the new bitmap to the ImageView
        mImageView.setImageBitmap(mResultsBitmap);
    }
    static Bitmap resamplePic(Context context, String imagePath) {
        // Get device screen size information
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        int targetH = metrics.heightPixels;
        int targetW = metrics.widthPixels;
        // Get the dimensions of the original bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        return BitmapFactory.decodeFile(imagePath);
    }

    static File createTempImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalCacheDir();

        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }
    static boolean deleteImageFile(Context context, String imagePath) {
        // Get the file
        File imageFile = new File(imagePath);
        // Delete the image
        boolean deleted = imageFile.delete();
        // If there is an error deleting the file, show a Toast
        if (!deleted) {
            Toast.makeText(context, "error!", Toast.LENGTH_SHORT).show();
        }
        return deleted;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
       // deleteImageFile(this, mTempPhotoPath);
    }
}
