package oel_ds;

import java.util.*;

class Course {
    String courseId;
    int capacity;
    int minYear;
    Set<String> prerequisites;
    Set<String> enrolledStudents;

    public Course(String courseId, int capacity, Set<String> prerequisites, int minYear) {
        this.courseId = courseId;
        this.capacity = capacity;
        this.minYear = minYear;
        this.prerequisites = prerequisites != null ? new LinkedHashSet<>(prerequisites) : new LinkedHashSet<>();
        this.enrolledStudents = new LinkedHashSet<>();
    }

    public boolean isEligible(Student student) {
        return student.year >= this.minYear && student.completedCourses.containsAll(this.prerequisites);
    }

    public boolean hasCapacity() {
        return this.enrolledStudents.size() < this.capacity;
    }
}

class Student {
    String studentId;
    int year;
    Set<String> completedCourses;
    Set<String> allocatedCourses;

    public Student(String studentId, int year, Set<String> completedCourses) {
        this.studentId = studentId;
        this.year = year;
        this.completedCourses = new LinkedHashSet<>(completedCourses);
        this.allocatedCourses = new LinkedHashSet<>();
    }
}

public class CourseAllocationSystem {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {

            Map<String, Set<String>> coursePrereqs = new LinkedHashMap<>();
            coursePrereqs.put("Programming Fundamental", Set.of());
            coursePrereqs.put("Functional English", Set.of());
            coursePrereqs.put("Calculus", Set.of());
            coursePrereqs.put("OOPs", Set.of("Programming Fundamental"));
            coursePrereqs.put("Communication Skill", Set.of("Functional English"));
            coursePrereqs.put("Multivariate Calculus", Set.of("Calculus"));
            coursePrereqs.put("DSA", Set.of("OOPs"));
            coursePrereqs.put("Academic and Professional Writing", Set.of("Communication Skill"));
            coursePrereqs.put("Probability", Set.of("Multivariate Calculus"));
            coursePrereqs.put("Computer Networking", Set.of("DSA"));
            coursePrereqs.put("Personal Development", Set.of("Academic and Professional Writing"));
            coursePrereqs.put("Machine Learning", Set.of("Probability"));

            Map<String, Integer> capacities = new LinkedHashMap<>();
            for (String course : coursePrereqs.keySet()) {
                capacities.put(course, 2); // default capacity
            }

            List<Course> courseList = new ArrayList<>();
            for (String cid : coursePrereqs.keySet()) {
                int cap = capacities.getOrDefault(cid, 2);
                courseList.add(new Course(cid, cap, coursePrereqs.get(cid), 1));
            }

            Map<String, Course> courseMap = new LinkedHashMap<>();
            for (Course c : courseList) {
                courseMap.put(c.courseId, c);
            }

            List<Student> studentList = new ArrayList<>();

            studentList.add(new Student("Shehzan", 3, new LinkedHashSet<>(Arrays.asList(
                    "Programming Fundamental", "Functional English", "Calculus", "OOPs",
                    "Communication Skill", "Multivariate Calculus", "DSA", "Academic and Professional Writing"))));

            studentList.add(new Student("Mahnoor", 2, new LinkedHashSet<>(Arrays.asList(
                    "Programming Fundamental", "Functional English", "Calculus"))));

            studentList.add(new Student("Yusra", 4, new LinkedHashSet<>(Arrays.asList(
                    "Programming Fundamental", "Functional English", "Calculus", "OOPs", "Communication Skill",
                    "Multivariate Calculus", "DSA", "Academic and Professional Writing", "Probability"))));

            studentList.add(new Student("Hanzala", 3, new LinkedHashSet<>(Arrays.asList(
                    "Programming Fundamental", "Functional English", "Calculus", "OOPs", "Multivariate Calculus",
                    "DSA", "Probability"))));

            studentList.add(new Student("Taiba", 2, new LinkedHashSet<>(Arrays.asList(
                    "Programming Fundamental", "Calculus"))));

