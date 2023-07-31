package com.hgu.histudyDB.CRUD;

import java.util.ArrayList;
import java.util.Scanner;

import com.hgu.histudyDB.Info.Group;
import com.hgu.histudyDB.Info.Students;

public class GroupCRUD implements ICRUD {
    ArrayList<Group> list;
    Scanner s;
    StudentsCRUD studentsCRUD;

    public GroupCRUD(Scanner s, StudentsCRUD studentsCRUD) {
        list = new ArrayList<>();
        this.s = s;
        this.studentsCRUD = studentsCRUD;
    }

    @Override
    public Object add() {
        System.out.println("그룹 리더 이름?");
        String firstMember = s.next();
        System.out.println("그룹 인원?");
        int num = s.nextInt();
        System.out.println("스터디 할 과목?");
        String lecture = s.next();
        Object[] memeber = new Object[num];
        ArrayList<Students> studentsList = studentsCRUD.getList();
        ArrayList<Integer> idList = studentsCRUD.listAll(firstMember);
        System.out.println("=> 수정할 번호 선택: ");
        int id = s.nextInt();
        Students students = studentsList.get(idList.get(id - 1));
        Object leader = students;
        memeber[0] = leader;
        int currentNum = 1;
        int studyTime = 0;

        return new Group(leader, num, currentNum, lecture, studyTime, memeber);
    }

    public void addGroup() {
        Group one = (Group) add();
        list.add(one);
        System.out.println("그룹 정보가 추가되었습니다");
    }

    public void listAll() {
        System.out.println("--------------------");
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + " ");
            System.out.println(list.get(i).toString());
        }
        System.out.println("--------------------");
    }

    @Override
    public int update(Object obj) {
        return 0;
    }

    @Override
    public int delete(Object obj) {
        return 0;
    }

    @Override
    public void selectOne(int id) {

    }
}
