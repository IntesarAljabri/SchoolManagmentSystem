package SchoolManagmentSystem.SchoolManagmentSystem.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    String recipient;
    String msgBody;
    String subject;
    String attachment;

}
