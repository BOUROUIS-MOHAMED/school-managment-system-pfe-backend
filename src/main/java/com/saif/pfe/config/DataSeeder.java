package com.saif.pfe.config;

import com.saif.pfe.models.*;
import com.saif.pfe.models.ennum.ERole;
import com.saif.pfe.models.ennum.NoteType;
import com.saif.pfe.repository.*;
import jakarta.persistence.EntityManager;
import net.datafaker.Faker;
import org.jeasy.random.EasyRandom;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ClassroomRepository classroomRepository;
    private final CourseRepository courseRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final PfeRepository pfeRepository;
    private final PfeTeacherRepository pfeTeacherRepository;
    private final NoteRepository noteRepository;
    private final TeacherClassroomRepository teacherClassroomRepository;
    private final TeacherCourseRepository teacherCourseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EasyRandom easyRandom = new EasyRandom();
    private final Faker faker = new Faker();
    private final EntityManager entityManager;
    private final PasswordEncoder encoder;
    public DataSeeder(ClassroomRepository classroomRepository, CourseRepository courseRepository, CourseStudentRepository courseStudentRepository, PfeRepository pfeRepository, PfeTeacherRepository pfeTeacherRepository, TeacherClassroomRepository teacherClassroomRepository, TeacherCourseRepository teacherCourseRepository, StudentRepository studentRepository, TeacherRepository teacherRepository, NoteRepository noteRepository, UserRepository userRepository, RoleRepository roleRepository, EntityManager entityManager, PasswordEncoder encoder) {
        this.classroomRepository = classroomRepository;
        this.courseRepository = courseRepository;
        this.courseStudentRepository = courseStudentRepository;
        this.pfeRepository = pfeRepository;
        this.pfeTeacherRepository = pfeTeacherRepository;
        this.teacherClassroomRepository = teacherClassroomRepository;
        this.teacherCourseRepository = teacherCourseRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.entityManager = entityManager;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(String... args) {

            roleRepository.save(Role.builder().name(ERole.ROLE_ADMIN).build());
            roleRepository.save(Role.builder().name(ERole.ROLE_USER).build());
            roleRepository.save(Role.builder().name(ERole.ROLE_MODERATOR).build());

        if (userRepository.count() > 0) {
            System.out.println("Users already exist, skipping seeding.");
            return;
        }

        for (int j = 0; j < 4; j++) {
            String username = faker.name().username();
            if (username.length() > 20) {
                username = username.substring(0, 20); // Limite à 20 caractères
            }

            String email = faker.internet().emailAddress();
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                email = "user" + j + "@example.com"; // Fallback en cas d'email invalide
            }

            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(encoder.encode("securePassword123")); // 🔹 Assure-toi que le mot de passe est encodé en production

            // 🔹 Ajouter un rôle si nécessaire
            Role role = roleRepository.findByName(j<3 ?ERole.values()[j] : ERole.values()[0]).orElse(null);
            if (role != null) {
                user.getRoles().add(role);
            }

            userRepository.save(user);
        }

        System.out.println("Fake users inserted successfully!");

        //**************---------------*************------------------------------------
        User user1 = userRepository.findById(1L).get();
        User user2 = userRepository.findById(2L).get();
        User user3 = userRepository.findById(3L).get();
        User user4 = userRepository.findById(4L).get();
        entityManager.merge(user1); // Reattaching detached user
        entityManager.merge(user2); //
        entityManager.merge(user3); //
        entityManager.merge(user4); //
       //  Génération des enseignants
        List<Teacher> teachers = teacherRepository.saveAll(
                List.of(
                        new Teacher(null, faker.name().fullName(), faker.internet().emailAddress(), faker.phoneNumber().cellPhone(), user1),
                        new Teacher(null, faker.name().fullName(), faker.internet().emailAddress(), faker.phoneNumber().cellPhone(), user2)
                )
        );

        // Génération des étudiants
        List<Student> students = studentRepository.saveAll(
                List.of(
                        new Student(null, faker.name().fullName(), faker.internet().emailAddress(), faker.phoneNumber().cellPhone(), null, user3),
                        new Student(null, faker.name().fullName(), faker.internet().emailAddress(), faker.phoneNumber().cellPhone(), null, user4)
                )
        );

        // Génération des cours
        List<Course> courses = courseRepository.saveAll(
                List.of(
                        new Course(null, "Math", "Mathematics basics"),
                        new Course(null, "Physics", "Physics introduction")
                )
        );

        // Génération des notes pour chaque étudiant
        students.forEach(student -> {
            teachers.forEach(teacher -> {
                courses.forEach(course -> {
                    Note note = new Note(
                            null,
                            easyRandom.nextFloat() * 20,  // Score entre 0 et 20
                            NoteType.values()[easyRandom.nextInt(NoteType.values().length)], // Type aléatoire
                            student,
                            teacher
                    );
                    noteRepository.save(note);
                });
            });
        });

        System.out.println("Fake data inserted successfully!");
    }
}
