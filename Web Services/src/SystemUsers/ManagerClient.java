/**
 * This class contains the methods which user would perform on the server.
 */

package SystemUsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import javax.xml.ws.WebServiceRef;

import Servers.DDOService;
import Servers.IDCMSInterface;
import Servers.LVLService;
import Servers.MTLService;
import TestCasesAndCheckers.LogEvent;

import java.util.Date;



public class ManagerClient {
	@WebServiceRef(wsdlLocation="http://localhost:8080/MTL, http://localhost:8080/DDO, http://localhost:8080/LVL")
	
	private static final String[] managerID={"DDO1001","DDO1002","DDO1003","DDO1004","DDO1005","DDO1006","DDO1007","DDO1008","MTL1009","MTL1010","MTL1011","MTL1012","MTL1013","MTL1014","MTL1015","MTL1016","LVL1017","LVL1018","LVL1019","LVL1020","LVL1021","LVL1022","LVL1023","LVL1024"};
	
	static String Stfirst(String x)
	{System.out.println(x);
	Scanner SScan = new Scanner(System.in);
	String fiName = SScan.nextLine();
	return fiName;

	}
	


	public static void main(String[] args) throws ParseException, Exception {
		// TODO Auto-generated method stub
		boolean valid = false;
		String aLine = "", fName = "", lName = "", pAddress = "", phone = "", specialization = "";
		String location="";
		String coursesRegistered;
		boolean status = false;
		String statusDate;
		String managerID = "";
		int choice = 0;
		String sName = null;
		int sPort = 0;
		IDCMSInterface service = null;
		
		while (true) {
			System.out.print("Please enter your Manager ID:");
			Scanner scan = new Scanner(System.in);
			managerID = scan.nextLine();
			System.out.println();

			if (ManagerClient.isManager(managerID)) {
				valid = true;
				if (managerID.substring(0, 3).equalsIgnoreCase("LVL")) {
					sName = "LVL";
					sPort = 8759;
					LVLService lvlService =  new LVLService();
					service = lvlService.getLVLServicePort();
					
				}
				else if (managerID.substring(0, 3).equalsIgnoreCase("MTL")) {
					sName = "MTL";
					sPort = 8758;
					MTLService mtlService = new MTLService();
					service = mtlService.getMTLServicePort();
				}
				if (managerID.substring(0, 3).equalsIgnoreCase("DDO")) {
					sName = "DDO";
					sPort = 8760;
					DDOService ddoService = new DDOService();
					service = ddoService.getDDOServicePort();
				}
				
				
				try {

					boolean isValid = service.signIn(managerID);
					LogEvent message1 = new LogEvent(managerID);
					if (!isValid) {
						continue;
					}
					else {
						message1.setMessage("Manager : " + managerID + " signed in.");
					} 
					while (valid) {
						displayMenu();
						Scanner options = new Scanner(System.in);
						String choice1 = options.nextLine();
						try {
							choice = Integer.parseInt(choice1);
						}catch(NumberFormatException ex) {
							System.out.println("Please enter a number between 1 and 6.");
							continue;
						}
						if(choice < 1) {
							System.out.println("Please enter a number between 1 and 6.");
							continue;
						}
						if(choice>6)
						{
							System.out.println("Please enter a number between 1 and 6.");
							continue;
						}

						switch (choice) {
						case 1:


							Scanner studScanner = new Scanner(System.in);
							System.out.println("*********Create Student Record*********");
							for(;;)
							{
								String dis="Please enter Student's First Name:";
								fName =Stfirst(dis);
								if(ManagerClient.checkString(fName))
									break;
							}

							for(;;)
							{
								String dis="Please enter Student's Last Name:";
								lName = Stfirst(dis);
								if(ManagerClient.checkString(lName))
									break;

							}

							for(;;)
							{
								String dis ="Please enter Student's Courses registered:";
								coursesRegistered= Stfirst(dis);
								String [] tmp = coursesRegistered.split(",");
								boolean flag =false;
								for(String e: tmp) {
									if(!ManagerClient.checkString(e.trim()))
										flag = true;
								}
								if (flag)
									continue;
								break;
							}

							for(;;) {

								String dis="Please enter Student's status (active/inactive):";
								aLine = Stfirst(dis);
								if(ManagerClient.checkStatus(aLine))
								{
									status=true;
									break;
								}
								else {
									status=false;
								}
							}

							for(;;)
							{
								System.out.println("Please enter Student's Status Date in format (DD/MM/YYYY):");
								statusDate = studScanner.nextLine();
								if(ManagerClient.checkDate(statusDate))
									break;
							}


							boolean createSrecordSucess = service.createSRecord(managerID, fName, lName, coursesRegistered,
									status,  statusDate);


							message1.setMessage("Creating Student Record:- FirstName : " + fName + " LastName : "
									+ lName + " Course Registered : " + coursesRegistered + " Status : " + status
									+ " StatusDate : " + statusDate);

							if (!createSrecordSucess) {
								System.out.println("Fail: Student record not created.");
								message1.setMessage("Fail: Not able to create Student Record.");
							}else{
								System.out.println("Student added successfully.");
								message1.setMessage("Student Record created sucessfully.");
							}

							break;

						case 2:
							Scanner tScan = new Scanner(System.in);
							System.out.println("*********Create Teacher Record*********");
							for(;;) {
								String dis1="Please enter Teacher's First Name:";

								fName = Stfirst(dis1);

								if (ManagerClient.checkString(fName))
									break;
							} 

							for(;;)
							{
								String dis1="Please enter Teacher's Last Name:";

								lName = Stfirst(dis1);
								if(ManagerClient.checkString(lName))
									break;
							}

							for(;;)
							{
								String dis1="Please enter Teacher's Address:";

								pAddress = Stfirst(dis1);

								if(ManagerClient.checkString(pAddress))
									break;



							}

							for(;;)
							{
								String dis1="Please enter Teacher's Phone:";

								phone = Stfirst(dis1);
								if(ManagerClient.checkNumber(phone))
									break;

							}

							for(;;)
							{
								String dis1="Please enter Teacher's Specialization:";

								specialization = Stfirst(dis1);	
								if(ManagerClient.checkString(specialization))
									break;
							}

							for(;;)
							{String dis1 ="Please enter Teacher's Location:";

							location = Stfirst(dis1);
							if(ManagerClient.checkCity(location))
								break;

							}



							boolean createTrecordSuccess = service.createTRecord(managerID, fName, lName, pAddress, phone,
									specialization, location);
							message1.setMessage("Creating Teacher Record:- First Name : " + fName + " Last Name : "
									+ lName + " Address : " + pAddress + " Phone : " + phone + " Specialization : "
									+ specialization + " Location : " + location);
							if (!createTrecordSuccess) {
								System.out.println("Fail: Teacher record not created.");
								message1.setMessage("Fail: Not able to create teacher record.");
							}else{
								System.out.println("Teacher added successfully.");
								message1.setMessage("Teacher Record created sucessfully......");	
							}
							break;

						case 3:
							message1.setMessage("Recieved request for count on all the servers.");
							String recordCount = service.getRecordCounts();
							System.out.println("Total Record count: " + recordCount);
							message1.setMessage("Server reply: (Total record count: " + recordCount + " )");
							break;


						case 4:

							boolean editLoop = true;
							String recordID = "";
							String fieldName = "";
							String newValue = "";
							Scanner recordscan = new Scanner(System.in);
							do{

								System.out.println("Enter Record ID:");
								recordID = recordscan.nextLine();
								
								System.out.println("Please select the field you want to edit.");

								if (recordID.substring(0, 2).equalsIgnoreCase("SR")) {
									studentOptions();
									String ch = recordscan.nextLine();
									int var = Integer.parseInt(ch);
									if(var==1)
									{

										fieldName = "coursesregistered";
										editLoop = false; 
									}
									else if(var==2)
									{
										fieldName = "status";
										editLoop = false;
									}
									else if(var==3)
									{
										fieldName = "statusdate";
										editLoop = false;
									}
									else {
										editLoop=false;
										break;
									}
								}
								else if (recordID.substring(0, 2).equalsIgnoreCase("TR")) {
									teacherOptions();
									String ch = recordscan.nextLine();
									int var = Integer.parseInt(ch);
									if(var==1)
									{
										fieldName = "address";
										editLoop = false;
									}

									else if(var==2)
									{
										fieldName = "phone";
										editLoop = false;
									}
									else if(var==3)
									{
										fieldName = "location";
										editLoop = false;

									}
									else
									{
										editLoop = false;
										break;
									}

								}
								System.out.println("Enter New value:");
								newValue = recordscan.nextLine();
								if (checkInputs(fieldName, newValue,sName)) {
									message1.setMessage("Record to be edited " +recordID+ " :- Record field -'" + fieldName
											+ "' Record Value - (" + newValue + ")");
									if (service.editRecord(managerID, recordID, fieldName, newValue)) {
										System.out.println("Successfully edited record.");
										message1.setMessage("Record edited" + recordID + " :- Record field -'" + fieldName
												+ "' Record Value - (" + newValue + ")");
									} else {
										System.out.println("Record does not exist or mismatch in field input.");
										message1.setMessage("Fail: Not able to edit record " + recordID);
									}
								}
							}while (editLoop);

							break;
							
							
							
						case 5:// transfer Record
							String Trecorddetail = null;
							String locoftransfer = null;
							Scanner filereadr = new Scanner(System.in);
							for(;;)
							{System.out.println("Type in the Id of record(Which you want to transfer): ");

							Trecorddetail = filereadr.nextLine();
								if(ManagerClient.checkRecordId(Trecorddetail))
								{
									break;
								}
							}
							
							for(;;)
							{System.out.println("You want to transfer the record to location:");
						locoftransfer = filereadr.nextLine();
								
								if(ManagerClient.checkCity(locoftransfer)&& !locoftransfer.equals(sName))
								{
									break;
								}
							}
							

							if (service.transferRecord(managerID,Trecorddetail, locoftransfer)) {
								message1.setMessage("Record with record ID: "+ Trecorddetail+ "moved to location:"+ locoftransfer);
								System.out.println("Record with record ID:"+ Trecorddetail+ " transferred successfully to location:" + locoftransfer);
							} else {
								message1.setMessage(" Record transfer of Record with ID:"+ Trecorddetail + " is unsuccesfull.");
								System.out.println("Failure of record transfer of Record with ID:"+ Trecorddetail);
							}
							break;

						case 6:
							System.out.println("Are you sure you want exit? (Y/N)");
							Scanner sc = new Scanner(System.in);
							String strExit = sc.nextLine();
							if (strExit.toUpperCase().trim().equals("Y")) {
								service.signOut();
								System.out.println("Manager signed out.");
								message1.setMessage("Manager : " + managerID + " signed out.");
								valid = false;
							}
							break;

						}
					}
				}
				catch (Exception e) {
					System.out.println("ERROR : " + e);
					e.printStackTrace(System.out);
				}
			}
			else {
				valid = false;
				System.out.println("Not a valid Manager ID.");
				continue;
			}
		}

	}

