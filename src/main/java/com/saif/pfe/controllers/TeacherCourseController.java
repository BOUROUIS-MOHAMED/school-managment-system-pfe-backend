package com.saif.pfe.controllers;

import com.saif.pfe.models.TeacherCourse;
import com.saif.pfe.models.embeddedId.TeacherCourseId;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.services.TeacherCourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teacher-courses")
public class TeacherCourseController {

    private final TeacherCourseService teacherCourseService;

    public TeacherCourseController(TeacherCourseService teacherCourseService) {
        this.teacherCourseService = teacherCourseService;
    }

    @GetMapping()
    public List<TeacherCourse> getAll(@ModelAttribute SearchCriteria searchCriteria) {
        return teacherCourseService.findAll(searchCriteria);
    }

    @GetMapping("/{teacherId}/{courseId}")
    public ResponseEntity<TeacherCourse> getById(@PathVariable("teacherId") Long teacherId, @PathVariable("courseId") Long courseId) {
        TeacherCourseId id = new TeacherCourseId(teacherId, courseId);
        Optional<TeacherCourse> teacherCourse = teacherCourseService.findById(id);
        return teacherCourse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TeacherCourse create(@RequestBody TeacherCourse teacherCourse) {
        return teacherCourseService.save(teacherCourse);
    }

    @DeleteMapping("/{teacherId}/{courseId}")
    public ResponseEntity<Void> delete(@PathVariable("teacherId") Long teacherId, @PathVariable("courseId") Long courseId) {
        TeacherCourseId id = new TeacherCourseId(teacherId, courseId);
        teacherCourseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
