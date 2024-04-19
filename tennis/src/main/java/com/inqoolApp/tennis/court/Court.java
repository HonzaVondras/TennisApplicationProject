package com.inqoolApp.tennis.court;

import lombok.*;

import jakarta.persistence.*;

/**
 * Entity class to store courts in H2 database. Automaticaly creates table in the database
 *
 * @author Jan Vondrasek
*/

@Entity
@Table(name="Courts")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@ToString
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURFACE_TYPE")
    private SurfaceType surface;

    @Column(name = "DELETED")
    private Boolean deleted;

}