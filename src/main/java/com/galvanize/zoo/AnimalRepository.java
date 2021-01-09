package com.galvanize.zoo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<AnimalEntity, Integer> {

    AnimalEntity findByName(String name);
}
