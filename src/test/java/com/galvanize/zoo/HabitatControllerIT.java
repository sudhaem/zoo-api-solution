package com.galvanize.zoo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.zoo.habitat.HabitatDto;
import com.galvanize.zoo.habitat.HabitatType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HabitatControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void create_fetchAll() throws Exception {
        HabitatDto input = new HabitatDto("Eagle exhibit", HabitatType.NEST);
        mockMvc.perform(
            post("/habitats")
                .content(objectMapper.writeValueAsString(input))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated());

        mockMvc.perform(get("/habitats"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("length()").value(1))
            .andExpect(jsonPath("[0].name").value("Eagle exhibit"))
            .andExpect(jsonPath("[0].type").value(HabitatType.NEST.name()));
    }
}
