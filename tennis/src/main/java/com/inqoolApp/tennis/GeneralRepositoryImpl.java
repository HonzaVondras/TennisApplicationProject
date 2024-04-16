package com.inqoolApp.tennis;

import jakarta.persistence.*;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class GeneralRepositoryImpl<T> implements GeneralRepository<T> {

    @PersistenceContext
    @Autowired
    private EntityManager entityManager;

    private Class<T> entityClass;

    public GeneralRepositoryImpl(Class<T> entityClass){
        this.entityClass = entityClass;
    }

    @Override
    public T findById(Long id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        return entityManager.createQuery("SELECT t FROM " + entityClass.getSimpleName() + " t WHERE t.deleted = false", entityClass)
                            .getResultList();
    }

    @Override
    @Transactional
    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        T entity = entityManager.find(entityClass, id);
        entityManager.remove(entity);
    }

    @Override
    @Transactional
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM " + entityClass.getSimpleName()).executeUpdate();
    }


    //Custom operations just for ReservationController

    public List<T> getReservationsByCourtId(Long courtId) {
        return entityManager.createQuery("SELECT r FROM Reservation r WHERE r.courtId = :courtId AND AND r.deleted = false ORDER BY r.startTime", entityClass)
                .setParameter("courtId", courtId)
                .getResultList();
    }

    public List<T> getReservationsByPhoneNumber(String phoneNumber) {
        return entityManager.createQuery("SELECT r FROM Reservation r WHERE r.phoneNumber = :phoneNumber AND r.deleted = false ORDER BY r.startTime", entityClass)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();
    }

    public List<T> getPresentReservationsByPhoneNumber(String phoneNumber) {
        LocalDateTime currentTimestamp = LocalDateTime.now();
    
        return entityManager.createQuery(
                "SELECT r FROM Reservation r WHERE r.phoneNumber = :phoneNumber AND r.startTime> :currentTimestamp AND r.deleted = false ORDER BY r.startTime", entityClass)
                .setParameter("phoneNumber", phoneNumber)
                .setParameter("currentTimestamp", currentTimestamp)
                .getResultList();
    }

    public List<T> getUserByPhoneNumber(String phoneNumber, Class<T> userClass) {
        return entityManager.createQuery(
                "SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber ", userClass)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();
        
    }

    public List<T> checkReservationTime(Long id, LocalDateTime start, LocalDateTime end) {
        return entityManager.createQuery(
                "SELECT r FROM Reservation r WHERE r.courtId = :courtId AND (r.startTime < :end AND r.endTime > :start) AND r.deleted = false ORDER BY r.startTime", entityClass)
                .setParameter("courtId", id)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}