package com.example.devicelocator;
public class User {
    //Initialize variables
    public String ID;
    public String Name;
    public String Email;
    public String Password;

    public User(String id, String Name, String Email, String Password) {
        //Assign values
        this.ID = id;
        this.Name = Name;
        this.Password = Password;
        this.Email = Email;
    }

    //Get name of user
    public String getName() {
        return Name;
    }

    //Set name of user
    public void setName(String name) {
        Name = name;
    }


}
