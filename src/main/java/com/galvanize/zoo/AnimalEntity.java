package com.galvanize.zoo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@EqualsAndHashCode
@Getter
@Setter
public class AnimalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Type type;
    private Mood mood;

    public AnimalEntity(String name, Type type) {
        this.name = name;
        this.type = type;
        this.mood = Mood.UNHAPPY;
    }

    public AnimalEntity() {
    }
}
