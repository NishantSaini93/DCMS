/**
 * Contains the basic structure for teacher and student records.
 */
package RecordManagement;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

public class Record implements Serializable{
	public String recordID="";
	public String fName="";
	public String lName="";
	public HashSet<String> coursesRegistered;
	public boolean status=false;
	public Date statusDate=null;
	public String address="";
	public String phone="";
	public String specialization="";
	public String location="";
	public String managerID="";
	
	
	//Student Record
	public Record(String mgID, String recordID, String fName, String lName,  String cRegister, boolean status,
			String statusDate) {
		super();
		this.status = status;
		this.lName = lName;
		this.managerID = mgID;
		this.coursesRegistered = new HashSet<String>();
		this.coursesRegistered.addAll(Arrays.asList(cRegister.split(",")));
		this.recordID = recordID;
		this.fName = fName;
		try {
			this.statusDate = new SimpleDateFormat("dd/MM/yyyy").parse(statusDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Teacher Record
	public Record(String mgID, String recordID, String fName, String lName, String address, String phone,
			String specialization, String location) {
		super();
		this.specialization = specialization;
		this.lName = lName;
		this.address = address;
		this.recordID = recordID;
		this.fName = fName;
		this.managerID = mgID;
		this.phone = phone;
		this.location = location;
	}

	public String getlName() {
		return lName;
	}
	
	public HashSet<String> getCoursesRegistered() {
		return coursesRegistered;
	}

	public void addCoursesRegistered(String[] courses) {
		this.coursesRegistered.addAll(Arrays.asList(courses));
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}
	
	public void setfName(String fName) {
		this.fName = fName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getRecordID() {
		return recordID;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setStatusDate(String statusDate) {
		try {
			this.statusDate = new SimpleDateFormat("dd/MM/yyyy").parse(statusDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String toString(String type) {
		if(type.equals("SR")) {
			
			return "Student Record :- Manager ID :" + managerID + 
					" Record ID : "+ recordID +
					" First Name : " + fName + 
					" Last Name : " + lName+
					" Course Registered : "+ coursesRegistered+
					" Status : "+status+
					" Status Date : "+statusDate +"\n";
		}
		else if(type.equals("TR")) {
			return "Teacher Record :- Manager ID :" + managerID + 
				    " Record ID : "+recordID+
					" First Name : " + fName + 
					" Last Name : " + lName+
					" Address : "+address+
					" Phone : "+phone+
					" Specialization : "+specialization+
					" Location : "+location +"\n";
		}
		return "";
	}
}
