package com.example.mixingproxy;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    private void startServer(){
        try {
            Registry registry = LocateRegistry.createRegistry(4500);

            registry.rebind("MixingProxyImpl", new MixingProxyImpl());
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