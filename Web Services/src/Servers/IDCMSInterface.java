package Servers;

import javax.jws.WebMethod;
import javax.jws.WebService;



@WebService
public interface IDCMSInterface {
	
	@WebMethod
	boolean signIn (String mgID);
	
	@WebMethod
	void signOut ();
	
	@WebMethod
	boolean createTRecord(String managerID, String fName, String lName, String address, String phone, 
			String specialization, String location);
	
	@WebMethod
	String getRecordCounts();
	
	@WebMethod
	boolean editRecord(String managerID, String recordID, String fieldName, String newValue) throws Exception;
	
	@WebMethod
	boolean createSRecord(String managerID, String fName, String lName, String coursesRegistered, boolean status,
			String statusDate);
	
	@WebMethod
	boolean transferRecord(String managerID, String recordID, String remoteCenterServerName);
	
	@WebMethod
	String printRecords();
	
}
