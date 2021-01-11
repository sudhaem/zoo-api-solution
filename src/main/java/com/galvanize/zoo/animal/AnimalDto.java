package com.galvanize.zoo.animal;

import com.galvanize.zoo.habitat.HabitatDto;
import lombok.Value;

@Value
public class AnimalDto {
    String name;
    AnimalType type;
    Mood mood;
    HabitatDto habitat;
}
