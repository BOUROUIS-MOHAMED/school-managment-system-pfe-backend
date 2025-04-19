package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.Course;
import com.saif.pfe.models.Role;
import com.saif.pfe.models.TeacherCourse;
import com.saif.pfe.models.User;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.CourseRepository;
import com.saif.pfe.repository.TeacherCourseRepository;
import com.saif.pfe.services.CourseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final TeacherCourseRepository teacherCourseRepository;

    public CourseServiceImpl(CourseRepository courseRepository, TeacherCourseRepository teacherCourseRepository) {
        this.courseRepository = courseRepository;
        this.teacherCourseRepository = teacherCourseRepository;
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

        if (user.getRoles().contains(Role.ADMIN)){
            return courseRepository.findAll(searchCriteria.getPageable()).toList();
        }else if (user.getRoles().contains(Role.MODERATOR)){
           List<TeacherCourse> teacherCourses=teacherCourseRepository.findAllByTeacherId(user.getId());
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
