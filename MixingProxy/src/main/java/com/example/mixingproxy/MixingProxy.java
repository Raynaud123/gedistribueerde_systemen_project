package com.example.mixingproxy;

import java.sql.Timestamp;
import java.util.ArrayList;

public class MixingProxy {

    ArrayList<Capsule> capsules;


    public MixingProxy() {
        this.capsules = new ArrayList<>();
    }


    public void receive(int randomNumber, String cateringFacility, String hashString, Timestamp ts, String token){
        //Check validity

        System.out.println("We zitten in de receive baby");
        // return visual representation if valid
    }

}
