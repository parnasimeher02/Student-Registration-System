package daahw;

import java.sql.*;
import java.util.Scanner;

import oracle.jdbc.*;
import java.math.*;
import java.io.*;
import java.awt.*;
import oracle.jdbc.pool.OracleDataSource;

public class StudentRegistration {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		try {
			
			//Connecting to Oracle server. Need to replace username and password by your username and your password.
			OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:acad111");
			Connection conn = ds.getConnection("pmeher", "Speed16");
			
			int choice=0;
			Scanner sc=new Scanner(System.in);
			//Menus for functionalities supported by Student Registration System
			while(choice!=7) {
				System.out.println("\nStudent Registration System");
				System.out.println("1.Printing tables in the database");
				System.out.println("2.Print all the students in any particular class");
				System.out.println("3.Print all the prerequisite courses for any particular course");
				System.out.println("4.Enroll Student into the class");
				System.out.println("5.Drop a graduate student from class");
				System.out.println("6.Delete a student");
				System.out.println("7.Exit");
				System.out.println("Please select one of the above options:");
				choice=sc.nextInt();
				String classId="";
				String deptCode="";
				int course=0;
				String b="";
				
				switch(choice) {
				
				case 1:
					int ch=0;
					while(ch!=10) {
						System.out.println("\nPrinting tables");
						System.out.println("1.Print all the tables in the database");
						System.out.println("2.Print Student Table");
						System.out.println("3.Print Courses Table");
						System.out.println("4.Print Course Credit Table");
						System.out.println("5.Print Classes Table");
						System.out.println("6.Print Enrollments Table");
						System.out.println("7.Print Score Grade Table");
						System.out.println("8.Print Prerequisites Table");
						System.out.println("9.Print Logs Table");
						System.out.println("10.Back to Main Menu");
						System.out.println("Please select one of the above options:");
						ch=sc.nextInt();
						switch(ch) {
						case 1:
							printTables(conn);
							break;
						case 2:
							printStudentTable(conn);
							break;
						case 3:
							printCoursesTable(conn);
							break;
						case 4:
							printCourseCreditTable(conn);
							break;
						case 5:
							printClassesTable(conn);
							break;
						case 6:
							printEnrollmentsTable(conn);
							break;
						case 7:
							printScoreGradeTable(conn);
							break;
						case 8:
							printPrerequisitesTable(conn);
							break;
						case 9:
							printLogsTable(conn);
							break;
						case 10:
							break;
						}
						
					}
					
					break;
					
				case 2:
					System.out.println("\n\nPlease enter classid:");
					try {
						classId=sc.next();
						printStudentsInClass(conn, classId);
						break;
					}
					catch(Exception e) {
						System.out.println("Please provide proper input for classid!!!!!!");
					}
					
				case 3:
					try {
						System.out.println("\n\nPlease enter dept_code:");
						deptCode=sc.next();
						System.out.println("Please enter course#:");
						course=sc.nextInt();
						printPreReqCourses(conn,deptCode, course);
						break;
					}
					catch(Exception e) {
						System.out.println("Please provide proper input for dept_code and course#!!!!!!");
					}
					
				case 4:
					try {
						System.out.println("\n\nPlease enter B#:");
						b=sc.next();
						System.out.println("Please enter classid:");
						classId=sc.next();
						enrollStudent(conn,b, classId);
						break;
					}
					catch(Exception e) {
						System.out.println("Please provide proper input for B# and classid!!!!!!");
					}
					
				case 5:
					try {
						System.out.println("\n\nPlease enter B#:");
						b=sc.next();
						System.out.println("Please enter classid:");
						classId=sc.next();
						dropEnrollment(conn,b, classId);
						break;
					}
					catch(Exception e) {
						System.out.println("Please provide proper input for B# and classid!!!!!!");
					}
					
				case 6:
					try {
						System.out.println("\n\nPlease enter B#:");
						b=sc.next();
						deleteStudent(conn,b);
						break;
					}
					catch(Exception e) {
						System.out.println("Please provide proper input for B# and classid!!!!!!");
					}	
					
				case 7:
					break;
				}
			}
			
