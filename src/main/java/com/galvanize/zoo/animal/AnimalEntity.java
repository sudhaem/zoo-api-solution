package com.galvanize.zoo.animal;

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
public class AnimalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private AnimalType type;
    private Mood mood;

    public AnimalEntity(String name, AnimalType type) {
        this.name = name;
        this.type = type;
        this.mood = Mood.UNHAPPY;
    }
}
