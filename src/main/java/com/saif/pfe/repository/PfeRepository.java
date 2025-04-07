package com.saif.pfe.repository;

import com.saif.pfe.models.Pfe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PfeRepository extends JpaRepository<Pfe, Long> {
}
