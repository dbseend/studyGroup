package com.hgu.histudyDB.Info;

import java.util.ArrayList;
import java.util.Objects;

public class Group {
    private int id;
    private int num;
    private int currentNum;
    private String lecture;
    private ArrayList<String> members;
    private int studyTime;

    public Group() {

    }

    public Group(int id, int num, int currentNum, String lecture, ArrayList<String> members, int studyTime) {
        this.id = id;
        this.num = num;
        this.currentNum = currentNum;
        this.lecture = lecture;
        this.members = members;
        this.studyTime = studyTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    public String getLecture() {
        return lecture;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public int getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(int studyTime) {
        this.studyTime = studyTime;
    }

    @Override
    /**
     * String.format(): 문자열의 형식을 설정하는 메소드
     */
    public String toString() {
        String str = String.format("%10s", num) + String.format("%15s", currentNum) + String.format("%15s", lecture)
                + String.format("%15s", studyTime) + String.format("%15s", members);
        return str;
    }
}
