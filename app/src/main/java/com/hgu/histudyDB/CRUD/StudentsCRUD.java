package com.hgu.histudyDB.CRUD;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import com.hgu.histudyDB.DB.DBConnection;
import com.hgu.histudyDB.Info.Students;

public class StudentsCRUD implements ICRUD {
    ArrayList<Students> list;
    Scanner s;
    final String fname = "StudentsInfo.txt";
    Connection conn;

    public StudentsCRUD() {

    }

    public StudentsCRUD(Scanner s) {
        list = new ArrayList<>();
        this.s = s;
        conn = DBConnection.getConnection();
    }

    public void loadData() {
        list.clear();

        String selectAll = "select * from students";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectAll);
            while (true) {
                if (!rs.next()) {
                    break;
                }
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int studentId = rs.getInt("studentId");
                String phoneNumber = rs.getString("phoneNumber");
                String email = rs.getString("email");
                list.add(new Students(name, studentId, phoneNumber, email));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Students> getList() {
        return list;
    }

    @Override
    public Object add() {
        System.out.println("이름?");
        String name = s.next();
        System.out.println("학번?");
        int studentsId = s.nextInt();
        System.out.println("전화번호?");
        String phoneNumber = s.next();
        System.out.println("이메일?");
        String email = s.next();

        return new Students(name, studentsId, phoneNumber, email);
    }

    public void addStudents() {
        Students one = (Students) add();
        list.add(one);
        System.out.println("학생 정보가 추가되었습니다");
    }

    public void listAll() {
        loadData();
        System.out.println("--------------------");
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + " ");
            System.out.println(list.get(i).toString());
        }
        System.out.println("--------------------");
    }

    public ArrayList<Integer> listAll(String keyword) {
        ArrayList<Integer> idList = new ArrayList<>();
        int j = 0;

        System.out.println("--------------------");
        for (int i = 0; i < list.size(); i++) {
            String name = list.get(i).getName();
            if (!name.contains(keyword)) {
                continue;
            }
            System.out.println((j + 1) + " ");
            System.out.println(list.get(i).toString());
            idList.add(i);
            j++;
        }
        System.out.println("--------------------");

        return idList;
    }

    @Override
    public int update(Object obj) {
        return 0;
    }

    public void updateStudents() {
        System.out.println("=> 수정할 학생 이름 검색: ");
        String keyword = s.next();
        ArrayList<Integer> idList = this.listAll(keyword);

        System.out.println("=> 수정할 번호 선택: ");
        int id = s.nextInt();
        System.out.println("=> 전화번호 입력: ");
        String phoneNumber = s.next();
        System.out.println("=> 이메일 입력: ");
        String email = s.next();

        Students students = list.get(idList.get(id - 1));
        students.setPhoneNumber(phoneNumber);
        students.setEmail(email);

        System.out.println("학생 정보가 수정되었습니다. ");
    }

    @Override
    public int delete(Object obj) {
        return 0;
    }

    public void deleteStudents() {
        System.out.println("=> 삭제할 학생 이름 검색: ");
        String keyword = s.next();
        ArrayList<Integer> idList = this.listAll(keyword);

        System.out.println("=> 수정할 번호 선택: ");
        int id = s.nextInt();

        System.out.println("=> 정말로 삭제하실래요(Y/N) ");
        String ans = s.next();
        if (ans.equalsIgnoreCase("Y")) {
            list.remove(idList.get(id - 1));
            System.out.println("학생 정보가 삭제되었습니다. ");
        } else {
            System.out.println("취소되었습니다. ");
        }

    }

    @Override
    public void selectOne(int id) {

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

    public void searchStudent() {
        System.out.println("=> 검색할 학생 이름은? ");
        String keyword = s.next();
        listAll(keyword);
    }

    public void searchLecture() {
        // TODO Auto-generated method stub

    }
}
