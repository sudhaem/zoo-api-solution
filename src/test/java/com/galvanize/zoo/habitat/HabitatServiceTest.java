package com.galvanize.zoo.habitat;

import com.galvanize.zoo.animal.AnimalEntity;
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

        List<HabitatDto> actual = subject.fetchAll(false);

        assertThat(actual).isEqualTo(
            List.of(
                new HabitatDto("Eagle's Nest", HabitatType.NEST),
                new HabitatDto("Monkey's Jungle", HabitatType.FOREST)
            )
        );
    }

    @Test
    void fetchAll_withFilter() {
        HabitatEntity nest = new HabitatEntity("Eagle's Nest", HabitatType.NEST);
        nest.setAnimal(new AnimalEntity());
        when(mockHabitatRepository.findAll()).thenReturn(
            List.of(
                nest,
                new HabitatEntity("Monkey's Jungle", HabitatType.FOREST)
            )
        );

        List<HabitatDto> actual = subject.fetchAll(true);

        assertThat(actual).containsExactly(
            new HabitatDto("Monkey's Jungle", HabitatType.FOREST)
        );
    }
}