	public static void teacherOptions() {
		System.out.println("1. Address:");
		System.out.println("2. Phone:");
		System.out.println("3. Location:");

	}

	public static boolean checkInputs(String namef, String Valuef, String sName) throws ParseException {
		if(namef.equalsIgnoreCase("phone"))
		{
			if(ManagerClient.checkNumber(Valuef))
			{
				return true;
			}
		}
		if(namef.equalsIgnoreCase("location"))
		{
			if(ManagerClient.checkCity(Valuef))
			{
				return true;
			}
		}
		if(namef.equalsIgnoreCase("statusdate"))
		{
			if(ManagerClient.checkDate(Valuef))
			{
				return true;
			}
		}
		if(namef.equalsIgnoreCase("status"))
		{
			if(ManagerClient.checkStatus(Valuef))
			{
				return true;
			}
		}
		if(ManagerClient.checkString(Valuef))
		{
			if ((namef.equalsIgnoreCase("address")))
				return true;

			if (namef.equalsIgnoreCase("coursesregistered"))
				return true;
		}
		return false;
	}



	public static boolean isManager(String mgID)
	{
		for(int i=0;i<managerID.length;i++)
		{
			if(managerID[i].equalsIgnoreCase(mgID))
				return true;
		}
		return false;
	}


	public static void studentOptions() {
		System.out.println("1. Courses Registered");
		System.out.println("2. Status");
		System.out.println("3. Status Date");
	}

