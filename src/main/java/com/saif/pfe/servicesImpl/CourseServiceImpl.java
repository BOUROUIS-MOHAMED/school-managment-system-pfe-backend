package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.*;
import com.saif.pfe.models.ennum.ERole;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.CourseRepository;
import com.saif.pfe.repository.TeacherCourseRepository;
import com.saif.pfe.repository.TeacherRepository;
import com.saif.pfe.services.CourseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final TeacherCourseRepository teacherCourseRepository;
    private final TeacherRepository teacherRepository;

    public CourseServiceImpl(CourseRepository courseRepository, TeacherCourseRepository teacherCourseRepository, TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.teacherCourseRepository = teacherCourseRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id " + id));
    }

    @Override
    public List<Course> getAllCourses(SearchCriteria searchCriteria, User user) {
        List<ERole> roles= user.getRoles().stream().map(Role::getName).toList();



        if (roles.contains(ERole.ROLE_ADMIN)){
            return courseRepository.findAll(searchCriteria.getPageable()).toList();
        }else if (roles.contains(ERole.ROLE_MODERATOR)){
            Optional<Teacher> teacher=teacherRepository.findByUserId(user.getId());
            if (teacher.isEmpty()){
                return new ArrayList<>();
            }
           List<TeacherCourse> teacherCourses=teacherCourseRepository.findAllByTeacherId(teacher.get().getId());
           List<Course> courses=new ArrayList<>();
           for (TeacherCourse teacherCourse:teacherCourses){
              courses.add(teacherCourse.getCourse());
           }
           return courses;
        }else return new ArrayList<>();
    }

    @Override
    public Course updateCourse(Long id, Course courseDetails) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id " + id));
        course.setName(courseDetails.getName());
        course.setDescription(courseDetails.getDescription());
        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id " + id));
        courseRepository.delete(course);
    }

}
