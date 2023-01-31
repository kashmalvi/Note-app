package com.example.notes;

import com.google.firebase.Timestamp;


public class Password {
    String Account;
    String pass;
    Timestamp timestamp;

    public Password() {
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String content) {
        this.pass = content;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String title) {
        this.Account = title;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

}
