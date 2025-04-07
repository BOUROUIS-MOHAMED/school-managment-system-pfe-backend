package com.saif.pfe.services;

import com.saif.pfe.models.Student;
import com.saif.pfe.models.searchCriteria.SearchCriteria;

import java.util.List;

public interface StudentService {
    Student createStudent(Student student);
    List<Student> getAllStudents(SearchCriteria searchCriteria);
    Student getStudentById(Long id);
    Student getStudentByUuid(String uuid);
    Student updateStudent(Long id, Student student);
    void deleteStudent(Long id);
}
