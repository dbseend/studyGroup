package com.hgu.histudyDB.Manage;

import java.util.Scanner;

import com.hgu.histudyDB.CRUD.GroupCRUD;
import com.hgu.histudyDB.CRUD.StudentsCRUD;

public class GroupManager {
    Scanner s = new Scanner(System.in);
    StudentsCRUD studentsCRUD;
    GroupCRUD groupCRUD;

    public GroupManager() {
        groupCRUD = new GroupCRUD(s, studentsCRUD);
    }

    public int selectMenu() {

        System.out.println("*****histudy*****\r\n" + "1. 그룹 등록 하기\r\n" + "2. 그룹 정보 읽기\r\n" + "3. 그룹 정보 수정\r\n"
                + "4. 그룹 정보 삭제\r\n" + "5. 나가기\r\n" + "****************\r\n" + "=> 원하는 메뉴는?");
        return s.nextInt();

    }

    public void start() {
        while (true) {
            int menu = selectMenu();

            if (menu == 1) {
                groupCRUD.addGroup();
            } else if (menu == 2) {
                groupCRUD.listAll();
            } else if (menu == 3) {
//                groupCRUD.updateGroup();
            } else if (menu == 4) {
//                groupCRUD.deleteGroup();
            } else if (menu == 5) {
                break;
            }
        }
    }

}
