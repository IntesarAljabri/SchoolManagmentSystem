package SchoolManagmentSystem.SchoolManagmentSystem.RepostService;

import SchoolManagmentSystem.SchoolManagmentSystem.Model.Course;
import SchoolManagmentSystem.SchoolManagmentSystem.Model.Mark;
import SchoolManagmentSystem.SchoolManagmentSystem.Model.School;
import SchoolManagmentSystem.SchoolManagmentSystem.Model.Student;
import SchoolManagmentSystem.SchoolManagmentSystem.RebortObject.*;
import SchoolManagmentSystem.SchoolManagmentSystem.RepositoryObject.CourseRepository;
import SchoolManagmentSystem.SchoolManagmentSystem.RepositoryObject.MarkRepository;
import SchoolManagmentSystem.SchoolManagmentSystem.RepositoryObject.SchoolRepository;
import SchoolManagmentSystem.SchoolManagmentSystem.RepositoryObject.StudentRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Report {

    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    MarkRepository markRepository;

    String pathToReports = "C:\\Users\\DELL\\Downloads\\SchoolManagmentSystem\\Report"; // Update the path


///////////////////////////////////////////////////////////////////////////////////////////////

    public String generateReport() throws FileNotFoundException, JRException {
        List<School> schoolList = schoolRepository.findAll();
        List<Student> studentList = studentRepository.findAll();

        List<SchoolReport> schoolsReportList = new ArrayList<>(); // New list to hold reports

        for (School s : schoolList) {
            for (Student student : studentList) {
                if (student.getSchool().getSchoolId().equals(s.getSchoolId())) {
                    SchoolReport schoolsReport = SchoolReport.builder()
                            .schoolName(s.getSchoolName())
                            .name(student.getName())
                            .rollNumber(student.getRollNumber())
                            .build();
                    schoolsReportList.add(schoolsReport); // Add report to the list
                }
            }
        }
        File file = ResourceUtils.getFile("classpath:SchoolReport1.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

// Create a data source from the reports list
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(schoolsReportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Entesar");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, pathToReports + "\\SchoolReport.pdf");
        return "Report generated: " + pathToReports + "\\SchoolReport.pdf";

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public String courseReport() throws FileNotFoundException, JRException {
        List<Course> courseList = courseRepository.findAll();
        List<Mark> markList = markRepository.findAll();

        List<CourseReport> coursesReportList = new ArrayList<>(); // New list to hold reports

        for (Course c : courseList) {
            for (Mark mark : markList) {
                if (mark.getCourse().getCourseId().equals(c.getCourseId())) {
                    CourseReport coursesReport = CourseReport.builder()
                            .courseName(c.getCourseName())
                            .courseMark(mark.getCourseMark())
                            .grade(mark.getGrade())
                            .build();
                    coursesReportList.add(coursesReport); // Add report to the list
                }
            }
        }

        File file = ResourceUtils.getFile("classpath:CourseReport.jrxml");
        InputStream resourceStream = getClass().getResourceAsStream("/CoursesReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Create a data source from the reports list
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(coursesReportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Entesar");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, pathToReports + "\\CourseReport.pdf");
        return "Report generated: " + pathToReports + "\\CourseReport.pdf";
    }
////////////////////////////////////////////////////////////////////////////////////////////

    /* Average Marks Report ------------------------------------------------------------- */
    public String averageMarksReport() throws FileNotFoundException, JRException {
        List<Course> courseList = courseRepository.findAll();
        List<Mark> markList = markRepository.findAll();

        Map<String, List<Double>> courseMarksMap = new HashMap<>();

        for (Mark mark : markList) {
            String courseName = mark.getCourse().getCourseName();
            courseMarksMap.computeIfAbsent(courseName, k -> new ArrayList<>()).add(mark.getCourseMark());
        }

        List<CourseAverageMarkReport> courseAverageReportList = new ArrayList<>(); // New list to hold reports

        for (Map.Entry<String, List<Double>> entry : courseMarksMap.entrySet()) {
            String courseName = entry.getKey();
            List<Double> marksForCourse = entry.getValue();

            if (!marksForCourse.isEmpty()) {
                Double sumMarks = marksForCourse.stream().mapToDouble(Double::doubleValue).sum();
                Double averageMark = sumMarks / marksForCourse.size();
                CourseAverageMarkReport courseAverageMarkReport = CourseAverageMarkReport.builder()
                        .courseName(courseName)
                        .averageMark(averageMark)
                        .build();
                courseAverageReportList.add(courseAverageMarkReport); // Add report to the list
            }
        }

        File file = ResourceUtils.getFile("classpath:CourseAverageMarkReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Create a data source from the reports list
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(courseAverageReportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Entesar");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, pathToReports + "\\CourseAverageMarksReport.pdf");
        return "Report generated: " + pathToReports + "\\CourseAverageMarksReport.pdf";
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    public String generateTopPerformingStudentReport() throws FileNotFoundException, JRException {
        List<School> schoolList = schoolRepository.findAll();
        List<Mark> markList = markRepository.findAll();

        List<TopPerformingStudentsReport> topPerformingStudentsReportList = new ArrayList<>();

        for (School school : schoolList) {
            Mark topMark = null;
            for (Mark mark : markList) {
                if (mark.getStudent().getSchool().getSchoolId().equals(school.getSchoolId())) {
                    if (topMark == null || mark.getCourseMark() > topMark.getCourseMark()) {
                        topMark = mark;
                    }
                }
            }

            if (topMark != null) {
                Student topStudent = topMark.getStudent();
                TopPerformingStudentsReport report = TopPerformingStudentsReport.builder()
                        .schoolName(school.getSchoolName())
                        .name(topStudent.getName())
                        .rollNumber(topStudent.getRollNumber())
                        .build();
                topPerformingStudentsReportList.add(report);
            }
        }

        File file = ResourceUtils.getFile("classpath:TopPerformingStudentsReport.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Create a data source from the reports list
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(topPerformingStudentsReportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Entesar");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, pathToReports + "\\TopPerformingStudentsReport.pdf");
        return "Report generated: " + pathToReports + "\\TopPerformingStudentsReport.pdf";
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////

    public String generateStudentPerformanceReport() throws FileNotFoundException, JRException {
        List<Student> studentList = studentRepository.findAll();
        List<Mark> markList = markRepository.findAll();

        Map<Long, List<Mark>> studentMarksMap = new HashMap<>();

        for (Mark mark : markList) {
            Long studentId = Long.valueOf(mark.getStudent().getStudentId());
            List<Mark> marksForStudent = studentMarksMap.get(studentId);

            if (marksForStudent == null) {
                marksForStudent = new ArrayList<>();
                studentMarksMap.put(studentId, marksForStudent);
            }

            marksForStudent.add(mark);
        }

        List<StudentPerformanceReport> studentPerformanceReportList = new ArrayList<>(); // New list to hold reports

        for (Student student : studentList) {
            Long studentId = Long.valueOf(student.getStudentId());
            List<Mark> marksForStudent = studentMarksMap.get(studentId);

            if (marksForStudent != null && !marksForStudent.isEmpty()) {
                Double totalMark = 0.0;
                int markCount = 0;

                for (Mark mark : marksForStudent) {
                    totalMark += mark.getCourseMark();
                    markCount++;
                }

                Double averageMark = totalMark / markCount;

                StudentPerformanceReport report = StudentPerformanceReport.builder()
                        .name(student.getName())
                        .rollNumber(student.getRollNumber())
                        .averageMark(averageMark)
                        .build();
                studentPerformanceReportList.add(report);
            }
        }


        File file = ResourceUtils.getFile("classpath:StudentPerformanceReport.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Create a data source from the reports list
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(studentPerformanceReportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Entesar");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, pathToReports + "\\StudentPerformanceReport.pdf");
        return "Report generated: " + pathToReports + "\\StudentPerformanceReport.pdf";
    }



  }

