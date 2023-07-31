package com.hgu.histudyDB.Info;

public class Group {
    private Object leader;
    private int memberNum;
    private int currentMemberNum;
    private String lecture;
    private int studyTime;
    private Object[] members;

    public Group() {

    }

    public Group(Object leader, int memberNum, int currentMemberNum, String lecture, int studyTime, Object[] members) {
        this.leader = leader;
        this.memberNum = memberNum;
        this.currentMemberNum = currentMemberNum;
        this.lecture = lecture;
        this.studyTime = studyTime;
        this.members = members;
    }

    public Object getObject() {
        return leader;
    }

    public void setName(Object leader) {
        this.leader = leader;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    public int getCurrentMemberNum() {
        return currentMemberNum;
    }

    public void setCurrentMemberNum(int currentMemberNum) {
        this.currentMemberNum = currentMemberNum;
    }

    public String getLecture() {
        return lecture;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }

    public int getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(int studyTime) {
        this.studyTime = studyTime;
    }

    public Object[] getMembers() {
        return members;
    }

    public void setMembers(Object[] members) {
        this.members = members;
    }

    @Override
    /**
     * String.format(): 문자열의 형식을 설정하는 메소드
     */
    public String toString() {
        String str = String.format("%10s", lecture) + String.format("%15s", memberNum) + String.format("%15s", currentMemberNum);
//                + String.format("%15s", leader);
        return str;
    }

}
