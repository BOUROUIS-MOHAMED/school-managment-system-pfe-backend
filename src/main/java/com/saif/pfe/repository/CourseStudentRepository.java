package com.saif.pfe.repository;

import com.saif.pfe.models.CourseStudent;
import com.saif.pfe.models.embeddedId.CourseStudentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseStudentRepository extends JpaRepository<CourseStudent, CourseStudentId> {
    List<CourseStudent> findAllByStudentId(Long studentId);

    List<CourseStudent> findAllByCourseIdIn(List<Long> courseIds);
}

