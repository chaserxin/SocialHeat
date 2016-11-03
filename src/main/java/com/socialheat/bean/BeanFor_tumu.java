package com.socialheat.bean;

/**
 * Created by sl on 16-11-2.
 */
public class BeanFor_tumu {

    private long time;
    private String message;

    public BeanFor_tumu(long time, String message) {
        this.time = time;
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
