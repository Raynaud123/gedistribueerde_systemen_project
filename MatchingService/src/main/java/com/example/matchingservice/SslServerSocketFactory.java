package com.example.matchingservice;

import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;
import java.security.*;
import java.security.cert.CertificateException;

public class SslServerSocketFactory implements RMIServerSocketFactory, Serializable {

    private final SSLServerSocketFactory ssf;

    public SslServerSocketFactory(String filename, String password) throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        KeyStore ks = KeyStore.getInstance("jks");
        ks.load(new FileInputStream(filename + ".ks"), password.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, password.toCharArray());
        KeyStore ts = KeyStore.getInstance("jks");
        ts.load(new FileInputStream(filename + ".ts"), password.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ts);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        ssf = sslContext.getServerSocketFactory();
    }

    @Override
    public ServerSocket createServerSocket(int port) throws IOException {
        SSLServerSocket sslSock = (SSLServerSocket) ssf.createServerSocket(port);
        sslSock.setNeedClientAuth(true);
        return sslSock;
    }

}
