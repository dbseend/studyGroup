package com.hgu.histudyDB.CRUD;

import com.hgu.histudyDB.DB.DBConnection;
import com.hgu.histudyDB.DB.DBConnectionGroup;
import com.hgu.histudyDB.Info.Group;
import com.hgu.histudyDB.Info.Students;
import com.hgu.histudyDB.CRUD.StudentsCRUD;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class GroupsCRUD implements ICRUD {
    final String GROUP_SELECTALL = "select * from GroupsInfo";
    final String GROUP_SELECT = "select * from GroupsInfo where name like ?";
    final String GROUP_INSERT = "insert into GroupsInfo (id, num, currentNum, lecture, studyTime, members, regdate)"
            + "values (?,?,?,?,?,?,?)";
    final String GROUP_UPDATE = "UPDATE GroupsInfo SET phoneNumber=? WHERE id=?";
    final String GROUP_DELETE = "DELETE FROM GroupsInfo WHERE id=?";
    Connection conn;

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
        conn = DBConnectionGroup.getConnection();
        this.s = s;
    }

    public void loadData(String keyword) {
        groupList.clear();

        try {
            PreparedStatement stmt;
            ResultSet rs;

            if (keyword.equals("")) {
                stmt = conn.prepareStatement(GROUP_SELECTALL);
                rs = stmt.executeQuery();
            } else {
                stmt = conn.prepareStatement(GROUP_SELECT);
                stmt.setString(1, "%" + keyword + "%");
                rs = stmt.executeQuery();
            }

            while (true) {
                if (!rs.next()) {
                    break;
                }
                int id = rs.getInt("id");
                int num = rs.getInt("num");
                int currentNum = rs.getInt("currentNum");
                String lecture = rs.getString("lecture");
                ArrayList<Students> members = (ArrayList<Students>) rs.getObject("members");
                int studyTime = rs.getInt("studyTime");
                groupList.add(new Group(id, num, currentNum, lecture, members, studyTime));
                count++;
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCurrentDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy--MM--dd");
        return f.format(now);
    }

    @Override
    public int add(Object one) {
        Group group = (Group) one;

        int retval = 0;
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(GROUP_INSERT);
            pstmt.setInt(1, group.getId());
            pstmt.setInt(2, group.getNum());
            pstmt.setInt(3, group.getCurrentNum());
            pstmt.setString(4, group.getLecture());
            pstmt.setInt(5, group.getStudyTime());
            pstmt.setObject(6, group.getMembers());
            pstmt.setString(7, getCurrentDate());
            retval = pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return retval;
    }

    public void addGroups() {
        String leader;
        int id = 0;
        int index = 0;
        int num = 0;
        int currentNum = 0;
        String lecture = "";
        ArrayList<Students> members = new ArrayList<>();
        int studyTime = 0;

        System.out.println("스터디 리더(본인)의 이름 찾기? ");
        leader = s.next();
        studentsCRUD.listAll(leader);
        System.out.println("번호 선택 ");
        index = s.nextInt();
        members.add(count, studentsCRUD.list.get(index - 1));
        System.out.println("스터디를 진행할 과목은? ");
        lecture = s.next();
        System.out.println("스터디 그룹 인원은? ");
        num = s.nextInt();
        currentNum = 1;
        id = ++count;

        Group one = new Group(id, num, currentNum, lecture, members, studyTime);
        int retavl = add(one);

        if (retavl > 0) {
            System.out.println("그룹 정보가 추가되었습니다");
        } else {
            System.out.println("그룹 정보 추가 중 오류가 발생했습니다");
        }
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
