package com.hgu.histudyDB.Info;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class Group {
    private int id;
    private int num;
    private int currentNum;
    private String lecture;
    private ArrayList<Integer> members;
    private int studyTime;

    public Group() {

    }

    public Group(int id, int num, int currentNum, String lecture, ArrayList<Integer> members, int studyTime) {
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

    public ArrayList<Integer> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Integer> members) {
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
        String str = String.format("%s ", lecture) + String.format("%s/%s ", currentNum, num)
                + String.format("%s ", studyTime) + String.format("%s ", members);
        return str;
    }
}
