package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.Note;
import com.saif.pfe.models.Student;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.NoteRepository;
import com.saif.pfe.services.NoteService;
import com.saif.pfe.services.StudentService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository, StudentService studentService) {
        this.noteRepository = noteRepository;
    }


    @Override
    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public List<Note> getAllNotes(SearchCriteria searchCriteria) {
        return noteRepository.findAll(searchCriteria.getPageable()).toList();
    }

    @Override
    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found."));
    }

    @Override
    public Note updateNote(Long id, Note note) {
        Note existingNote = getNoteById(id);
        existingNote.setScore(note.getScore());
        existingNote.setType(note.getType());
        existingNote.setStudent(note.getStudent());
        existingNote.setTeacher(note.getTeacher());
        return noteRepository.save(existingNote);
    }
    @Override
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    @Override
    public List<Note> getNotesByStudentId(Long studentId) {
        return noteRepository.findByStudentId(studentId);
    }

    @Override
    public byte[] generatePdfForStudentNotes(Student student) {
        List<Note> notes = noteRepository.findByStudentId(student.getId());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            document.add(new Paragraph("Notes Report For " + student.getName()));
            document.add(new Paragraph(" "));

            for (Note note : notes) {
                document.add(new Paragraph("Note Type: " + note.getType() + ", Score: " + note.getScore()));
            }

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

}
