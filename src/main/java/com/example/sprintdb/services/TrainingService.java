package com.example.sprintdb.services;

import com.example.sprintdb.dto.*;
import com.example.sprintdb.entities.*;
import com.example.sprintdb.repository.*;
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

    // --- CREACIÓN ---

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

    // --- LECTURA ---

    @Transactional(readOnly = true)
    public List<Object> listAllByAthlete(Long athleteId) {
        return trainingRepo.findByAthleteIdOrderByDateTimeDesc(athleteId).stream()
                .map(this::convertPolymorphic)
                .collect(Collectors.toList());
    }

    // --- ACTUALIZACIÓN ---

    @Transactional
    public GymTrainingDTO updateGym(Long id, GymTrainingDTO dto) {
        GymTraining gym = gymRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrenamiento de Gym no encontrado"));

        gym.setDateTime(dto.getDateTime());
        gym.setDescription(dto.getDescription());
        gym.setCompleted(dto.getCompleted());
        gym.setSeries(dto.getSeries());
        gym.setRepetitions(dto.getRepetitions());
        gym.setWeight(dto.getWeight());

        return convertToGymDto(gymRepo.save(gym));
    }

    @Transactional
    public TrackTrainingDTO updateTrack(Long id, TrackTrainingDTO dto) {
        TrackTraining track = trackRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrenamiento de Pista no encontrado"));

        track.setDateTime(dto.getDateTime());
        track.setDescription(dto.getDescription());
        track.setCompleted(dto.getCompleted());
        track.setSeries(dto.getSeries());
        track.setRepetitions(dto.getRepetitions());
        track.setDistanceMeters(dto.getDistanceMeters());
        track.setTimeSeconds(dto.getTimeSeconds());

        return convertToTrackDto(trackRepo.save(track));
    }

    // --- REPLICACIÓN (PLANIFICACIÓN SEMANAL) ---

    @Transactional
    public void replicate(Long sourceId, List<ReplicationDTO> copies) {
        Training original = trainingRepo.findById(sourceId)
                .orElseThrow(() -> new RuntimeException("Entrenamiento base no encontrado"));

        for (ReplicationDTO entry : copies) {
            if (original instanceof GymTraining) {
                GymTraining source = (GymTraining) original;
                GymTraining copy = new GymTraining();

                // Clonamos ADN del original
                copy.setDescription(source.getDescription());
                copy.setAthlete(source.getAthlete());
                copy.setPlace(source.getPlace());
                copy.setSeries(source.getSeries());
                copy.setRepetitions(source.getRepetitions());

                // Aplicamos variaciones de la réplica
                copy.setDateTime(entry.getNewDateTime());
                copy.setWeight(entry.getNewValue()); // BigDecimal
                copy.setCompleted(false);

                gymRepo.save(copy);
            }
            else if (original instanceof TrackTraining) {
                TrackTraining source = (TrackTraining) original;
                TrackTraining copy = new TrackTraining();

                copy.setDescription(source.getDescription());
                copy.setAthlete(source.getAthlete());
                copy.setPlace(source.getPlace());
                copy.setSeries(source.getSeries());
                copy.setRepetitions(source.getRepetitions());
                copy.setDistanceMeters(source.getDistanceMeters());

                copy.setDateTime(entry.getNewDateTime());
                copy.setTimeSeconds(entry.getNewValue()); // BigDecimal
                copy.setCompleted(false);

                trackRepo.save(copy);
            }
        }
    }

    // --- ELIMINACIÓN ---

    @Transactional
    public void delete(Long id) {
        if (!trainingRepo.existsById(id)) {
            throw new RuntimeException("ID inexistente: " + id);
        }
        trainingRepo.deleteById(id);
    }
    @Transactional(readOnly = true)
    public List<String> searchDescription(String q) {
        // Si el query es nulo o vacío, devolvemos una lista vacía para evitar errores
        if (q == null || q.trim().isEmpty()) {
            return List.of();
        }
        return trainingRepo.findUniqueDescriptions(q);
    }
    // --- MAPPERS INTERNOS ---

    private void mapCommonToEntity(TrainingDTO dto, Training entity, Athlete athlete) {
        entity.setDateTime(dto.getDateTime());
        entity.setDescription(dto.getDescription());
        entity.setAthlete(athlete);
        entity.setCompleted(dto.getCompleted() != null ? dto.getCompleted() : false);
    }

    private Object convertPolymorphic(Training training) {
        if (training instanceof GymTraining) return convertToGymDto((GymTraining) training);
        if (training instanceof TrackTraining) return convertToTrackDto((TrackTraining) training);
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
                        s.getObservations(),
                        s.getTraining().getId()
                ))
                .collect(Collectors.toList());
    }
}