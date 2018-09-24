package Servers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import RecordManagement.Record;
import Services.GenerateID;
import Services.LogEvent;
import CORBAImplementation.CORBAImplementation;
import CORBAImplementation.CORBAImplementationHelper;
import CORBAImplementation.CORBAImplementationPOA;

public class CenterServers extends CORBAImplementationPOA implements Runnable {

	String sName = "";
	int sPort = 0;
	HashMapRecord objectofrecord;
	int ListeningportUDP = 0;
	LogEvent message1=null;
	private ORB ddoorb;
	String managerID = "";


	protected CenterServers(String serverName, int s1Port, int UDP_Port1) {
		super();
		sPort = s1Port;
		objectofrecord = new HashMapRecord();
		ListeningportUDP = UDP_Port1;
		this.sName = serverName;
		message1= new LogEvent(serverName);
		
		try {
			if(serverName.equals("DDO")) {
			objectofrecord.createRecord(new Record("DDO1001", "SR99995", "Nishant", "Saini", "Algorithms",true, 
					"08/01/2018"));
			objectofrecord.createRecord(new Record("DDO1002","TR99994", "Jayant", "Verma", "ENCS", "345897", 
					"Python", "DDO"));
			}
			else if(serverName.equals("LVL")) {
				objectofrecord.createRecord(new Record("LVL1017","SR99999", "Ashish", "Sharma", "Distributed Systems,Software Design Methodologies", true, 
						"01/01/2018"));
				objectofrecord.createRecord(new Record("LVL1018","TR99998", "Pradeep", "Verma", "Concordia", "123456", 
						"C#", "LVL"));
			}
			else {
				objectofrecord.createRecord(new Record("MTL1009","SR99997", "Nitin", "Rodrigues", "Comparative studies,APP", true, 
						"07/01/2018"));
				objectofrecord.createRecord(new Record("MTL1010","TR99996", "Kartik", "Nagpal", "Concordia", "234123", 
						"C++", "MTL"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void setORB(ORB ddoobjectorb) {
		this.ddoorb = ddoobjectorb;
	}


	/**
	 * This method creates a new record in DDO Server
	 */
	public boolean createTRecord(String managerID, String firstName, String lastName, String address, String phone, String specialization,
			String location) {
		String teacherID = GenerateID.getInstance().generateNewID("TR");
		Record tRecord = new Record(managerID, teacherID, firstName, lastName, address, phone, specialization, 
				location);
		if (!objectofrecord.createRecord(tRecord))
		{
			message1.setMessage(managerID + " failed to create " + tRecord.toString("TR"));
			return false;
		} 
		else{
			message1.setMessage(managerID + " created " + tRecord.toString("TR"));
			return true;
		}
	}

	public boolean signIn(String managerID){
		if(managerID.substring(0, 3).equalsIgnoreCase(sName)){
			this.managerID = managerID;		
			message1.setMessage("Manager " +managerID +" signed in. ");
			return true;
		}
		return false;
	}

	public String getRecordCounts() {
		message1.setMessage("Manager " +managerID + " requested for record count.");
		String result = "DDO" + " :- " + objectofrecord.fetchRecordCount();
		result += " LVL :- " + UDPhandlingClient(9214,null) + " MTL :- " + UDPhandlingClient(9213,null);
		return result;
	}

	public void signOut()
	{
		this.managerID = "";
		message1.setMessage("Manager " + managerID +" has signed out. ");
	}


	public boolean createSRecord(String managerID, String firstName, String lastName, String coursesRegistered, boolean status,
			String statusDate)  {
		String studentID = GenerateID.getInstance().generateNewID("SR");
		Record sRecord = new Record(managerID, studentID, firstName, lastName, coursesRegistered, status, 
				statusDate);

		if (!objectofrecord.createRecord(sRecord))

		{
			message1.setMessage(managerID +" failed to create " + sRecord.toString("SR"));
			return false;
		}

		else{
			message1.setMessage(managerID +" created " + sRecord.toString("SR"));
			return true;

		}		
	}

	/**
	 * This method edits an existing record in DDO Server
	 */
	public boolean editRecord(String managerID, String recordID, String fieldName, String newValue) {

		boolean editResult = objectofrecord.editRecord(recordID, fieldName, newValue);
		if (!editResult) {
			message1.setMessage(managerID + " failed to edit RecordID:- " + recordID);

		}
		else{
			message1.setMessage(managerID + " edited RecordID :- " + recordID + " changed (" + fieldName + ") to (" + newValue + ")");
		}
		return editResult;
	}

	
	
	public static void main(String [] args) {
		try {
			
			ORB objectorb = ORB.init(args, null);
		
			POA cobraimplroot = POAHelper.narrow(objectorb.resolve_initial_references("RootPOA"));
			cobraimplroot.the_POAManager().activate();

			
			org.omg.CORBA.Object objectofreference = objectorb.resolve_initial_references("NameService");
			
			NamingContextExt refncincorba = NamingContextExtHelper.narrow(objectofreference);

			
			CenterServers MTLServer = new CenterServers("MTL", 8758, 9213);
			MTLServer.setORB(objectorb);
			
			org.omg.CORBA.Object objectreference2 = cobraimplroot.servant_to_reference(MTLServer);
			CORBAImplementation objectreference3 = CORBAImplementationHelper.narrow(objectreference2);
			
			String name = "MTLServer";
			NameComponent nameobject[] = refncincorba.to_name(name);
			refncincorba.rebind(nameobject, objectreference3);
			System.out.println(MTLServer.sName + " server started");
			(new Thread(MTLServer)).start();

			CenterServers LVLServer = new CenterServers("LVL", 8759, 9214);
			LVLServer.setORB(objectorb);

			objectreference2 = cobraimplroot.servant_to_reference(LVLServer);
			objectreference3 = CORBAImplementationHelper.narrow(objectreference2);

			name = "LVLServer";
			nameobject = refncincorba.to_name(name);
			refncincorba.rebind(nameobject, objectreference3);
			System.out.println(LVLServer.sName + " server started");
			(new Thread(LVLServer)).start();

			CenterServers DDOServer = new CenterServers("DDO", 8760, 9215);
			DDOServer.setORB(objectorb);
			objectreference2 = cobraimplroot.servant_to_reference(DDOServer);
			objectreference3 = CORBAImplementationHelper.narrow(objectreference2);
			name = "DDOServer";
			nameobject = refncincorba.to_name(name);
			refncincorba.rebind(nameobject, objectreference3);
			System.out.println(DDOServer.sName + " server started");
			(new Thread(DDOServer)).start();

			objectorb.run();
		} catch (Exception e) {
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}
	}

	@Override
	public boolean transferRecord(String managerID, String recordID, String remoteCenterServerName) {
		Record alreadypresentR = objectofrecord.fetchRecordByID(recordID);
		if (alreadypresentR == null) {
			return false;
		} else {
			System.out.println("Existing record(Found during transfer) " + alreadypresentR.toString(alreadypresentR.getRecordID().substring(0, 2)));
			if (remoteCenterServerName.equalsIgnoreCase(managerID.substring(0, 3)))
				return false;
			
			if (remoteCenterServerName.equalsIgnoreCase("MTL")) {
				System.out.println("Record is being transferred to  MTL");
				if (this.UDPhandlingClient(9213, alreadypresentR).startsWith("true")) {
					message1.setMessage("Record has moved to MTL with ID :- " + alreadypresentR.getRecordID());
					System.out.println("There is successful transfer of Record with ID:-" + alreadypresentR.getRecordID()+"to MTL server");
					return this.Recordtobedeleted(managerID, recordID);
				} else {
					message1.setMessage("The transfer of Record with ID:-" + alreadypresentR.getRecordID() + " is unsuccesful.");
					System.out.println("The transferring of Record with ID:- " + alreadypresentR.getRecordID() + " to MTL server is unsuccessful.");
					return false;
				}
			}
			if (remoteCenterServerName.equalsIgnoreCase("LVL")) {
				System.out.println("Record is being transferred to LVL");
				if (this.UDPhandlingClient(9214, alreadypresentR).startsWith("true")) {
					message1.setMessage("Record has moved to LVL with ID :- " + alreadypresentR.getRecordID());
					System.out.println("There is successful transfer of Record with ID:- " + alreadypresentR.getRecordID()+" to LVL server");
					return this.Recordtobedeleted(managerID, recordID);
				}
				else {
					message1.setMessage("The transfer of Record with ID:- " + alreadypresentR.getRecordID() + " is unsuccesful." );
					System.out.println("The transferring of Record with ID:- " + alreadypresentR.getRecordID() + " to LVL server is unsuccessful.");
					return false;
				}
			}
			if (remoteCenterServerName.equalsIgnoreCase("DDO")) {
				System.out.println("Record is being transferred to DDO");
				if (this.UDPhandlingClient(9215, alreadypresentR).startsWith("true")) {
					message1.setMessage("Record has moved to DDO with ID :- " + alreadypresentR.getRecordID());
					System.out.println("There is successful transfer of Record with ID:- " + alreadypresentR.getRecordID()+" to DDO server");
					return this.Recordtobedeleted(managerID, recordID);
				}
				else {
					message1.setMessage("The transfer of Record with ID:- "+ alreadypresentR.getRecordID() +" is unsuccesful.");
					System.out.println("The transferring of Record with ID:- " + alreadypresentR.getRecordID() + " to DDO server is unsuccessful.");
					return false;
				}
			}
			return false;
		}
	}

	
	public boolean Recordtobedeleted(String Idofmanager, String IdofRecord) {
		if (objectofrecord.deletefromhashmap(IdofRecord)) {
			message1.setMessage(Idofmanager + "Deleting of Record with RecordID:- " + IdofRecord + " is successful");
			return true;
		} else {
			message1.setMessage(
					Idofmanager + "Deleting of Record with RecordID:- " + IdofRecord + " is unsuccessful");
			return false;
		}
	}

	public boolean addRecordtootherplace(Record getrecord) {
		if (this.objectofrecord.Recordaddition(getrecord)) {
			message1.setMessage("Record with RecordID:- " + getrecord.recordID+" is successfully added");
			return true;
		} else {
			message1.setMessage("Unable to add record with RecordID:- " + getrecord.recordID);
			return false;
		}
	}

	
	public String printRecords() {
		String stringrec = "";
		synchronized(objectofrecord) {
		for (ArrayList<Record> variablerefrence : objectofrecord.getRecordInfoTable().values()) {
			for (Record objrecord : variablerefrence) {
				stringrec += objrecord.toString(objrecord.getRecordID().substring(0, 2));
			}
		}
	return stringrec;}
	}
	
	
	public void run() {
		System.out.println("At port number: " + this.ListeningportUDP+ " " + this.sName + " UDP socket is listening");
		DatagramSocket datatransferSocket = null;
		try {
			datatransferSocket = new DatagramSocket(this.ListeningportUDP);
			DatagramPacket datareply = null;
			byte[] dataofbf = new byte[65536];
			while (true) {

				DatagramPacket datarequesting = new DatagramPacket(dataofbf, dataofbf.length);
				datatransferSocket.receive(datarequesting);

				byte[] streamdatarequest = datarequesting.getData();
				if (streamdatarequest[0] == 102) {
					String numberofc = "" + objectofrecord.fetchRecordCount();
					datareply = new DatagramPacket(numberofc.getBytes(), numberofc.getBytes().length, datarequesting.getAddress(),
							datarequesting.getPort());
					datatransferSocket.send(datareply);

				} else {

					ByteArrayInputStream Streamofdata = new ByteArrayInputStream(streamdatarequest);
					ObjectInputStream outputdatastream = new ObjectInputStream(Streamofdata);
					Record insertingtherecord = (Record) outputdatastream.readObject();
					String instatus = null;
					if (this.addRecordtootherplace(insertingtherecord)) {
						instatus = "true";
						datareply = new DatagramPacket(instatus.getBytes(), instatus.getBytes().length,
								datarequesting.getAddress(), datarequesting.getPort());
						datatransferSocket.send(datareply);

					} else {
						instatus = "false";
						datareply = new DatagramPacket(instatus.getBytes(), instatus.getBytes().length,
								datarequesting.getAddress(), datarequesting.getPort());
						datatransferSocket.send(datareply);

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String UDPhandlingClient(int pusedUDP, Record recToTransfer) {
		System.out.println("Port with Port Number:-"+ pusedUDP+" received a service request");
		String backmessage = null;
		DatagramSocket datatransferobj = null;
		byte[] args = new byte[1];

		try {
			InetAddress ahost = InetAddress.getByName("localhost");
			datatransferobj = new DatagramSocket();
			if (recToTransfer == null) {
				args[0] = 102;

				ByteArrayOutputStream Streamgoingout = new ByteArrayOutputStream();
				Streamgoingout.write(args);
				Streamgoingout.write(sName.getBytes());
				DatagramPacket request = new DatagramPacket(Streamgoingout.toByteArray(),
						Streamgoingout.toByteArray().length, ahost, pusedUDP);
				datatransferobj.send(request);
			
				System.out.println("waiting....");

				byte[] gatheringdata = new byte[65536];
				DatagramPacket responseofdatagather = new DatagramPacket(gatheringdata, gatheringdata.length);
				datatransferobj.receive(responseofdatagather);
				byte[] receivingdataresponse = responseofdatagather.getData();
				backmessage = new String(receivingdataresponse);
			} 
			else {
				ByteArrayOutputStream Sgoingoutin = new ByteArrayOutputStream();
				ObjectOutputStream streamofobject = new ObjectOutputStream(Sgoingoutin);
				streamofobject.writeObject(recToTransfer);
				byte[] datastore = Sgoingoutin.toByteArray();

				DatagramPacket requestingfordata = new DatagramPacket(Sgoingoutin.toByteArray(), Sgoingoutin.toByteArray().length,
						ahost, pusedUDP);
				datatransferobj.send(requestingfordata);

				DatagramPacket receivedataf = new DatagramPacket(datastore, datastore.length);
				datatransferobj.receive(receivedataf);
				byte[] receiveddatasave = receivedataf.getData();
				backmessage = new String(receiveddatasave);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return backmessage;
	}
}
