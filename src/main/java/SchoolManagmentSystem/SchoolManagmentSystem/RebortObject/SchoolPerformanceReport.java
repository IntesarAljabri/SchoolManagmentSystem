package SchoolManagmentSystem.SchoolManagmentSystem.RebortObject;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SchoolPerformanceReport{
    private String schoolName;
    private double averageMark;
}
