package SchoolManagmentSystem.SchoolManagmentSystem.Controller;

import SchoolManagmentSystem.SchoolManagmentSystem.RepostService.Report;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private Report report;

    @GetMapping("/schools")
    public ResponseEntity<String> generateSchoolReport() {
        try {
            String reportPath = report.generateReport();
            return ResponseEntity.ok(reportPath);
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating report.");
        }
    }


    @GetMapping("/courses")
    public ResponseEntity<String> generateCourseReport() {
        try {
            String reportPath = report.courseReport();
            return ResponseEntity.ok(reportPath);
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating report.");
        }
    }

    @GetMapping("/averageMarks")
    public ResponseEntity<String> generateAverageMarksReport() {
        try {
            String reportPath = report.averageMarksReport();
            return ResponseEntity.ok(reportPath);
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating report.");
        }
    }


    @GetMapping("/topPerformingStudents")
    public ResponseEntity<String> generateTopPerformingStudentsReport() {
        try {
            String reportPath = report.generateTopPerformingStudentReport();
            return ResponseEntity.ok(reportPath);
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating report.");
        }
    }

    @GetMapping("/studentPerformance")
    public ResponseEntity<String> generateStudentPerformanceReport() {
        try {
            String reportPath = report.generateStudentPerformanceReport();
            return ResponseEntity.ok(reportPath);
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating report.");
        }
    }
}
