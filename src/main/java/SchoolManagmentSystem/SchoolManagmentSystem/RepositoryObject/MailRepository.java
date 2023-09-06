package SchoolManagmentSystem.SchoolManagmentSystem.RepositoryObject;

import SchoolManagmentSystem.SchoolManagmentSystem.Model.Email;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository {
    String sendEmail(Email email);
    String sendMailWithAttachment(Email details);
}
