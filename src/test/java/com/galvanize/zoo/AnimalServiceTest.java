package com.galvanize.zoo;

import com.galvanize.zoo.animal.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    AnimalRepository mockAnimalRepository;

    @InjectMocks
    AnimalService subject;

    @Test
    void create() {
        AnimalDto eagle = new AnimalDto("eagle", AnimalType.FLYING, null);
        subject.create(eagle);
        verify(mockAnimalRepository).save(
            new AnimalEntity("eagle", AnimalType.FLYING)
        );
    }

    @Test
    void fetchAll() {
        when(mockAnimalRepository.findAll()).thenReturn(
            List.of(
                new AnimalEntity("eagle", AnimalType.FLYING),
                new AnimalEntity("monkey", AnimalType.WALKING)
            )
        );

        List<AnimalDto> actual = subject.fetchAll();

        assertThat(actual).isEqualTo(
            List.of(
                new AnimalDto("eagle", AnimalType.FLYING, Mood.UNHAPPY),
                new AnimalDto("monkey", AnimalType.WALKING, Mood.UNHAPPY)
            )
        );
    }

    @Test
    void feed() {
        when(mockAnimalRepository.findByName("monkey"))
            .thenReturn(new AnimalEntity("monkey", AnimalType.WALKING));

        subject.feed("monkey");

        ArgumentCaptor<AnimalEntity> captor = ArgumentCaptor.forClass(AnimalEntity.class);
        verify(mockAnimalRepository).save(captor.capture());
        assertThat(captor.getValue().getMood()).isEqualTo(Mood.HAPPY);
    }
}