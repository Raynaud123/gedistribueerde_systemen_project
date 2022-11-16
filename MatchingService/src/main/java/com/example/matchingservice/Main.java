package com.example.matchingservice;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    private static void startServer() {
        try {
            Registry registry = LocateRegistry.createRegistry(5000);
            registry.rebind("MatchingServiceImpl", new MatchingServiceImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("system is ready");
    }

    public static void main(String[] args) {
        startServer();
    }

}