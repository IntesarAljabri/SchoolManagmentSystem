package SchoolManagmentSystem.SchoolManagmentSystem.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Data
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer courseId;
    String courseName;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Mark> marks;

    @ManyToOne
    @JoinColumn(name = "schoolId")
    private School school;
}
