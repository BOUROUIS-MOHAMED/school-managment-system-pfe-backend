package com.saif.pfe.repository;

import com.saif.pfe.models.Pfe;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PfeRepository extends JpaRepository<Pfe, Long> {
    List<Pfe> findAllByStudentOneIdOrStudentTwoId(Long studentOneId,Long studentTwoId);
    List<Pfe> findAllBySupervisorIdOrPresidentIdOrRapporteurId(Long supervisorId,Long presidentId,Long rapporteurId);
}