	public static void displayMenu() {
		System.out.println("Please Select one of below options (1-5):");
		System.out.println("1. Create Student Record");
		System.out.println("2. Create Teacher Record");
		System.out.println("3. Get Record Count");
		System.out.println("4. Edit Record");
		System.out.println("5. Transfer Record");
		System.out.println("6. Log out");
	}

	public static boolean checkString(String str) {
		if(str == null || str.trim().equalsIgnoreCase(""))
			return false;

		return(!str.matches(".*\\d.*"));
	}

	public static boolean checkCity(String city) {
		return(city.toUpperCase().equals("MTL") || city.toUpperCase().equals("DDO")
				|| city.toUpperCase().equals("LVL"));
	}

	public static boolean checkStatus(String status) {
		return (status.trim().toLowerCase().equals("active") || 
				status.trim().toLowerCase().equals("inactive"));
	}

	public static boolean checkNumber(String num) {
		return (!(num.matches(".*[a-zA-Z]+.*") || num.length() < 6));
	}
	
	public static boolean checkRecordId(String rec) {
		if (rec.length() != 7)
			return false;
		if (rec.substring(0, 2).equals("TR") || rec.substring(0, 2).equals("SR"))
			return true;
			
		return false;
	}
	
	public static boolean checkDate(String date) throws java.text.ParseException {
		if (date == null) {
			return false;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		try {
			Date tmp = dateFormat.parse(date);
			System.out.println(date);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}













