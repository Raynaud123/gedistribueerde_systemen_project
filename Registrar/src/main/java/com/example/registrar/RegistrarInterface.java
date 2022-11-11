package com.example.registrar;

import java.rmi.Remote;
import java.time.LocalDate;
import java.util.Map;

public interface RegistrarInterface extends Remote {
    void register(String CF, String location);

    Map<LocalDate, byte[]> getPseudonyms(String CF, String location, LocalDate startDate, LocalDate endDate);
}
