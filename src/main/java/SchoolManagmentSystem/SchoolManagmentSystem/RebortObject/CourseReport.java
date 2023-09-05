package SchoolManagmentSystem.SchoolManagmentSystem.RebortObject;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CourseReport {
    String courseName;
    Double courseMark;
    String grade;
}
