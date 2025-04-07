package com.saif.pfe.repository;

import com.saif.pfe.models.CourseStudent;
import com.saif.pfe.models.embeddedId.CourseStudentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseStudentRepository extends JpaRepository<CourseStudent, CourseStudentId> {
}
