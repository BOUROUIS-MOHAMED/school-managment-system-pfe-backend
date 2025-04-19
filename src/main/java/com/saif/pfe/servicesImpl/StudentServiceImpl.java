package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.*;
import com.saif.pfe.models.ennum.ERole;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.*;
import com.saif.pfe.services.StudentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final TeacherClassroomRepository teacherClassroomRepository;
    private final TeacherCourseRepository teacherCourseRepository;
    private final TeacherRepository teacherRepository;
    private final CourseStudentRepository courseStudentRepository;

    public StudentServiceImpl(StudentRepository studentRepository, TeacherClassroomRepository teacherClassroomRepository, TeacherCourseRepository teacherCourseRepository, TeacherRepository teacherRepository, CourseStudentRepository courseStudentRepository) {
        this.studentRepository = studentRepository;
        this.teacherClassroomRepository = teacherClassroomRepository;
        this.teacherCourseRepository = teacherCourseRepository;
        this.teacherRepository = teacherRepository;
        this.courseStudentRepository = courseStudentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents(SearchCriteria searchCriteria, User user) {

        List<ERole> roles= user.getRoles().stream().map(Role::getName).toList();


        if (roles.contains(ERole.ROLE_ADMIN)) {
            return studentRepository.findAll(searchCriteria.getPageable()).toList();
        }if (roles.contains(ERole.ROLE_MODERATOR)){

            Optional<Teacher> teacher=teacherRepository.findByUserId(user.getId());
            if (teacher.isEmpty()){
                return new ArrayList<>();
            }
            Long id=teacher.get().getId();

            // Fetch the courses taught by this teacher
            List<TeacherCourse> teacherCourses = teacherCourseRepository.findAllByTeacherId(id);

            if (teacherCourses.isEmpty()) {
                return new ArrayList<>();
            }

            // Collect the course IDs
            List<Long> courseIds = teacherCourses.stream()
                    .map(tc -> tc.getCourse().getId())
                    .toList();

            // Fetch CourseStudent relations where courseId matches
            List<CourseStudent> courseStudents = courseStudentRepository.findAllByCourseIdIn(courseIds);

            // Collect distinct students
            return courseStudents.stream()
                    .map(CourseStudent::getStudent)
                    .distinct()
                    .toList();

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
