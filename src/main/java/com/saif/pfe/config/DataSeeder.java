package com.saif.pfe.config;

import com.saif.pfe.controllers.NoteController;
import com.saif.pfe.controllers.StudentController;
import com.saif.pfe.controllers.TeacherController;
import com.saif.pfe.models.*;
import com.saif.pfe.models.embeddedId.CourseStudentId;
import com.saif.pfe.models.embeddedId.TeacherClassroomId;
import com.saif.pfe.models.embeddedId.TeacherCourseId;
import com.saif.pfe.models.ennum.ERole;
import com.saif.pfe.models.ennum.NoteType;
import com.saif.pfe.models.ennum.Status;
import com.saif.pfe.repository.*;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.LongStream;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ClassroomRepository classroomRepository;
    private final CourseRepository courseRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final PfeRepository pfeRepository;
    private final NoteController noteController;
    private final StudentController studentController;
    private final TeacherController teacherController;
    private final TeacherClassroomRepository teacherClassroomRepository;
    private final TeacherCourseRepository teacherCourseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SemesterRepository semesterRepository;
    private final PasswordEncoder encoder;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    public DataSeeder(
            ClassroomRepository classroomRepository,
            CourseRepository courseRepository,
            CourseStudentRepository courseStudentRepository,
            PfeRepository pfeRepository,
            NoteController noteController,
            StudentController studentController,
            TeacherController teacherController,
            TeacherClassroomRepository teacherClassroomRepository,
            TeacherCourseRepository teacherCourseRepository,
            StudentRepository studentRepository,
            TeacherRepository teacherRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            SemesterRepository semesterRepository,
            PasswordEncoder encoder
    ) {
        this.classroomRepository = classroomRepository;
        this.courseRepository = courseRepository;
        this.courseStudentRepository = courseStudentRepository;
        this.pfeRepository = pfeRepository;
        this.noteController = noteController;
        this.studentController = studentController;
        this.teacherController = teacherController;
        this.teacherClassroomRepository = teacherClassroomRepository;
        this.teacherCourseRepository = teacherCourseRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.semesterRepository = semesterRepository;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // 1) Seed roles if not exist
        if (roleRepository.count() == 0) {
            roleRepository.save(Role.builder().name(ERole.ROLE_ADMIN).build());
            roleRepository.save(Role.builder().name(ERole.ROLE_USER).build());
            roleRepository.save(Role.builder().name(ERole.ROLE_MODERATOR).build());
        }

        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN missing"));
        Role userRole  = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("ROLE_USER missing"));
        Role modRole   = roleRepository.findByName(ERole.ROLE_MODERATOR)
                .orElseThrow(() -> new IllegalStateException("ROLE_MODERATOR missing"));

        // 2) Seed admin, students, teachers if no users
        if (userRepository.count() == 0) {
            // Admin
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(encoder.encode("securePassword123"));
            admin.setRoles(Set.of(adminRole));
            admin.setUuid(UUID.randomUUID().toString());
            userRepository.save(admin);

            // Students
            for (int j = 0; j < 40; j++) {
                String username = "student" + j;
                if (username.length() > 20) username = username.substring(0, 20);
                String email = faker.internet().emailAddress();
                if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    email = "student" + j + "@example.com";
                }
                User userStudent = new User();
                userStudent.setUsername(username);
                userStudent.setEmail(email);
                userStudent.setPassword(encoder.encode("securePassword123"));
                userStudent.setRoles(Set.of(userRole));
                userStudent.setUuid(UUID.randomUUID().toString());

                Student student = new Student();
                student.setName(faker.name().fullName());
                student.setEmail(userStudent.getEmail());
                student.setPhone(faker.phoneNumber().cellPhone());
                student.setUser(userStudent);
                student.setUuid(userStudent.getUuid());

                studentController.createStudent(student);
            }

            // Teachers
            for (int j = 0; j < 10; j++) {
                String username = "teacher" + j;
                if (username.length() > 20) username = username.substring(0, 20);
                String email = faker.internet().emailAddress();
                if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    email = "teacher" + j + "@example.com";
                }
                User teacherUser = new User();
                teacherUser.setUsername(username);
                teacherUser.setEmail(email);
                teacherUser.setPassword(encoder.encode("securePassword123"));
                teacherUser.setRoles(Set.of(modRole));
                teacherUser.setUuid(UUID.randomUUID().toString());

                Teacher teacher = new Teacher();
                teacher.setName(faker.name().fullName());
                teacher.setEmail(teacherUser.getEmail());
                teacher.setPhone(faker.phoneNumber().cellPhone());
                teacher.setUser(teacherUser);

                teacherController.createTeacher(teacher);
            }

            System.out.println("Fake users inserted successfully!");
        }

        // 3) Seed classrooms
        List<Classroom> classes = new ArrayList<>();
        for (int i = 0; i < 7; i++) classes.add(new Classroom(null, "I" + i, random.nextInt(40)));
        for (int i = 0; i < 11; i++) classes.add(new Classroom(null, "K" + i, random.nextInt(40)));
        for (int i = 0; i < 5; i++) classes.add(new Classroom(null, "G" + i, random.nextInt(40)));
        classroomRepository.saveAll(classes);

        // 4) Seed semester
        List<Semester> semesters = semesterRepository.saveAll(
                List.of(new Semester(null, "2025", 2))
        );

        // 5) Seed courses
        List<Course> courses = courseRepository.saveAll(List.of(
                new Course(null, "Math", "Mathematics basics", 3, 30, 50, 20, List.of(NoteType.DS, NoteType.EXAM, NoteType.TP)),
                new Course(null, "Physics", "Physics introduction", 3, 30, 70, 0, List.of(NoteType.DS, NoteType.EXAM)),
                new Course(null, "Computer Science", "Intro to programming & algorithms", 4, 25, 50, 25, List.of(NoteType.DS, NoteType.EXAM, NoteType.TP)),
                new Course(null, "Chemistry", "Basic concepts of chemistry", 3, 20, 60, 20, List.of(NoteType.DS, NoteType.EXAM, NoteType.TP)),
                new Course(null, "Biology", "Foundations of biology", 2, 30, 50, 20, List.of(NoteType.DS, NoteType.EXAM, NoteType.TP)),
                new Course(null, "Literature", "World literature overview", 2, 50, 50, 0, List.of(NoteType.DS, NoteType.EXAM)),
                new Course(null, "History", "Modern world history", 1, 40, 60, 0, List.of(NoteType.DS, NoteType.EXAM)),
                new Course(null, "Geography", "Physical & human geography", 1, 30, 50, 20, List.of(NoteType.DS, NoteType.EXAM, NoteType.TP)),
                new Course(null, "English", "English language skills", 2, 20, 60, 20, List.of(NoteType.DS, NoteType.EXAM, NoteType.TP)),
                new Course(null, "Art", "Visual arts fundamentals", 1, 0, 100, 0, List.of(NoteType.EXAM))
        ));

        // 6) Load all students & teachers
        List<Student> allStudents = studentRepository.findAll();
        List<Teacher> allTeachers = teacherRepository.findAll();

        // 7) Seed notes
        for (Student student : allStudents) {
            for (Teacher teacher : allTeachers) {
                for (Course course : courses) {
                    Note note = new Note(
                            null,
                            random.nextFloat() * 20,
                            NoteType.values()[random.nextInt(NoteType.values().length)],
                            student,
                            course,
                            semesters.get(random.nextInt(semesters.size())),
                            teacher
                    );
                    noteController.createNote(note);
                }
            }
        }

        // 8) Seed PFEs
        List<Pfe> pfeList = new ArrayList<>();
        Set<Long> assigned = new HashSet<>();
        int totalStudents = allStudents.size();
        for (int i = 0; i < 20; i++) {
            Student one;
            do { one = allStudents.get(random.nextInt(totalStudents)); }
            while (assigned.contains(one.getId()));
            assigned.add(one.getId());

            Student two = null;
            if (i < 8) {
                do { two = allStudents.get(random.nextInt(totalStudents)); }
                while (assigned.contains(two.getId()));
                assigned.add(two.getId());
            }

            Teacher sup = random.nextBoolean() ? allTeachers.get(random.nextInt(allTeachers.size())) : null;
            Teacher pres = random.nextBoolean() ? allTeachers.get(random.nextInt(allTeachers.size())) : null;
            Teacher rap = random.nextBoolean() ? allTeachers.get(random.nextInt(allTeachers.size())) : null;

            pfeList.add(
                    Pfe.builder()
                            .name("PFE Project " + (i + 1))
                            .studentOne(one)
                            .studentTwo(two)
                            .supervisor(sup)
                            .president(pres)
                            .rapporteur(rap)
                            .guest(random.nextBoolean() ? "Guest " + faker.name().fullName() : null)
                            .date(LocalDate.now().plusDays(random.nextInt(90)))
                            .noteStudentOne(Math.round(random.nextDouble() * 20 * 100.0) / 100.0)
                            .noteStudentTwo(two != null ? Math.round(random.nextDouble() * 20 * 100.0) / 100.0 : 0)
                            .linkReport(faker.internet().url())
                            .linkPresentation(faker.internet().url())
                            .linkCertificate(faker.internet().url())
                            .information(faker.lorem().sentence())
                            .status(Status.values()[random.nextInt(Status.values().length)])
                            .build()
            );
        }
        pfeRepository.saveAll(pfeList);

        // 9) Seed CourseStudent relations
        List<CourseStudent> csList = new ArrayList<>();
        for (Student student : allStudents) {
            for (Course course : courses) {
                if (random.nextDouble() < 0.7) {
                    CourseStudent cs = CourseStudent.builder()
                            .id(new CourseStudentId(course.getId(), student.getId()))
                            .course(course)
                            .student(student)
                            .build();
                    csList.add(cs);
                }
            }
        }
        courseStudentRepository.saveAll(csList);

        // 10) Seed TeacherClassroom & TeacherCourse
        List<TeacherClassroom> tcList = new ArrayList<>();
        List<TeacherCourse> tCourseList = new ArrayList<>();
        for (Teacher teacher : allTeachers) {
            for (Classroom cls : classes) {
                if (random.nextBoolean()) {
                    tcList.add(TeacherClassroom.builder()
                            .id(new TeacherClassroomId(teacher.getId(), cls.getId()))
                            .teacher(teacher)
                            .classroom(cls)
                            .disabled(random.nextBoolean())
                            .build());
                }
            }
            for (Course course : courses) {
                if (random.nextDouble() < 0.6) {
                    tCourseList.add(TeacherCourse.builder()
                            .id(new TeacherCourseId(teacher.getId(), course.getId()))
                            .teacher(teacher)
                            .course(course)
                            .build());
                }
            }
        }
        teacherClassroomRepository.saveAll(tcList);
        teacherCourseRepository.saveAll(tCourseList);

        System.out.println("Fake data inserted successfully!");
    }
}
