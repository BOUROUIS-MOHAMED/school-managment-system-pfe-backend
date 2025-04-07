package com.saif.pfe.repository;

import com.saif.pfe.models.TeacherClassroom;
import com.saif.pfe.models.embeddedId.TeacherClassroomId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherClassroomRepository extends JpaRepository<TeacherClassroom, TeacherClassroomId> {
}
