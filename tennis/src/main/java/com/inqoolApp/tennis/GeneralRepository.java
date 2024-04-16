package com.inqoolApp.tennis;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;


@Repository
public interface GeneralRepository<T> {
    T findById(Long id);
    List<T> findAll();
    T save(T entity);
    void deleteById(Long id);
    void deleteAll();
    List<T> getReservationsByCourtId(Long id);
    List<T> getReservationsByPhoneNumber(String phoneNumber);
    List<T> getPresentReservationsByPhoneNumber(String phoneNumber);
    List<T> getUserByPhoneNumber(String phoneNumber, Class<T> userClass);
    List<T> checkReservationTime(Long id, LocalDateTime start, LocalDateTime end);
}