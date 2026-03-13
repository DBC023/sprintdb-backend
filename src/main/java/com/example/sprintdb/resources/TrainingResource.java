package com.example.sprintdb.resources;

import com.example.sprintdb.dto.GymTrainingDTO;
import com.example.sprintdb.dto.TrackTrainingDTO;
import com.example.sprintdb.services.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainings")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Permitir conexión desde React
public class TrainingResource {

    private final TrainingService trainingService;

    // --- CREATE (Especializados) ---

    @PostMapping("/gym")
    public ResponseEntity<GymTrainingDTO> createGym(@RequestBody GymTrainingDTO dto) {
        return new ResponseEntity<>(trainingService.createGym(dto), HttpStatus.CREATED);
    }

    @PostMapping("/track")
    public ResponseEntity<TrackTrainingDTO> createTrack(@RequestBody TrackTrainingDTO dto) {
        return new ResponseEntity<>(trainingService.createTrack(dto), HttpStatus.CREATED);
    }

    // --- READ (Historial completo del atleta) ---

    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<List<Object>> getHistory(@PathVariable Long athleteId) {
        // Devuelve una lista mezclada de GymTrainingDTO y TrackTrainingDTO
        return ResponseEntity.ok(trainingService.listAllByAthlete(athleteId));
    }

    // --- UPDATE (Especializados) ---

    @PutMapping("/gym/{id}")
    public ResponseEntity<GymTrainingDTO> updateGym(@PathVariable Long id, @RequestBody GymTrainingDTO dto) {
        return ResponseEntity.ok(trainingService.updateGym(id, dto));
    }

    @PutMapping("/track/{id}")
    public ResponseEntity<TrackTrainingDTO> updateTrack(@PathVariable Long id, @RequestBody TrackTrainingDTO dto) {
        return ResponseEntity.ok(trainingService.updateTrack(id, dto));
    }

    // --- DELETE (Genérico) ---

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        trainingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}