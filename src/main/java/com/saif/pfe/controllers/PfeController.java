package com.saif.pfe.controllers;

import com.saif.pfe.models.Pfe;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.services.PfeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pfes")
public class PfeController {

    @Autowired
    private PfeService pfeService;

    // Create or Update
    @PostMapping
    public ResponseEntity<Pfe> createOrUpdatePfe(@RequestBody Pfe pfe) {
        return ResponseEntity.ok(pfeService.saveOrUpdatePfe(pfe));
    }

    // Find by ID
    @GetMapping("/{id}")
    public ResponseEntity<Pfe> getPfeById(@PathVariable Long id) {
        Optional<Pfe> pfe = pfeService.getPfeById(id);
        return pfe.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Find all
    @GetMapping
    public ResponseEntity<List<Pfe>> getAllPfes(@ModelAttribute SearchCriteria searchCriteria) {
        return ResponseEntity.ok(pfeService.getAllPfes(searchCriteria));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePfe(@PathVariable Long id) {
        pfeService.deletePfe(id);
        return ResponseEntity.noContent().build();
    }
}

