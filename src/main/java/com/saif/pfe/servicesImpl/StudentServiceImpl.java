package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.Role;
import com.saif.pfe.models.Student;
import com.saif.pfe.models.TeacherClassroom;
import com.saif.pfe.models.User;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.StudentRepository;
import com.saif.pfe.repository.TeacherClassroomRepository;
import com.saif.pfe.services.StudentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final TeacherClassroomRepository teacherClassroomRepository;

    public StudentServiceImpl(StudentRepository studentRepository, TeacherClassroomRepository teacherClassroomRepository) {
        this.studentRepository = studentRepository;
        this.teacherClassroomRepository = teacherClassroomRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents(SearchCriteria searchCriteria, User user) {

        if (user.getRoles().contains(Role.ADMIN)){
            return studentRepository.findAll(searchCriteria.getPageable()).toList();
        }if (user.getRoles().contains(Role.MODERATOR)){
        List<Student> students = studentRepository.findAll();
        List<TeacherClassroom> teacherClassrooms = teacherClassroomRepository.findAll();
        List<Student> filteredStudents = new ArrayList<>();
        for (Student student : students) {
            for (TeacherClassroom teacherClassroom : teacherClassrooms) {
                if (student.getClassroom().getId().equals(teacherClassroom.getClassroom().getId())) {
                    filteredStudents.add(student);
                }
            }
        }
        return filteredStudents;
        }else return new ArrayList<>();

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
