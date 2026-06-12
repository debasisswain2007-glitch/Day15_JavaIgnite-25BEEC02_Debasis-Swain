/*
Classroom Attendance System

Teacher thread:

Takes attendance

Student thread:

Waits until attendance starts
Requirements
Students should not proceed immediately.
Students must wait.
Teacher gives signal.
Students continue after signal.

Expected Flow:

Student Waiting...

Teacher Started Attendance

Student Marked Present
Concepts Tested
wait()
notifyAll()


Instructions - 
Create Class Classroom

    Variable:
        attendanceStarted = false

    synchronized method waitForAttendance()

        While attendanceStarted is false

            Print:
                "Student Waiting..."

            wait()

        Print:
            "Student Marked Present"


    synchronized method startAttendance()

        attendanceStarted = true

        Print:
            "Teacher Started Attendance"

        notifyAll()


Create Class TeacherThread

    Classroom classroom

    run()

        classroom.startAttendance()


Create Class StudentThread

    Classroom classroom

    run()

        classroom.waitForAttendance()


Main Method

    Create Classroom object

    Create multiple Student threads

    Create Teacher thread

    Start Student threads

    Wait for a few seconds

    Start Teacher thread
Thread Coordination
Synchronization
*/
class Classroom {
    private boolean attendanceStarted = false;

    synchronized void waitForAttendance() {
        while (!attendanceStarted) {
            System.out.println(Thread.currentThread().getName() + ": Student Waiting...");
            try { wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        System.out.println(Thread.currentThread().getName() + ": Student Marked Present");
    }

    synchronized void startAttendance() {
        attendanceStarted = true;
        System.out.println("Teacher Started Attendance");
        notifyAll();
    }
}

class StudentThread extends Thread {
    private Classroom classroom;

    StudentThread(Classroom classroom, String name) {
        super(name);
        this.classroom = classroom;
    }

    public void run() {
        classroom.waitForAttendance();
    }
}

class TeacherThread extends Thread {
    private Classroom classroom;

    TeacherThread(Classroom classroom) {
        this.classroom = classroom;
    }

    public void run() {
        classroom.startAttendance();
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Classroom classroom = new Classroom();

        // Create and start multiple student threads
        StudentThread[] students = new StudentThread[3];
        for (int i = 0; i < 3; i++) {
            students[i] = new StudentThread(classroom, "Student-" + (i + 1));
            students[i].start();
        }

        Thread.sleep(2000);  // Students wait before teacher starts

        // Start teacher thread
        TeacherThread teacher = new TeacherThread(classroom);
        teacher.start();

        // Wait for all threads to complete
        for (StudentThread student : students) {
            student.join();
        }
        teacher.join();

        System.out.println("Attendance completed!");
    }
}
