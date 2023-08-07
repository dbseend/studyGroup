package com.hgu.histudyDB.CRUD;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import com.hgu.histudyDB.DB.DBConnection;
import com.hgu.histudyDB.Exceptions.*;
import com.hgu.histudyDB.Info.Students;

public class StudentsCRUD implements ICRUD {
    final String STUDENT_SELECTALL = "select * from StudentsInfo";
    final String STUDENT_SELECT = "select * from StudentsInfo where name like ?";
    final String STUDENT_INSERT = "insert into StudentsInfo (id, name, studentId, phoneNumber, email, regdate)" + "values (?,?,?,?,?,?)";
    final String STUDENT_UPDATE = "UPDATE StudentsInfo SET name=?, studentId=?, phoneNumber=?, email=? WHERE id=?";
    final String STUDENT_UPDATE_INDEX = "UPDATE StudentsInfo SET id=?, name=?, studentId=?, phoneNumber=?, email=? WHERE id=?";
    final String STUDENT_DELETE = "DELETE FROM StudentsInfo WHERE id=?";

    final String idException = "잘못된 번호 입니다.";
    final String keywordException = "검색된 이름이 없습니다.";
    final String studentIdException = "학번 형식이 잘못되었습니다.";
    final String phoneNumberException = "전화번호 형식이 잘못되었습니다.";
    final String emailException = "이메일 형식이 잘못되었습니다.";

    Connection conn;
    ArrayList<Students> list;
    ArrayList<Students> searchList;
    Scanner s;
    final String fname = "StudentsInfo.txt";
    private int count = 0;
    private int searchCount = 0;

    public StudentsCRUD() {

    }

    public StudentsCRUD(Scanner s) {
        list = new ArrayList<>();
        searchList = new ArrayList<>();
        this.s = s;
        conn = DBConnection.getConnection();
    }

