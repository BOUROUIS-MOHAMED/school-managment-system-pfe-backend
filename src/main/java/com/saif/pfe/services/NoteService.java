package com.saif.pfe.services;

import com.saif.pfe.models.Note;
import com.saif.pfe.models.Student;
import com.saif.pfe.models.searchCriteria.SearchCriteria;

import java.util.List;

public interface NoteService {

    Note createNote(Note note);
    List<Note> getAllNotes(SearchCriteria searchCriteria);
    Note getNoteById(Long id);
    Note updateNote(Long id, Note note);
    void deleteNote(Long id);
    List<Note> getNotesByStudentId(Long studentId);
    byte[] generatePdfForStudentNotes(Student student);
}
