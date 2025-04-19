package com.saif.pfe.controllers;

import com.saif.pfe.models.Teacher;
import com.saif.pfe.models.User;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.UserRepository;
import com.saif.pfe.security.jwt.JwtUtils;
import com.saif.pfe.services.TeacherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public TeacherController(TeacherService teacherService, UserRepository userRepository, JwtUtils jwtUtils) {
        this.teacherService = teacherService;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public Teacher createTeacher(@RequestBody Teacher teacher) {
        return teacherService.createTeacher(teacher);
    }

    @GetMapping
    public List<Teacher> getAllTeachers(@ModelAttribute SearchCriteria searchCriteria, HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove the "Bearer " prefix
        }

        // Use JwtUtil to extract accountUsernameId from the token
        String accountUsernameId = jwtUtils.getUserNameFromJwtToken(token);

        System.out.println("the account username id extracted from token is " + accountUsernameId);

        Optional<User> user = userRepository.findByUsername(accountUsernameId);
        if (user.isPresent()) {
            return (teacherService.getAllTeachers(searchCriteria, user.get()));
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping("/{id}")
    public Teacher getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id);
    }

    @PutMapping("/{id}")
    public Teacher updateTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        return teacherService.updateTeacher(id, teacher);
    }

    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
    }
}

