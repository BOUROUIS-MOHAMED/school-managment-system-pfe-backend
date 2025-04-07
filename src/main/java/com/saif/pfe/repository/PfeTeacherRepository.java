package com.saif.pfe.repository;

import com.saif.pfe.models.PfeTeacher;
import com.saif.pfe.models.embeddedId.PfeTeacherId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PfeTeacherRepository extends JpaRepository<PfeTeacher, PfeTeacherId> {
}
