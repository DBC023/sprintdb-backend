package com.example.sprintdb.resources;

import com.example.sprintdb.dto.AthleteDTO;
import com.example.sprintdb.services.AthleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/athletes")
@RequiredArgsConstructor
public class AthleteResource {

    private final AthleteService athleteService;

    @GetMapping
    public ResponseEntity<List<AthleteDTO>> getAll() {
        return ResponseEntity.ok(athleteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AthleteDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(athleteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AthleteDTO> create(@RequestBody AthleteDTO dto, @RequestParam Long coachId) {
        return new ResponseEntity<>(athleteService.crear(dto, coachId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AthleteDTO> update(@PathVariable Long id, @RequestBody AthleteDTO dto) {
        return ResponseEntity.ok(athleteService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        athleteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
