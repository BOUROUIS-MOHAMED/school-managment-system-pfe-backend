package com.saif.pfe.controllers;

import com.saif.pfe.models.TeacherClassroom;
import com.saif.pfe.models.embeddedId.TeacherClassroomId;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.services.TeacherClassroomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teacher-classrooms")
public class TeacherClassroomController {


    private final TeacherClassroomService teacherClassroomService;

    public TeacherClassroomController(TeacherClassroomService service) {
        this.teacherClassroomService = service;
    }

    @GetMapping()
    public List<TeacherClassroom> getAll(@ModelAttribute SearchCriteria searchCriteria) {
        return teacherClassroomService.findAll(searchCriteria);
    }

    @GetMapping("/{teacherId}/{classroomId}")
    public ResponseEntity<TeacherClassroom> getById(@PathVariable("teacherId") Long teacherId, @PathVariable("classroomId") Long classroomId) {
        TeacherClassroomId id = new TeacherClassroomId(teacherId, classroomId);
        Optional<TeacherClassroom> teacherClassroom = teacherClassroomService.findById(id);
        return teacherClassroom.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TeacherClassroom create(@RequestBody TeacherClassroom teacherClassroom) {
        return teacherClassroomService.save(teacherClassroom);
    }

    @DeleteMapping("/{teacherId}/{classroomId}")
    public ResponseEntity<Void> delete(@PathVariable Long teacherId, @PathVariable Long classroomId) {
        TeacherClassroomId id = new TeacherClassroomId(teacherId, classroomId);
        teacherClassroomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
