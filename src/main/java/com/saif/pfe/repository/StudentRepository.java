package com.saif.pfe.repository;

import com.saif.pfe.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findStudentByUuid(String studentUuid);
    Optional<Student> findByUserId(Long userId);

}
