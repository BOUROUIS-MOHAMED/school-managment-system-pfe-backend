package com.saif.pfe.controllers;

import com.saif.pfe.models.TeacherClassroom;
import com.saif.pfe.models.User;
import com.saif.pfe.models.embeddedId.TeacherClassroomId;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.UserRepository;
import com.saif.pfe.security.jwt.JwtUtils;
import com.saif.pfe.services.TeacherClassroomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teacher-classrooms")
public class TeacherClassroomController {


    private final TeacherClassroomService teacherClassroomService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public TeacherClassroomController(TeacherClassroomService service, JwtUtils jwtUtils, UserRepository userRepository) {
        this.teacherClassroomService = service;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public List<TeacherClassroom> getAll(@ModelAttribute SearchCriteria searchCriteria, HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove the "Bearer " prefix
        }

        // Use JwtUtil to extract accountUsernameId from the token
        String accountUsernameId = jwtUtils.getUserNameFromJwtToken(token);

        System.out.println("the account username id extracted from token is " + accountUsernameId);

        Optional<User> user=userRepository.findByUsername(accountUsernameId);
        if(user.isPresent()) {
            return (teacherClassroomService.findAll(searchCriteria,user.get()));
        }else{
            return new ArrayList<>();
        }


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
