package com.saif.pfe.services;

import com.saif.pfe.models.Teacher;
import com.saif.pfe.models.searchCriteria.SearchCriteria;

import java.util.List;

public interface TeacherService {
    Teacher createTeacher(Teacher teacher);
    List<Teacher> getAllTeachers(SearchCriteria searchCriteria);
    Teacher getTeacherById(Long id);
    Teacher updateTeacher(Long id, Teacher teacher);
    void deleteTeacher(Long id);
}
