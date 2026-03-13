package com.example.sprintdb.services;

import com.example.sprintdb.dto.StatsDTO;
import com.example.sprintdb.entities.Stats;
import com.example.sprintdb.entities.Training;
import com.example.sprintdb.repository.StatsRepository;
import com.example.sprintdb.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepo;
    private final TrainingRepository trainingRepo;

    @Transactional
    public StatsDTO registrarFeedback(StatsDTO dto) {
        Training training = trainingRepo.findById(dto.getTrainingId())
                .orElseThrow(() -> new RuntimeException("Entrenamiento no encontrado"));

        // Creamos o actualizamos el Stat
        Stats stats = statsRepo.findByTrainingId(dto.getTrainingId()).orElse(new Stats());

        stats.setRpe(dto.getRpe());
        stats.setObservations(dto.getObservations());
        stats.setTraining(training);

        // Lógica de Negocio: Al recibir feedback, el entrenamiento se da por cumplido
        training.setCompleted(true);
        trainingRepo.save(training);

        Stats saved = statsRepo.save(stats);
        return new StatsDTO(saved.getId(), saved.getRpe(), saved.getObservations(), training.getId());
    }

    @Transactional(readOnly = true)
    public StatsDTO obtenerPorEntrenamiento(Long trainingId) {
        return statsRepo.findByTrainingId(trainingId)
                .map(s -> new StatsDTO(s.getId(), s.getRpe(), s.getObservations(), trainingId))
                .orElse(null);
    }
}