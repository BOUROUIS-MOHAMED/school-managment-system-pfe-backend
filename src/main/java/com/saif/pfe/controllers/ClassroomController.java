package com.saif.pfe.controllers;

import com.saif.pfe.models.Classroom;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.servicesImpl.ClassroomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/api/classrooms")
    public class ClassroomController {

        @Autowired
        private ClassroomServiceImpl classroomService;

        @PostMapping
        public ResponseEntity<Classroom> createClassroom(@RequestBody Classroom classroom) {
            Classroom createdClassroom = classroomService.saveClassroom(classroom);
            return ResponseEntity.ok(createdClassroom);
        }

        @GetMapping
        public ResponseEntity<List<Classroom>> getAllClassrooms(@ModelAttribute SearchCriteria searchCriteria) {
            List<Classroom> classrooms = classroomService.getAllClassrooms(searchCriteria);
            return ResponseEntity.ok(classrooms);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Classroom> getClassroomById(@PathVariable Long id) {
            return classroomService.getClassroomById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        @PutMapping("/{id}")
        public ResponseEntity<Classroom> updateClassroom(@PathVariable Long id, @RequestBody Classroom classroom) {
            try {
                Classroom updatedClassroom = classroomService.updateClassroom(id, classroom);
                return ResponseEntity.ok(updatedClassroom);
            } catch (RuntimeException e) {
                return ResponseEntity.notFound().build();
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteClassroom(@PathVariable Long id) {
            try {
                classroomService.deleteClassroom(id);
                return ResponseEntity.noContent().build();
            } catch (RuntimeException e) {
                return ResponseEntity.notFound().build();
            }
        }

    }