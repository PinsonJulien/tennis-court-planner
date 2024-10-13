package com.pinson.tennis_backend.courts.repositories;

import com.pinson.tennis_backend.courts.entities.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICourtRepository extends JpaRepository<Court, Long> {
    //
}
