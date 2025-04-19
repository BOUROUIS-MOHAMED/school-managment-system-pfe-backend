package com.saif.pfe.controllers;

import com.saif.pfe.models.Note;
import com.saif.pfe.models.Student;
import com.saif.pfe.models.User;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.UserRepository;
import com.saif.pfe.security.jwt.JwtUtils;
import com.saif.pfe.services.NoteService;
import com.saif.pfe.services.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;
    private final StudentService studentService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public NoteController(NoteService noteService, StudentService studentService, UserRepository userRepository, JwtUtils jwtUtils) {
        this.noteService = noteService;
        this.studentService = studentService;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        return noteService.createNote(note);
    }

    @GetMapping
    public List<Note> getAllNotes(@ModelAttribute SearchCriteria searchCriteria, HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove the "Bearer " prefix
        }

        // Use JwtUtil to extract accountUsernameId from the token
        String accountUsernameId = jwtUtils.getUserNameFromJwtToken(token);

        System.out.println("the account username id extracted from token is " + accountUsernameId);

        Optional<User> user=userRepository.findByUsername(accountUsernameId);
        if(user.isPresent()) {
            return noteService.getAllNotes(searchCriteria,user.get());
        }else{
            return new ArrayList<>();
        }


    }

    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable Long id) {
        return noteService.getNoteById(id);
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Long id, @RequestBody Note note) {
        return noteService.updateNote(id, note);
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
    }

    @GetMapping("/student/{studentId}")
    public List<Note> getNotesByStudentId(@PathVariable Long studentId) {
        return noteService.getNotesByStudentId(studentId);
    }
    @GetMapping("/teacher/{teacherId}")
    public List<Note> getNotesByTeacherId(@PathVariable Long teacherId) {
        return noteService.getNotesByTeacherId(teacherId);
    }
    @GetMapping("/course/{courseId}")
    public List<Note> getNotesByCourseId(@PathVariable Long courseId) {
        return noteService.getNotesByCourseId(courseId);
    }

    @GetMapping("/student/pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestParam("studentUuid") String studentUuid) {
        Student student = studentService.getStudentByUuid(studentUuid);
        if (student == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        byte[] pdf = noteService.generatePdfForStudentNotes(student);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=student_notes.pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}

