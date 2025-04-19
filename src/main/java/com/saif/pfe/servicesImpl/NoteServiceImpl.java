package com.saif.pfe.servicesImpl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.saif.pfe.models.*;
import com.saif.pfe.models.ennum.ERole;
import com.saif.pfe.models.ennum.NoteType;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.NoteRepository;
import com.saif.pfe.repository.StudentRepository;
import com.saif.pfe.repository.TeacherRepository;
import com.saif.pfe.services.NoteService;
import com.saif.pfe.services.StudentService;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public NoteServiceImpl(NoteRepository noteRepository, StudentService studentService, TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.noteRepository = noteRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }


    @Override
    public Note createNote(Note note) {
        Optional<Note> noteOptional = noteRepository.findByCourseIdAndStudentIdAndSemesterIdAndType(
                note.getCourse().getId(),
                note.getStudent().getId(),
                note.getSemester().getId(),
                note.getType()

                );
        if (noteOptional.isPresent()) {
            return null;
        }
        return noteRepository.save(note);
    }

    @Override
    public List<Note> getAllNotes(SearchCriteria searchCriteria, User user) {

        List<ERole> roles= user.getRoles().stream().map(Role::getName).toList();


        if (roles.contains(ERole.ROLE_ADMIN)) {
            return noteRepository.findAll(searchCriteria.getPageable()).toList();
        }else if(roles.contains(ERole.ROLE_USER)){
            Optional<Student> student=studentRepository.findByUserId(user.getId());
            if (student.isEmpty()){
                return new ArrayList<>();
            }
            return  noteRepository.findByStudentId(student.get().getId());
        }else if (roles.contains(ERole.ROLE_MODERATOR)){
            Optional<Teacher> teacher=teacherRepository.findByUserId(user.getId());
            if (teacher.isEmpty()){
                return new ArrayList<>();
            }
            return  noteRepository.findByTeacherId(teacher.get().getId());
        }else return new ArrayList<>();

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
    public List<Note> getNotesByTeacherId(Long teacherId) {
        return noteRepository.findByTeacherId(teacherId);
    }

    @Override
    public List<Note> getNotesByCourseId(Long courseId) {
        return noteRepository.findByCourseId(courseId);
    }

    @Override
    public byte[] generatePdfForStudentNotes(Student student) {
        List<Note> notes = noteRepository.findByStudentId(student.getId());

        // Group notes by course and then by type
        Map<Course, Map<NoteType, List<Note>>> notesByCourse = notes.stream()
                .collect(Collectors.groupingBy(Note::getCourse,
                        Collectors.groupingBy(Note::getType)));

        double totalWeightedSum = 0.0;
        int totalCoefficient = 0;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Header
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            document.add(new Paragraph("Notes Report for " + student.getName(), titleFont));
            document.add(Chunk.NEWLINE);

            // Process each course
            for (Map.Entry<Course, Map<NoteType, List<Note>>> entry : notesByCourse.entrySet()) {
                Course course = entry.getKey();
                Map<NoteType, List<Note>> typeNotes = entry.getValue();

                List<Note> dsNotes = typeNotes.getOrDefault(NoteType.DS, Collections.emptyList());
                List<Note> tpNotes = typeNotes.getOrDefault(NoteType.TP, Collections.emptyList());
                List<Note> examNotes = typeNotes.getOrDefault(NoteType.EXAM, Collections.emptyList());

                // Calculate averages
                double avgDS = dsNotes.isEmpty() ? 0.0 : dsNotes.stream().mapToDouble(n -> n.getScore()).average().orElse(0.0);
                double avgTP = tpNotes.isEmpty() ? 0.0 : tpNotes.stream().mapToDouble(n -> n.getScore()).average().orElse(0.0);
                double avgExam = examNotes.isEmpty() ? 0.0 : examNotes.stream().mapToDouble(n -> n.getScore()).average().orElse(0.0);

                // Convert coefficients to decimals
                double dsCoeff = course.getCoefficientDsPercent() / 100.0;
                double tpCoeff = course.getCoefficientTpPercent() / 100.0;
                double examCoeff = course.getCoefficientExamPercent() / 100.0;

                // Compute contributions and base average
                double dsContribution = avgDS * dsCoeff;
                double tpContribution = avgTP * tpCoeff;
                double examContribution = avgExam * examCoeff;
                double baseMoy = dsContribution + tpContribution + examContribution;
                double courseWeighted = baseMoy * course.getCoefficient();

                totalWeightedSum += courseWeighted;
                totalCoefficient += course.getCoefficient();

                // Add course section to PDF
                Font courseFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
                document.add(new Paragraph(
                        String.format("%s (Coefficient: %d)", course.getName(), course.getCoefficient()),
                        courseFont));
                document.add(Chunk.NEWLINE);

                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setHorizontalAlignment(Element.ALIGN_LEFT);

                // Table headers
                addTableHeader(table, "Type", "Scores", "Weighted Contribution");

                // Add rows for each note type
                addNoteTypeRow(table, NoteType.DS, dsNotes, dsCoeff);
                addNoteTypeRow(table, NoteType.TP, tpNotes, tpCoeff);
                addNoteTypeRow(table, NoteType.EXAM, examNotes, examCoeff);

                // Course total row
                PdfPCell totalLabelCell = new PdfPCell(new Phrase("Course Average", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                totalLabelCell.setColspan(2);
                totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(totalLabelCell);

                PdfPCell totalValueCell = new PdfPCell(new Phrase(String.format("%.2f", baseMoy)));
                totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(totalValueCell);

                document.add(table);
                document.add(Chunk.NEWLINE);
            }

            // Overall student average
            double studentMoy = totalCoefficient == 0 ? 0.0 : totalWeightedSum / totalCoefficient;
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Paragraph footer = new Paragraph(
                    String.format("Overall Student Average: %.2f", studentMoy),
                    footerFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF", e);
        }
        return baos.toByteArray();
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        Stream.of(headers)
                .forEach(header -> {
                    PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(new BaseColor(240, 240, 240));
                    table.addCell(cell);
                });
    }

    private void addNoteTypeRow(PdfPTable table, NoteType type, List<Note> notes, double coefficient) {
        String typeName = type.name();
        String scores = notes.isEmpty()
                ? "N/A"
                : notes.stream()
                .map(n -> String.format("%.2f", n.getScore()))
                .collect(Collectors.joining(", "));

        double avg = notes.isEmpty() ? 0.0 : notes.stream().mapToDouble(n -> n.getScore()).average().orElse(0.0);
        double weighted = avg * coefficient;

        table.addCell(new Phrase(typeName));
        table.addCell(new Phrase(scores));

        PdfPCell weightedCell = new PdfPCell(new Phrase(String.format("%.2f", weighted)));
        weightedCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(weightedCell);
    }


}
