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

public class Visitor {
        private ArrayList<Integer> randomNumbers;
        private ArrayList<String> CFs;
        private ArrayList<String> hashes;
        private ArrayList<Timestamp> timestamps;
        private ArrayList<String> tokens;
        private PublicKey publicKeyMixing;

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
        }

        public BufferedImage addCapsuleInformation(int  randomNumber, String cateringFacility, String hashString, Timestamp ts) throws RemoteException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
                randomNumbers.add(randomNumber);
                CFs.add(cateringFacility);
                hashes.add(hashString);
                timestamps.add(ts);
                String token = tokens.get(0);
                tokens.remove(0);
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
}
