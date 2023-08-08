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

        System.out.println("*****histudy*****\r\n" + "1. 그룹 정보 등록\r\n" + "2. 그룹 정보 읽기\r\n" + "3. 그룹 가입\r\n"
                + "4. 공부 시간 기록\r\n" + "5. 그룹 삭제\r\n" + "6. 그룹 검색\r\n" + "7. 종료하기\r\n"
                + "****************\r\n" + "=> 원하는 메뉴는?");
        return s.nextInt();

    }

    public void start() {
//		studentsCRUD.loadFile();

        while (true) {
            groupsCRUD.loadData();
            int menu = selectMenu();

            if (menu == 1) {
                groupsCRUD.addGroups();
            } else if (menu == 2) {
                groupsCRUD.listAll("");
            } else if (menu == 3) {
                groupsCRUD.joinGroup();
            } else if (menu == 4) {
                groupsCRUD.addStudyTime();
            } else if (menu == 5) {
                groupsCRUD.deleteGroup();
            } else if (menu == 6) {
                groupsCRUD.searchGroup();
            } else if (menu == 7) {
                groupsCRUD.rank();
            } else if (menu == 8) {
                break;
            }
        }
    }

}
