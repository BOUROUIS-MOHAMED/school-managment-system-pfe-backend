package com.saif.pfe.controllers;

import com.saif.pfe.models.Semester;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.services.SemesterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semester")
public class SemesterController {

    private final SemesterService semesterService;

    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @PostMapping
    public ResponseEntity<Semester> createSemester(@RequestBody Semester semester) {
        return ResponseEntity.ok(semesterService.createSemester(semester));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Semester> getSemesterById(@PathVariable Long id) {
        return ResponseEntity.ok(semesterService.getSemesterById(id));
    }

    @GetMapping
    public ResponseEntity<List<Semester>> getAllSemesters(@ModelAttribute SearchCriteria searchCriteria) {
        return ResponseEntity.ok(semesterService.getAllSemesters(searchCriteria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Semester> updateSemester(@PathVariable Long id, @RequestBody Semester semesterDetails) {
        return ResponseEntity.ok(semesterService.updateSemester(id, semesterDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSemester(@PathVariable Long id) {
        semesterService.deleteSemester(id);
        return ResponseEntity.noContent().build();
    }
}

