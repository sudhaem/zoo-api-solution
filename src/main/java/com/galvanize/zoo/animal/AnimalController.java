package com.galvanize.zoo.animal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public List<AnimalDto> fetchAll(@RequestParam(required = false) AnimalType type,
                                    @RequestParam(required = false) Mood mood) {
        return animalService.fetchAll(type, mood);
    }

    @PostMapping("{name}/feed")
    public void feed(@PathVariable String name) {
        animalService.feed(name);
    }

    @PostMapping(value = "{name}/move")
    public void move(@PathVariable String name,
                     @RequestBody String habitatName) throws Exception {
        animalService.move(name, habitatName);
    }
}
