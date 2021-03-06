package com.inkp.boostcamp.Boostme.data;

import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by inkp on 2017-02-10.
 */

public class ScheduleRealm extends RealmObject{


    @PrimaryKey
    private int id;

    private String title;
    private Date date;
    private String location;
    private int week_of_day_repit;
    private boolean alarm_flag;
    private long date_in_long;

    private boolean sun;
    private boolean mon;
    private boolean tue;
    private boolean wed;
    private boolean thu;
    private boolean fri;
    private boolean sat;


    private RealmList<SmallScheduleRealm> small_schedule;

    @Ignore
    private int sessionId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getWeek_of_day_repit() {
        return week_of_day_repit;
    }

    public void setWeek_of_day_repit(int week_of_day_repit) {
        this.week_of_day_repit = week_of_day_repit;
    }

    public boolean isAlarm_flag() {
        return alarm_flag;
    }

    public void setAlarm_flag(boolean alarm_flag) {
        this.alarm_flag = alarm_flag;
    }

    public RealmList<SmallScheduleRealm> getSmall_schedule() {
        return small_schedule;
    }

    public void setSmall_schedule(RealmList<SmallScheduleRealm> small_schedule) {
        this.small_schedule = small_schedule;
    }

    public long getDate_in_long() {
        return date_in_long;
    }

    public void setDate_in_long(long date_in_long) {
        this.date_in_long = date_in_long;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }

    public boolean isMon() {
        return mon;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isTue() {
        return tue;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public boolean isWed() {
        return wed;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isThu() {
        return thu;
    }

    public void setThu(boolean thu) {
        this.thu = thu;
    }

    public boolean isFri() {
        return fri;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isSat() {
        return sat;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }
}
