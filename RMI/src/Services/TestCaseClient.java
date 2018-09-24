package Services;
/**
 * This class has the test case scenarios for the system.
 */
import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import Servers.IRMIInterface;
import Servers.MTLServer;
import Services.LogEvent;

public class TestCaseClient extends Thread{


	private final String sName;
	private final String managerID;
	private final int sPort;
	
	public TestCaseClient(String server, String mgrID, int serverP){
		sName = server;
		managerID = mgrID;
		sPort = serverP;
	}
	
	public static void main(String args[]) {
		
		TestCaseClient d1 = new TestCaseClient("DDO", "DDO1001", 8760);
		TestCaseClient d2 = new TestCaseClient("DDO", "DDO1002", 8760);
		TestCaseClient d3 = new TestCaseClient("DDO", "DDO1003", 8760);
		TestCaseClient d4 = new TestCaseClient("DDO", "DDO1004", 8760);
		TestCaseClient d5 = new TestCaseClient("DDO", "DDO1005", 8760);
		TestCaseClient d6 = new TestCaseClient("DDO", "DDO1006", 8760);
		TestCaseClient d7 = new TestCaseClient("DDO", "DDO1007", 8760);
		TestCaseClient d8 = new TestCaseClient("DDO", "DDO1008", 8760);
		
		TestCaseClient m1 = new TestCaseClient("MTL", "MTL1009", 8758);
		TestCaseClient m2 = new TestCaseClient("MTL", "MTL10010", 8758);
		TestCaseClient m3 = new TestCaseClient("MTL", "MTL10011", 8758);
		TestCaseClient m4 = new TestCaseClient("MTL", "MTL10012", 8758);
		TestCaseClient m5 = new TestCaseClient("MTL", "MTL10013", 8758);
		TestCaseClient m6 = new TestCaseClient("MTL", "MTL10014", 8758);
		TestCaseClient m7 = new TestCaseClient("MTL", "MTL10015", 8758);
		TestCaseClient m8 = new TestCaseClient("MTL", "MTL10016", 8758);
		
		TestCaseClient l1 = new TestCaseClient("LVL", "LVL1017", 8759);
		TestCaseClient l2 = new TestCaseClient("LVL", "LVL1018", 8759);
		TestCaseClient l3 = new TestCaseClient("LVL", "LVL1019", 8759);
		TestCaseClient l4 = new TestCaseClient("LVL", "LVL1020", 8759);
		TestCaseClient l5 = new TestCaseClient("LVL", "LVL1021", 8759);
		TestCaseClient l6 = new TestCaseClient("LVL", "LVL1022", 8759);
		TestCaseClient l7 = new TestCaseClient("LVL", "LVL1023", 8759);
		TestCaseClient l8 = new TestCaseClient("LVL", "LVL1024", 8759);
		
		
		Thread td1 = new Thread (d1);
		Thread td2 = new Thread (d2);
		Thread td3 = new Thread (d3);
		Thread td4 = new Thread (d4);
		Thread td5 = new Thread (d5);
		Thread td6 = new Thread (d6);
		Thread td7 = new Thread (d7);
		Thread td8 = new Thread (d8);
		
		
		Thread tm1 = new Thread (m1);
		Thread tm2 = new Thread (m2);
		Thread tm3 = new Thread (m3);
		Thread tm4 = new Thread (m4);
		Thread tm5 = new Thread (m5);
		Thread tm6 = new Thread (m6);
		Thread tm7 = new Thread (m7);
		Thread tm8 = new Thread (m8);
		
		
		Thread tl1 = new Thread (l1);
		Thread tl2 = new Thread (l2);
		Thread tl3 = new Thread (l3);
		Thread tl4 = new Thread (l4);
		Thread tl5 = new Thread (l5);
		Thread tl6 = new Thread (l6);
		Thread tl7 = new Thread (l7);
		Thread tl8 = new Thread (l8);
		
		
		
		td1.start();
		td2.start();
		td3.start();
		td4.start();
		td5.start();
		td6.start();
		td7.start();
		td8.start();
		
		
		tm1.start();
		tm2.start();
		tm3.start();
		tm4.start();
		tm5.start();
		tm6.start();
		tm7.start();
		tm8.start();
		
		
		
		tl1.start();
		tl2.start();
		tl3.start();
		tl4.start();
		tl5.start();
		tl6.start();
		tl7.start();
		tl8.start();
		
						
	}

