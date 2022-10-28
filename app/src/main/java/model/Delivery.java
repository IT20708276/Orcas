package model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Delivery implements Serializable {

    @Exclude private String delNumber;
    private String del_name;
    private String del_id;
    private String del_status;
    private String userName;
    private String del_list;
    private String del_location;
    private String pickupTime;

    public Delivery() {
    }

    public Delivery(String delNumber, String del_name, String del_id, String del_status, String userName, String del_list, String del_location, String pickupTime) {
        this.delNumber = delNumber;
        this.del_name = del_name;
        this.del_id = del_id;
        this.del_status = del_status;
        this.userName = userName;
        this.del_list = del_list;
        this.del_location = del_location;
        this.pickupTime = pickupTime;
    }

    public String getDel_list() {
        return del_list;
    }

    public void setDel_list(String del_list) {
        this.del_list = del_list;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDel_status() {
        return del_status;
    }

    public void setDel_status(String del_status) {
        this.del_status = del_status;
    }

    public String getDelNumber() {
        return delNumber;
    }

    public void setDelNumber(String delNumber) {
        this.delNumber = delNumber;
    }

    public String getDel_name() {
        return del_name;
    }

    public void setDel_name(String del_name) {
        this.del_name = del_name;
    }

    public String getDel_id() {
        return del_id;
    }

    public void setDel_id(String del_id) {
        this.del_id = del_id;
    }

    public String getDel_location() {
        return del_location;
    }

    public void setDel_location(String del_location) {
        this.del_location = del_location;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }
}
