package model;

import java.io.Serializable;
import java.util.Date;

public class Card implements Serializable {
    private String cardId;
    private String userName;
    private String userEmail;
    private String cardNumber;
    private String cardName;
    private String cnvNumber;
    private Date validThrough;

    public Card() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCnvNumber() {
        return cnvNumber;
    }

    public void setCnvNumber(String cnvNumber) {
        this.cnvNumber = cnvNumber;
    }

    public Card(String cardId, String cardNumber, String cardName, Date validThrough) {
        this.cardId = cardId;
        this.cardNumber = cardNumber;
        this.cardName = cardName;
        this.validThrough = validThrough;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Date getValidThrough() {
        return validThrough;
    }

    public void setValidThrough(Date validThrough) {
        this.validThrough = validThrough;
    }
}

