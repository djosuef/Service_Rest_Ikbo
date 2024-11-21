package com.ikbo.inventory.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ikbo.inventory.entity.Movement;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {


}
