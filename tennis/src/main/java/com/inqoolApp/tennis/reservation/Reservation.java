package com.inqoolApp.tennis.reservation;

import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="Reservations")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "COURT_ID")
    private Long courtId;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "FOUR_PLAYERS")
    private boolean fourPlayers;

    @Column(name = "DELETED")
    private Boolean deleted;


}