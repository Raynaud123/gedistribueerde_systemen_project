package com.example.matchingservice;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    private void startServer(){
        try {
            Registry registry = LocateRegistry.createRegistry(5000);

            registry.rebind("MatchingServiceImpl", new MatchingServiceImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("system is ready");

    }

    public static void main(String[] args) {
        Main main = new Main();
        main.startServer();
    }

}