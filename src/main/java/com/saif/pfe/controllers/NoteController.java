package com.saif.pfe.controllers;

import com.saif.pfe.models.Note;
import com.saif.pfe.models.Student;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.services.NoteService;
import com.saif.pfe.services.StudentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;
    private final StudentService studentService;

    public NoteController(NoteService noteService, StudentService studentService) {
        this.noteService = noteService;
        this.studentService = studentService;
    }

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        return noteService.createNote(note);
    }

    @GetMapping
    public List<Note> getAllNotes(@ModelAttribute SearchCriteria searchCriteria) {
        return noteService.getAllNotes(searchCriteria);
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

