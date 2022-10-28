package model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Donations implements Serializable {
    @Exclude private String donationNumber;
    private int did;
    private String orgName;
    private long doncontactNumber;
    private String orglocation;
    private String donationList;
    private String donationAmount;


    public Donations() {
    }

    public Donations(String donationNumber, int did, String orgName, long doncontactNumber, String orglocation, String donationList, String donationAmount) {
        this.donationNumber = donationNumber;
        this.did = did;
        this.orgName = orgName;
        this.doncontactNumber = doncontactNumber;
        this.orglocation = orglocation;
        this.donationList = donationList;
        this.donationAmount = donationAmount;
    }

    public String getOrglocation() {
        return orglocation;
    }

    public void setOrglocation(String orglocation) {
        this.orglocation = orglocation;
    }

    public String getDonationNumber() {
        return donationNumber;
    }

    public void setDonationNumber(String donationNumber) {
        this.donationNumber = donationNumber;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public long getDoncontactNumber() {
        return doncontactNumber;
    }

    public void setDoncontactNumber(long doncontactNumber) {
        this.doncontactNumber = doncontactNumber;
    }

    public String getDonationList() {
        return donationList;
    }

    public void setDonationList(String donationList) {
        this.donationList = donationList;
    }

    public String getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(String donationAmount) {
        this.donationAmount = donationAmount;
    }
}
