package org.techdealers.mchat;

public class pojo_msg {

    private String message, phone, image, key, status, time;

    public pojo_msg(String phone, String message, String image, String key, String status, String time) {
        this.message = message;
        this.phone = phone;
        this.image = image;
        this.key = key;
        this.status = status;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String getKey() {
        return key;
    }

    public String getImage() {
        return image;
    }

    public String getPhone() {
        return phone;
    }

    public String getMessage() {
        return message;
    }
}
