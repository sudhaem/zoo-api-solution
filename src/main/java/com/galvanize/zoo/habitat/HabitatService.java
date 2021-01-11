package com.galvanize.zoo.habitat;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabitatService {

    private final HabitatRepository habitatRepository;

    public HabitatService(HabitatRepository habitatRepository) {
        this.habitatRepository = habitatRepository;
    }

    public void create(HabitatDto habitatDto) {
        habitatRepository.save(
            new HabitatEntity(habitatDto.getName(), habitatDto.getType())
        );
    }

    public List<HabitatDto> fetchAll(boolean onlyShowEmpty) {
        return habitatRepository
            .findAll()
            .stream()
            .filter(habitatEntity -> {
                if (onlyShowEmpty) {
                    return habitatEntity.getAnimal() == null;
                } else {
                    return true;
                }
            })
            .map(habitatEntity -> new HabitatDto(habitatEntity.getName(), habitatEntity.getType()))
            .collect(Collectors.toList());
    }
}
