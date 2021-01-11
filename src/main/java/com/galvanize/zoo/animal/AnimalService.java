package com.galvanize.zoo.animal;

import com.galvanize.zoo.habitat.HabitatDto;
import com.galvanize.zoo.habitat.HabitatEntity;
import com.galvanize.zoo.habitat.HabitatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private HabitatRepository habitatRepository;

    public AnimalService(AnimalRepository animalRepository,
                         HabitatRepository habitatRepository) {
        this.animalRepository = animalRepository;
        this.habitatRepository = habitatRepository;
    }

    public void create(AnimalDto animalDto) {
        animalRepository.save(new AnimalEntity(animalDto.getName(), animalDto.getType()));
    }

    public List<AnimalDto> fetchAll() {
        return animalRepository.findAll()
            .stream()
            .map(animalEntity -> {
                HabitatDto habitat = animalEntity.getHabitat() == null ? null :
                    new HabitatDto(
                        animalEntity.getHabitat().getName(),
                        animalEntity.getHabitat().getType()
                    );
                return new AnimalDto(
                    animalEntity.getName(),
                    animalEntity.getType(),
                    animalEntity.getMood(),
                    habitat
                );
            })
            .collect(Collectors.toList());
    }

    public void feed(String name) {
        AnimalEntity animal = animalRepository.findByName(name);
        animal.setMood(Mood.HAPPY);
        animalRepository.save(animal);
    }

    public void move(String name, String habitatName) {
        AnimalEntity animal = animalRepository.findByName(name);
        HabitatEntity habitat = habitatRepository.findByName(habitatName);

        animal.setHabitat(habitat);

        animalRepository.save(animal);
    }
}
