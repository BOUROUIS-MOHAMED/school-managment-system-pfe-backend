package com.saif.pfe.controllers;

import com.saif.pfe.models.TeacherCourse;
import com.saif.pfe.models.User;
import com.saif.pfe.models.embeddedId.TeacherCourseId;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.UserRepository;
import com.saif.pfe.security.jwt.JwtUtils;
import com.saif.pfe.services.TeacherCourseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teacher-courses")
public class TeacherCourseController {

    private final TeacherCourseService teacherCourseService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public TeacherCourseController(TeacherCourseService teacherCourseService, JwtUtils jwtUtils, UserRepository userRepository) {
        this.teacherCourseService = teacherCourseService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public List<TeacherCourse> getAll(@ModelAttribute SearchCriteria searchCriteria, HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove the "Bearer " prefix
        }

        // Use JwtUtil to extract accountUsernameId from the token
        String accountUsernameId = jwtUtils.getUserNameFromJwtToken(token);

        System.out.println("the account username id extracted from token is " + accountUsernameId);

        Optional<User> user=userRepository.findByUsername(accountUsernameId);
        if(user.isPresent()) {
            return (teacherCourseService.findAll(searchCriteria,user.get()));
        }else{
            return new ArrayList<>();
        }


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