    public int loadData() {
        list.clear();
        count = 0;

        try {
            PreparedStatement stmt;
            ResultSet rs;

            stmt = conn.prepareStatement(STUDENT_SELECTALL);
            rs = stmt.executeQuery();


            while (true) {
                if (!rs.next()) {
                    break;
                }
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String studentId = rs.getString("studentId");
                String phoneNumber = rs.getString("phoneNumber");
                String email = rs.getString("email");
                list.add(new Students(id, name, studentId, phoneNumber, email));
                count++;
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return count;
    }

    public int loadSearchData(String keyword) {
        searchList.clear();
        searchCount = 0;

        try {
            PreparedStatement stmt;
            ResultSet rs;

            stmt = conn.prepareStatement(STUDENT_SELECT);
            stmt.setString(1, "%" + keyword + "%");
            rs = stmt.executeQuery();


            while (true) {
                if (!rs.next()) {
                    break;
                }
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String studentId = rs.getString("studentId");
                String phoneNumber = rs.getString("phoneNumber");
                String email = rs.getString("email");
                searchList.add(new Students(id, name, studentId, phoneNumber, email));
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
        Students student = (Students) one;
        int retval = 0;
        PreparedStatement pstmt;

        try {
            pstmt = conn.prepareStatement(STUDENT_INSERT);
            pstmt.setInt(1, student.getId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getStudentId());
            pstmt.setString(4, student.getPhoneNumber());
            pstmt.setString(5, student.getEmail());
            pstmt.setString(6, getCurrentDate());
            retval = pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return retval;
    }

    public void addStudent() {
        int id = 0;
        String name = "";
        String studentsId = "";
        String phoneNumber = "";
        String email = "";
        boolean validStudentId = false;
        boolean validPhoneNumber = false;
        boolean validEmail = false;
        System.out.println("이름?");
        name = s.next();
        while (!validStudentId) {
            try {
                System.out.println("학번?");
                studentsId = s.next();
                if (studentsId.length() != 8) {
                    throw new InvalidStudentIdException(studentIdException);
                }

                validStudentId = true;
            } catch (InvalidStudentIdException e) {
                System.out.println(e.getMessage());
            }
        }
        while (!validPhoneNumber) {
            try {
                System.out.println("전화번호?(-를 사용해서 구분해주세요)");
                phoneNumber = s.next();
                char check1 = phoneNumber.charAt(3);
                char check2 = phoneNumber.charAt(8);
                if (check1 != '-' || check2 != '-') {
                    throw new InvalidPhoneNumberException(phoneNumberException);
                }

                validPhoneNumber = true;
            } catch (InvalidPhoneNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (!validEmail) {
            try {
                System.out.println("이메일?");
                email = s.next();
                if (!email.contains("@")) {
                    throw new InvalidEmailException(emailException);
                }
                validEmail = true;
            } catch (InvalidEmailException e) {
                System.out.println(e.getMessage());
            }
        }
        id = count + 1;
        System.out.println(id);

        Students one = new Students(id, name, studentsId, phoneNumber, email);
        int retval = add(one);

        if (retval > 0) {
            System.out.println("학생 정보가 추가되었습니다");
        } else {
            System.out.println("학생 정보 추가 중 오류가 발생했습니다");
        }
    }


    public int listAll(String keyword) {
        loadSearchData(keyword);
        System.out.println("--------------------");
        for (int i = 0; i < searchList.size(); i++) {
            System.out.println((i + 1) + " " + searchList.get(i).toString());
        }
        System.out.println("--------------------");

        return list.size();
    }

    @Override
    public int update(Object one) {
        Students student = (Students) one;
        int retval = 0;
        PreparedStatement pstmt;

        try {
            pstmt = conn.prepareStatement(STUDENT_UPDATE);
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getStudentId());
            pstmt.setString(3, student.getPhoneNumber());
            pstmt.setString(4, student.getEmail());
            pstmt.setInt(5, student.getId());
            retval = pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return retval;
    }

    public void updateStudent() {
        int size = 0;
        int retval = 0;
        String keyword = "";
        int id = 0;
        int index = 0;
        String phoneNumber = "";
        String email = "";
        boolean validKeyword = false;
        boolean validId = false;
        boolean validPhoneNumber = false;
        boolean validEmail = false;

        try {
            System.out.println("=> 수정할 학생 이름 검색: ");
            keyword = s.next();
            size = loadSearchData(keyword);

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
                    System.out.println("=> 수정할 번호 선택: ");
                    id = s.nextInt();

                    if (id - 1 < 0 || id - 1 > size - 1) {
                        throw new InvalidIdException(idException);
                    }
                    validId = true;
                    index = searchList.get(id - 1).getId();
                } catch (InvalidIdException e) {
                    System.out.println(e.getMessage());
                }
            }

            System.out.println("=> 무엇을 수정하시겠습니까?(1.전화번호 2.이메일)");
            String select = s.next();
            System.out.println(select);
            if (select.equals("1") || select.equals("전화번호")) {
                while (!validPhoneNumber) {
                    try {
                        System.out.println("=> 전화번호 입력: ");
                        phoneNumber = s.next();

                        if (phoneNumber.length() != 13) {
                            throw new InvalidPhoneNumberException(phoneNumberException);
                        }
                        validPhoneNumber = true;
                        Students t = list.get(index - 1);
                        retval = update(new Students(t.getId(), t.getName(), t.getStudentId(), phoneNumber, t.getEmail()));
                    } catch (InvalidPhoneNumberException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else if (select == "2" || select == "이메일") {
                while (!validEmail) {
                    try {
                        System.out.println("=> 이메일 입력: ");
                        email = s.next();

                        if (!email.contains("@")) {
                            throw new InvalidEmailException(emailException);
                        }
                        validEmail = true;
                        Students t = list.get(index - 1);
                        retval = update(new Students(t.getId(), t.getName(), t.getStudentId(), phoneNumber, t.getEmail()));
                    } catch (InvalidEmailException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }

        if (retval > 0) {
            System.out.println("학생 정보가 수정되었습니다");

        } else {
            System.out.println("학생 정보 수정 중 오류가 발생했습니다");
        }
    }

    public int updateIndex(Object one) {
        Students student = (Students) one;
        int retval = 0;
        PreparedStatement pstmt;

        try {
            pstmt = conn.prepareStatement(STUDENT_UPDATE_INDEX);
            pstmt.setInt(1, student.getId() - 1);
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getStudentId());
            pstmt.setString(4, student.getPhoneNumber());
            pstmt.setString(5, student.getEmail());
            pstmt.setInt(6, student.getId());
            retval = pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return retval;
    }

    @Override
    public int delete(Object one) {
        Students student = (Students) one;
        int retval = 0;
        PreparedStatement pstmt;

        try {
            pstmt = conn.prepareStatement(STUDENT_DELETE);
            pstmt.setInt(1, student.getId());
            retval = pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        count--;
        return retval;
    }

    public void deleteStudent() {
        String keyword = "";
        int size = 0;
        boolean validKeyword = false;

        try {
            System.out.println("=> 삭제할 학생 이름 검색: ");
            keyword = s.next();
            size = loadSearchData(keyword);

            if (size == 0) {
                throw new NotFoundationException(keywordException);
            }
            validKeyword = true;
            listAll(keyword);
        } catch (NotFoundationException e) {
            System.out.println(e.getMessage());
        }

        if (validKeyword == true) {
            System.out.println("=> 삭제할 번호 선택: ");
            int id = s.nextInt();
            int index = list.get(id - 1).getId();
            Students t = list.get(index - 1);
            int retval = delete(new Students(t.getId(), "", "", "", ""));
            if (retval > 0) {
                loadData();
                for (int i = 0; i < count; i++) {
                    if (list.get(i).getId() > index) {
                        updateIndex(new Students(list.get(i).getId(), list.get(i).getName(), list.get(i).getStudentId(), list.get(i).getPhoneNumber(), list.get(i).getEmail()));
                    }
                }
                System.out.println("학생 정보가 삭제되었습니다");
            } else {
                System.out.println("학생 정보 삭제 중 오류가 발생했습니다");
            }
        }
    }

    public void searchStudent() {
        System.out.println("=> 검색할 학생 이름은? ");
        String keyword = s.next();
        listAll(keyword);
    }

    public void saveFile() {
        try {
            PrintWriter pr = new PrintWriter(new FileWriter(fname));
            for (Students one : list) {
                pr.write(one.toFileString() + "\n");
            }
            pr.close();
            System.out.println("==> 데이터 저장 완료!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
