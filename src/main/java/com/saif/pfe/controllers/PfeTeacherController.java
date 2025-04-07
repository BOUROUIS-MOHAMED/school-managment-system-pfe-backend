package com.saif.pfe.controllers;

import com.saif.pfe.models.PfeTeacher;
import com.saif.pfe.models.embeddedId.PfeTeacherId;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.services.PfeTeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pfe-teacher")
@RequiredArgsConstructor
public class PfeTeacherController {
    private final PfeTeacherService pfeTeacherService;

    @GetMapping
    public List<PfeTeacher> getAllPfeTeachers(@ModelAttribute SearchCriteria searchCriteria) {
        return pfeTeacherService.findAll(searchCriteria);
    }

    @GetMapping("/{pfeId}/{teacherId}")
    public ResponseEntity<PfeTeacher> getPfeTeacherById(@PathVariable Long pfeId, @PathVariable Long teacherId) {
        PfeTeacherId id = new PfeTeacherId(pfeId, teacherId);
        return pfeTeacherService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PfeTeacher createPfeTeacher(@RequestBody PfeTeacher pfeTeacher) {
        return pfeTeacherService.save(pfeTeacher);
    }

    @DeleteMapping("/{pfeId}/{teacherId}")
    public ResponseEntity<Void> deletePfeTeacher(@PathVariable Long pfeId, @PathVariable Long teacherId) {
        PfeTeacherId id = new PfeTeacherId(pfeId, teacherId);
        pfeTeacherService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

