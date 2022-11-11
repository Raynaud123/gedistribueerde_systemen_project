package com.example.mixingproxy;

import java.rmi.Remote;

public interface MixingProxyInterface  extends Remote {

    String receiveCapsule();

    String flushCapsules();


}
