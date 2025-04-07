package com.saif.pfe.services;

import com.saif.pfe.models.TeacherClassroom;
import com.saif.pfe.models.embeddedId.TeacherClassroomId;
import com.saif.pfe.models.searchCriteria.SearchCriteria;

import java.util.List;
import java.util.Optional;

public interface TeacherClassroomService {
    List<TeacherClassroom> findAll(SearchCriteria searchCriteria);
    Optional<TeacherClassroom> findById(TeacherClassroomId id);
    TeacherClassroom save(TeacherClassroom teacherClassroom);
    void deleteById(TeacherClassroomId id);
}
