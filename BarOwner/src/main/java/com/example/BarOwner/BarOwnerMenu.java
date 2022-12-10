package com.example.BarOwner;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class BarOwnerMenu {
    public static void showMenu(RegistrarInterface registrarInterface) throws RemoteException {
        BarOwner barOwner = null;
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Scanner sc = new Scanner(System.in);
        int userChoice = 0;
        while (userChoice != 9) {
            System.out.println();
            System.out.println("1: Create and enroll catering facility");
            System.out.println("2: Get pseudonym batch");
            System.out.println("3: Get QR code");
            System.out.println("9: Exit");

            userChoice = sc.nextInt();

            switch (userChoice) {
                case 1 -> {
                    System.out.println("Please type the cf's unique info");
                    String CF = sc.next();
                    System.out.println("Please type the cf's location");
                    String location = sc.next();
                    barOwner = new BarOwner(CF, location, registrarInterface);
                    barOwner.register();
                    System.out.println("Registration complete");
                }
                case 2 -> {
                    if (barOwner == null) {
                        System.out.println("Please create catering facility first");
                        break;
                    }
                    System.out.println("Please type the start date 'yyyy-MM-dd' of the batch");
                    LocalDate startDate = LocalDate.parse(sc.next(), dateFormat);
                    System.out.println("Please type the end date 'yyyy-MM-dd' of the batch");
                    LocalDate endDate = LocalDate.parse(sc.next(), dateFormat);
                    barOwner.getPseudonymBatch(startDate, endDate);
                }
                case 3 -> {
                    if (barOwner == null) {
                        System.out.println("Please create catering facility first");
                        System.out.println();
                        break;
                    }
                    System.out.println("Please type the date 'yyyy-MM-dd' of the QR code");
                    LocalDate date = LocalDate.parse(sc.next(), dateFormat);
                    barOwner.generateQRCode(date);
                }
            }
        }
    }
}
