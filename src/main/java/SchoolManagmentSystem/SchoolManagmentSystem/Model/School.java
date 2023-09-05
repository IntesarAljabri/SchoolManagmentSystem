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
@Table(name = "school")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer schoolId;
    String schoolName;


    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
    List<Student> students;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
    List<Course> courses;

}
