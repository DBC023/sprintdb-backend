package com.example.sprintdb.services;

import com.example.sprintdb.dto.AthleteDTO;
import com.example.sprintdb.entities.Athlete;
import com.example.sprintdb.entities.Coach;
import com.example.sprintdb.repository.AthleteRepository;
import com.example.sprintdb.repository.CoachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AthleteService {

    private final AthleteRepository athleteRepository;
    private final CoachRepository coachRepository;

    @Transactional(readOnly = true)
    public List<AthleteDTO> listarTodos() {
        return athleteRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AthleteDTO buscarPorId(Long id) {
        Athlete athlete = athleteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atleta no encontrado con ID: " + id));
        return convertToDto(athlete);
    }

    @Transactional
    public AthleteDTO crear(AthleteDTO dto, Long coachId) {
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new RuntimeException("No se puede crear atleta: Coach no encontrado"));

        Athlete athlete = new Athlete();
        athlete.setName(dto.getName());
        athlete.setLastName(dto.getLastName());
        athlete.setAge(dto.getAge());
        athlete.setWeight(dto.getWeight());
        athlete.setCoach(coach);

        return convertToDto(athleteRepository.save(athlete));
    }

    @Transactional
    public AthleteDTO actualizar(Long id, AthleteDTO dto) {
        Athlete existente = athleteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atleta no encontrado"));

        existente.setName(dto.getName());
        existente.setLastName(dto.getLastName());
        existente.setAge(dto.getAge());
        existente.setWeight(dto.getWeight());

        return convertToDto(athleteRepository.save(existente));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!athleteRepository.existsById(id)) {
            throw new RuntimeException("ID no existe");
        }
        athleteRepository.deleteById(id);
    }

    private AthleteDTO convertToDto(Athlete athlete) {
        AthleteDTO dto = new AthleteDTO();
        dto.setId(athlete.getId());
        dto.setName(athlete.getName());
        dto.setLastName(athlete.getLastName());
        dto.setAge(athlete.getAge());
        dto.setWeight(athlete.getWeight());
        if (athlete.getCoach() != null) {
            dto.setCoachName(athlete.getCoach().getName());
        }
        return dto;
    }
}