package com.saif.pfe.repository;

import com.saif.pfe.models.Note;
import com.saif.pfe.models.ennum.NoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByStudentId(Long studentId);
    List<Note> findByTeacherId(Long teacherId);
    List<Note> findByCourseId(Long courseId);
    Optional<Note> findByCourseIdAndStudentIdAndSemesterIdAndType(Long courseId, Long studentId, Long semesterId, NoteType type);
}
