package com.example.visitor;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Visitor {
        private ArrayList<Integer> randomNumbers;
        private ArrayList<String> CFs;
        private ArrayList<String> hashes;
        private ArrayList<Timestamp> timestamps;


        public Visitor() {
                this.CFs = new ArrayList<String>();
                this.hashes = new ArrayList<String>();
                this.randomNumbers = new ArrayList<Integer>();
                this.timestamps = new ArrayList<Timestamp>();
        }

        public void addCapsuleInformation(String randomNumber, String cateringFacility, String hashString, Timestamp ts){
                randomNumbers.add(Integer.getInteger(randomNumber));
                CFs.add(cateringFacility);
                hashes.add(hashString);
                timestamps.add(ts);
        }
}
