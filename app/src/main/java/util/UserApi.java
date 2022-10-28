package util;

import android.app.Application;

import com.google.firebase.firestore.auth.User;

public class UserApi extends Application {
    private String UserID;
    private String UserName;
    private String UserEmail;
    private String UserphoneNumber;
    private static UserApi instance;

    public static UserApi getInstance(){
        if(instance == null)
            instance = new UserApi();
        return instance;
    }
    public UserApi() {
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserphoneNumber() {
        return UserphoneNumber;
    }

    public void setUserphoneNumber(String userphoneNumber) {
        UserphoneNumber = userphoneNumber;
    }
}
