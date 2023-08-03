package com.hgu.histudyDB.CRUD;

import com.hgu.histudyDB.Info.Group;
import com.hgu.histudyDB.Info.Students;
import com.hgu.histudyDB.CRUD.StudentsCRUD;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class GroupsCRUD implements ICRUD {
    Scanner s;
    ArrayList<Students> studentsList;
    ArrayList<Group> groupList;
    final String fname = "GroupsInfo.txt";
    private int count = 0;

    Students student;
    StudentsCRUD studentsCRUD = new StudentsCRUD(s);

    public GroupsCRUD() {

    }

    public GroupsCRUD(Scanner s) {
        studentsList = new ArrayList<>();
        groupList = new ArrayList<>();
        this.s = s;
    }

    @Override
    public int add(Object one) {
        return 0;
    }

    public void addGroups() {
        String leader;
        int num;
        int currentNum;
        String lecture;
        ArrayList<Students> members;
        int studyTime;

        System.out.println("스터디 리더(본인)의 이름 찾기? ");
        leader = s.next();
        studentsCRUD.listAll(leader);
        System.out.println("스터디를 진행할 과목은? ");
        lecture = s.next();
        System.out.println("스터디 그룹 인원은? ");
        num = s.nextInt();
    }

    @Override
    public int update(Object one) {
        return 0;
    }

    @Override
    public int delete(Object one) {
        return 0;
    }

}
