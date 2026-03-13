package com.example.sprintdb.services;

import com.example.sprintdb.dto.GymTrainingDTO;
import com.example.sprintdb.dto.StatsDTO;
import com.example.sprintdb.dto.TrackTrainingDTO;
import com.example.sprintdb.dto.TrainingDTO;
import com.example.sprintdb.entities.*;
import com.example.sprintdb.repository.AthleteRepository;
import com.example.sprintdb.repository.GymTrainingRepository;
import com.example.sprintdb.repository.TrackTrainingRepository;
import com.example.sprintdb.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepo;
    private final GymTrainingRepository gymRepo;
    private final TrackTrainingRepository trackRepo;
    private final AthleteRepository athleteRepo;

    // --- CREATE ---
    @Transactional
    public GymTrainingDTO createGym(GymTrainingDTO dto) {
        Athlete athlete = athleteRepo.findById(dto.getAthleteId())
                .orElseThrow(() -> new RuntimeException("Atleta no encontrado"));

        GymTraining gym = new GymTraining();
        mapCommonToEntity(dto, gym, athlete);
        gym.setSeries(dto.getSeries());
        gym.setRepetitions(dto.getRepetitions());
        gym.setWeight(dto.getWeight());

        return convertToGymDto(gymRepo.save(gym));
    }

    @Transactional
    public TrackTrainingDTO createTrack(TrackTrainingDTO dto) {
        Athlete athlete = athleteRepo.findById(dto.getAthleteId())
                .orElseThrow(() -> new RuntimeException("Atleta no encontrado"));

        TrackTraining track = new TrackTraining();
        mapCommonToEntity(dto, track, athlete);
        track.setSeries(dto.getSeries());
        track.setRepetitions(dto.getRepetitions());
        track.setDistanceMeters(dto.getDistanceMeters());
        track.setTimeSeconds(dto.getTimeSeconds());

        return convertToTrackDto(trackRepo.save(track));
    }

    // --- READ (POLIMÓRFICO) ---
    @Transactional(readOnly = true)
    public List<Object> listAllByAthlete(Long athleteId) {
        // El repositorio base nos devuelve la mezcla de ambos tipos automáticamente
        return trainingRepo.findByAthleteIdOrderByDateTimeDesc(athleteId).stream()
                .map(this::convertPolymorphic)
                .collect(Collectors.toList());
    }

    // --- UPDATE ---
    // --- UPDATE CORREGIDO ---

    @Transactional
    public GymTrainingDTO updateGym(Long id, GymTrainingDTO dto) {
        GymTraining gym = gymRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrenamiento de Gimnasio no encontrado"));

        // 1. Actualizamos datos comunes y FEEDBACK
        gym.setDateTime(dto.getDateTime());
        gym.setDescription(dto.getDescription());
        gym.setCompleted(dto.getCompleted());      // <--- FUNDAMENTAL

        // 2. Actualizamos métricas específicas
        gym.setSeries(dto.getSeries());
        gym.setRepetitions(dto.getRepetitions());
        gym.setWeight(dto.getWeight());

        return convertToGymDto(gymRepo.save(gym));
    }

    @Transactional
    public TrackTrainingDTO updateTrack(Long id, TrackTrainingDTO dto) {
        TrackTraining track = trackRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrenamiento de Pista no encontrado"));

        // 1. Actualizamos datos comunes y FEEDBACK
        track.setDateTime(dto.getDateTime());
        track.setDescription(dto.getDescription());
        track.setCompleted(dto.getCompleted());      // <--- FUNDAMENTAL

        // 2. Actualizamos métricas específicas
        track.setSeries(dto.getSeries());
        track.setRepetitions(dto.getRepetitions());
        track.setDistanceMeters(dto.getDistanceMeters());
        track.setTimeSeconds(dto.getTimeSeconds());

        return convertToTrackDto(trackRepo.save(track));
    }
    // --- DELETE ---
    @Transactional
    public void delete(Long id) {
        // Al borrar el ID en la tabla base (Training), JPA borra automáticamente en la tabla hija
        if (!trainingRepo.existsById(id)) {
            throw new RuntimeException("El entrenamiento con ID " + id + " no existe.");
        }
        trainingRepo.deleteById(id);
    }

    // --- HELPER MAPPERS ---
    private void mapCommonToEntity(TrainingDTO dto, Training entity, Athlete athlete) {
        entity.setDateTime(dto.getDateTime());
        entity.setDescription(dto.getDescription());
        entity.setAthlete(athlete);

        entity.setCompleted(dto.getCompleted());


    }

    private Object convertPolymorphic(Training training) {
        if (training instanceof GymTraining) {
            return convertToGymDto((GymTraining) training);
        } else if (training instanceof TrackTraining) {
            return convertToTrackDto((TrackTraining) training);
        }
        return null;
    }

    private GymTrainingDTO convertToGymDto(GymTraining entity) {
        GymTrainingDTO dto = new GymTrainingDTO();
        dto.setId(entity.getId());
        dto.setDateTime(entity.getDateTime());
        dto.setDescription(entity.getDescription());
        dto.setAthleteId(entity.getAthlete().getId());
        dto.setSeries(entity.getSeries());
        dto.setRepetitions(entity.getRepetitions());
        dto.setWeight(entity.getWeight());
        dto.setCompleted(entity.getCompleted());
        dto.setStats(mapStatsToDto(entity.getStats()));

        return dto;
    }

    private TrackTrainingDTO convertToTrackDto(TrackTraining entity) {
        TrackTrainingDTO dto = new TrackTrainingDTO();
        dto.setId(entity.getId());
        dto.setDateTime(entity.getDateTime());
        dto.setDescription(entity.getDescription());
        dto.setAthleteId(entity.getAthlete().getId());
        dto.setSeries(entity.getSeries());
        dto.setRepetitions(entity.getRepetitions());
        dto.setDistanceMeters(entity.getDistanceMeters());
        dto.setTimeSeconds(entity.getTimeSeconds());
        dto.setCompleted(entity.getCompleted());
        dto.setStats(mapStatsToDto(entity.getStats()));

        return dto;
    }

    private List<StatsDTO> mapStatsToDto(List<Stats> statsEntities) {
        if (statsEntities == null || statsEntities.isEmpty()) return null;
        return statsEntities.stream()
                .map(s -> new StatsDTO(
                        s.getId(),
                        s.getRpe(),
                        s.getObservations(), // <--- Agregamos este cable de datos
                        s.getTraining().getId()
                ))
                .collect(Collectors.toList());
    }
}