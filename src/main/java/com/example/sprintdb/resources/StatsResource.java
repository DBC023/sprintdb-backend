package com.example.sprintdb.resources;

import com.example.sprintdb.dto.StatsDTO;
import com.example.sprintdb.services.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class StatsResource {

    private final StatsService statsService;

    @PostMapping
    public ResponseEntity<StatsDTO> reportar(@RequestBody StatsDTO dto) {
        return ResponseEntity.ok(statsService.registrarFeedback(dto));
    }

    @GetMapping("/training/{id}")
    public ResponseEntity<StatsDTO> verFeedback(@PathVariable Long id) {
        return ResponseEntity.ok(statsService.obtenerPorEntrenamiento(id));
    }
}