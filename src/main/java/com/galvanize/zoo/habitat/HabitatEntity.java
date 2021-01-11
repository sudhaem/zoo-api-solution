package com.galvanize.zoo.habitat;

import com.galvanize.zoo.animal.AnimalEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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

    @OneToOne(cascade = CascadeType.ALL)
    private AnimalEntity animal;

    public HabitatEntity(String name, HabitatType type) {
        this.name = name;
        this.type = type;
    }
}
