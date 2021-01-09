package com.galvanize.zoo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public void create(AnimalDto animalDto) {
        animalRepository.save(new AnimalEntity(animalDto.getName(), animalDto.getType()));
    }

    public List<AnimalDto> fetchAll() {
        return animalRepository.findAll()
            .stream()
            .map(animalEntity -> new AnimalDto(
                animalEntity.getName(),
                animalEntity.getType(),
                animalEntity.getMood()
            ))
            .collect(Collectors.toList());
    }

    public void feed(String name) {
        AnimalEntity animal = animalRepository.findByName(name);
        animal.setMood(Mood.HAPPY);
        animalRepository.save(animal);
    }
}
