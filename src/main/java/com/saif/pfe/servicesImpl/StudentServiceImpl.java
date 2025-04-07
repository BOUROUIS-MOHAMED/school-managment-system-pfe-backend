package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.Student;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.StudentRepository;
import com.saif.pfe.services.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents(SearchCriteria searchCriteria) {
        return studentRepository.findAll(searchCriteria.getPageable()).toList();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found."));
    }

    @Override
    public Student getStudentByUuid(String uuid) {
        return studentRepository.findStudentByUuid(uuid);
    }

    public Student updateStudent(Long id, Student student) {
        Student existingStudent = getStudentById(id);
        existingStudent.setName(student.getName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setPhone(student.getPhone());
        existingStudent.setClassroom(student.getClassroom());
        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

}
