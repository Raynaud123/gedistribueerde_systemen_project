package com.example.mixingproxy;

import com.example.matchingservice.MatchingServiceInterface;
import com.example.registrar.RegistrarInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.security.*;
import java.sql.Timestamp;
import java.util.*;

public class MixingProxy {

    private final ObservableList<Capsule> capsules;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private final PublicKey registrarPublicKey;

    private final MatchingServiceInterface matchingService;
    private final RegistrarInterface registrarInterface;


    public MixingProxy(MatchingServiceInterface matchingServiceInterface, RegistrarInterface registrarInterface) throws NoSuchAlgorithmException, RemoteException {
        this.capsules = FXCollections.observableArrayList(new ArrayList<>());
        KeyPair kp = getKeypair();
        privateKey = kp.getPrivate();
        publicKey = kp.getPublic();
        this.matchingService = matchingServiceInterface;
        this.registrarInterface = registrarInterface;
        this.registrarPublicKey = this.registrarInterface.getPublicKey();
        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                flushCapsules();
            }
        };
        //flushCapsules from visitors every Hour
        timer.schedule (hourlyTask, 0L, 1000*60*60);
    }


    public String receive(String hashString, Timestamp ts, String token, String signature) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        //Check validity
        if (valid(token, signature)) {
            System.out.println("Token valid");
            capsules.add(new Capsule(ts,token, hashString));
            byte[] messageBytes = Base64.getDecoder().decode(hashString);
            Signature sig = Signature.getInstance("NONEwithRSA");
            sig.initSign(privateKey);
            sig.update(messageBytes);
            byte[] digitalSignature = sig.sign();
            return Base64.getEncoder().encodeToString(digitalSignature);
        }
        else return null;
    }


    private Boolean valid(String token, String signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(this.registrarPublicKey);
        sig.update(token.getBytes());
        return sig.verify(Base64.getDecoder().decode(signature));
    }

    public void flushCapsules() {
        Collections.shuffle(capsules);
        for (Capsule capsule : capsules) {
            try {
                matchingService.flushCapsules(capsule.getHash(), capsule.getTimeInterval(), capsule.getUserToken());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        capsules.clear();
    }

    //Keypair genereren
    private KeyPair getKeypair() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        return kpg.genKeyPair();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public ObservableList<Capsule> getCapsules() {
        return capsules;
    }

    public void sendInformedToken(String token) throws RemoteException {
        matchingService.sendInformedToken(token);
    }
}
