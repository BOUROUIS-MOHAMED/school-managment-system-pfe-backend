package com.saif.pfe.services;

import com.saif.pfe.models.Classroom;
import com.saif.pfe.models.searchCriteria.SearchCriteria;

import java.util.List;
import java.util.Optional;

public interface ClassroomService {
    Classroom saveClassroom(Classroom classroom);
    List<Classroom> getAllClassrooms(SearchCriteria searchCriteria);
    Optional<Classroom> getClassroomById(Long id);
    Classroom updateClassroom(Long id, Classroom updatedClassroom);
    void deleteClassroom(Long id);

}
