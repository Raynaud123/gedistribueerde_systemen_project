package com.example.mixingproxy;

import java.rmi.Remote;

public interface MixingProxyInterface  extends Remote {

    boolean receiveCapsule();

    String sendBackSignedHas();

    String flushCapsules();


}
