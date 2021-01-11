package com.galvanize.zoo;

import com.galvanize.zoo.habitat.HabitatDto;
import com.galvanize.zoo.habitat.HabitatEntity;
import com.galvanize.zoo.habitat.HabitatRepository;
import com.galvanize.zoo.habitat.HabitatService;
import com.galvanize.zoo.habitat.HabitatType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HabitatServiceTest {

    @Mock
    HabitatRepository mockHabitatRepository;

    @InjectMocks
    HabitatService subject;

    @Test
    void create() {
        HabitatDto nest = new HabitatDto("Eagle's Nest", HabitatType.NEST);
        subject.create(nest);
        verify(mockHabitatRepository).save(
            new HabitatEntity("Eagle's Nest", HabitatType.NEST)
        );
    }

    @Test
    void fetchAll() {
        when(mockHabitatRepository.findAll()).thenReturn(
            List.of(
                new HabitatEntity("Eagle's Nest", HabitatType.NEST),
                new HabitatEntity("Monkey's Jungle", HabitatType.FOREST)
            )
        );

        List<HabitatDto> actual = subject.fetchAll();

        assertThat(actual).isEqualTo(
            List.of(
                new HabitatDto("Eagle's Nest", HabitatType.NEST),
                new HabitatDto("Monkey's Jungle", HabitatType.FOREST)
            )
        );
    }
}