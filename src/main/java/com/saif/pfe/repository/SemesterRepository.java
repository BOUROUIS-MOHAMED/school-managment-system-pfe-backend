package com.saif.pfe.repository;

import com.saif.pfe.models.Pfe;
import com.saif.pfe.models.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
}
