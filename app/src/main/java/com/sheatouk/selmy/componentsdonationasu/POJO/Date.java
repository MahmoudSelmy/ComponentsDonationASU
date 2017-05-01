package com.sheatouk.selmy.componentsdonationasu.POJO;

/**
 * Created by pc on 01/05/2017.
 */

public class Date {
    String day,month,year;

    public Date() {
    }

    public Date(String name, String month, String year) {
        this.day = name;
        this.month = month;
        this.year = year;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String name) {
        this.day = name;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    private Boolean equals(Date date){
        if (month.equals(date.getMonth()) && day.equals(date.getDay()) && year.equals(date.getYear())){
            return true;
        }
        return false;
    }
}
