package com.galvanize.zoo.animal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.zoo.habitat.HabitatEntity;
import com.galvanize.zoo.habitat.HabitatRepository;
import com.galvanize.zoo.habitat.HabitatType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class AnimalControllerDocIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AnimalRepository animalRepository;

    @Autowired
    HabitatRepository habitatRepository;

    @Test
    void create_fetchAll() throws Exception {
        AnimalEntity input = new AnimalEntity("monkey", AnimalType.WALKING);
        input.setHabitat(new HabitatEntity("savanah", HabitatType.FOREST));
        animalRepository.save(input);


        mockMvc.perform(get("/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("[0].name").value("monkey"))
                .andExpect(jsonPath("[0].mood").value(Mood.UNHAPPY.name()))
                .andExpect(jsonPath("[0].type").value(AnimalType.WALKING.name()))
                .andDo(document("animals", responseFields(
                        fieldWithPath("[]").description("array of animals"),
                        fieldWithPath("[].name").description("name of animal"),
                        fieldWithPath("[].mood").description("animal's mood"),
                        fieldWithPath("[].type").description("type of animal"),
                        fieldWithPath("[].habitat").description("animal's habitat"),
                        fieldWithPath("[].habitat.name").description("animal's habitat name"),
                        fieldWithPath("[].habitat.type").description("animal's habitat type")
                )));
    }

    @Test
    void create_conflict() throws Exception {
        animalRepository.save(new AnimalEntity("monkey", AnimalType.WALKING));

        AnimalDto input = new AnimalDto("monkey", AnimalType.WALKING, null, null);
        mockMvc.perform(
                post("/animals")
                        .content(objectMapper.writeValueAsString(input))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isConflict())
                .andDo(document("saveAnimalConflict"));
    }

    @Test
    void feed() throws Exception {
        animalRepository.save(new AnimalEntity("monkey", AnimalType.WALKING));

        mockMvc.perform(post("/animals/{name}/feed", "monkey"))
                .andExpect(status().isOk())
                .andDo(document("feed", pathParameters(
                        parameterWithName("name").description("animal to feed")
                )));
    }

    @Test
    void move() throws Exception {
        animalRepository.save(new AnimalEntity("monkey", AnimalType.WALKING));
        habitatRepository.save(new HabitatEntity("Monkey's Jungle", HabitatType.FOREST));

        mockMvc.perform(post("/animals/{name}/move", "monkey").content("Monkey's Jungle"))
                .andExpect(status().isOk())
                .andDo(document("move", pathParameters(
                        parameterWithName("name").description("animal to move"))));

        mockMvc.perform(get("/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].name").value("monkey"))
                .andExpect(jsonPath("[0].habitat.name").value("Monkey's Jungle"));
    }

    @Test
    void move_incompatible() throws Exception {
        AnimalEntity eagle = new AnimalEntity("eagle", AnimalType.FLYING);
        eagle.setMood(Mood.HAPPY);
        animalRepository.save(eagle);
        habitatRepository.save(new HabitatEntity("Monkey's Jungle", HabitatType.FOREST));

        mockMvc.perform(post("/animals/{name}/move", "eagle").content("Monkey's Jungle"))
                .andExpect(status().isConflict())
                .andDo(document("moveIncompatible", pathParameters(
                        parameterWithName("name").description("animal to move"))));

        mockMvc.perform(get("/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].name").value("eagle"))
                .andExpect(jsonPath("[0].mood").value(Mood.UNHAPPY.name()))
                .andExpect(jsonPath("[0].habitat").isEmpty());
    }

    @Test
    void move_occupied() throws Exception {
        animalRepository.save(new AnimalEntity("monkey", AnimalType.WALKING));

        HabitatEntity habitat = new HabitatEntity("Monkey's Jungle", HabitatType.FOREST);
        AnimalEntity chimp = new AnimalEntity("chimp", AnimalType.WALKING);
        chimp.setHabitat(habitat);
        habitat.setAnimal(chimp);
        habitatRepository.save(habitat);

        mockMvc.perform(post("/animals/{name}/move", "monkey").content("Monkey's Jungle"))
                .andExpect(status().isConflict())
                .andDo(document("moveOccupied", pathParameters(
                        parameterWithName("name").description("animal to move"))));

        mockMvc.perform(get("/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].name").value("monkey"))
                .andExpect(jsonPath("[0].habitat").isEmpty())
                .andExpect(jsonPath("[1].name").value("chimp"))
                .andExpect(jsonPath("[1].habitat.name").value("Monkey's Jungle"));
    }

    @Test
    void fetch_withParams() throws Exception {
        HabitatEntity habitatEntity1 = new HabitatEntity("habitat1", HabitatType.FOREST);
        HabitatEntity habitatEntity2 = new HabitatEntity("habitat2", HabitatType.OCEAN);
        HabitatEntity habitatEntity3 = new HabitatEntity("habitat3", HabitatType.NEST);
        AnimalEntity monkey = new AnimalEntity("monkey", AnimalType.WALKING);
        monkey.setHabitat(habitatEntity1);
        AnimalEntity eagle = new AnimalEntity("eagle", AnimalType.FLYING);
        eagle.setHabitat(habitatEntity3);
        AnimalEntity whale = new AnimalEntity("whale", AnimalType.SWIMMING);
        whale.setHabitat(habitatEntity2);
        AnimalEntity chimp = new AnimalEntity("chimp", AnimalType.WALKING);
        chimp.setHabitat(habitatEntity1);
        chimp.setMood(Mood.HAPPY);
        animalRepository.saveAll(List.of(monkey, eagle, whale, chimp));

        mockMvc.perform(get("/animals?mood=HAPPY&type=WALKING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("[0].name").value("chimp"))
                .andExpect(jsonPath("[0].mood").value(Mood.HAPPY.name()))
                .andExpect(jsonPath("[0].type").value(AnimalType.WALKING.name()))
                .andDo(document("animalsByParams", responseFields(
                        fieldWithPath("[]").description("array of animals"),
                        fieldWithPath("[].name").description("name of animal"),
                        fieldWithPath("[].mood").description("animal's mood"),
                        fieldWithPath("[].type").description("type of animal"),
                        fieldWithPath("[].habitat").description("animal's habitat"),
                        fieldWithPath("[].habitat.name").description("animal's habitat name"),
                        fieldWithPath("[].habitat.type").description("animal's habitat type")
                )));
    }

}
