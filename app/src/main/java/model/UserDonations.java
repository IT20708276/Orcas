package model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class UserDonations implements Serializable {
    @Exclude  private String UserDonNumber;
    private String DonationName;
    private String userName;
    private String userID;
    private int did;
    private float donatedAmount;

    public String getUserDonNumber() {
        return UserDonNumber;
    }

    public void setUserDonNumber(String userDonNumber) {
        UserDonNumber = userDonNumber;
    }

    public String getDonationName() {
        return DonationName;
    }

    public void setDonationName(String donationName) {
        DonationName = donationName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public float getDonatedAmount() {
        return donatedAmount;
    }

    public void setDonatedAmount(float donatedAmount) {
        this.donatedAmount = donatedAmount;
    }
}
