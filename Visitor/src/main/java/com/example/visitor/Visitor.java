package com.example.visitor;

import com.example.mixingproxy.MixingProxyInterface;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.security.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class Visitor {
        private ArrayList<Integer> randomNumbers;
        private ArrayList<String> CFs;
        private ArrayList<String> hashes;
        private ArrayList<Timestamp> timestamps;
        private ArrayList<String> tokens;
        private PublicKey publicKeyMixing;
        private int currentIndex;

        private final MixingProxyInterface mixingProxyInterface;


        public Visitor(MixingProxyInterface mixingProxyInterface) throws RemoteException {
                this.mixingProxyInterface = mixingProxyInterface;
                this.CFs = new ArrayList<String>();
                this.hashes = new ArrayList<String>();
                this.randomNumbers = new ArrayList<Integer>();
                this.timestamps = new ArrayList<Timestamp>();
                this.tokens = new ArrayList<String>();
                publicKeyMixing = mixingProxyInterface.getPublicKey();
                //Voor test
                tokens.add("test");
                currentIndex = -1;
        }

        public BufferedImage addCapsuleInformation(int  randomNumber, String cateringFacility, String hashString, Timestamp ts) throws RemoteException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
                randomNumbers.add(randomNumber);
                CFs.add(cateringFacility);
                hashes.add(hashString);
                timestamps.add(ts);
                currentIndex++;
                String token = tokens.get(currentIndex);
                String signedHash = mixingProxyInterface.receiveCapsule(hashString,ts, token);
                if (hashStringCheck(signedHash,hashString)){
                       return IconGenVisitor.generateIdenticons(hashString, 300,300);
                }
                else {
                        return  null;
                }
        }

        private boolean hashStringCheck(String signedHash, String hashString) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
                Signature signature = Signature.getInstance("NONEwithRSA");
                signature.initVerify(publicKeyMixing);
                byte[] messageBytes = Base64.getDecoder().decode(hashString);
                byte[] signedBytes  = Base64.getDecoder().decode(signedHash);


                signature.update(messageBytes);
                return signature.verify(signedBytes);
        }

        public void flushCapsules() throws RemoteException {
                Date date = new Date();
                mixingProxyInterface.flushCapsules(hashes.get(currentIndex),tokens.get(currentIndex),new Timestamp(date.getTime()));
        }
}
