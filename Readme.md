Database Systems Project 2
Using PL/SQL and JDBC to Implement Student Registration System

Parnasi Chandrakant Meher
B01027641


Packages for the Project 2:
proj2_procedures:
 It includes the procedures created for the project




Procedures for Project 2:
proj2_procedures.sql

procedure show_students:
This procedure is used to show the students table from the database.

procedure show_courses:
This procedure is used to show the courses table from the database.

procedure show_course_credit:
This procedure is used to show the course credit table from the database.

procedure show_classes:
This procedure is used to show the classes table from the database.

procedure show_enrollments:
This procedure is used to show the g_enrollments table from the database.

procedure show_score_grade:
This procedure is used to show the score_grade table from the database.

procedure show_prereq:
This procedure is used to show the prerequisites table from the database.

procedure show_logs:
This procedure is used to show the logs table from the database.

procedure class_info:
This procedure takes classid as input and displays B#, first_name and last_name of students enrolled in the class as the output.

procedure course_info:
This procedure takes dept_code and course# as input and shows the dept_code, course_no as a concatenated string of the direct/indirect pre-requisite courses.

procedure enroll_student:
This procedure takes B# and classid as input and enrolls the student in that particular class, based on the conditions that are provided.
procedure drop_enrollment:
This procedure takes B# and classid as input and drops the student from that particular class, based on the conditions that are provided.

procedure delete_student:
This procedure takes B# as input and deletes the student from the student table, based on the conditions that are provided.


Trigger for Project2:
proj2_triggers.sql
TRIGGER g_enrollments_trigger_insert:
This trigger is created to increment the class_size by 1, whenever a student is enrolled in a class.

TRIGGER g_enrollments_trigger_delete:
This trigger is created to decrement the class_size by 1, whenever a student is dropped from a class.

TRIGGER student_trigger_delete:
This trigger is created to delete the student from enrollments.

TRIGGER logs_delete_student:
This trigger is created to insert an entry into the log table after a student is deleted from the students table.

TRIGGER logs_insert_enrollments:
This trigger is created to insert an entry into the log table after a student is a enrolled in a class.
TRIGGER logs_delete_enrollments:
This trigger is created to insert an entry into the log table after a student is dropped from a class.


Sequence for Project2:
proj2_sequence.sql 
It creates sequence “log_id” which is a 4 digit number that starts from 1000 and increments by 1


