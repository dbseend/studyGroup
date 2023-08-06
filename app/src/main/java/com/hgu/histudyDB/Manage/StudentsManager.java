package com.hgu.histudyDB.Manage;

import java.util.Scanner;

import com.hgu.histudyDB.CRUD.StudentsCRUD;
import com.hgu.histudyDB.Manage.GroupManager;

public class StudentsManager {
    Scanner s = new Scanner(System.in);
    StudentsCRUD studentsCRUD;

    public StudentsManager() {
        studentsCRUD = new StudentsCRUD(s);
    }

    public int selectMenu() {

        System.out.println("*****histudy*****\r\n" + "1. 학생 정보 등록\r\n" + "2. 학생 정보 읽기\r\n" + "3. 학생 정보 수정\r\n"
                + "4. 학생 정보 삭제\r\n" + "5. 학생 정보 저장\r\n" + "6. 학생 정보 검색\r\n" + "7. 그룹 관리 페이지로 이동\r\n" + "8. 종료하기\r\n"
                + "****************\r\n" + "=> 원하는 메뉴는?");
        return s.nextInt();

    }

    public void start() {
//		studentsCRUD.loadFile();

        while (true) {
            studentsCRUD.loadData("");
            int menu = selectMenu();

            if (menu == 1) {
                studentsCRUD.addStudent();
            } else if (menu == 2) {
                studentsCRUD.listAll("");
            } else if (menu == 3) {
                studentsCRUD.updateStudent();
            } else if (menu == 4) {
                studentsCRUD.deleteStudent();
            } else if (menu == 5) {
                studentsCRUD.saveFile();
            } else if (menu == 6) {
                studentsCRUD.searchStudent();
            } else if (menu == 7) {
                new GroupManager().start();
            } else if (menu == 8) {
                System.exit(0);
            }
        }
    }

}
