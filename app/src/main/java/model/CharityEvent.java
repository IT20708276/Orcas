package model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Date;

public class CharityEvent implements Serializable {
    @Exclude private String eventId;
    private String eventName;
    private String eventDesc;
    private String location;
    private int parCount;
    private String sponsrAmount;
    private Date eventDate;
    private transient Timestamp timeadded;
    private long phoneNumber;
    private String userId;
    private String userName;

    public CharityEvent() {
    }

    public CharityEvent(String eventId, String eventName, String eventDesc, String location, int parCount, String sponsrAmount, Date eventDate, Timestamp timeadded, long phoneNumber, String userId, String userName) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDesc = eventDesc;
        this.location = location;
        this.parCount = parCount;
        this.sponsrAmount = sponsrAmount;
        this.eventDate = eventDate;
        this.timeadded = timeadded;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.userName = userName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getSponsrAmount() {
        return sponsrAmount;
    }

    public void setSponsrAmount(String sponsrAmount) {
        this.sponsrAmount = sponsrAmount;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getTimeadded() {
        return timeadded;
    }

    public void setTimeadded(Timestamp timeadded) {
        this.timeadded = timeadded;
    }


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getParCount() {
        return parCount;
    }

    public void setParCount(int parCount) {
        this.parCount = parCount;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
