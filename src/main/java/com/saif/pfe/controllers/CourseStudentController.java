package com.saif.pfe.controllers;

import com.saif.pfe.models.CourseStudent;
import com.saif.pfe.models.User;
import com.saif.pfe.models.embeddedId.CourseStudentId;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.UserRepository;
import com.saif.pfe.security.jwt.JwtUtils;
import com.saif.pfe.services.CourseStudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/course-students")
public class CourseStudentController {

    @Autowired
    private CourseStudentService service;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseStudentService courseStudentService;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<List<CourseStudent>> getAll(@ModelAttribute SearchCriteria searchCriteria, HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove the "Bearer " prefix
        }

        // Use JwtUtil to extract accountUsernameId from the token
        String accountUsernameId = jwtUtils.getUserNameFromJwtToken(token);

        System.out.println("the account username id extracted from token is " + accountUsernameId);

        Optional<User> user=userRepository.findByUsername(accountUsernameId);
        if(user.isPresent()) {
            return ResponseEntity.ok(courseStudentService.findAll(searchCriteria,user.get()));
        }else{
            return ResponseEntity.notFound().build();
        }


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

