package RecordManagement;
/**
 * Contains the bsic structure for teacher ans student records.
 */
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

public class Record {
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
	
	
	//Student Record
	public Record(String recordID, String fName, String lName,  String [] cRegister, boolean status,
			Date statusDate) {
		this.status = status;
		this.lName = lName;
		this.coursesRegistered = new HashSet<String>();
		this.coursesRegistered.addAll(Arrays.asList(cRegister));
		this.recordID = recordID;
		this.fName = fName;
		this.statusDate = statusDate;
	}

	//Teacher Record
	public Record(String recordID, String fName, String lName, String address, String phone,
			String specialization, String location) {
		this.specialization = specialization;
		this.lName = lName;
		this.address = address;
		this.recordID = recordID;
		this.fName = fName;
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

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public String toString(String type) {
		if(type.equalsIgnoreCase("SR")) {
			
			return "Student Record :- Record ID : "+recordID+
					" First Name : " + fName + 
					" Last Name : " + lName+
					" Course Registered : "+ coursesRegistered+
					" Status : "+status+
					" Status Date : "+statusDate +"\n";
		}
		else if(type.equalsIgnoreCase("TR")) {
			return "Teacher Record :- Record ID : "+recordID+
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
