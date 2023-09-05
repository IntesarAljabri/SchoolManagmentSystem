package SchoolManagmentSystem.SchoolManagmentSystem.RebortObject;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SchoolCoursePerformanceReport {
    private String schoolName;
    private String courseName;
    private double averageMark;

}