            studentList.add(new Student("Faizan", 3, new LinkedHashSet<>(Arrays.asList(
                    "Programming Fundamental", "Functional English", "Calculus", "OOPs", "Communication Skill",
                    "DSA"))));

            studentList.add(new Student("Ayesha", 4, new LinkedHashSet<>(Arrays.asList(
                    "Programming Fundamental", "Functional English", "Calculus", "OOPs", "Communication Skill",
                    "Multivariate Calculus", "DSA", "Academic and Professional Writing", "Probability", "Computer Networking"))));

            studentList.add(new Student("Aqsa", 2, new LinkedHashSet<>(Arrays.asList(
                    "Functional English", "Communication Skill", "Academic and Professional Writing"))));

            studentList.add(new Student("Zohaib", 1, new LinkedHashSet<>()));

            while (true) {
                System.out.println("\nAvailable Students:");
                for (int i = 0; i < studentList.size(); i++) {
                    System.out.println((i + 1) + ". " + studentList.get(i).studentId);
                }
                System.out.println("0. Exit");

                System.out.print("Select a student by number (or 0 to exit): ");
                int choice = sc.nextInt();

                if (choice == 0) {
                    System.out.println("Exiting the program.");
                    break;
                }

                if (choice < 1 || choice > studentList.size()) {
                    System.out.println("Invalid student selection. Try again.");
                    continue;
                }

                Student selectedStudent = studentList.get(choice - 1);
                System.out.println("\nSelected Student: " + selectedStudent.studentId);
                System.out.println("Year: " + selectedStudent.year);
                System.out.println("Completed Courses: " + selectedStudent.completedCourses);

                while (true) {
                    List<Course> eligibleCourses = new ArrayList<>();
                    int courseIndex = 1;

                    System.out.println("\nEligible Courses to Enroll:");
                    for (Course c : courseList) {
                        if (!selectedStudent.completedCourses.contains(c.courseId)
                                && !selectedStudent.allocatedCourses.contains(c.courseId)) {
                            if (c.isEligible(selectedStudent)) {
                                if (c.hasCapacity()) {
                                    System.out.println(courseIndex + ". " + c.courseId + " (✅ Eligible, Capacity Left: "
                                            + (c.capacity - c.enrolledStudents.size()) + ")");
                                } else {
                                    System.out.println(courseIndex + ". " + c.courseId + " (⚠ Eligible, ❌ No Capacity)");
                                }
                                eligibleCourses.add(c);
                                courseIndex++;
                            }
                        }
                    }

                    if (eligibleCourses.isEmpty()) {
                        System.out.println("No eligible courses available or all have been allocated for this student.");
                        break;
                    }

                    System.out.print("Enter course number to allocate (or 0 to stop for this student): ");
                    int courseChoice = sc.nextInt();

                    if (courseChoice == 0) {
                        System.out.println("Finished allocating courses for " + selectedStudent.studentId);
                        break;
                    }

                    if (courseChoice > 0 && courseChoice <= eligibleCourses.size()) {
                        Course selectedCourse = eligibleCourses.get(courseChoice - 1);
                        if (selectedCourse.hasCapacity()) {
                            selectedCourse.enrolledStudents.add(selectedStudent.studentId);
                            selectedStudent.allocatedCourses.add(selectedCourse.courseId);
                            System.out.println("✅ Course '" + selectedCourse.courseId + "' allocated to " + selectedStudent.studentId);
                        } else {
                            System.out.println("❌ Cannot allocate. Capacity for course '" + selectedCourse.courseId + "' is full.");
                        }
                    } else {
                        System.out.println("Invalid course selection.");
                    }
                }
            }

            System.out.println("\n📋 Final Allocations Summary:");
            for (Student s : studentList) {
                System.out.println(s.studentId + " => " + s.allocatedCourses);
            }
        }
    }
}
