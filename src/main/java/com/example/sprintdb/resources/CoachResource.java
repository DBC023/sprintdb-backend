package com.example.sprintdb.resources;

import com.example.sprintdb.dto.CoachDTO;
import com.example.sprintdb.services.CoachService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173", "https://sprintdb-frontend.onrender.com"})
@RestController
@RequestMapping("/api/coaches")
@RequiredArgsConstructor

public class CoachResource {

    private final CoachService coachService;

    @PostMapping
    public ResponseEntity<CoachDTO> create(@RequestBody CoachDTO dto) {
        return new ResponseEntity<>(coachService.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CoachDTO>> getAll() {
        return ResponseEntity.ok(coachService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoachDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(coachService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoachDTO> update(@PathVariable Long id, @RequestBody CoachDTO dto) {
        return ResponseEntity.ok(coachService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        coachService.eliminar(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content
    }
}