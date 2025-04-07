package com.saif.pfe.controllers;

import com.saif.pfe.models.CourseStudent;
import com.saif.pfe.models.embeddedId.CourseStudentId;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.services.CourseStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/course-students")
public class CourseStudentController {

    @Autowired
    private CourseStudentService service;

    @GetMapping
    public ResponseEntity<List<CourseStudent>> getAll(@ModelAttribute SearchCriteria searchCriteria) {
        return ResponseEntity.ok(service.findAll(searchCriteria));
    }

    @GetMapping("/{courseId}/{studentId}")
    public ResponseEntity<CourseStudent> getById(@PathVariable Long courseId, @PathVariable Long studentId) {
        CourseStudentId id = new CourseStudentId(courseId, studentId);
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CourseStudent> create(@RequestBody CourseStudent courseStudent) {
        return ResponseEntity.ok(service.save(courseStudent));
    }

    @PutMapping("/{courseId}/{studentId}")
    public ResponseEntity<CourseStudent> update(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            @RequestBody CourseStudent courseStudent) {
        CourseStudentId id = new CourseStudentId(courseId, studentId);
        try {
            return ResponseEntity.ok(service.update(id, courseStudent));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{courseId}/{studentId}")
    public ResponseEntity<Void> delete(@PathVariable Long courseId, @PathVariable Long studentId) {
        CourseStudentId id = new CourseStudentId(courseId, studentId);
        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

