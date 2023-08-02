package com.hgu.histudyDB.Info;

public class Students {
    private int id;
    private String name;
    private int studentId;
    private String phoneNumber;
    private String email;

    public Students() {
    }

    public Students(int id, String name, int studentId, String phoneNumber, String email) {
        this.id = id;
        this.name = name;
        this.studentId = studentId;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    /**
     * String.format(): 문자열의 형식을 설정하는 메소드
     */
    public String toString() {
        String str = String.format("%10s", name) + String.format("%15s", studentId) + String.format("%15s", phoneNumber)
                + String.format("%15s", email);
        return str;
    }

    public String toFileString() {
        return this.name + "|" + this.studentId + "|" + this.phoneNumber + "|" + this.email;
    }
}
