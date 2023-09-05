package SchoolManagmentSystem.SchoolManagmentSystem.RebortObject;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SchoolReport {

    String name;
    Long rollNumber;
    String schoolName;
}
