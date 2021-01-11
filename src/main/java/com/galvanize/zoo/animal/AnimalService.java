package com.galvanize.zoo.animal;

import com.galvanize.zoo.habitat.HabitatDto;
import com.galvanize.zoo.habitat.HabitatEntity;
import com.galvanize.zoo.habitat.HabitatOccupiedException;
import com.galvanize.zoo.habitat.HabitatRepository;
import com.galvanize.zoo.habitat.HabitatType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final HabitatRepository habitatRepository;
    private final Map<AnimalType, HabitatType> typeCompatibility;

    public AnimalService(AnimalRepository animalRepository,
                         HabitatRepository habitatRepository) {
        this.animalRepository = animalRepository;
        this.habitatRepository = habitatRepository;
        typeCompatibility = new HashMap<>();
        typeCompatibility.put(AnimalType.FLYING, HabitatType.NEST);
        typeCompatibility.put(AnimalType.SWIMMING, HabitatType.OCEAN);
        typeCompatibility.put(AnimalType.WALKING, HabitatType.FOREST);
    }

    public void create(AnimalDto animalDto) {
        animalRepository.save(new AnimalEntity(animalDto.getName(), animalDto.getType()));
    }

    public List<AnimalDto> fetchAll(AnimalType type, Mood mood) {
        return animalRepository.findAll()
            .stream()
            .filter(animalEntity -> {
                if (type != null) {
                    return animalEntity.getType().equals(type);
                } else {
                    return true;
                }
            })
            .filter(animalEntity -> {
                if (mood != null) {
                    return animalEntity.getMood().equals(mood);
                } else {
                    return true;
                }
            })
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

    public void move(String name, String habitatName) throws IncompatibleTypeException, HabitatOccupiedException {
        AnimalEntity animal = animalRepository.findByName(name);
        HabitatEntity habitat = habitatRepository.findByName(habitatName);

        if (!typeCompatibility.get(animal.getType()).equals(habitat.getType())) {
            animal.setMood(Mood.UNHAPPY);
            animalRepository.save(animal);
            throw new IncompatibleTypeException();
        } else if (habitat.getAnimal() != null) {
            throw new HabitatOccupiedException();
        } else {
            animal.setHabitat(habitat);
            animalRepository.save(animal);
        }
    }
}
