package com.galvanize.zoo.habitat;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public List<HabitatDto> fetchAll(@RequestParam(required = false) boolean onlyShowEmpty) {
        return habitatService.fetchAll(onlyShowEmpty);
    }
}
