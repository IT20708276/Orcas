package util;

import android.app.Application;

public class AdminApi extends Application {
    private String AdminID;
    private String nameIn;
    private String email;
    private String phonenumber;
    private String adminrole;
    private String adminNumber;
    private static AdminApi instance;

    public static AdminApi getInstance(){
        if(instance == null)
            instance = new AdminApi();
        return instance;
    }

    public AdminApi(){}

    public String getNameIn() {return nameIn;}

    public void setNameIn(String nameIn) {
        this.nameIn = nameIn;
    }

    //Work ID of admin
    public String getAdminID() {
        return AdminID;
    }

    public void setAdminID(String adminID) {
        AdminID = adminID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAdminrole() {
        return adminrole;
    }

    public void setAdminrole(String adminrole) {
        this.adminrole = adminrole;
    }

    //database ID
    public String getAdminNumber() {
        return adminNumber;
    }

    public void setAdminNumber(String adminNumber) {
        this.adminNumber = adminNumber;
    }
}
