package com.myapps.aniruddha.myattendance;

public class updateDB {
    String date,c1,c2,c3,c4,c5;
    public updateDB(){};

    public updateDB(String date,String c1, String c2,String c3, String c4,String c5) {
        this.date= date;
        this.c1= c1;
        this.c2= c2;
        this.c3= c3;
        this.c4= c4;
        this.c5= c5;
    }

    public void set_date(String date) {
        this.date = date;
    }

    public void set_c1(String c1) {
        this.c1= c1;
    }
    public void set_c2(String c2) {
        this.c2= c2;
    }
    public void set_c3(String c3) {
        this.c3= c3;
    }
    public void set_c4(String c4) {
        this.c4= c4;
    }
    public void set_c5(String c5) {
        this.c5= c5;
    }

    public String get_date() {
        return date;
    }
    public String get_c1() {
        return c1;
    }
    public String get_c2() {
        return c2;
    }
    public String get_c3() {
        return c3;
    }
    public String get_c4() {
        return c4;
    }
    public String get_c5() {
        return c5;
    }

}
