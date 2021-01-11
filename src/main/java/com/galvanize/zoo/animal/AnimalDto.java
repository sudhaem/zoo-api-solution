package com.galvanize.zoo.animal;

import lombok.Value;

@Value
public class AnimalDto {
    String name;
    AnimalType type;
    Mood mood;
}
