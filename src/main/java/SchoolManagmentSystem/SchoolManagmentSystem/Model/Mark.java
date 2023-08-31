package SchoolManagmentSystem.SchoolManagmentSystem.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Data
@Table(name = "mark")
public class Mark {

    String grade;
}
