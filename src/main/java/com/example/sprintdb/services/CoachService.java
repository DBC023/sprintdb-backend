package com.example.sprintdb.services;

import com.example.sprintdb.dto.AthleteSummaryDTO;
import com.example.sprintdb.dto.CoachDTO;
import com.example.sprintdb.entities.Coach;
import com.example.sprintdb.repository.CoachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoachService {

    private final CoachRepository coachRepository;

    // --- CREATE ---
    @Transactional
    public CoachDTO crear(CoachDTO dto) {
        Coach coach = new Coach();
        coach.setName(dto.getName());
        coach.setSpeciality(dto.getSpeciality());
        return convertToDto(coachRepository.save(coach));
    }

    // --- READ (ALL) ---
    @Transactional(readOnly = true)
    public List<CoachDTO> listarTodos() {
        return coachRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // --- READ (ONE) ---
    @Transactional(readOnly = true)
    public CoachDTO buscarPorId(Long id) {
        Coach coach = coachRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado con ID: " + id));
        return convertToDto(coach);
    }

    // --- UPDATE ---
    @Transactional
    public CoachDTO actualizar(Long id, CoachDTO dto) {
        Coach coachExistente = coachRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar, ID no encontrado: " + id));

        coachExistente.setName(dto.getName());
        coachExistente.setSpeciality(dto.getSpeciality());

        return convertToDto(coachRepository.save(coachExistente));
    }

    // --- DELETE ---
    @Transactional
    public void eliminar(Long id) {
        if (!coachRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar, ID no existe: " + id);
        }
        coachRepository.deleteById(id);
    }

    // --- MAPPER ---
    private CoachDTO convertToDto(Coach coach) {
        CoachDTO dto = new CoachDTO();
        dto.setId(coach.getId());
        dto.setName(coach.getName());
        dto.setSpeciality(coach.getSpeciality());

        if (coach.getAthletes() != null) {
            List<AthleteSummaryDTO> summaries = coach.getAthletes().stream()
                    .map(athlete -> new AthleteSummaryDTO(
                            athlete.getId(),
                            athlete.getName() + " " + athlete.getLastName()
                    ))
                    .collect(Collectors.toList());

            dto.setAthletes(summaries); // Llenamos la lista que React está buscando
        }
        return dto;
    }
}