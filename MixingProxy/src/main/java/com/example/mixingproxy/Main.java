package com.example.mixingproxy;

import com.example.matchingservice.MatchingServiceInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

//    private static void startServer(){
//        Registry matchingService;
//        MatchingServiceInterface matchingServiceInterface;
//        try {
//            matchingService = LocateRegistry.getRegistry("localhost", 5000);
//            matchingServiceInterface = (MatchingServiceInterface) matchingService.lookup("MatchingServiceImpl");
//
//        } catch (RemoteException | NotBoundException e) {
//            throw new RuntimeException(e);
//        }
//
//
//        try {
//            Registry registry = LocateRegistry.createRegistry(4500);
//
//            registry.rebind("MixingProxyImpl", new MixingProxyImpl(new MixingProxy(matchingServiceInterface)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("system is ready");
//
//    }

    public static void main(String[] args) {
        //startServer();
        MixingProxyGui.main(args);
    }

}