package com.hgu.histudyDB.CRUD;

import com.hgu.histudyDB.DB.DBConnectionGroup;
import com.hgu.histudyDB.Info.Group;
import com.hgu.histudyDB.Info.Students;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GroupsCRUD implements ICRUD {
    final String GROUP_SELECTALL = "select * from GroupsInfo";
    final String GROUP_SELECT = "select * from GroupsInfo where lecture like ?";
    final String GROUP_INSERT = "insert into GroupsInfo (id, num, currentNum, lecture, studyTime, members, regdate)"
            + "values (?,?,?,?,?,?,?)";
    final String GROUP_UPDATE = "UPDATE GroupsInfo SET num=?, currentNum=?, lecture=?, studyTime=?, members=? WHERE id=?";
    final String GROUP_DELETE = "DELETE FROM GroupsInfo WHERE id=?";
    Connection conn;

    Scanner s;
    ArrayList<Students> studentsList;
    ArrayList<Group> groupList;
    final String fname = "GroupsInfo.txt";
    private int count = 0;
    ArrayList<String> members = new ArrayList<>();

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
                //class java.lang.String cannot be cast to class java.util.ArrayList
//                ArrayList<String> members = (ArrayList<String>) rs.getString("members");
                String membersString = rs.getString("members");
                ArrayList<String> member = new ArrayList<>(Arrays.asList(membersString.split(",")));
                int studyTime = rs.getInt("studyTime");
                groupList.add(new Group(id, num, currentNum, lecture, member, studyTime));
                members = member;
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
        int studyTime = 0;

        System.out.println("스터디 리더(본인)의 이름 찾기? ");
        leader = s.next();
        studentsCRUD.listAll(leader);
        System.out.println("번호 선택 ");
        index = s.nextInt();
        members.add(count, studentsCRUD.list.get(index - 1).getName());
        System.out.println("스터디를 진행할 과목`은? ");
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
        Group group = (Group) one;

        int retval = 0;
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(GROUP_UPDATE);
            pstmt.setInt(1, group.getNum());
            pstmt.setInt(2, group.getCurrentNum());
            pstmt.setString(3, group.getLecture());
            pstmt.setInt(4, group.getStudyTime());
            pstmt.setString(5, String.valueOf(group.getMembers()));
            pstmt.setInt(6, group.getId());
            retval = pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return retval;
    }

    public void joinGroup() {
        String keyword;
        int id;
        String name;
        int index;

        System.out.println("가입할 과목 검색: ");
        keyword = s.next();
        listAll(keyword);

        System.out.println("가입할 번호 선택: ");
        id = s.nextInt();

        System.out.println("본인 이름 선택: ");
        name = s.next();
        studentsCRUD.listAll(name);
        System.out.println("번호 선택 ");
        index = s.nextInt();
        System.out.println(members.size());
        members.add(count, studentsCRUD.list.get(index - 1).getName());
        Group t = groupList.get(id - 1);
        t.setCurrentNum(t.getCurrentNum() + 1);

        int retval = update(new Group(t.getId(), t.getNum(), t.getCurrentNum(), t.getLecture(), t.getMembers(), t.getStudyTime()));
        if (retval > 0) {
            System.out.println("그룹에 추가 되었습니다.");
        } else {
            System.out.println("그룹에 추가 중 오류가 발생했습니다");
        }
    }

    public void addStudyTime() {
        String keyword;
        int id;
        int time;

        System.out.println("공부한 스터디 그룹 과목 검색: ");
        keyword = s.next();
        listAll(keyword);

        System.out.println("해당하는 번호 선택: ");
        id = s.nextInt();

        System.out.println("공부한 시간 입력(분단위)");
        time = s.nextInt();
        Group t = groupList.get(id - 1);
        t.setStudyTime(t.getStudyTime() + time);

        int retval = update(new Group(t.getId(), t.getNum(), t.getCurrentNum(), t.getLecture(), t.getMembers(), t.getStudyTime()));
        if (retval > 0) {
            System.out.println("그룹에 추가 되었습니다.");
        } else {
            System.out.println("그룹에 추가 중 오류가 발생했습니다");
        }
    }


    @Override
    public int delete(Object one) {
        Group group = (Group) one;

        int retval = 0;
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(GROUP_DELETE);
            pstmt.setInt(1, group.getId());
            retval = pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return retval;
    }

    public void deleteGroup() {
        System.out.println("삭제할 그룹은? ");
        listAll("");
        int id = s.nextInt();
        int index = groupList.get(id - 1).getId();
        int retval = delete(new Group(groupList.get(id - 1).getId(), 0, 0, "", null, 0));
        if (retval > 0) {
            System.out.println("그룹 정보가 삭제되었습니다");
        } else {
            System.out.println("그룹 정보 삭제 중 오류가 발생했습니다");
        }
    }

    public void listAll(String keyword) {
        loadData(keyword);

        System.out.println("--------------------");
        for (int i = 0; i < groupList.size(); i++) {
            System.out.println((i + 1) + " ");
            System.out.println(groupList.get(i).toString());
        }
        System.out.println("--------------------");
    }

    public void searchGroup() {
        System.out.println("=> 검색할 과목 이름은? ");
        String keyword = s.next();
        listAll(keyword);
    }

}
