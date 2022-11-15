package com.example.mixingproxy;

//import com.example.matchingservice.MatchingServiceInterface;

import com.example.matchingservice.MatchingServiceInterface;

import java.security.*;
import java.sql.Timestamp;
import java.util.*;

public class MixingProxy {

    ArrayList<Capsule> capsules;
    PrivateKey privateKey;
    PublicKey publicKey;

   private final MatchingServiceInterface matchingService;


    public MixingProxy(MatchingServiceInterface matchingServiceInterface) throws NoSuchAlgorithmException {
        this.capsules = new ArrayList<>();
        KeyPair kp = getKeypair();
        privateKey = kp.getPrivate();
        publicKey = kp.getPublic();
        this.matchingService = matchingServiceInterface;
//        Timer timer = new Timer ();
//        TimerTask hourlyTask = new TimerTask () {
//            @Override
//            public void run () {
//                try {
//                    Collections.shuffle(capsules);
//                   // matchingService.flushCapsules(capsules);
//                    capsules = new ArrayList<>();
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        //flushCapsules from visitor every Hour
//        timer.schedule (hourlyTask, 0l, 1000*60*60);
    }




    public String receive(String hashString, Timestamp ts, String token) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        //Check validity
        //TODO: functie valid nog implementeren
        if(valid(token)){
            capsules.add(new Capsule(ts,token, hashString));
            byte[] messageBytes = Base64.getDecoder().decode(hashString);
            Signature signature = Signature.getInstance("NONEwithRSA");
            signature.initSign(privateKey);
            signature.update(messageBytes);
            byte[] digitalSignature = signature.sign();
            return Base64.getEncoder().encodeToString(digitalSignature);
        }
        else return null;
    }

    //TODO: implementeren
    private Boolean valid(String token){
        return true;
    }

    //Keypair genereren
    private KeyPair getKeypair() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        return kpg.genKeyPair();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
