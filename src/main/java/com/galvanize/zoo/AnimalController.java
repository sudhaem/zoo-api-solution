package com.galvanize.zoo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("animals")
public class AnimalController {

    public AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody AnimalDto animalDto) {
        animalService.create(animalDto);
    }

    @GetMapping
    public List<AnimalDto> fetchAll() {
        return animalService.fetchAll();
    }
}
