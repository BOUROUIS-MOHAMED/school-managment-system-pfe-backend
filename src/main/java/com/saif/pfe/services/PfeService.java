package com.saif.pfe.services;

import com.saif.pfe.models.Pfe;
import com.saif.pfe.models.searchCriteria.SearchCriteria;

import java.util.List;
import java.util.Optional;

public interface PfeService {

    Pfe saveOrUpdatePfe(Pfe pfe);
    Optional<Pfe> getPfeById(Long id);
    List<Pfe> getAllPfes(SearchCriteria searchCriteria);
    void deletePfe(Long id);
}
