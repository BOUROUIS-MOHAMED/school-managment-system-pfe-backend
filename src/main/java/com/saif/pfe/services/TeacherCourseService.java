package com.saif.pfe.services;

import com.saif.pfe.models.TeacherCourse;
import com.saif.pfe.models.embeddedId.TeacherCourseId;
import com.saif.pfe.models.searchCriteria.SearchCriteria;

import java.util.List;
import java.util.Optional;

public interface TeacherCourseService {

    List<TeacherCourse> findAll(SearchCriteria searchCriteria);

    Optional<TeacherCourse> findById(TeacherCourseId id) ;

    TeacherCourse save(TeacherCourse teacherCourse) ;

    void deleteById(TeacherCourseId id) ;
}
