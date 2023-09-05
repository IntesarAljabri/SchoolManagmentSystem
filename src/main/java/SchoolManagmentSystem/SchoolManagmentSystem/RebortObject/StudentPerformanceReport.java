package SchoolManagmentSystem.SchoolManagmentSystem.RebortObject;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentPerformanceReport {
    String name;
    Long rollNumber;
    Double averageMark;
}
