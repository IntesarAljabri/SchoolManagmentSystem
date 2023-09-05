package SchoolManagmentSystem.SchoolManagmentSystem.RepostService;

import SchoolManagmentSystem.SchoolManagmentSystem.Model.Course;
import SchoolManagmentSystem.SchoolManagmentSystem.Model.Mark;
import SchoolManagmentSystem.SchoolManagmentSystem.Model.School;
import SchoolManagmentSystem.SchoolManagmentSystem.Model.Student;
import SchoolManagmentSystem.SchoolManagmentSystem.RebortObject.*;
import SchoolManagmentSystem.SchoolManagmentSystem.RepositoryObject.*;
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

///////////////////////////////////////////////////////////////////////////////////////////////////////

    public String StudentSchoolReport() throws FileNotFoundException, JRException {
        List<School> schoolList = schoolRepository.findAll();
        List<Student> studentList = studentRepository.findAll();

        Map<Long, Integer> schoolStudentCountMap = new HashMap<>();

        for (School school : schoolList) {
            int studentCount = 0;
            for (Student student : studentList) {
                if (student.getSchool().getSchoolId().equals(school.getSchoolId())) {
                    studentCount++;
                }
            }
            schoolStudentCountMap.put(Long.valueOf(school.getSchoolId()), studentCount);
        }

        List<StudentSchoolReport> schoolStudentsReportList = new ArrayList<>();

        for (School school : schoolList) {
            Integer studentCount = schoolStudentCountMap.get(school.getSchoolId());
            if (studentCount != null) {


                StudentSchoolReport report = StudentSchoolReport.builder()
                        .schoolName(school.getSchoolName())
                        .totalStudents(studentCount)
                        .build();
                schoolStudentsReportList.add(report);
            }
        }

        File file = ResourceUtils.getFile("classpath:SchoolStudentsReport.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(schoolStudentsReportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Entesar");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, pathToReports + "\\SchoolStudentsReport.pdf");
        return "Report generated: " + pathToReports + "\\SchoolStudentsReport.pdf";
    }

/////////////////////////////////////////////////////////////////////////////////////////////////

    public String generateSchoolCoursePerformanceReport() throws FileNotFoundException, JRException {
        List<School> schoolList = schoolRepository.findAll();
        List<Course> courseList = courseRepository.findAll();
        List<Mark> markList = markRepository.findAll();

        Map<Long, Map<Long, List<Double>>> schoolCourseMarksMap = new HashMap<>();

        // Iterate through the marks
        for (Mark mark : markList) {
            Long schoolId = Long.valueOf(mark.getStudent().getSchool().getSchoolId());
            Long courseId = Long.valueOf(mark.getCourse().getCourseId());
            Double courseMark = mark.getCourseMark();

            // Get or create the school's map
            Map<Long, List<Double>> schoolMap = schoolCourseMarksMap.computeIfAbsent(schoolId, k -> new HashMap<>());

            // Get or create the course's list of marks
            List<Double> courseMarks = schoolMap.computeIfAbsent(courseId, k -> new ArrayList<>());

            // Add the course mark to the list
            courseMarks.add(courseMark);
        }

        File file = ResourceUtils.getFile("classpath:SchoolCoursePerformanceReport.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        List<SchoolCoursePerformanceReport> schoolCoursePerformanceReportList = new ArrayList<>();

        // Iterate through the schools
        for (School school : schoolList) {
            // Get the map of course marks for the school
            Map<Long, List<Double>> courseMarksMap = schoolCourseMarksMap.get(school.getSchoolId());

            // Check if the map is not null
            if (courseMarksMap != null) {
                double highestAverageMark = 0.0;
                Course highestAverageMarkCourse = null;

                // Iterate through the courses
                for (Course course : courseList) {
                    // Get the list of marks for the course
                    List<Double> marksForCourse = courseMarksMap.get(course.getCourseId());

                    // Check if the list is not null and not empty
                    if (marksForCourse != null && !marksForCourse.isEmpty()) {
                        double totalMarks = 0.0;
                        int numMarks = 0;

                        // Calculate the total marks and count the number of marks
                        for (Double mark : marksForCourse) {
                            totalMarks += mark;
                            numMarks++;
                        }

                        // Calculate the average mark
                        double averageMark;

                        if (numMarks > 0) {
                            averageMark = totalMarks / numMarks;
                        } else {
                            averageMark = 0.0;
                        }

                        // Check if this course has a higher average mark
                        if (averageMark > highestAverageMark) {
                            highestAverageMark = averageMark;
                            highestAverageMarkCourse = course;
                        }
                    }
                }

                // Create a performance report for the course with the highest average mark
                if (highestAverageMarkCourse != null) {
                    SchoolCoursePerformanceReport report = SchoolCoursePerformanceReport.builder()
                            .schoolName(school.getSchoolName())
                            .courseName(highestAverageMarkCourse.getCourseName())
                            .averageMark(highestAverageMark)
                            .build();
                    schoolCoursePerformanceReportList.add(report);
                }
            }
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(schoolCoursePerformanceReportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Entesar");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, pathToReports + "\\SchoolCoursePerformanceReport.pdf");
        return "Report generated: " + pathToReports + "\\SchoolCoursePerformanceReport.pdf";
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    public String generateSchoolPerformanceReport() throws FileNotFoundException, JRException {
        List<School> schoolList = schoolRepository.findAll();
        List<SchoolPerformanceReport> schoolPerformanceReportList = new ArrayList<>();

        for (School school : schoolList) {
            List<Student> studentsInSchool = school.getStudents(); // Assuming you have a relationship set up in the School entity

            if (!studentsInSchool.isEmpty()) {
                double totalMarks = 0.0;
                int totalStudents = studentsInSchool.size();

                for (Student student : studentsInSchool) {
                    for (Mark mark : student.getMarks()) {
                        totalMarks += mark.getCourseMark();
                    }
                }

                double averageMark = (totalStudents > 0) ? (totalMarks / totalStudents) : 0.0;

                SchoolPerformanceReport schoolPerformanceReport = SchoolPerformanceReport.builder()
                        .schoolName(school.getSchoolName())
                        .averageMark(averageMark)
                        .build();

                schoolPerformanceReportList.add(schoolPerformanceReport);
            }
        }

        File file = ResourceUtils.getFile("classpath:SchoolPerformanceReport1.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(schoolPerformanceReportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Entesar");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, pathToReports + "\\SchoolPerformanceReport.pdf");

        return "Report generated: " + pathToReports + "\\SchoolPerformanceReport.pdf";
    }
}