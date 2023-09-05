package SchoolManagmentSystem.SchoolManagmentSystem.RebortObject;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentSchoolReport {
    private String schoolName;
    private int totalStudents;

}
