package com.galvanize.zoo.habitat;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("habitats")
public class HabitatController {

    public HabitatService habitatService;

    public HabitatController(HabitatService habitatService) {
        this.habitatService = habitatService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody HabitatDto habitatDto) {
        habitatService.create(habitatDto);
    }

    @GetMapping
    public List<HabitatDto> fetchAll() {
        return habitatService.fetchAll();
    }
}
