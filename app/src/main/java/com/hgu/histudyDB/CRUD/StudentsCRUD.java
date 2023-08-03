package com.hgu.histudyDB.CRUD;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import com.hgu.histudyDB.DB.DBConnectionStudent;
import com.hgu.histudyDB.Exceptions.InvalidEmailException;
import com.hgu.histudyDB.Exceptions.InvalidPhoneNumberException;
import com.hgu.histudyDB.Exceptions.InvalidStudentIdException;
import com.hgu.histudyDB.Info.Students;

public class StudentsCRUD implements ICRUD {
    final String STUDENT_SELECTALL = "select * from StudentsInfo";
    final String STUDENT_SELECT = "select * from StudentsInfo where name like ?";
    final String STUDENT_INSERT = "insert into StudentsInfo (name, studentId, phoneNumber, email, regdate)"
            + "values (?,?,?,?,?)";
    final String STUDENT_UPDATE = "UPDATE StudentsInfo SET phoneNumber=? WHERE id=?";
    final String STUDENT_DELETE = "DELETE FROM StudentsInfo WHERE id=?";

    final String nameException = "해당하는 이름이 없습니다.";
    final String studentIdException = "학번 형식이 잘못되었습니다.";
    final String phoneNumberException = "전화번호 형식이 잘못되었습니다.";
    final String emailException = "이메일 형식이 잘못되었습니다.";

    ArrayList<Students> list;
    Scanner s;
    final String fname = "StudentsInfo.txt";
    Connection conn;
    private int count;

    public StudentsCRUD() {

    }

    public StudentsCRUD(Scanner s) {
        list = new ArrayList<>();
        this.s = s;
        conn = DBConnectionStudent.getConnection();
    }

    public void loadData(String keyword) {
        list.clear();

        try {
            PreparedStatement stmt;
            ResultSet rs;

            if (keyword.equals("")) {
                stmt = conn.prepareStatement(STUDENT_SELECTALL);
                rs = stmt.executeQuery();
            } else {
                stmt = conn.prepareStatement(STUDENT_SELECT);
                stmt.setString(1, "%" + keyword + "%");
                rs = stmt.executeQuery();
            }

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
    }

//    public ArrayList<Students> getList() {
//        return list;
//    }

    public String getCurrentDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy--MM--dd");
        return f.format(now);
    }

    @Override
    public int add(Object one) {
        Students student = (Students) one;

        int retval = 0;
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(STUDENT_INSERT);
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getStudentId());
            pstmt.setString(3, student.getPhoneNumber());
            pstmt.setString(4, student.getEmail());
            pstmt.setString(5, getCurrentDate());
            retval = pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return retval;
    }

    public void addStudent() {
        String studentsId = "";
        String phoneNumber = "";
        String email = "";
        boolean validStudentId = false;
        boolean validPhoneNumber = false;
        boolean validEmail = false;

        System.out.println("이름?");
        String name = s.next();
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
                System.out.println("전화번호?");
                phoneNumber = s.next();
                if (phoneNumber.length() != 13) {
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

        Students one = new Students(count + 1, name, studentsId, phoneNumber, email);
        int retval = add(one);

        if (retval > 0) {
            System.out.println("학생 정보가 추가되었습니다");
        } else {
            System.out.println("학생 정보 추가 중 오류가 발생했습니다");
        }
    }


    public void listAll(String keyword) {
        loadData(keyword);

        System.out.println("--------------------");
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + " ");
            System.out.println(list.get(i).toString());
        }
        System.out.println("--------------------");
    }

//    public ArrayList<Integer> listAll(String keyword) {
//        ArrayList<Integer> idList = new ArrayList<>();
//        int j = 0;
//
//        System.out.println("--------------------");
//        for (int i = 0; i < list.size(); i++) {
//            String name = list.get(i).getName();
//            if (!name.contains(keyword)) {
//                continue;
//            }
//            System.out.println((j + 1) + " ");
//            System.out.println(list.get(i).toString());
//            idList.add(i);
//            j++;
//        }
//        System.out.println("--------------------");
//
//        return idList;
//    }

    @Override
    public int update(Object one) {
        Students student = (Students) one;

        int retval = 0;
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(STUDENT_UPDATE);
            pstmt.setString(1, student.getPhoneNumber());
            pstmt.setInt(2, student.getId());
            retval = pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return retval;
    }

    public void updateStudent() {
        String keyword = "";
        int id = 0;
        String phoneNumber = "";

        boolean validKeyword = false;
        boolean validId = false;
        boolean validPhoneNumber = false;

        System.out.println("=> 수정할 학생 이름 검색: ");
        keyword = s.next();
        listAll(keyword);

        System.out.println("=> 수정할 번호 선택: ");
        id = s.nextInt();

        while (!validPhoneNumber) {
            try {
                System.out.println("=> 전화번호 입력: ");
                phoneNumber = s.next();

                if (phoneNumber.length() != 13) {
                    throw new InvalidPhoneNumberException(phoneNumberException);
                }
                validPhoneNumber = true;
            } catch (InvalidPhoneNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        int retval = update(new Students(list.get(id - 1).getId(), list.get(id - 1).getName(), "", phoneNumber, ""));
        System.out.println(list.get(id - 1).getName());
        if (retval > 0) {
            System.out.println("학생 정보가 수정되었습니다");
        } else {
            System.out.println("학생 정보 수정 중 오류가 발생했습니다");
        }
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
        return retval;
    }

    public void deleteStudent() {
        System.out.println("=> 삭제할 학생 이름 검색: ");
        String keyword = s.next();
        listAll(keyword);

        System.out.println("=> 삭제할 번호 선택: ");
        int id = s.nextInt();
        int retval = delete(new Students(list.get(id - 1).getId(), "", "", "", ""));
        if (retval > 0) {
            System.out.println("학생 정보가 삭제되었습니다");
        } else {
            System.out.println("학생 정보 삭제 중 오류가 발생했습니다");
        }
    }

    public void searchStudent() {
        System.out.println("=> 검색할 학생 이름은? ");
        String keyword = s.next();
        listAll(keyword);
    }

//	public void loadFile() {
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(fname));
//
//			String line;
//			int count = 0;
//			while (true) {
//				line = br.readLine();
//				if (line == null) {
//					break;
//				}
//				String data[] = line.split("\\|");
//				String name = data[0];
//				int studentId = Integer.parseInt(data[1]);
//				String phoneNumber = data[2];
//				String email = data[3];
//				list.add(new Students(name, studentId, phoneNumber, email));
//				count++;
//			}
//			br.close();
//			System.out.println("==> " + count + "개 로딩 완료!!");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

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
