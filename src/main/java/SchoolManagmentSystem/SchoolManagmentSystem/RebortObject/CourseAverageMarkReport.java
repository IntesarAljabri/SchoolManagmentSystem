package SchoolManagmentSystem.SchoolManagmentSystem.RebortObject;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CourseAverageMarkReport {
    String courseName;
    Double averageMark;
}
