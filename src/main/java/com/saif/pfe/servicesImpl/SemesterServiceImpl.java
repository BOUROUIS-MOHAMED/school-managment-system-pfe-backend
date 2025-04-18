package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.Course;
import com.saif.pfe.models.Semester;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.CourseRepository;
import com.saif.pfe.repository.SemesterRepository;
import com.saif.pfe.services.CourseService;
import com.saif.pfe.services.SemesterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemesterServiceImpl implements SemesterService {

    private final SemesterRepository semesterRepository;

    public SemesterServiceImpl(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    @Override
    public Semester createSemester(Semester semester) {
        return semesterRepository.save(semester);
    }

    @Override
    public Semester getSemesterById(Long id) {
        return semesterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id " + id));
    }

    @Override
    public List<Semester> getAllSemesters(SearchCriteria searchCriteria) {
        return semesterRepository.findAll(searchCriteria.getPageable()).toList();
    }

    @Override
    public Semester updateSemester(Long id, Semester semesterDetails) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semester not found with id " + id));
        semester.setSemester(semesterDetails.getSemester());
        semester.setYear(semesterDetails.getYear());
        return semesterRepository.save(semester);
    }

    @Override
    public void deleteSemester(Long id) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id " + id));
        semesterRepository.delete(semester);
    }

}
