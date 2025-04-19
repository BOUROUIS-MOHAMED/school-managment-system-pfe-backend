package com.saif.pfe.repository;

import com.saif.pfe.models.TeacherCourse;
import com.saif.pfe.models.embeddedId.TeacherCourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherCourseRepository extends JpaRepository<TeacherCourse, TeacherCourseId> {
List<TeacherCourse> findAllByTeacherId(long id);
}
