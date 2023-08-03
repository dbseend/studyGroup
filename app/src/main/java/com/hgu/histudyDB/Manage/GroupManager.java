package com.hgu.histudyDB.Manage;

import java.util.Scanner;

import com.hgu.histudyDB.CRUD.GroupsCRUD;
import com.hgu.histudyDB.CRUD.StudentsCRUD;

public class GroupManager {
    Scanner s = new Scanner(System.in);
    GroupsCRUD groupsCRUD;

    public GroupManager() {
        groupsCRUD = new GroupsCRUD(s);
    }

    public int selectMenu() {

        System.out.println("*****histudy*****\r\n" + "1. 그룹 정보 등록\r\n" + "2. 그룹 정보 읽기\r\n" + "3. 그룹 정보 수정\r\n"
                + "4. 그룹 정보 삭제\r\n" + "5. 그룹 정보 저장\r\n" + "6. 그룹 정보 검색\r\n" + "8. 종료하기\r\n"
                + "****************\r\n" + "=> 원하는 메뉴는?");
        return s.nextInt();

    }

    public void start() {
//		studentsCRUD.loadFile();

        while (true) {
            int menu = selectMenu();

            if (menu == 1) {
                groupsCRUD.addGroups();
//            } else if (menu == 2) {
//                studentsCRUD.listAll("");
//            } else if (menu == 3) {
//                studentsCRUD.updateStudent();
//            } else if (menu == 4) {
//                studentsCRUD.deleteStudent();
//            } else if (menu == 5) {
//                studentsCRUD.saveFile();
//            } else if (menu == 6) {
//                studentsCRUD.searchStudent();
            } else if (menu == 7) {
                break;
            }
        }
    }

}
