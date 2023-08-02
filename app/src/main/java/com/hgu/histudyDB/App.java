package com.hgu.histudyDB;

import com.hgu.histudyDB.Manage.StudentsManager;

import java.util.Scanner;

public class App {
    Scanner s = new Scanner(System.in);

    public static void main(String[] args) {
        App myApp = new App();

        myApp.run();
    }

    public void run() {
        System.out.println("*****histudy*****\r\n" + "1. 학생 관리\r\n" + "2. 종료하기\r\n" + "=> 선택할 메뉴는?");
        int menu = s.nextInt();

        while (true) {
            if (menu == 1) {
                new StudentsManager().start();
            } else if (menu == 2) {
                break;
            }
        }
    }
}
