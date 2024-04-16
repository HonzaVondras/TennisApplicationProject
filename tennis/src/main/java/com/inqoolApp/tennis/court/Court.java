package com.inqoolApp.tennis.court;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name="Courts")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Court {

    public Court(String name, SurfaceType surfaceType){
        this.name = name;
        this.surface = surfaceType;
        this.deleted = false;
    }

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