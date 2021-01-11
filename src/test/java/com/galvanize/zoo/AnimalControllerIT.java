package com.galvanize.zoo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.zoo.animal.AnimalDto;
import com.galvanize.zoo.animal.AnimalEntity;
import com.galvanize.zoo.animal.AnimalRepository;
import com.galvanize.zoo.animal.AnimalType;
import com.galvanize.zoo.animal.Mood;
import com.galvanize.zoo.habitat.HabitatEntity;
import com.galvanize.zoo.habitat.HabitatRepository;
import com.galvanize.zoo.habitat.HabitatType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AnimalControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AnimalRepository animalRepository;

    @Autowired
    HabitatRepository habitatRepository;

    @BeforeEach
    void setUp() {
        animalRepository.deleteAll();
    }

    @Test
    void create_fetchAll() throws Exception {
        AnimalDto input = new AnimalDto("monkey", AnimalType.WALKING, null, null);
        mockMvc.perform(
            post("/animals")
                .content(objectMapper.writeValueAsString(input))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated());

        mockMvc.perform(get("/animals"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("length()").value(1))
            .andExpect(jsonPath("[0].name").value("monkey"))
            .andExpect(jsonPath("[0].mood").value(Mood.UNHAPPY.name()))
            .andExpect(jsonPath("[0].type").value(AnimalType.WALKING.name()));
    }

    @Test
    void feed() throws Exception {
        animalRepository.save(new AnimalEntity("monkey", AnimalType.WALKING));

        mockMvc.perform(
            post("/animals/monkey/feed")
        )
            .andExpect(status().isOk());

        mockMvc.perform(get("/animals"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].name").value("monkey"))
            .andExpect(jsonPath("[0].mood").value(Mood.HAPPY.name()));
    }

    @Test
    void move() throws Exception {
        animalRepository.save(new AnimalEntity("monkey", AnimalType.WALKING));
        habitatRepository.save(new HabitatEntity("Monkey's Jungle", HabitatType.FOREST));

        mockMvc.perform(
            post("/animals/monkey/move")
                .content("Monkey's Jungle")
        )
            .andExpect(status().isOk())
            .andDo(print());

        mockMvc.perform(get("/animals"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].name").value("monkey"))
            .andExpect(jsonPath("[0].habitat.name").value("Monkey's Jungle"));
    }
}
