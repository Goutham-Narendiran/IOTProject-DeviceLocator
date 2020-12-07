package com.example.devicelocator;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class RegisterActivity extends AppCompatActivity {
    //Initialize Variables
    MySQLiteHelper mydb;
    EditText txtEmail;
    EditText txtUserName;
    EditText txtPassword;
    Button btnSignUp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Initialize database
        mydb = new MySQLiteHelper(this);

        //Call function to initialize variables by id
        initViews();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //Get values from EditText fields
                    String Username = txtUserName.getText().toString();
                    String Email = txtEmail.getText().toString();
                    String Password = txtPassword.getText().toString();

                    //if Email does not exist now add new user to database
                    if (!mydb.emailVerification(Email)) {
                        mydb.addUser(new User(null, Username, Email, Password));
                        //display message of successful login creation
                        Snackbar.make(btnSignUp, "User created successfully! Please Login ", Snackbar.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, Snackbar.LENGTH_LONG);
                    }else {

                        //display user already exists message with existing email
                        Snackbar.make(btnSignUp, "User already exists with same email ", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private void initViews() {
        //Initialize variables by Id
        txtEmail = (EditText) findViewById(R.id.txtEmailR);
        txtPassword = (EditText) findViewById(R.id.txtPasswordR);
        txtUserName = (EditText) findViewById(R.id.txtUsernameR);
        btnSignUp = (Button) findViewById(R.id.btbSignUp);
    }

    //Function to validate email and password for registration
    private boolean validate() {
        boolean validation = false;

        //Get values and convert to string
        String Email = txtEmail.getText().toString();
        String Password = txtPassword.getText().toString();
        String Username = txtUserName.getText().toString();

        //If username is empty, or doesn't satisfy requirements, display message
        if (Username.isEmpty()) {
            validation = false;
            txtUserName.setError("Please enter valid username!");
        } else {
            if (Username.length() > 3) {
                validation = true;
                txtUserName.setError(null);
            } else {
                validation = false;
                txtUserName.setError("Username is to short!");
            }
        }

        //If email entered does not match regular email format, send error message
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            validation = false;
            txtEmail.setError("Please enter a valid Email");
        }else{
            validation = true;
            txtEmail.setError(null);
        }
        //If password field is empty or password entered is too short,display error message
        if(Password.isEmpty()){
            validation=false;
            txtPassword.setError("Please enter a valid password");
        }else{
            if(Password.length()>5){
                validation=true;
                txtPassword.setError(null);
            }else{
                validation=false;
                txtPassword.setError("Password is too short!");
            }
        }
        return validation;
    }
}
