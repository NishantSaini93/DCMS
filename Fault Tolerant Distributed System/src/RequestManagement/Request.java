package RequestManagement;

import java.io.Serializable;

import RecordManagement.Record;

public class Request implements Serializable{
	public final static int SINREQ = 0;  //Sign in Request
	public final static int CSR = 1; //Create Student Request
	public final static int CTR = 2; //Create Teacher Request
	public final static int EREQ = 3; // Edit record request
	public final static int TREQ = 4; // Transfer record request
	public final static int GCREQ = 5; //GetCount request
	public final static int SOUTREQ = 6;  // Sign out Request 
	public final static int GMYCREQ = 7; //Send only my own server count request
	public final static int TROWNREQ = 8; //transfer to won server request
	
	
	
	public String recordID;
	public String fName;
	public String lName;
	public String courseRegistered;
	public boolean status;
	public String statusDate;
	public String address;
	public String phone;
	public String specialization;
	public String location;
	public String fieldName;
	public String newValue;
	public int requestType;
	public String managerID;
	public String receivingServer; 
	public Record record;

	
	public Request(int requestType, String managerID) {
		this.requestType = requestType;
		this.managerID = managerID;
	}
	
	
	public Request() {
	}
	
	@Override
	public String toString(){
		return "ManagerID: " + this.managerID + ", Type of Request: " + this.requestType ;
	}
}
