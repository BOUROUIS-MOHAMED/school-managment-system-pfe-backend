package com.saif.pfe.config;

import com.saif.pfe.controllers.NoteController;
import com.saif.pfe.models.*;
import com.saif.pfe.models.embeddedId.CourseStudentId;
import com.saif.pfe.models.embeddedId.TeacherClassroomId;
import com.saif.pfe.models.embeddedId.TeacherCourseId;
import com.saif.pfe.models.ennum.ERole;
import com.saif.pfe.models.ennum.NoteType;
import com.saif.pfe.models.ennum.Status;
import com.saif.pfe.repository.*;
import jakarta.persistence.EntityManager;
import net.datafaker.Faker;
import org.jeasy.random.EasyRandom;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.LongStream;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ClassroomRepository classroomRepository;
    private final CourseRepository courseRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final PfeRepository pfeRepository;
   private final NoteRepository noteRepository;
   private final NoteController noteController;
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
    private final SemesterRepository semesterRepository;

    public DataSeeder(ClassroomRepository classroomRepository, CourseRepository courseRepository, CourseStudentRepository courseStudentRepository, PfeRepository pfeRepository, NoteController noteController, TeacherClassroomRepository teacherClassroomRepository, TeacherCourseRepository teacherCourseRepository, StudentRepository studentRepository, TeacherRepository teacherRepository, NoteRepository noteRepository, UserRepository userRepository, RoleRepository roleRepository, EntityManager entityManager, PasswordEncoder encoder, SemesterRepository semesterRepository) {
        this.classroomRepository = classroomRepository;
        this.courseRepository = courseRepository;
        this.courseStudentRepository = courseStudentRepository;
        this.pfeRepository = pfeRepository;
        this.noteController = noteController;
        this.teacherClassroomRepository = teacherClassroomRepository;
        this.teacherCourseRepository = teacherCourseRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.entityManager = entityManager;
        this.encoder = encoder;
        this.semesterRepository = semesterRepository;
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
        for (int j = 0; j < 40; j++) {
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

        // Reattaching all needed users
        List<User> users = userRepository.findAllById(
                LongStream.rangeClosed(1, 38).boxed().toList()
        );
        users.forEach(entityManager::merge);

// Génération des enseignants
        List<Teacher> teachers = teacherRepository.saveAll(
                List.of(
                        new Teacher(null, faker.name().fullName(), faker.internet().emailAddress(), faker.phoneNumber().cellPhone(), users.get(0)), // ID 1
                        new Teacher(null, faker.name().fullName(), faker.internet().emailAddress(), faker.phoneNumber().cellPhone(), users.get(1)), // ID 2
                        new Teacher(null, faker.name().fullName(), faker.internet().emailAddress(), faker.phoneNumber().cellPhone(), users.get(2)), // ID 3
                        new Teacher(null, faker.name().fullName(), faker.internet().emailAddress(), faker.phoneNumber().cellPhone(), users.get(3)), // ID 4
                        new Teacher(null, faker.name().fullName(), faker.internet().emailAddress(), faker.phoneNumber().cellPhone(), users.get(4)), // ID 5
                        new Teacher(null, faker.name().fullName(), faker.internet().emailAddress(), faker.phoneNumber().cellPhone(), users.get(5)), // ID 6
                        new Teacher(null, faker.name().fullName(), faker.internet().emailAddress(), faker.phoneNumber().cellPhone(), users.get(6)), // ID 7
                        new Teacher(null, faker.name().fullName(), faker.internet().emailAddress(), faker.phoneNumber().cellPhone(), users.get(7))  // ID 8
                )
        );

// Génération des étudiants
        List<Student> students = new ArrayList<>();
        for (int i = 8; i < 38; i++) {  // users with IDs 9-38 (index 8-37)
            students.add(
                    new Student(null,
                            faker.name().fullName(),
                            faker.internet().emailAddress(),
                            faker.phoneNumber().cellPhone(),
                            null,
                            users.get(i)  // each student is linked to a user
                    )
            );
        }
        Random random = new Random();

        List<Classroom> toSaveClassroom = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            toSaveClassroom.add(new Classroom(null,"I"+i,random.nextInt(40)));
        }
        for (int i = 0; i < 11; i++) {
            toSaveClassroom.add(new Classroom(null,"K"+i,random.nextInt(40)));
        }
        for (int i = 0; i < 5; i++) {
            toSaveClassroom.add(new Classroom(null,"G"+i,random.nextInt(40)));
        }
        classroomRepository.saveAll(toSaveClassroom);
        students = studentRepository.saveAll(students);

        // Génération des cours
        List<Semester> semesters = semesterRepository.saveAll(
                List.of(
                       new Semester(null,"2025",2)
                )
        );

        // Génération des cours
        List<Course> courses = courseRepository.saveAll(List.of(
                new Course(null,
                        "Math",
                        "Mathematics basics",
                        3,      // course coefficient
                        30,     // DS  30%
                        50,     // Exam 50%
                        20,     // TP  20%
                        List.of(NoteType.DS, NoteType.EXAM, NoteType.TP)
                ),
                new Course(null,
                        "Physics",
                        "Physics introduction",
                        3,
                        30,
                        70,
                        0,
                        List.of(NoteType.DS, NoteType.EXAM)
                ),
                new Course(null,
                        "Computer Science",
                        "Intro to programming & algorithms",
                        4,
                        25,
                        50,
                        25,
                        List.of(NoteType.DS, NoteType.EXAM, NoteType.TP)
                ),
                new Course(null,
                        "Chemistry",
                        "Basic concepts of chemistry",
                        3,
                        20,
                        60,
                        20,
                        List.of(NoteType.DS, NoteType.EXAM, NoteType.TP)
                ),
                new Course(null,
                        "Biology",
                        "Foundations of biology",
                        2,
                        30,
                        50,
                        20,
                        List.of(NoteType.DS, NoteType.EXAM, NoteType.TP)
                ),
                new Course(null,
                        "Literature",
                        "World literature overview",
                        2,
                        50,
                        50,
                        0,
                        List.of(NoteType.DS, NoteType.EXAM)
                ),
                new Course(null,
                        "History",
                        "Modern world history",
                        1,
                        40,
                        60,
                        0,
                        List.of(NoteType.DS, NoteType.EXAM)
                ),
                new Course(null,
                        "Geography",
                        "Physical & human geography",
                        1,
                        30,
                        50,
                        20,
                        List.of(NoteType.DS, NoteType.EXAM, NoteType.TP)
                ),
                new Course(null,
                        "English",
                        "English language skills",
                        2,
                        20,
                        60,
                        20,
                        List.of(NoteType.DS, NoteType.EXAM, NoteType.TP)
                ),
                new Course(null,
                        "Art",
                        "Visual arts fundamentals",
                        1,
                        0,
                        100,
                        0,
                        List.of(NoteType.EXAM)
                )
        ));




        // Génération des notes pour chaque étudiant
        students.forEach(student -> {
            teachers.forEach(teacher -> {
                courses.forEach(course -> {
                    Note note = new Note(
                            null,
                            easyRandom.nextFloat() * 20,  // Score entre 0 et 20
                            NoteType.values()[easyRandom.nextInt(NoteType.values().length)], // Type aléatoire
                            student,
                            course,
                            semesters.get(easyRandom.nextInt(semesters.size())),
                            teacher
                    );
                    noteController.createNote(note);
                });
            });
        });

        List<Student> allStudents = studentRepository.findAll();
        List<Teacher> allTeachers = teacherRepository.findAll();


        List<Pfe> pfeList = new ArrayList<>();
        Set<Long> assignedStudents = new HashSet<>(); // Track already assigned student IDs

// Generate 8 projects with two students
        for (int i = 0; i < 8; i++) {
            Student studentOne = null;
            Student studentTwo = null;

            // Ensure student_one is unique
            while (studentOne == null || assignedStudents.contains(studentOne.getId())) {
                studentOne = allStudents.get(random.nextInt(allStudents.size()));
            }
            assignedStudents.add(studentOne.getId());

            // Ensure student_two is unique and different from student_one
            while (studentTwo == null || assignedStudents.contains(studentTwo.getId()) || studentOne.getId().equals(studentTwo.getId())) {
                studentTwo = allStudents.get(random.nextInt(allStudents.size()));
            }
            assignedStudents.add(studentTwo.getId());

            // Randomly assign 1-3 teachers
            Teacher supervisor = random.nextBoolean() ? allTeachers.get(random.nextInt(allTeachers.size())) : null;
            Teacher president = random.nextBoolean() ? allTeachers.get(random.nextInt(allTeachers.size())) : null;
            Teacher rapporteur = random.nextBoolean() ? allTeachers.get(random.nextInt(allTeachers.size())) : null;

            pfeList.add(
                    Pfe.builder()
                            .name("PFE Project " + (i + 1))
                            .student_one(studentOne)
                            .student_two(studentTwo)
                            .supervisor(supervisor)
                            .president(president)
                            .rapporteur(rapporteur)
                            .guest(random.nextBoolean() ? "Guest " + faker.name().fullName() : null)
                            .date(LocalDate.now().plusDays(random.nextInt(90))) // random date in next 3 months
                            .note_student_one(Math.round(random.nextDouble() * 20 * 100.0) / 100.0)
                            .note_student_two(Math.round(random.nextDouble() * 20 * 100.0) / 100.0)
                            .link_report(faker.internet().url())
                            .link_presentation(faker.internet().url())
                            .link_certificate(faker.internet().url())
                            .information(faker.lorem().sentence())
                            .status(Status.values()[random.nextInt(Status.values().length)])
                            .build()
            );
        }

// Generate 12 projects with only one student
        for (int i = 8; i < 20; i++) {
            Student studentOne = null;

            // Ensure student_one is unique
            while (studentOne == null || assignedStudents.contains(studentOne.getId())) {
                studentOne = allStudents.get(random.nextInt(allStudents.size()));
            }
            assignedStudents.add(studentOne.getId());

            // Randomly assign 1-3 teachers
            Teacher supervisor = random.nextBoolean() ? allTeachers.get(random.nextInt(allTeachers.size())) : null;
            Teacher president = random.nextBoolean() ? allTeachers.get(random.nextInt(allTeachers.size())) : null;
            Teacher rapporteur = random.nextBoolean() ? allTeachers.get(random.nextInt(allTeachers.size())) : null;

            pfeList.add(
                    Pfe.builder()
                            .name("PFE Project " + (i + 1))
                            .student_one(studentOne)
                            .student_two(null) // No second student
                            .supervisor(supervisor)
                            .president(president)
                            .rapporteur(rapporteur)
                            .guest(random.nextBoolean() ? "Guest " + faker.name().fullName() : null)
                            .date(LocalDate.now().plusDays(random.nextInt(90))) // random date in next 3 months
                            .note_student_one(Math.round(random.nextDouble() * 20 * 100.0) / 100.0)
                            .note_student_two(0)  // student_two is null so note is 0
                            .link_report(faker.internet().url())
                            .link_presentation(faker.internet().url())
                            .link_certificate(faker.internet().url())
                            .information(faker.lorem().sentence())
                            .status(Status.values()[random.nextInt(Status.values().length)])
                            .build()
            );
        }

// Save all to DB
        pfeRepository.saveAll(pfeList);

        List<CourseStudent> courseStudentList = new ArrayList<>();
        for (Student student : allStudents) {
            for (Course course : courses) {

                // Randomly decide if the student takes this course (70% chance)
                if (random.nextDouble() < 0.7) {
                    CourseStudentId id = new CourseStudentId(course.getId(), student.getId());

                    CourseStudent relation = CourseStudent.builder()
                            .id(id)
                            .course(course)
                            .student(student)
                            .build();

                    courseStudentList.add(relation);
                }
            }
        }

// Save all to DB
        courseStudentRepository.saveAll(courseStudentList);

        List<Classroom> allClassrooms = classroomRepository.findAll();


        List<TeacherClassroom> teacherClassroomList = new ArrayList<>();

        for (Teacher teacher : allTeachers) {
            for (Classroom classroom : allClassrooms) {

                // Randomly decide if this teacher is linked to this classroom (around 50% chance)
                if (random.nextDouble() < 0.5) {

                    TeacherClassroomId id = new TeacherClassroomId(teacher.getId(), classroom.getId());

                    TeacherClassroom relation = TeacherClassroom.builder()
                            .id(id)
                            .teacher(teacher)
                            .classroom(classroom)
                            .disabled(random.nextBoolean())  // Randomly enable/disable relation
                            .build();

                    teacherClassroomList.add(relation);
                }
            }
        }

// Save all to DB in one go
        teacherClassroomRepository.saveAll(teacherClassroomList);

        List<TeacherCourse> teacherCourseList = new ArrayList<>();

        for (Teacher teacher : allTeachers) {
            for (Course course : courses) {

                // Randomly decide if the teacher teaches this course (e.g. 60% chance)
                if (random.nextDouble() < 0.6) {
                    TeacherCourseId id = new TeacherCourseId(teacher.getId(), course.getId());

                    TeacherCourse relation = TeacherCourse.builder()
                            .id(id)
                            .teacher(teacher)
                            .course(course)
                            .build();

                    teacherCourseList.add(relation);
                }
            }
        }

// Save to DB
        teacherCourseRepository.saveAll(teacherCourseList);
        System.out.println("Fake data inserted successfully!");
    }
}
