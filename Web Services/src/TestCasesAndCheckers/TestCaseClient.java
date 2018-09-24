/**
 * This class has the test case scenarios for the system.
 */
package TestCasesAndCheckers;

import java.net.MalformedURLException;
import java.text.ParseException;

import Servers.DDOService;
import Servers.IDCMSInterface;
import Servers.LVLService;
import Servers.MTLService;
import TestCasesAndCheckers.LogEvent;

public class TestCaseClient extends Thread{


	private final String sName;
	private final String managerID;
	private final int sPort;
	
	public TestCaseClient(String server, String mgrID, int serverP){
		sName = server;
		managerID = mgrID;
		sPort = serverP;
	}
	
	public static void main (String args[]) throws MalformedURLException, ParseException {
		
		TestCaseClient d1 = new TestCaseClient("DDO", "DDO1001", 8760);
		Thread td1 = new Thread (d1);
		TestCaseClient d2 = new TestCaseClient("DDO", "DDO1002", 8760);
		Thread td2 = new Thread (d2);
		TestCaseClient d3 = new TestCaseClient("DDO", "DDO1003", 8760);
		Thread td3 = new Thread (d3);
		TestCaseClient d4 = new TestCaseClient("DDO", "DDO1004", 8760);
		Thread td4 = new Thread (d4);
		TestCaseClient d5 = new TestCaseClient("DDO", "DDO1005", 8760);
		Thread td5 = new Thread (d5);
		TestCaseClient d6 = new TestCaseClient("DDO", "DDO1006", 8760);
		Thread td6 = new Thread (d6);
		TestCaseClient d7 = new TestCaseClient("DDO", "DDO1007", 8760);
		Thread td7 = new Thread (d7);
		TestCaseClient d8 = new TestCaseClient("DDO", "DDO1008", 8760);
		Thread td8 = new Thread (d8);
		
		TestCaseClient m1 = new TestCaseClient("MTL", "MTL1009", 8758);
		Thread tm1 = new Thread (m1);
		TestCaseClient m2 = new TestCaseClient("MTL", "MTL10010", 8758);
		Thread tm2 = new Thread (m2);
		TestCaseClient m3 = new TestCaseClient("MTL", "MTL10011", 8758);
		Thread tm3 = new Thread (m3);
		TestCaseClient m4 = new TestCaseClient("MTL", "MTL10012", 8758);
		Thread tm4 = new Thread (m4);
		TestCaseClient m5 = new TestCaseClient("MTL", "MTL10013", 8758);
		Thread tm5 = new Thread (m5);
		TestCaseClient m6 = new TestCaseClient("MTL", "MTL10014", 8758);
		Thread tm6 = new Thread (m6);
		TestCaseClient m7 = new TestCaseClient("MTL", "MTL10015", 8758);
		Thread tm7 = new Thread (m7);
		TestCaseClient m8 = new TestCaseClient("MTL", "MTL10016", 8758);
		Thread tm8 = new Thread (m8);
		
		TestCaseClient l1 = new TestCaseClient("LVL", "LVL1017", 8759);
		Thread tl1 = new Thread (l1);		
		TestCaseClient l2 = new TestCaseClient("LVL", "LVL1018", 8759);
		Thread tl2 = new Thread (l2);
		TestCaseClient l3 = new TestCaseClient("LVL", "LVL1019", 8759);
		Thread tl3 = new Thread (l3);
		TestCaseClient l4 = new TestCaseClient("LVL", "LVL1020", 8759);
		Thread tl4 = new Thread (l4);
		TestCaseClient l5 = new TestCaseClient("LVL", "LVL1021", 8759);
		Thread tl5 = new Thread (l5);
		TestCaseClient l6 = new TestCaseClient("LVL", "LVL1022", 8759);
		Thread tl6 = new Thread (l6);
		TestCaseClient l7 = new TestCaseClient("LVL", "LVL1023", 8759);
		Thread tl7 = new Thread (l7);
		TestCaseClient l8 = new TestCaseClient("LVL", "LVL1024", 8759);
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
		IDCMSInterface beginserver = null;
			
			if (managerID.substring(0, 3).equalsIgnoreCase("LVL")) {
				LVLService lvlService =  new LVLService();
				beginserver = lvlService.getLVLServicePort();
				
			}
			else if (managerID.substring(0, 3).equalsIgnoreCase("MTL")) {
				MTLService mtlService = new MTLService();
				beginserver = mtlService.getMTLServicePort();
			}
			else if (managerID.substring(0, 3).equalsIgnoreCase("DDO")) {
				DDOService ddoService = new DDOService();
				beginserver = ddoService.getDDOServicePort();
			}
		
		boolean isSignedIn;
		try {

			isSignedIn = beginserver.signIn(managerID);
			LogEvent message1 = new LogEvent(managerID);
			if (isSignedIn) {
				message1.setMessage( managerID + " has signed in.");
				message1.setMessage("Recieved request for count on all the servers.");
				String recordData;
				recordData = beginserver.getRecordCounts();
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
				
					successsfullyCreatedStudentRecord = beginserver.createSRecord(managerID, firstStudentname, lastStudentname, coursesRegistered,
							status, statusDate);
				
					if (!successsfullyCreatedStudentRecord) {
						System.out.println("Fail: Student record not created.");
						message1.setMessage("Fail: Not able to create student record.");
					}else{
						System.out.println("Student added successfully.");
						message1.setMessage("Student Record created sucessfully......");
					}
				
				       message1.setMessage("Recieved request for count on all the servers.");
				
					recordData = beginserver.getRecordCounts();
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
					successfullyCreatedTeacherRecord = beginserver.createTRecord(managerID, firstteachername, lastteachername, addressteacher, phoneteacher,
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
					//Transfer Student record from MTL to DDO (First Transfer record and then try to edit)
					if(this.managerID.startsWith("MTL")){
						beginserver.transferRecord(managerID, "SR99997", "DDO"); 
					}
					
					System.out.println("********Edit Student Record*********");
					String recID = "SR99997";
					String cRegistered = "";
					String editStatusDate = "";
					String status1 = "";
						if((Integer.parseInt(managerID.substring(3)))%2==0){
						cRegistered = "Distributed Systems,Database";
						editStatusDate = "01/05/2018";
						status1 = "inactive";
						}
						else {
							cRegistered = "Pattern Recognition,APP";
							editStatusDate = "07/03/2018";
							status1 = "active";
						}
						if(beginserver.editRecord(managerID, recID, "coursesregistered", cRegistered)) {
							message1.setMessage("Record edited" + recID + " :- Record field -'coursesregistered' Record Value - (" + cRegistered + ")");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
						if(beginserver.editRecord(managerID, recID, "status", status1)) {
							message1.setMessage("Record edited" + recID + " :- Record field -'status' Record Value - " + status1);
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
						if(beginserver.editRecord(managerID, recID, "statusdate", editStatusDate)) {
							message1.setMessage("Record edited" + recID + " :- Record field -'statusdate' Record Value - " + editStatusDate);
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
						String address1 = "Quebec:" + managerID;
						String phone1 = managerID.substring(3) + "11";
						int tmp = Integer.parseInt(managerID.substring(3));
						String loc1 = "";
						if(tmp%2==0)
							loc1 = "MTL";
						else
							loc1 = "LVL";
						if(beginserver.editRecord(managerID, recID,"address", address1)) {
							message1.setMessage("Record edited " + recID + " :- Record field -'address' Record Value - (" + address1 +")");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
						if(beginserver.editRecord(managerID, recID,"phone", phone1)) {
							message1.setMessage("Record edited " + recID + " :- Record field -'phone' Record Value - (" + phone1 +")");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
						if(beginserver.editRecord(managerID, recID,"location", loc1)) {
							message1.setMessage("Record edited " + recID + " :- Record field -'location' Record Value - (" + loc1 +")");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
					}
				
				
				
				if(sName.equals("LVL")) {
					String recID = "SR99999";
					System.out.println("********Edit Student Record*********");
					String cRegistered = "";
					String editStatusDate = "";
					String status1 = "";
						if((Integer.parseInt(managerID.substring(3)))%2==0){
						cRegistered = "Comparative,SRS,Patern Recognition";
						editStatusDate = "02/05/2018";
						status1 = "inactive";
						}
						else {
							cRegistered = "Pattern Recognition,APP";
							editStatusDate = "07/02/2018";
							status1 = "active";
						}
						if(beginserver.editRecord(managerID, recID, "coursesregistered", cRegistered)) {
							message1.setMessage("Record edited" + recID + " :- Record field -'coursesregistered' Record Value - (" + cRegistered + ")");
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
						if(beginserver.editRecord(managerID, recID, "status", status1)) {
							message1.setMessage("Record edited" + recID + " :- Record field -'status' Record Value - " + status1);
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
						if(beginserver.editRecord(managerID, recID, "statusdate", editStatusDate)) {
							message1.setMessage("Record edited" + recID + " :- Record field -'statusdate' Record Value - " + editStatusDate);
							System.out.println("Successfully edited record.");
						}
						else {
							message1.setMessage("Fail: Not able to edit record " + recID);
							System.out.println("Record does not exist or mismatch in field input.");
						}
					}
					
				
				//Transfer student record from DDO to LVL
				if(this.managerID.startsWith("DDO")) {
					beginserver.transferRecord(managerID, "SR99995", "LVL");	
				}
				
				
				//Transfer Teacher record from LVL to MTL
				if(this.managerID.startsWith("LVL")){
					beginserver.transferRecord(managerID, "TR99998", "MTL"); 
				}
				
				
				
				
				//Print the records in Server
				message1.setMessage("Records of server :" + this.sName+" is printing");
				String printinr = beginserver.printRecords();
				message1.setMessage("Listing Records:->" + printinr);
				System.out.println("All Records:-> \n" + printinr);
				
				//Sign out the Manager
				beginserver.signOut();
				isSignedIn = false;
				message1.setMessage("Manager : " + managerID + " signed out in test case..");
				System.out.println("Manager has signed out in testcase");
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
