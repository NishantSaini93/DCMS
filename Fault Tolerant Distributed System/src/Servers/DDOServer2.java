package Servers;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import RecordManagement.Record;
import RequestManagement.HeartBeatCounter;
import RequestManagement.Request;
import Services.LogEvent;
import CORBAImplementation.CORBAImplementation;
import CORBAImplementation.CORBAImplementationHelper;
import CORBAImplementation.CORBAImplementationPOA;
import Ports.PortAssignment;
import net.rudp.ReliableSocket;

public class DDOServer2 extends CORBAImplementationPOA implements Runnable {
	
	static int count ;
	String sName = "";
	int sPort = 0;
	HashMapRecord objectofrecord;
	int ListeningportUDP = 0;
	LogEvent message1=null;
	private ORB ddoorb;
	String managerID = "";
	DatagramSocket conSocket = null;
	DatagramSocket conSocket2 = null;
	DatagramSocket conSocket3 =null;

	protected DDOServer2(String serverName, int s1Port, int UDP_Port1) {
		super();
		sPort = s1Port;
		objectofrecord = new HashMapRecord();
		ListeningportUDP = UDP_Port1;
		this.sName = serverName;
		message1= new LogEvent(serverName);
		
		objectofrecord.createRecord(new Record("DDO1001", "SR99995", "Nishant", "Saini", "Algorithms",true, 
					"08/01/2018"));
		objectofrecord.createRecord(new Record("DDO1002","TR99994", "Jayant", "Verma", "ENCS", "345897", 
					"Python", "DDO"));
		
		try {
			conSocket = new DatagramSocket(this.ListeningportUDP);
			conSocket2 = new DatagramSocket(PortAssignment.UDPPort2DDOServer2);
			conSocket3 = new DatagramSocket(PortAssignment.UDPPort3DDOServer2);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void setORB(ORB ddoobjectorb) {
		this.ddoorb = ddoobjectorb;
	}


	/**
	 * This method creates a new record in DDO Server
	 */
	public boolean createTRecord(String managerID, String recordID, String firstName, String lastName, String address, String phone, String specialization,
			String location) {
		Record tRecord = new Record(managerID, recordID, firstName, lastName, address, phone, specialization, 
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

	public String getRecordCounts() {
		message1.setMessage("Manager " +managerID + " requested for record count.");
		String result = "DDO" + " :- " + objectofrecord.fetchRecordCount();
		Request req = new Request();
		req.requestType = Request.GMYCREQ;
		result += " LVL :- " + UDPhandlingClient(PortAssignment.UDPPortLVLServer2,req) + " MTL :- " + UDPhandlingClient(PortAssignment.UDPPortMTLServer2,req);
		return result;
	}


	public boolean createSRecord(String managerID, String recordID, String firstName, String lastName, String coursesRegistered, boolean status,
			String statusDate)  {
		Record sRecord = new Record(managerID, recordID, firstName, lastName, coursesRegistered, status, 
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

			
			DDOServer2 DDOServer2 = new DDOServer2("DDOServer2", PortAssignment.DDOServer2, PortAssignment.UDPPortDDOServer2);
			DDOServer2.setORB(objectorb);
			
			org.omg.CORBA.Object objectreference2 = cobraimplroot.servant_to_reference(DDOServer2);
			CORBAImplementation objectreference3 = CORBAImplementationHelper.narrow(objectreference2);
			
			String name = "DDOServer2";
			NameComponent nameobject[] = refncincorba.to_name(name);
			refncincorba.rebind(nameobject, objectreference3);
			System.out.println(DDOServer2.sName + " server started");
			(new Thread(DDOServer2)).start();
			new Thread(new Runnable() {

				public void run() {
					System.out.println("I am in thread of DDOServer2");
					HearBeat();
				}

			}).start();

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
			
			Request req = new Request();
			req.requestType = Request.TROWNREQ;
			req.record = alreadypresentR;
			
			if (remoteCenterServerName.equalsIgnoreCase("MTL")) {
				System.out.println("Record is being transferred to  MTL");
				if (this.UDPhandlingClient(PortAssignment.UDPPortMTLServer2, req).startsWith("true")) {
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
				if (this.UDPhandlingClient(PortAssignment.UDPPortLVLServer2, req).startsWith("true")) {
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
	
	@Override
	public void finalize() {
	    System.out.println("Inside finalize of Server: " + this.sName);
	    conSocket.close();
	  }
	
	public void run() {
		System.out.println("At port number: " + this.ListeningportUDP+ " " + this.sName + " UDP socket is listening");
		try {
				
			DatagramPacket reply = null;
			ReliableSocket relSocket = null;
			byte[] buffer = new byte[65536];
			byte[] buffer2 = new byte[65536];
			byte[] buffer3 = new byte[65536];
			while (true) {
				String result = "false", result2 = "false", result3 = "false";
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				conSocket.receive(request);
				relSocket = new ReliableSocket(new DatagramSocket());
				relSocket.connect(new InetSocketAddress(InetAddress.getLocalHost(),9999));
				message1.setMessage("Request received at DDOServer2 from port: "+request.getPort());
				byte[] requestByteArray = request.getData();
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestByteArray);
				ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
				Request reqReceived = (Request) objectInputStream.readObject();
				message1.setMessage("Request is: " + reqReceived.toString());
				
				System.out.println("Server Name is" + this.sName);
			
				if(reqReceived.requestType == Request.GMYCREQ) {
					result = "" + this.objectofrecord.fetchRecordCount(); 
				}
				
				else if(reqReceived.requestType == Request.TROWNREQ){
					if(this.addRecordtootherplace(reqReceived.record)) {
						result = "true";
					}
				}
				else {
				
				if (reqReceived.requestType == Request.CTR) {
					boolean createTrecordSuccess = createTRecord(reqReceived.managerID, reqReceived.recordID,
							reqReceived.fName, reqReceived.lName, reqReceived.address, reqReceived.phone,
							reqReceived.specialization, reqReceived.location);
					if (createTrecordSuccess) {
						message1.setMessage("Teacher Record :"+reqReceived.recordID+" has been created successfully.");
						System.out.println("DDOServer2:Teacher is added successfully.");
						result = "true";
					} else {
						message1.setMessage("Failed: Teacher Record has not been created.");
						System.out.println("DDOServer2:Error: Teacher is not added.");
						result = "false";
					}
				} else if (reqReceived.requestType == Request.CSR) {
					boolean createSrecordSucess = createSRecord(reqReceived.managerID, reqReceived.recordID,
							reqReceived.fName, reqReceived.lName, reqReceived.courseRegistered,
							reqReceived.status, reqReceived.statusDate);
					if (createSrecordSucess) {
						message1.setMessage("Student Record :"+reqReceived.recordID+" has been created successfully.");
						System.out.println("DDOServer2:Student is added successfully.");
						result = "true";
					} else {
						 message1.setMessage("Failed: Student Record has not been created.");
						System.out.println("DDOServer2:Error: Student is not added.");
						result = "false";
					}
				} else if (reqReceived.requestType == Request.EREQ) {
					if (editRecord(reqReceived.managerID, reqReceived.recordID, reqReceived.fieldName,
							reqReceived.newValue)) {
						message1.setMessage("Records edited" + " Record field -'" + reqReceived.fieldName
								+ "' Record Value - '" + reqReceived.newValue + "'");
						System.out.println("DDOServer2:Record is successfully edited.");
						result = "true";
					} else {
						message1.setMessage("Failed: Unable to edit record" + reqReceived.recordID);
						System.out.println("DDOServer2:Record is not existed or new value is not valid");
						result = "false";
					}
				} else if (reqReceived.requestType == Request.TREQ) {
					message1.setMessage(">>>>>>>>DDOServer2: in request transfer.."+reqReceived.managerID+" "+reqReceived.recordID+" "+reqReceived.receivingServer);
					if (transferRecord(reqReceived.managerID, reqReceived.recordID, reqReceived.receivingServer)) {
						message1.setMessage("Record ID: " + reqReceived.recordID + " has been moved to location "
								+ reqReceived.receivingServer);
						System.out.println("DDOServer2:Transfer successfull of Record:" + reqReceived.recordID + " to location"
								+ reqReceived.receivingServer);
						result = "true";
					} else {
						message1.setMessage("Transfer of Record " + reqReceived.recordID + " has been failed.");
						System.out.println("DDOServer2:Transfer unsuccessfull of Record:" + reqReceived.recordID);
						result = "false";
					}
				} else if (reqReceived.requestType == Request.GCREQ) {
					message1.setMessage("Requested for count on all servers");
					String recordInfo = getRecordCounts();
					message1.setMessage("Server response: (Total record number: " + recordInfo + " )");
					System.out.println("DDOServer2:Records are: " + recordInfo);
					System.out.println(">>>>sending to port"+request.getPort());
					result = "true";
					System.out.println("DDOServer2: string send is"+result);
				}
				//after executing on its own
				//broadcasting to other Slaves
				message1.setMessage("Result after executing on DDOServer2 servers is "+result);
				if (result.startsWith("true")) {
					message1.setMessage("Request has sucessfully executed on "+ this.sName);
					if (request.getPort() == PortAssignment.UDPPort_FE && reqReceived.requestType!= Request.GCREQ) {
						
						message1.setMessage("Request executed sucessfully so broadcasting to other Slaves");

												
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ObjectOutput oo = new ObjectOutputStream(bos);
						oo.writeObject(reqReceived);
						oo.close();
											
						byte[] serializedMsg = bos.toByteArray();
						InetAddress ahost = InetAddress.getLocalHost();
						DatagramPacket request2 = new DatagramPacket(serializedMsg, serializedMsg.length, ahost,
								PortAssignment.UDPPortDDOServer1);
						
						conSocket2.setSoTimeout(4000);
						
						try {
							message1.setMessage("******Sending to DDOServer1******");
							conSocket2.send(request2);
							relSocket.write(serializedMsg, 0, serializedMsg.length);
							message1.setMessage("******Sent to DDOServer1******");
							DatagramPacket reply2 = new DatagramPacket(buffer2, buffer2.length);
							conSocket2.receive(reply2);
							message1.setMessage("******Received reply from DDOServer1******");
							byte[] resultRecieved2 = reply2.getData();
							result2 = new String(resultRecieved2);
							message1.setMessage("Result received from DDOServer1 after Broadcast: "+result2);
						} catch (SocketTimeoutException e) {
							System.out.println("Socket has timed out to send to DDOServer1");
						}
						
						DatagramPacket request3 = new DatagramPacket(serializedMsg, serializedMsg.length, ahost,
								PortAssignment.UDPPortDDOServer3);
						
						conSocket3.setSoTimeout(4000);
						
						try {
							message1.setMessage("******Sending to DDOServer3******");
							conSocket3.send(request3);
							relSocket.write(serializedMsg, 0, serializedMsg.length);
							message1.setMessage("******Sent to DDOServer3******");
							DatagramPacket reply3 = new DatagramPacket(buffer3, buffer3.length);
							conSocket3.receive(reply3);
							message1.setMessage("******Received reply from DDOServer3******");
							byte[] resultRecieved3 = reply3.getData();
							result3 = new String(resultRecieved3);
							message1.setMessage("Result received from DDOServer3 after Broadcast: "+result3);
						} catch (SocketTimeoutException e) {
							System.out.println("Socket has timed out to send to DDOServer3");
						}
						
					} 
				}
				}
						
				// send reply back
				System.out.println("******Sending to port: "+request.getPort());
				reply = new DatagramPacket(result.getBytes(), result.getBytes().length, request.getAddress(),
						request.getPort());
				relSocket.write(result.getBytes(), 0, result.getBytes().length);
				conSocket.send(reply);
		} 
		}catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public String UDPhandlingClient(int pusedUDP, Request req) {
		System.out.println("Port with Port Number:-"+ pusedUDP+" received a service request");
		String backmessage = null;
		DatagramSocket datatransferobj = null;
		ReliableSocket relSocket = null;
		try {
			InetAddress ahost = InetAddress.getByName("localhost");
			datatransferobj = new DatagramSocket();
			relSocket = new ReliableSocket(new DatagramSocket());
			relSocket.connect(new InetSocketAddress(ahost,9999));
				ByteArrayOutputStream Streamgoingout = new ByteArrayOutputStream();
				ObjectOutput oo = new ObjectOutputStream(Streamgoingout);
				oo.writeObject(req);
				oo.close();
				DatagramPacket request = new DatagramPacket(Streamgoingout.toByteArray(),
						Streamgoingout.toByteArray().length, ahost, pusedUDP);
				datatransferobj.send(request);
				relSocket.write(Streamgoingout.toByteArray(), 0, Streamgoingout.toByteArray().length);
				byte[] gatheringdata = new byte[65536];
				DatagramPacket responseofdatagather = new DatagramPacket(gatheringdata, gatheringdata.length);
				datatransferobj.receive(responseofdatagather);
				byte[] receivingdataresponse = responseofdatagather.getData();
				backmessage = new String(receivingdataresponse);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			if(datatransferobj!=null) {
				datatransferobj.close();
			}
		}
		return backmessage;
	}
	
	
	public static void HearBeat() {

		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				System.out.println("Inside heartbeat timer");

				DatagramSocket datagramSocket;
				ReliableSocket relSocket = null;
				try {
					datagramSocket = new DatagramSocket(PortAssignment.UDPPortHeartBeat_DDO2);
					System.out.println("Inside try");
					relSocket = new ReliableSocket(new DatagramSocket());
					relSocket.connect(new InetSocketAddress(InetAddress.getByName("localhost"),9999));
					String message = "DDOServer2 is Alive!" + HeartBeatCounter.getInstance().getNextInteger() + "!" + PortAssignment.UDPPortHeartBeat_DDO2;
					
					System.out.println("DDO Server2 "+ message.split("!")[1]);
					InetAddress address = InetAddress.getLocalHost();
					byte[] bufferSend = message.getBytes();

					DatagramPacket sendRequestpacket = new DatagramPacket(bufferSend, bufferSend.length, address,
							PortAssignment.UDPPortHeartBeat_FE);
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(relSocket.getOutputStream()));
					bw.write(message);
					bw.flush();
					datagramSocket.send(sendRequestpacket);
					System.out.println("sent packet");

					if (datagramSocket != null)
						datagramSocket.close();

				} catch (Exception e) {
					System.err.println("ERROR: " + e);
					e.printStackTrace(System.out);
				}
			}
		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(task, 1, 4000);

	}
}