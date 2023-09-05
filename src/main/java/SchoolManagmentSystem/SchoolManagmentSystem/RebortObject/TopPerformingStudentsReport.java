package SchoolManagmentSystem.SchoolManagmentSystem.RebortObject;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TopPerformingStudentsReport {
    String schoolName;
    String name;
    Long rollNumber;
}