	public void run() {
		IRMIInterface startServer = null;

		try {
			startServer = (IRMIInterface) Naming.lookup("rmi://localhost:" + sPort + "/" + sName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		boolean isSignedIn;
		try {

			isSignedIn = startServer.signIn(managerID);
			LogEvent message1 = new LogEvent(managerID);
			if (isSignedIn) {
				message1.setMessage( managerID + " has signed in.");
				message1.setMessage("Recieved request for count on all the servers.");
				String recordData;
				recordData = startServer.getRecordCounts();
				System.out.println("Total number of records are: " + recordData);
				message1.setMessage("Server reply: (Total record count: "+ recordData + " )");			
				
				String firstStudentname = "Server " + managerID + "studentFName";
				String lastStudentname = "Server " + managerID + "studentLName";
				String coursesRegistered = "java,C#,Python,Clojure,C++";
				boolean status = true;
			    String statusDate = "08/01/2018";
				
				String addressteacher = "Server " +managerID + "teacherAddress";
				String phoneteacher = "12" + managerID.substring(3);
				String specializationteacher = "Server " + managerID + "teacherSpecialization";
				String locationteacher = "";
				String firstteachername ="Server " + managerID + "teacherFName";
				String lastteachername = "Server " + managerID + "teacherLName";
				
				
				
				
				System.out.println("Manager:- " + managerID);
				System.out.println("********Create Student Record*********");

				message1.setMessage("Creating Student Record:- FirstName : " + firstStudentname + " LastName : "
						+ lastStudentname + " Course Registered : " + coursesRegistered + " Status : " + status
						+ " StatusDate : " + statusDate);
				boolean successsfullyCreatedStudentRecord;
				
					successsfullyCreatedStudentRecord = startServer.createSRecord(firstStudentname, lastStudentname, coursesRegistered.split(","),
							status, new SimpleDateFormat("dd/MM/yyyy").parse(statusDate));
				
					if (!successsfullyCreatedStudentRecord) {
						System.out.println("Fail: Student record not created.");
						message1.setMessage("Fail: Not able to create student record.");
					}else{
						System.out.println("Student added successfully.");
						message1.setMessage("Student Record created sucessfully......");
					}
				
				       message1.setMessage("Recieved request for count on all the servers.");
				
					recordData = startServer.getRecordCounts();
					System.out.println("Total Record count: " + recordData);
					message1.setMessage("Server reply: (Total record count: " + recordData + " )");
				
				
				
				
				
				
				
				
				
				
				
				if(sName.equals("MTL")) {
				locationteacher = "MTL";
				}
				else if (sName.equals("LVL")) {
					locationteacher = "LVL";
				}
				else {
					locationteacher = "DDO";
				}
				System.out.println("Manager:- " + managerID);
				System.out.println("********Create Teacher Record*********");
				message1.setMessage("Creating Teacher Record:- FirstName : " + firstteachername + " LastName : "
						+ lastteachername + " Address : " + addressteacher + " Phone : " + phoneteacher + " Specialization : "
						+ specializationteacher + " Location : " + locationteacher);
				boolean successfullyCreatedTeacherRecord;
					successfullyCreatedTeacherRecord = startServer.createTRecord(firstteachername, lastteachername, addressteacher, phoneteacher,
							specializationteacher, locationteacher);
					if (!successfullyCreatedTeacherRecord) {
						System.out.println("Fail: Teacher record not created.");
						message1.setMessage("Fail: Not able to create teacher record.");
					}else{
						System.out.println("Teacher added successfully.");
						message1.setMessage("Teacher Record created sucessfully......");
					}
				
				
		
				
				
				
				//editRecord
				if(sName.equals("MTL")) {
					System.out.println("********Edit Student Record*********");
					String recID = "SR99997";
					if(managerID.equals("MTL1009")) {
						String cRegistered = "Distributed Systems,Database";
						String editStatusDate = "01/05/2018";
						if(startServer.editRecord(recID, "coursesregistered", cRegistered)) {
							message1.setMessage("Record edited" + recID + " :- Record field -'coursesregistered' Record Value - (" + cRegistered + ")");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
						if(startServer.editRecord(recID, "status", "inactive")) {
							message1.setMessage("Record edited" + recID + " :- Record field -'status' Record Value - (inactive)");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
						if(startServer.editRecord(recID, "statusdate", editStatusDate)) {
							message1.setMessage("Record edited" + recID + " :- Record field -'statusdate' Record Value - (01/05/2018)");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
					}
					else if (managerID.equals("MTL10016")) {
						String cRegistered = "DS, DB";
						String editStatusDate = "07/05/2018";
						if(startServer.editRecord(recID, "coursesregistered", cRegistered)) {
							message1.setMessage("Record edited" + recID + " :- Record field -'coursesregistered' Record Value - (" + coursesRegistered  + ")");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
						if(startServer.editRecord(recID, "status", "inactive")) {
							message1.setMessage("Record edited" + recID + " :- Record field -'status' Record Value - (inactive)");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
						if(startServer.editRecord(recID, "statusdate", editStatusDate)) {
							message1.setMessage("Record edited" + recID + " :- Record field -'statusdate' Record Value - (07/05/2018)");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
					}
				}
				
				if(sName.equals("DDO")) {
					String recID = "TR99994";
					System.out.println("********Edit Teacher Record*********");
					if(managerID .equals("DDO1001")) {
						String address1 = "Quebec";
						String phone1 = "783412";
						String loc1 = "MTL";
						if(startServer.editRecord(recID,"address", address1)) {
							message1.setMessage("Record edited " + recID + " :- Record field -'address' Record Value - (" + address1 +")");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
						if(startServer.editRecord(recID,"phone", phone1)) {
							message1.setMessage("Record edited " + recID + " :- Record field -'phone' Record Value - (" + phone1 +")");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
						if(startServer.editRecord(recID,"location", loc1)) {
							message1.setMessage("Record edited " + recID + " :- Record field -'location' Record Value - (" + loc1 +")");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
					}
					else if(managerID .equals("DDO1003")) {
						String address1 = "xyz";
						String phone1 = "762341";
						String loc1 = "LVL";
						if(startServer.editRecord(recID,"address", address1)) {
							message1.setMessage("Record edited " + recID + " :- Record field -'address' Record Value - (" + address1 +")");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
						if(startServer.editRecord(recID,"phone", phone1)) {
							message1.setMessage("Record edited " + recID + " :- Record field -'phone' Record Value - (" + phone1 +")");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
						if(startServer.editRecord(recID,"location", loc1)) {
							message1.setMessage("Record edited " + recID + " :- Record field -'location' Record Value - (" + loc1 +")");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
					}
				}
				startServer.signOut();
				isSignedIn = false;
				message1.setMessage("Manager : " + managerID + " signed out in test case..");
				System.out.println("Manager has signed out in testcase");
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
