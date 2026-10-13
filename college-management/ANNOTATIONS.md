# Homework 1 - Spring Data JPA annotations seen so far

## Entity and mapping to table
| Annotation | Purpose |
|---|---|
| `@Entity` | Marks a class as a JPA entity (a table) |
| `@Table(name="...")` | Custom table name |
| `@Id` | Primary key field |
| `@GeneratedValue(strategy=...)` | Auto-generate the id (`IDENTITY`, `SEQUENCE`, `AUTO`, `TABLE`) |
| `@Column(name, nullable, unique, length)` | Customize a column |
| `@Transient` | Field is not saved to DB |
| `@Enumerated(EnumType.STRING)` | Store enum as text instead of ordinal |
| `@Temporal` | Date/time mapping (older Date types) |
| `@Embedded` / `@Embeddable` | Reuse a value object's fields inside an entity |

## Relationships
| Annotation | Purpose |
|---|---|
| `@OneToOne` | One-to-one link |
| `@OneToMany` | One-to-many link |
| `@ManyToOne` | Many-to-one link (owns the FK) |
| `@ManyToMany` | Many-to-many link |
| `mappedBy` | Marks the inverse (non-owning) side |
| `@JoinColumn` | FK column name on the owning side |
| `@JoinTable` | Join table for many-to-many |
| `cascade = CascadeType.*` | Propagate persist / merge / remove to related entities |
| `fetch = FetchType.LAZY / EAGER` | When related data loads |
| `orphanRemoval = true` | Delete children removed from the parent collection |

## Repository / queries
| Annotation | Purpose |
|---|---|
| `@Repository` | Marks a data-access bean (optional on Spring Data interfaces) |
| `@Query` | Custom JPQL or native query |
| `@Param` | Named parameter in `@Query` |
| `@Modifying` | Update/delete queries |
| `@Transactional` | Run in a transaction |

## Used in this project (beyond JPA)
`@SpringBootApplication`, `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@PathVariable`, `@RequestParam`, `@RequestBody`, `@ResponseStatus`, `@Service`, `@JsonIgnoreProperties`.
