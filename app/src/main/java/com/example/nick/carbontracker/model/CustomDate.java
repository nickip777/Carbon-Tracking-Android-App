package com.example.nick.carbontracker.model;

import java.text.ParseException;
import java.util.Calendar;

/**
 * a customized date class which help us to save the date. used Julian date.
 */

public class CustomDate {
    public static int JGREG= 15 + 31*(10+12*1582);
    public static double HALFSECOND = 0.5;
    private int year;
    private int month;
    private int day;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public static int toJulian(int year, int month, int date) {
        month = month+1;
        int jy = year;
        if (year < 0)
            jy++;
        int jm = month;
        if (month > 2)
            jm++;
        else {
            jy--;
            jm += 13;
        }
        int jul = (int) (java.lang.Math.floor(365.25 * jy)
                + java.lang.Math.floor(30.6001 * jm) + date + 1720995.0);

        int IGREG = 15 + 31 * (10 + 12 * 1582);
        // Gregorian Calendar adopted Oct. 15, 1582

        if (date + 31 * (month + 12 * year) >= IGREG)
        // Change over to Gregorian calendar
        {
            int ja = (int) (0.01 * jy);
            jul += 2 - ja + (int) (0.25 * ja);
        }
        return jul;
    }

    public CustomDate getLastNumDays(int numDays) throws ParseException {
        int julian = (int) (this.toJulian(this.getYear(), this.getMonth(), this.getDay()) - numDays);
        int[] calDate = fromJulian(julian);
        CustomDate dateTemp = new CustomDate(calDate[0], calDate[1],calDate[2]);
        return dateTemp;
    }

    public boolean compareEqual(CustomDate d2) {
        if (this.getYear() == d2.getYear()) {
            if (this.getMonth() == d2.getMonth()) {
                if (this.getDay() == d2.getDay()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean compareGreater(CustomDate d2){
        if(this.getYear()>d2.getYear()){
            return true;
        }else if(this.getYear() == d2.getYear()){
            if(this.getMonth()>d2.getMonth()){
                return true;
            }
            else if(this.getMonth() == d2.getMonth()){
                if(this.getDay() > d2.getDay()){
                    return true;
                }
            }
        }
        return false;
    }
    public CustomDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public CustomDate(){
        Calendar calobj = Calendar.getInstance();
        this.year = calobj.get(Calendar.YEAR);
        this.month = calobj.get(Calendar.MONTH)+1;
        this.day = calobj.get(Calendar.DAY_OF_MONTH);
    }

    public CustomDate(int id){
        Calendar calobj = Calendar.getInstance();
        this.year = calobj.get(Calendar.YEAR);
        this.month = calobj.get(Calendar.MONTH);
        this.day = calobj.get(Calendar.DAY_OF_MONTH);
    }

    public String toString(){
        return this.day + "/" + (this.month+1) + "/" + this.year;
    }
    //code inspired from http://www.rgagnon.com/javadetails/java-0506.html
    public static int[] fromJulian(int j) {
        int ja = j;

        // The Julian day number of the adoption of the Gregorian calendar
        int JGREG = 2299161;

        // Cross-over to Gregorian Calendar produces this correction
        if (j >= JGREG)
        {
            int jalpha = (int) (((float) (j - 1867216) - 0.25) / 36524.25);
            ja += 1 + jalpha - (int) (0.25 * jalpha);
        }
        int jb = ja + 1524;
        int jc = (int) (6680.0 + ((float) (jb - 2439870) - 122.1) / 365.25);
        int jd = (int) (365 * jc + (0.25 * jc));
        int je = (int) ((jb - jd) / 30.6001);
        int date = jb - jd - (int) (30.6001 * je);
        int month = je - 1;
        if (month > 12)
            month -= 12;
        int year = jc - 4715;
        if (month > 2)
            --year;
        if (year <= 0)
            --year;
        return new int[] { year, month-1, date };
    }
}