			//closing the connection
			conn.close();
			
		}
		catch (SQLException ex) {
			System.out.println ("\nSQL Exception in main method:"+ex);
		}
		catch (Exception e) {
			System.out.println ("\nException:"+e);
		}

	}
	
	//Prints all the tables in the database
	public static void printTables(Connection conn) throws SQLException{
		
		try{
			
			printStudentTable(conn);
			printCoursesTable(conn);
			printCourseCreditTable(conn);
			printClassesTable(conn);
			printEnrollmentsTable(conn);
			printScoreGradeTable(conn);
			printPrerequisitesTable(conn);
			printLogsTable(conn);
 
		}
		catch(SQLException ex){
			System.out.println("SQL Exception in Print table function:"+ex);
		}
		catch(Exception e){
		    System.out.println("Exception:"+e);
		}
	}
	
	//Prints logs table
	public static void printLogsTable(Connection conn) throws SQLException{
		
		try {
			
			//Prepare to call stored procedure
			CallableStatement cs = conn.prepareCall("begin proj2_procedures.show_logs(?); end;");
			
			//register the out parameter (the first parameter)
			cs.registerOutParameter(1,OracleTypes.CURSOR);
			
			// execute and retrieve the result set
			cs.execute();
			
			ResultSet rs = (ResultSet)cs.getObject(1);
			System.out.println("\n\nLOGS TABLE");
			System.out.println("-------------------------------------------------------------------------------------------------------------");                                       
			System.out.printf("LOG#\tUSER_NAME\tOP_TIME\t\t\tTABLE_NAME\tOPERATION\tTUPLE_KEYVALUE\n");
			System.out.println("-------------------------------------------------------------------------------------------------------------");
			// print the results
			while(rs.next())
			{
			System.out.println(rs.getString(1)+"\t"+rs.getString(2)+"\t\t"+rs.getString(3)+"\t"+rs.getString(4)+"\t"+rs.getString(5)+"\t\t"+rs.getString(6));
			}
			//close the result set, statement
			cs.close();
			rs.close(); 
		}
		catch(SQLException ex){
			System.out.println("SQL Exception in Print table function:"+ex);
		}
		catch(Exception e){
		    System.out.println("Exception:"+e);
		}
		
	}
	
	//Printing Prerequisites table
	public static void printPrerequisitesTable(Connection conn) throws SQLException{
		
		try {
			
			//Prepare to call stored procedure
			CallableStatement cs = conn.prepareCall("begin proj2_procedures.show_prereq(?); end;");
			//register the out parameter (the first parameter)
			cs.registerOutParameter(1,OracleTypes.CURSOR);
			
			// execute and retrieve the result set
			cs.execute();
			
			ResultSet rs = (ResultSet)cs.getObject(1);
			System.out.println("\n\nPREREQUISITES TABLE");
			System.out.println("-------------------------------------------------------------------------------------------------------------");
			System.out.println("DEPT_CODE\tCOURSE#\t\tPRE_DEPT_CODE\tPRE_COURSE#");
			System.out.println("-------------------------------------------------------------------------------------------------------------");
			while (rs.next())
			{
			System.out.println(rs.getString(1)+"\t\t"+rs.getString(2)+"\t\t"+rs.getString(3)+"\t\t"+rs.getString(4));
			}
			cs.close();
			rs.close();
		}
		catch(SQLException ex){
			System.out.println("SQL Exception in Print table function:"+ex);
		}
		catch(Exception e){
		    System.out.println("Exception:"+e);
		}
	}
	
	//Printing score grade table
	public static void printScoreGradeTable(Connection conn) throws SQLException{
		
		try {
			CallableStatement cs = conn.prepareCall("begin proj2_procedures.show_score_grade(?); end;");
			cs.registerOutParameter(1,OracleTypes.CURSOR);
			// execute and retrieve the result set
			cs.execute();
			ResultSet rs = (ResultSet)cs.getObject(1);
			System.out.println("\n\nSCORE_GRADE TABLE");
			System.out.println("----------------------------------------------------------------------");
			System.out.println("SCORE\t\tLGRADE");
			System.out.println("----------------------------------------------------------------------");
			while (rs.next())
			{
			System.out.println(rs.getString (1)+"\t\t"+rs.getString(2));
			}
			cs.close();
			rs.close();
		}
		catch(SQLException ex){
			System.out.println("SQL Exception in Print table function:"+ex);
		}
		catch(Exception e){
		    System.out.println("Exception:"+e);
		}
	}
	
	//Printing Enrollments table
	public static void printEnrollmentsTable(Connection conn) throws SQLException{
		
		try {
			CallableStatement cs = conn.prepareCall("begin proj2_procedures.show_enrollments(?); end;");
			cs.registerOutParameter(1,OracleTypes.CURSOR);
			// execute and retrieve the result set
			cs.execute();
			ResultSet rs = (ResultSet)cs.getObject(1);
			System.out.println("\n\nG_ENROLLMENTS TABLE");
			System.out.println("----------------------------------------------------------------------");
			System.out.println("G_B#\t\t\tCLASSID\t\t\tSCORE");
			System.out.println("----------------------------------------------------------------------");
			while (rs.next())
			{
			System.out.println(rs.getString (1)+"\t\t"+rs.getString(2)+"\t\t\t"+rs.getString(3));
			}
			cs.close();
			rs.close();
		}
		catch(SQLException ex){
			System.out.println("SQL Exception in Print table function:"+ex);
		}
		catch(Exception e){
		    System.out.println("Exception:"+e);
		}
	}
	
	//Printing classes table
	public static void printClassesTable(Connection conn) throws SQLException{
		
		try {
			CallableStatement cs = conn.prepareCall("begin proj2_procedures.show_classes(?); end;");
			cs.registerOutParameter(1,OracleTypes.CURSOR);
			//Printing Classes table
			// execute and retrieve the result set
			cs.execute();
			ResultSet rs = (ResultSet)cs.getObject(1);
			System.out.println("\n\nCLASSES TABLE");
			System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("CLASSID\t\tDEPT_CODE\tCOURSE#\t\tSECT#\t\tYEAR\t\tSEMESTER\tLIMIT\t\tCLASS_SIZE\tROOM");
			System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
			while (rs.next())
			{
			System.out.println(rs.getString(1)+"\t\t"+rs.getString(2)+"\t\t"+rs.getString(3)+"\t\t"+rs.getString(4)+"\t\t"+rs.getString(5)+"\t\t"+rs.getString(6)+"\t\t"+rs.getString(7)+"\t\t"+rs.getString(8)+"\t\t"+rs.getString(9));
			}
			cs.close();
			rs.close();
		}
		catch(SQLException ex){
			System.out.println("SQL Exception in Print table function:"+ex);
		}
		catch(Exception e){
		    System.out.println("Exception:"+e);
		}
	}
	
	//Printing course credit table
	public static void printCourseCreditTable(Connection conn) throws SQLException{
		
		try {
			CallableStatement cs = conn.prepareCall("begin proj2_procedures.show_course_credit(?); end;");
			cs.registerOutParameter(1,OracleTypes.CURSOR);
			//Printing Course table
			// execute and retrieve the result set
			cs.execute();
			ResultSet rs = (ResultSet)cs.getObject(1);
			System.out.println("\n\nCOURSE_CREDIT TABLE");
			System.out.println("-----------------------------");
			System.out.println("COURSE#\t\tCREDITS");
			System.out.println("-----------------------------");
			while (rs.next())
			{
			System.out.println(rs.getString(1)+"\t\t"+rs.getString(2));
			}
			cs.close();
			rs.close();
		}
		catch(SQLException ex){
			System.out.println("SQL Exception in Print table function:"+ex);
		}
		catch(Exception e){
		    System.out.println("Exception:"+e);
		}
	}
	
	//Printing courses table
	public static void printCoursesTable(Connection conn) throws SQLException{
		
		try {
			CallableStatement cs = conn.prepareCall("begin proj2_procedures.show_courses(?); end;");
			cs.registerOutParameter(1,OracleTypes.CURSOR);
			//Printing Course table
			// execute and retrieve the result set
			cs.execute();
			ResultSet rs = (ResultSet)cs.getObject(1);
			System.out.println("\n\nCOURSES TABLE");
			System.out.println("----------------------------------------------------------");
			System.out.println("DEPT_CODE\t\tCOURSE#\t\tTITLE");
			System.out.println("----------------------------------------------------------");
			while (rs.next())
			{
			System.out.println(rs.getString(1)+"\t\t\t"+rs.getString(2)+"\t\t"+rs.getString(3));
			}
			cs.close();
			rs.close();
		}
		catch(SQLException ex){
			System.out.println("SQL Exception in Print table function:"+ex);
		}
		catch(Exception e){
		    System.out.println("Exception:"+e);
		}
		
	}
	
	//Printing student table
	public static void printStudentTable(Connection conn) throws SQLException{
		
		try{
			CallableStatement cs = conn.prepareCall("begin proj2_procedures.show_students(?); end;");
			cs.registerOutParameter(1,OracleTypes.CURSOR);
			//Printing Student table
			// execute and retrieve the result set
			cs.execute();
			ResultSet rs = (ResultSet)cs.getObject(1);
			System.out.println("\nSTUDENTS TABLE");
			System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("B#\t\t\tFIRST_NAME\t\tLAST_NAME\t\tST_LEVEL\t\tGPA\t\tEMAIL\t\t\tBDATE");
			System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			while (rs.next())
			{
			System.out.println(rs.getString(1)+"\t\t"+rs.getString(2)+"\t\t\t"+rs.getString(3)+"\t\t\t"+rs.getString(4)+"\t\t\t"+rs.getString(5)+"\t\t"+rs.getString(6)+"\t\t"+rs.getString(7));
			}
			cs.close();
			rs.close();
		}
		catch(SQLException ex){
			System.out.println("SQL Exception in Print table function:"+ex);
		}
		catch(Exception e){
		    System.out.println("Exception:"+e);
		}
	}
	
	//prints B#, first_name and last_name of students for the given classid
	public static void printStudentsInClass(Connection conn, String classId) {
		try {
			CallableStatement cs = conn.prepareCall("begin proj2_procedures.class_info(:1,:2,:3); end;");
			cs.setString(1,classId);
			cs.registerOutParameter(2,Types.VARCHAR);
			cs.registerOutParameter(3,OracleTypes.CURSOR);
			cs.execute();

			String showmessage = null;
			showmessage =  cs.getString(2);
			ResultSet rs = (ResultSet)cs.getObject(3);
			//if showmessage is null, records are returned else some issue has occurred
			if(showmessage == null) {
				System.out.println("\n\nSTUDENTS IN CLASS:"+classId);
				System.out.println("B#\t\tFIRST_NAME\tLAST_NAME");
				System.out.println("---------------------------------------------------------------------------------------------------------------------------------");
				while(rs.next()){	
					System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t\t" + rs.getString(3));
				}
				rs.close();
			}
			else{
				System.out.println(showmessage);
			}
			
			cs.close();
		}
		catch(SQLException ex){
			System.out.println("SQL Exception in printStudentsInClass function");
		}
		catch(Exception e){
			System.out.println("Exception in printStudentsInClass");
		}
	}
	
	//prints the dept_code and course# of prerequisites courses of the given dept_code and course#
	public static void printPreReqCourses(Connection conn, String deptCode, int course) throws SQLException{
		
		try {
			CallableStatement cs = conn.prepareCall("begin proj2_procedures.course_info(:1,:2,:3,:4); end;");
			cs.setString(1,deptCode);
			cs.setInt(2,course);
			cs.registerOutParameter(3,Types.VARCHAR);
			cs.registerOutParameter(4,OracleTypes.CURSOR);
			cs.execute();
			
			String showmessage = null;
			showmessage =  cs.getString(3);
			ResultSet rs = (ResultSet)cs.getObject(4);
			
			//if showmessage is null, records are returned else some issue has occurred
			if(showmessage==null) {
				System.out.println("PREREQUISITE COURSES");
				System.out.println("-----------------------------------------------------------------------------------------------");     
				while(rs.next()){	
					System.out.println(rs.getString(1));
				}
				rs.close();
			}
			else {
				System.out.println(deptCode+course+showmessage);
			}
			
			
			cs.close();
		}
		catch(SQLException ex){
			System.out.println("SQL Exception in printPreReqCourses function:"+ex);
		}
		catch(Exception e){
			System.out.println("Exception in printPreReqCourses");
		}
	}
	
	public static void enrollStudent(Connection conn,String b, String clId) throws SQLException{
		try {
			CallableStatement cs = conn.prepareCall("begin proj2_procedures.enroll_student(:1,:2,:3); end;");
			cs.setString(1,b);
			cs.setString(2,clId);
			cs.registerOutParameter(3,Types.VARCHAR);
			cs.execute();
			
			String showmessage = null;
			showmessage =  cs.getString(3);
			System.out.println(showmessage);		

			cs.close();
		}
		catch(SQLException ex){
			System.out.println("SQL Exception in enrollment function:"+ex);
		}
		catch(Exception e){
			System.out.println("Exception in enrollement");
		}
	}
	
	
	public static void dropEnrollment(Connection conn,String b, String clId) throws SQLException{
		try {
			CallableStatement cs = conn.prepareCall("begin proj2_procedures.drop_enrollment(:1,:2,:3); end;");
			cs.setString(1,b);
			cs.setString(2,clId);
			cs.registerOutParameter(3,Types.VARCHAR);
			cs.execute();
			
			String showmessage = null;
			showmessage =  cs.getString(3);
			System.out.println(showmessage);		

			cs.close();
		}
		catch(SQLException ex){
			System.out.println("SQL Exception in enrollment function");
		}
		catch(Exception e){
			System.out.println("Exception in enrollement");
		}
	}
	
	public static void deleteStudent(Connection conn,String b) throws SQLException{
		try {
			CallableStatement cs = conn.prepareCall("begin proj2_procedures.delete_Student(:1,:2); end;");
			cs.setString(1,b);
			cs.registerOutParameter(2,Types.VARCHAR);
			cs.execute();
			
			String showmessage = null;
			showmessage =  cs.getString(2);
			System.out.println(showmessage);		

			cs.close();
		}
		catch(SQLException ex){
			System.out.println("SQL Exception in enrollment function");
		}
		catch(Exception e){
			System.out.println("Exception in enrollement");
		}
	}

}
