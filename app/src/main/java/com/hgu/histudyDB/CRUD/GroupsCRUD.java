package com.hgu.histudyDB.CRUD;

import com.hgu.histudyDB.DB.DBConnection;
import com.hgu.histudyDB.Exceptions.DuplicationException;
import com.hgu.histudyDB.Exceptions.ExceedException;
import com.hgu.histudyDB.Exceptions.InvalidIdException;
import com.hgu.histudyDB.Exceptions.NotFoundationException;
import com.hgu.histudyDB.Info.Group;
import com.hgu.histudyDB.Info.Students;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class GroupsCRUD implements ICRUD {
    final String GROUP_SELECTALL = "select * from GroupsInfo";
    final String GROUP_SELECT = "select * from GroupsInfo where lecture like ?";
    final String GROUP_INSERT = "insert into GroupsInfo (id, num, currentNum, lecture, studyTime, members, regdate)" + "values (?,?,?,?,?,?,?)";
    final String GROUP_UPDATE = "UPDATE GroupsInfo SET num=?, currentNum=?, lecture=?, studyTime=?, members=? WHERE id=?";
    final String GROUP_DELETE = "DELETE FROM GroupsInfo WHERE id=?";

    final String idException = "잘못된 번호 입니다.";
    final String keywordException = "이용가능한 그룹이 없습니다.";
    final String nameException = "검색된 이름이 없습니다.";
    final String duplicationException = "이미 그룹에 가입했습니다.";
    final String phoneNumberException = "전화번호 형식이 잘못되었습니다.";
    final String emailException = "이메일 형식이 잘못되었습니다.";

    Connection conn;
    ArrayList<Students> studentsList;
    ArrayList<Group> list;
    ArrayList<Group> searchList;
    ArrayList<ArrayList<Integer>> members;
    Scanner s;
    final String fname = "GroupsInfo.txt";
    private int count = 0;
    private int searchCount = 0;
    private Group t;

    Students student;
    StudentsCRUD studentsCRUD = new StudentsCRUD(s);

    public GroupsCRUD() {

    }

    public GroupsCRUD(Scanner s) {
        members = new ArrayList<>();
        studentsList = new ArrayList<>();
        list = new ArrayList<>();
        searchList = new ArrayList<>();
        conn = DBConnection.getConnection();
        this.s = s;
    }

    public void loadData() {
        list.clear();
        members.clear();
        count = 0;

        try {
            PreparedStatement stmt;
            ResultSet rs;

            stmt = conn.prepareStatement(GROUP_SELECTALL);
            rs = stmt.executeQuery();


            while (true) {
                if (!rs.next()) {
                    break;
                }
                int id = rs.getInt("id");
                int num = rs.getInt("num");
                int currentNum = rs.getInt("currentNum");
                String lecture = rs.getString("lecture");
                String member = rs.getString("members");
                member = member.replaceAll("\\[", "").replaceAll("\\]", "");
                String[] memberArray = member.split(",");
                ArrayList<Integer> memberList = new ArrayList<>();
                for (int i = 0; i < memberArray.length; i++) {
                    memberList.add(Integer.parseInt(memberArray[i].trim()));
                }
                members.add(memberList);
                int studyTime = rs.getInt("studyTime");
                list.add(new Group(id, num, currentNum, lecture, memberList, studyTime));
                count++;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int loadSearchData(String keyword) {
        searchList.clear();
        searchCount = 0;

        try {
            PreparedStatement stmt;
            ResultSet rs;

            stmt = conn.prepareStatement(GROUP_SELECT);
            stmt.setString(1, "%" + keyword + "%");
            rs = stmt.executeQuery();

            while (true) {
                if (!rs.next()) {
                    break;
                }
                int id = rs.getInt("id");
                int num = rs.getInt("num");
                int currentNum = rs.getInt("currentNum");
                String lecture = rs.getString("lecture");
                String member = rs.getString("members");
                member = member.replaceAll("\\[", "").replaceAll("\\]", "");
                String[] memberArray = member.split(",");
                ArrayList<Integer> memberList = new ArrayList<>();
                for (int i = 0; i < memberArray.length; i++) {
                    memberList.add(Integer.parseInt(memberArray[i].trim()));
                }
                int studyTime = rs.getInt("studyTime");
                searchList.add(new Group(id, num, currentNum, lecture, memberList, studyTime));
                searchCount++;
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return searchCount;
    }

    public String getCurrentDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd");
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
        ArrayList<Integer> member = new ArrayList<>();

        System.out.println("스터디 리더(본인)의 이름 찾기? ");
        leader = s.next();
        studentsCRUD.listAll(leader);
        System.out.println("번호 선택 ");
        index = s.nextInt();
        int first = studentsCRUD.searchList.get(index - 1).getId();
        member.add(first);
        members.add(member);
        System.out.println("스터디를 진행할 과목은? ");
        lecture = s.next();
        System.out.println("스터디 그룹 인원은? ");
        num = s.nextInt();
        currentNum = 1;
        id = count + 1;

        Group one = new Group(id, num, currentNum, lecture, member, studyTime);
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
        String keyword = "";
        int id = 0;
        String name = "";
        int groupId = 0;
        int index = 0;
        int size = 0;
        int people = 0;
        int nameIndex = 0;
        ArrayList<Integer> updateMember = new ArrayList<>();

        boolean validKeyword = false;
        boolean validId = false;
        boolean validName = false;
        boolean validNameId = false;
        boolean validDuplication = false;

        try {
            System.out.println("가입할 과목 검색: ");
            keyword = s.next();
            size = loadSearchData(keyword);

            for (int i = 0; i < size; i++) {
                if (searchList.get(i).getCurrentNum() == searchList.get(i).getNum()) {
                    size--;
                }
            }
            if (size == 0) {
                throw new NotFoundationException(keywordException);
            }
            validKeyword = true;

            listAll(keyword);
        } catch (NotFoundationException e) {
            System.out.println(e.getMessage());
        }

        if (validKeyword == true) {
            while (!validId) {
                try {
                    System.out.println("가입할 번호 선택: ");
                    id = s.nextInt();

                    if (id - 1 < 0 || id - 1 > size - 1) {
                        throw new InvalidIdException(idException);
                    }
                    validId = true;
                    groupId = searchList.get(id - 1).getId();
                } catch (InvalidIdException e) {
                    System.out.println(e.getMessage());
                }
            }

            try {
                System.out.println("본인 이름 선택: ");
                name = s.next();
                people = studentsCRUD.loadSearchData(name);
                if (people == 0) {
                    throw new NotFoundationException(nameException);
                }
                validName = true;
                studentsCRUD.listAll(name);
            } catch (NotFoundationException e) {
                System.out.println(e.getMessage());
            }

            if (validName == true) {
                while (!validNameId) {
                    try {
                        System.out.println("번호 선택 ");
                        index = s.nextInt();

                        if (index - 1 < 0 || index - 1 > people - 1) {
                            throw new InvalidIdException(idException);
                        }
                        validNameId = true;
                        nameIndex = studentsCRUD.searchList.get(index - 1).getId();
                    } catch (InvalidIdException e) {
                        System.out.println(e.getMessage());
                    }

                    try {
                        for (int i = 0; i < members.get(groupId - 1).size(); i++) {
                            if (nameIndex == members.get(groupId - 1).get(i)) {
                                throw new DuplicationException(duplicationException);
                            }
                        }
                        validDuplication = true;
                    } catch (DuplicationException e) {
                        System.out.println(e.getMessage());
                    }

                    if (validDuplication == true) {
                        studentsCRUD.loadData();
                        int newMember = studentsCRUD.list.get(nameIndex - 1).getId();
                        members.get(groupId - 1).add(newMember);

                        t = list.get(groupId - 1);
                        t.setCurrentNum(t.getCurrentNum() + 1);


                        int retval = update(new Group(t.getId(), t.getNum(), t.getCurrentNum(), t.getLecture(), members.get(groupId - 1), t.getStudyTime()));

                        if (retval > 0) {
                            System.out.println("그룹에 추가 되었습니다.");
                        } else {
                            System.out.println("그룹에 추가 중 오류가 발생했습니다");
                        }
                    }
                }
            }
        }
    }

    public void addStudyTime() {
        String keyword = "";
        int id = 0;
        int time = 0;
        int size = 0;
        boolean validKeyword = false;
        boolean validId = false;

        try {
            System.out.println("공부한 스터디 그룹 과목 검색: ");
            keyword = s.next();
            size = loadSearchData(keyword);

            if (size == 0) {
                throw new NotFoundationException(keywordException);
            }
            validKeyword = true;
            listAll(keyword);
        } catch (NotFoundationException e) {
            e.getMessage();
        }

        if (validKeyword == true) {
            while (!validId) {
                try {
                    System.out.println("해당하는 번호 선택: ");
                    id = s.nextInt();

                    if (id - 1 < 0 || id - 1 > size - 1) {
                        throw new InvalidIdException(idException);
                    }
                    validId = true;
                } catch (InvalidIdException e) {
                    e.getMessage();
                }
            }
        }

        System.out.println("공부한 시간 입력(분단위)");
        time = s.nextInt();
        Group t = list.get(id - 1);
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
        int index = list.get(id - 1).getId();
        int retval = delete(new Group(list.get(id - 1).getId(), 0, 0, "", null, 0));
        if (retval > 0) {
            System.out.println("그룹 정보가 삭제되었습니다");
        } else {
            System.out.println("그룹 정보 삭제 중 오류가 발생했습니다");
        }
    }

    public void listAll(String keyword) {
        loadSearchData(keyword);
        studentsCRUD.loadData();
        System.out.println("--------------------");
        for (int i = 0; i < searchList.size(); i++) {
            String mem = "";
            Group t = searchList.get(i);
            for (int j = 0; j < t.getMembers().size(); j++) {
                int nameId = t.getMembers().get(j);
                String name = studentsCRUD.list.get(nameId - 1).getName();
                mem += name + " ";
            }
            System.out.println((i + 1) + " " + t.getLecture() + " " + t.getCurrentNum() + "/" + t.getNum() + " " + mem + " " + t.getStudyTime());
        }
        System.out.println("--------------------");
    }

    public void searchGroup() {
        System.out.println("=> 검색할 과목 이름은? ");
        String keyword = s.next();
        listAll(keyword);
    }

    public void rank() {
        Integer time[] = new Integer[list.size()];
        ArrayList<Group> rank = new ArrayList<>();
        String mem = "";

        for (int i = 0; i < list.size(); i++) {
            time[i] = list.get(i).getStudyTime();
        }
        Arrays.sort(time, Collections.reverseOrder());

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (time[i] == list.get(j).getStudyTime()) {
                    boolean duplication = false;
                    for (int k = 0; k < rank.size(); k++) {
                        if (rank.get(k).getId() == list.get(j).getId()) {
                            duplication = true;
                            break;
                        }
                    }
                    if (!duplication) {
                        rank.add(list.get(j));
                    }
                }
            }
        }

        for (int i = 0; i < rank.size(); i++) {
            System.out.println("Group " + rank.get(i).getId() + " " + rank.get(i).getStudyTime());
        }
    }

}
