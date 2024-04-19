package com.inqoolApp.tennis.user;

import lombok.*;

import jakarta.persistence.*;

/**
 * Entity class to store users in H2 database. Automaticaly creates table in the database
 *
 * @author Jan Vondrasek
*/

@Entity
@Table(name="Users")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String fullName;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "DELETED")
    private Boolean deleted;

}