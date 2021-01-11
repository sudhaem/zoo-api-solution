package com.galvanize.zoo.habitat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitatRepository extends JpaRepository<HabitatEntity, Integer> {

    HabitatEntity findByName(String name);

}
