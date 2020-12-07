package com.example.devicelocator;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    //Initialize variables
    private MySQLiteHelper mydb;
    private  EditText email;
    private EditText password;
    private Button login;
    private Button signup;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mydb = new MySQLiteHelper(this);

        createAccountView();
        initViews();        //Initialize database


        //Set on click listener for login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //Get values from EditText fields
                    String Email = email.getText().toString();
                    String Password = password.getText().toString();

                    //Authenticate user
                    User currentUser = mydb.Authentication(new User(null, null, Email, Password));

                    //Check Authentication is successful or not
                    if (currentUser != null) {
                        Snackbar.make(login, "Successfully Logged in!", Snackbar.LENGTH_LONG).show();

                        //User Logged in Successfully, launch Map activity screen
                        Intent intent=new Intent(LoginActivity.this,MapsActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //if User Login is incorrect, display message
                        Snackbar.make(login, "Incorrect Login, Please Try again!", Snackbar.LENGTH_LONG).show();

                    }
                }
            }
        });
    }
    //Function to start Register activity screen
    private void createAccountView() {
        //initialize variables by id
        signup = (Button) findViewById(R.id.btnRegister);
        //Launch register screen upon click of signup button
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        //initialize variables by id
        email = (EditText) findViewById(R.id.txtEmailR);
        password = (EditText) findViewById(R.id.txtPassword);
        login = (Button) findViewById(R.id.btn_login);
    }

    //Function to validate email and password
    private boolean validate() {
        boolean validation = false;

        //Get values and convert to string
        String Email = email.getText().toString();
        String Password = password.getText().toString();

        //If email entered does not match regular email format, send error message
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            validation = false;
            email.setError("Please enter a valid Email");
        }else{
            validation = true;
            email.setError(null);
        }
        //If password field is empty or password entered is too short,display error message
        if(Password.isEmpty()){
            validation=false;
            password.setError("Please enter a valid password");
        }else{
            if(Password.length()>5){
                validation=true;
                password.setError(null);
            }else{
                validation=false;
                password.setError("Password is too short!");
            }
        }
        return validation;
    }
}