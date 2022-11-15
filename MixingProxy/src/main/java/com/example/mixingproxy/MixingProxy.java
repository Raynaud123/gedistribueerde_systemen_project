package com.example.mixingproxy;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;

public class MixingProxy {

    ArrayList<Capsule> capsules;
    PrivateKey privateKey;
    PublicKey publicKey;



    public MixingProxy() throws NoSuchAlgorithmException {
        this.capsules = new ArrayList<>();
        KeyPair kp = getKeypair();
        privateKey = kp.getPrivate();
        publicKey = kp.getPublic();
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
