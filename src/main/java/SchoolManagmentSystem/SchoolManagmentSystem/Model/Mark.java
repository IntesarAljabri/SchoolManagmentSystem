package SchoolManagmentSystem.SchoolManagmentSystem.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Data
@Table(name = "mark")
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double courseMark;
    private String grade;

    @ManyToOne
    @JoinColumn(name = "student-id")
    Student student;

    @ManyToOne
    @JoinColumn(name = "course-id")
    Course course;
}
