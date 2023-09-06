package SchoolManagmentSystem.SchoolManagmentSystem.Controller;

import SchoolManagmentSystem.SchoolManagmentSystem.Model.Email;
import SchoolManagmentSystem.SchoolManagmentSystem.RepostService.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/school")
public class MailController {

    @Autowired
    EmailService emailService;

    @PostMapping("/msg")
    public String sendMail(@RequestBody Email email) {
        String status = emailService.sendEmail(email);
        return status;
    }



    @Scheduled(cron = "/30 * * * * *")
    public String sendMailWithAttachment(@RequestBody Email email) {
        String status = emailService.sendMailWithAttachment(email);
        return status;
    }
}
