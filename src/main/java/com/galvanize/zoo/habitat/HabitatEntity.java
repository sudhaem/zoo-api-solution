package com.galvanize.zoo.habitat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class HabitatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private HabitatType type;

    public HabitatEntity(String name, HabitatType type) {
        this.name = name;
        this.type = type;
    }
}
