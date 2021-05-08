package org.techdealers.mchat;

public class pojo_msg {

    private String message, phone;

    public pojo_msg(String phone, String message) {
        this.message = message;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getMessage() {
        return message;
    }
}
