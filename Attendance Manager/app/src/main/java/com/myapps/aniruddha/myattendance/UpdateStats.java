package com.myapps.aniruddha.myattendance;

/**
 * Created by ANIRUDDHA on 22-09-2015.
 */
public class UpdateStats {

    String te,ta,p;

    public UpdateStats() {
    }

    public UpdateStats(String te, String ta, String p) {
        this.te = te;
        this.ta = ta;
        this.p = p;
    }

    public void set_te(String te) {
        this.te = te;
    }

    public void set_ta(String ta) {
        this.ta = ta;
    }

    public void set_p(String p) {
        this.p = p;
    }


    public String get_te() {
        return te;
    }

    public String get_ta() {
        return ta;
    }

    public String get_p() {
        return p;
    }

}
