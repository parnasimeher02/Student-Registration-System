set serveroutput on

create or replace package proj2_procedures as
	procedure show_students(students_curs out sys_refcursor);
	procedure show_courses(courses_curs out sys_refcursor);
	procedure show_course_credit(course_credit_curs out sys_refcursor);
	procedure show_classes(classes_curs out sys_refcursor);
	procedure show_enrollments(enrollments_curs out sys_refcursor);
	procedure show_score_grade(score_grade_curs out sys_refcursor);
	procedure show_prereq(prereq_curs out sys_refcursor);
	procedure show_logs(logs_curs out sys_refcursor);
	procedure class_info(id in classes.classid%type,showmessage out VARCHAR2,class1_info out sys_refcursor);
	procedure course_info(deptcode in courses.dept_code%TYPE,course_no IN courses.course#%TYPE, showmessage out VARCHAR2, course1_info in out sys_refcursor);
	procedure enroll_student(std_id in students.B#%type,cl_id in classes.classid%type,showmessage OUT VARCHAR2);
	procedure drop_enrollment(std_id in students.B#%type,cl_id in classes.classid%type,showmessage OUT VARCHAR2);
	procedure delete_student(std_id in students.B#%type,showmessage OUT VARCHAR2);
	end;
/


create or replace package body proj2_procedures as
	procedure show_students(students_curs out sys_refcursor) as
		begin
			open students_curs for
			select * from students;
		end show_students;


	procedure show_courses(courses_curs out sys_refcursor) as
		begin
			open courses_curs for
			select * from courses;
		end show_courses;


	procedure show_course_credit(course_credit_curs out sys_refcursor) as
		begin
			open course_credit_curs for
			select * from course_credit;
		end show_course_credit;


	procedure show_classes(classes_curs out sys_refcursor) as
		begin
			open classes_curs for
			select * from classes;
		end show_classes;


	procedure show_enrollments(enrollments_curs out sys_refcursor) as
		begin
			open enrollments_curs for
			select * from g_enrollments;
		end show_enrollments;


	procedure show_score_grade(score_grade_curs out sys_refcursor) as
		begin
			open score_grade_curs for
			select * from score_grade;
		end show_score_grade;


	procedure show_prereq(prereq_curs out sys_refcursor) as
		begin
			open prereq_curs for
			select * from prerequisites;
		end show_prereq;


	procedure show_logs(logs_curs out sys_refcursor) as
		begin
			open logs_curs for
			select * from logs;
		end show_logs;

	procedure class_info(id in classes.classid%type,showmessage out VARCHAR2,class1_info out sys_refcursor)
		is
		class_exist number;
		begin
			select count(*) into class_exist from classes where classid=id;
			if class_exist = 0 then
				showmessage := 'The classid is invalid';
			else
				open class1_info for
				select B#,first_name,last_name from students, g_enrollments g, classes c where c.classid=id and B#=G_B# and c.classid=g.classid;
			end if;
		end class_info;
		
	procedure course_info(deptcode in courses.dept_code%TYPE,course_no IN courses.course#%TYPE, showmessage out VARCHAR2, course1_info in out sys_refcursor) is
		course_exist number;
		begin
			course_exist:=0;
			begin
				select course# into course_exist from courses where course#=course_no and dept_code=deptcode;
			exception
				when no_data_found then
					showmessage:=' does not exist.';
					return;
			end;
			
			begin
				open course1_info for with mainCourses(pre_dept_code, pre_course#, dept_code, course#) as
				(select pre_dept_code, pre_course#, dept_code, course# from prerequisites p where deptcode=dept_code and course#=course_no 
				union all
				select p.pre_dept_code, p.pre_course#, p.dept_code, p.course# from prerequisites p inner join mainCourses m on m.pre_dept_code=p.dept_code and m.pre_course#=p.course#)
				select pre_dept_code||pre_course# from mainCourses;
			end;
		end course_info;
		
	procedure enroll_student(std_id in students.B#%type,cl_id in classes.classid%type,showmessage OUT VARCHAR2) is
		student_exist number;
		student_graduate number;
		class_exist number;
		class_in_semester_exist number;
		class_full number;
		student_already_exists number;
		student_five_course number;
		student_pre_req_count number;
		begin
			select count(*) into student_exist from students where std_id=B#;
			select count(*) into student_graduate from students where std_id=B# and st_level in ('master', 'PhD');
			select count(*) into class_exist from classes where cl_id = classid;
			select count(*) into class_in_semester_exist from classes where cl_id = classid and semester='Spring' and year=2021;
			select count(*) into class_full from classes where class_size<limit and cl_id = classid;
			select count(*) into student_already_exists from g_enrollments where std_id=G_B# and classid=cl_id;
			select count(*) into student_five_course from g_enrollments g, classes c where std_id=G_B# and g.classid=c.classid and semester='Spring' and year=2021;
			SELECT count(lgrade) into student_pre_req_count FROM score_grade WHERE score IN (SELECT score FROM g_enrollments WHERE classid IN (SELECT classid FROM classes WHERE dept_code IN (SELECT pre_dept_code FROM prerequisites WHERE dept_code IN (SELECT dept_code FROM classes WHERE classid = cl_id) AND course# IN (SELECT course# FROM classes WHERE classid = cl_id)) AND course# IN (SELECT  pre_course# FROM prerequisites WHERE dept_code IN (SELECT dept_code FROM classes WHERE classid = cl_id) AND course# IN (SELECT course# FROM classes WHERE classid = cl_id))) AND g_B# = std_id) AND lgrade in ('A', 'A-', 'B+', 'B', 'B-', 'C+', 'C');
			
			if student_exist=0 then
				showmessage:='The B# is invalid.';
			elsif student_graduate=0 then
				showmessage:='This is not a graduate student.';
			elsif class_exist=0 then
				showmessage:='The classid is invalid.';
			elsif class_in_semester_exist=0 then
				showmessage:='Cannot enroll into a class from previous semester.';
			elsif class_full=0 then
				showmessage:='The class is already full.';
			elsif student_already_exists!=0 then
				showmessage:='The student is already in the class.';
			elsif student_five_course=5 then
				showmessage:='Students cannot be enrolled in more than five classes.';
			elsif student_pre_req_count=0 then
				showmessage:='Prerequisite not satisfied.';
			else
				showmessage := 'Successfully enrolled.';
				insert into g_enrollments values (std_id,cl_id,null);
			end if;
		end enroll_student;
		
	procedure drop_enrollment(std_id in students.B#%type,cl_id in classes.classid%type,showmessage OUT VARCHAR2) is
		student_exist number;
		student_graduate number;
		class_exist number;
		student_already_exists number;
		class_in_semester_exist number;
		student_only_course number;
		begin
			select count(*) into student_exist from students where std_id=B#;
			select count(*) into student_graduate from students where std_id=B# and st_level in ('master', 'PhD');
			select count(*) into class_exist from classes where cl_id = classid;
			select count(*) into student_already_exists from g_enrollments where std_id=G_B# and classid=cl_id;
			select count(*) into class_in_semester_exist from classes where cl_id = classid and semester='Spring' and year=2021;
			select count(*) into student_only_course from g_enrollments g, classes c where std_id=G_B# and g.classid=c.classid and semester='Spring' and year=2021;
			
			if student_exist=0 then
				showmessage:='The B# is invalid.';
			elsif student_graduate=0 then
				showmessage:='This is not a graduate student.';
			elsif class_exist=0 then
				showmessage:='The classid is invalid.';
			elsif student_already_exists=0 then
				showmessage:='The student is not enrolled in the class.';
			elsif class_in_semester_exist=0 then
				showmessage:='Only enrollment in the current semester can be dropped.';
			elsif student_only_course=1 then
				showmessage:='This is the only class for this student in Spring 2021 and cannot be dropped.';
			else
				showmessage := 'Successfully dropped.';
				delete from g_enrollments where std_id=G_B# and classid=cl_id;
			end if;
		end drop_enrollment;
		
	procedure delete_student(std_id in students.B#%type,showmessage OUT VARCHAR2) is
		student_exist number;
		begin
			select count(*) into student_exist from students where std_id=B#;
			
			if student_exist=0 then
				showmessage:='The B# is invalid.';
			else
				showmessage := 'Successfully deleted student.';
				delete from students where std_id=B#;
			end if;
		end delete_student;
	
end proj2_procedures;
/
show errors