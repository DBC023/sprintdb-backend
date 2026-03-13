package com.example.sprintdb.repository;

import com.example.sprintdb.entities.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {
    // Buscar atletas por apellido
    List<Athlete> findByLastNameIgnoreCase(String lastName);

    // Buscar todos los atletas que pertenecen a un coach específico
    List<Athlete> findByCoachId(Long coachId);
}