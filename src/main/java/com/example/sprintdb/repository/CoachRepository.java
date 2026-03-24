package com.example.sprintdb.repository;

import com.example.sprintdb.entities.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
    // Buscar entrenador por especialidad
    List<Coach> findBySpecialityContainingIgnoreCase(String speciality);
}