package Servers;
/**
 * Stores the student and teacher records
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.rmi.RemoteException;
import RecordManagement.Record;

public class HashMapRecord extends UnicastRemoteObject {
	private HashMap<Character, ArrayList<Record>> recordInfoTable = new HashMap<Character, ArrayList<Record>>();

	public HashMapRecord() throws RemoteException{

	}

	public boolean editRecord(String recID, String fieldName, String newValue) throws Exception{
		fieldName = fieldName.trim().toUpperCase();

		Record rec = this.fetchRecordByID(recID);
		if (rec == null) {
			System.out.println("Record with " + recID + " does not exist.");
			return false;
		} else {
			System.out.println("Record is found.");
		}
		synchronized (rec) {
			if (rec != null) {
				if (recID.startsWith("SR")) {
					if(fieldName.equals("STATUS"))
					{
						boolean status=false;
						if(newValue.toUpperCase().equals("INACTIVE"))
							{
							status=false;
							}
						else if(newValue.toUpperCase().equals("ACTIVE"))
						{
							status=true;
						}
						else 
						{
							status=false;
						}
						rec.status=status;
						
					}
					else if(fieldName.equals("STATUSDATE"))
					{
						rec.setStatusDate(new SimpleDateFormat("dd/MM/yyyy").parse(newValue));
						
					}
					else if(fieldName.equals("COURSESREGISTERED"))
					{
						String ele[] = newValue.split(",");
						rec.addCoursesRegistered(ele);
					}
					else 
						return false;
					
				
				}
				else if (recID.startsWith("TR")) {
					
					if(fieldName.equals("PHONE"))
					{
						rec.setPhone(newValue);
					}
					else if(fieldName.equals("LOCATION"))
					{
						rec.setLocation(newValue);
					}
					else if(fieldName.equals("ADDRESS"))
					{
						
						rec.setAddress(newValue);
					}
					
					else
					{
						return false;
					}
						
						
				}
			}
		}
		return true;
	}

	
	
	public boolean createRecord(Record rec) {
		Character key = rec.getlName().toUpperCase().charAt(0);
		
		if (recordInfoTable.get(key) == null) 
		{
			recordInfoTable.put(key, new ArrayList<Record>());
		}
		System.out.println("*******Record has been added*********");
		ArrayList<Record> tmpList = recordInfoTable.get(key);
		synchronized (tmpList)
		{			
			return tmpList.add(rec);
		}
	}

	
	public int fetchRecordCount() {
		
		int recordn = 0;
		
		for (ArrayList<Record> tmpList : recordInfoTable.values())
	    {
			recordn = recordn + tmpList.size();
		}
		return recordn;
	}


	
	
	
	public Record fetchRecordByID(String recID) {
		for (ArrayList<Record> recordList : recordInfoTable.values())
		{
			for (Record record : recordList) 
			{
				if (record.recordID.equals(recID)) {
					return record;
				}
			}
		}
		return null;
	}

}
