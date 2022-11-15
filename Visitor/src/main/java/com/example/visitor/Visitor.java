package com.example.visitor;

import com.example.mixingproxy.MixingProxyInterface;

import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Visitor {
        private ArrayList<Integer> randomNumbers;
        private ArrayList<String> CFs;
        private ArrayList<String> hashes;
        private ArrayList<Timestamp> timestamps;
        private ArrayList<String> tokens;

        private final MixingProxyInterface mixingProxyInterface;


        public Visitor(MixingProxyInterface mixingProxyInterface) {
                this.mixingProxyInterface = mixingProxyInterface;
                this.CFs = new ArrayList<String>();
                this.hashes = new ArrayList<String>();
                this.randomNumbers = new ArrayList<Integer>();
                this.timestamps = new ArrayList<Timestamp>();
                this.tokens = new ArrayList<String>();
                //Voor testen
                tokens.add("test");
        }

        public String addCapsuleInformation(int  randomNumber, String cateringFacility, String hashString, Timestamp ts) throws RemoteException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
                randomNumbers.add(randomNumber);
                CFs.add(cateringFacility);
                hashes.add(hashString);
                timestamps.add(ts);
                String token = tokens.get(0);
                tokens.remove(0);
                return  mixingProxyInterface.receiveCapsule(hashString,ts, token);
        }
}
