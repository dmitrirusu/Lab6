package com.example.sylar.lab6;

import java.io.Serializable;

public class Message implements Serializable {

    public String type;
    public String val1;
    public String val2;
    public String val3;

    public Message(String type, String val1, String val2, String val3) {
        this.type = type;
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
    }

    public Message() {
    }
}
