# College Management System (Spring Data JPA)

Spring Boot 3.3 + Spring Data JPA + H2 (in-memory). Implements the ER diagram from the homework.

## Relationships (from the diagram)

| Relationship | Type | Owning side (holds FK / join table) |
|---|---|---|
| Professor -> Subject | One-to-Many / Many-to-One | `Subject.professor` (column `subject.professor_id`) |
| Professor <-> Student | Many-to-Many | `Student.professors` (table `student_professor`) |
| Student <-> Subject | Many-to-Many | `Student.subjects` (table `student_subject`) |
| Student <-> AdmissionRecord | One-to-One | `AdmissionRecord.student` (unique `student_id`) |

Note: the diagram names the Student field `professor` (a `List<Professor>`); it is called `professors` in code because it is a list.

## Run it

Requirements: JDK 17+ and Maven 3.9+ (or open the folder in IntelliJ / VS Code / Eclipse as a Maven project).

```bash
cd college-management
mvn spring-boot:run
```

The app starts on http://localhost:8080. Stop with Ctrl+C. The DB is in-memory, so data resets each restart.

## See the data and tables

* H2 console (browser): http://localhost:8080/h2-console
  JDBC URL `jdbc:h2:mem:collegedb`, user `sa`, empty password. Run `SELECT * FROM student_subject;` etc.
* SQL Hibernate runs is printed in the terminal (`spring.jpa.show-sql=true`).
* Any GET endpoint below can be opened directly in the browser.

## API reference

### CRUD
| Method | URL | Body |
|---|---|---|
| POST/GET | `/api/professors` | `{"title":"Dr. Rao"}` (GET supports `?title=`) |
| GET/PUT/DELETE | `/api/professors/{id}` | PUT: `{"title":"..."}` |
| POST/GET | `/api/students` | `{"name":"Asha"}` (GET supports `?name=`) |
| GET/PUT/DELETE | `/api/students/{id}` | PUT: `{"name":"..."}` |
| POST/GET | `/api/subjects` | `{"title":"Maths"}` (GET supports `?professorId=`) |
| GET/PUT/DELETE | `/api/subjects/{id}` | PUT: `{"title":"..."}` |
| GET | `/api/admission-records`, `/api/admission-records/{id}`, `/api/admission-records/by-student/{studentId}` | |
| PUT/DELETE | `/api/admission-records/{id}` | PUT: `{"fees":60000}` |

### Mapping (assignment) APIs
| Method | URL | What it does |
|---|---|---|
| POST | `/api/professors/{pid}/subjects/{sid}` | Professor teaches Subject (one-to-many) |
| DELETE | `/api/professors/{pid}/subjects/{sid}` | Remove that link |
| POST | `/api/students/{stid}/subjects/{sid}` | Enroll Student in Subject (many-to-many) |
| DELETE | `/api/students/{stid}/subjects/{sid}` | Drop Student from Subject |
| POST | `/api/students/{stid}/professors/{pid}` | Assign Professor to Student (many-to-many) |
| DELETE | `/api/students/{stid}/professors/{pid}` | Remove that link |
| POST | `/api/students/{stid}/admission-record` | Body `{"fees":50000}` creates the one-to-one record |

## Try the whole flow (curl)

```bash
H='Content-Type: application/json'
curl -X POST localhost:8080/api/professors -H "$H" -d '{"title":"Dr. Rao"}'
curl -X POST localhost:8080/api/professors -H "$H" -d '{"title":"Prof. Iyer"}'
curl -X POST localhost:8080/api/subjects   -H "$H" -d '{"title":"Maths"}'
curl -X POST localhost:8080/api/subjects   -H "$H" -d '{"title":"Physics"}'
curl -X POST localhost:8080/api/students   -H "$H" -d '{"name":"Asha"}'
curl -X POST localhost:8080/api/students   -H "$H" -d '{"name":"Ravi"}'

curl -X POST localhost:8080/api/professors/1/subjects/1     # Dr. Rao teaches Maths
curl -X POST localhost:8080/api/professors/1/subjects/2     # Dr. Rao teaches Physics
curl -X POST localhost:8080/api/students/1/subjects/1       # Asha -> Maths
curl -X POST localhost:8080/api/students/2/subjects/1       # Ravi -> Maths
curl -X POST localhost:8080/api/students/1/professors/1     # Asha -> Dr. Rao
curl -X POST localhost:8080/api/students/1/professors/2     # Asha -> Prof. Iyer
curl -X POST localhost:8080/api/students/1/admission-record -H "$H" -d '{"fees":50000}'

curl localhost:8080/api/professors
curl localhost:8080/api/students
curl localhost:8080/api/subjects
curl localhost:8080/api/admission-records
```

`requests.http` has the same calls for IntelliJ / VS Code (REST Client).

## Project structure

```
src/main/java/com/college
  CollegeManagementApplication.java
  entity/       Professor, Student, Subject, AdmissionRecord   (the mappings)
  repository/   one Spring Data JPA repository per entity
  service/      MappingService (assign / unassign / safe deletes, @Transactional)
  controller/   CRUD controllers + AssignmentController (mapping APIs)
src/main/resources/application.properties
```

## Concepts to remember

* **Owning side**: the side without `mappedBy`. Only changes on the owning side are written to the DB, so the service updates both sides.
* `@JsonIgnoreProperties` stops infinite JSON loops (Professor -> Subject -> Professor ...).
* Switching to MySQL/PostgreSQL: replace the H2 dependency and the `spring.datasource.*` properties.
