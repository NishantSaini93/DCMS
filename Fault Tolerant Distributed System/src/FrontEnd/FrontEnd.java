package FrontEnd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import FrontEndImplementation.*;
import Ports.PortAssignment;
import RequestManagement.Request;
import Services.GenerateID;
import Services.LogEvent;

import net.rudp.ReliableServerSocket;
import net.rudp.ReliableSocket;


public class FrontEnd extends FrontEndImplementationPOA implements Runnable{

	public int ListeningportUDP;
	public static int MTLLeadPort = PortAssignment.UDPPortMTLServer1;
	public static int LVLLeadPort = PortAssignment.UDPPortLVLServer1;
	public static int DDOLeadPort = PortAssignment.UDPPortDDOServer1;
	static boolean isMTL1Alive;
	static boolean isMTL2Alive;
	static boolean isMTL3Alive;
	static boolean isLVL1Alive;
	static boolean isLVL2Alive;
	static boolean isLVL3Alive;
	static boolean isDDO1Alive;
	static boolean isDDO2Alive;
	static boolean isDDO3Alive;
	static final int server1ID = 1;
	static final int server2ID = 2;
	static final int server3ID = 3;
	static final int primaryID = 10;

	static HashMap<String, Integer> leader_status = new HashMap<String, Integer>(); 
	HashMap<Integer, String> heartBeatInfoMap = new HashMap<Integer, String>();
	private LogEvent message1 = null;
	private ORB orb;

	public void setORB(ORB ddoobjectorb) {
		this.orb = ddoobjectorb;
	}
	
	
	public FrontEnd(){
		this.message1 = new LogEvent("FrontEnd");
		this.ListeningportUDP = PortAssignment.UDPPort_FE;
		leader_status.put("DDOServer1", FrontEnd.primaryID);
		leader_status.put("MTLServer1", FrontEnd.primaryID);
		leader_status.put("LVLServer1", FrontEnd.primaryID);
		leader_status.put("DDOServer2", FrontEnd.server2ID);
		leader_status.put("MTLServer2", FrontEnd.server2ID);
		leader_status.put("LVLServer2", FrontEnd.server2ID);
		leader_status.put("DDOServer3", FrontEnd.server3ID);
		leader_status.put("MTLServer3", FrontEnd.server3ID);
		leader_status.put("LVLServer3", FrontEnd.server3ID);
	}

	public boolean createTRecord(String managerID, String firstName, String lastName, String address, String phone, String specialization,
			String location) {
		Request request = new Request();
		request.requestType = Request.CTR;
		request.managerID = managerID;
		request.recordID = GenerateID.getInstance().generateNewID("TR");
		request.fName = firstName;
		request.lName = lastName;
		request.address = address;
		request.phone = phone;
		request.specialization = specialization;
		request.location = location;
		message1.setMessage("Manager "+ managerID + ": Teacher record " + request.recordID + " has sent request to " + managerID.substring(0,3) + " leader.");
			String response = UDPhandlingClient(request);
			if(response.startsWith("true")) {
				message1.setMessage("Manager " + managerID + " created Teacher Record: " + request.recordID);
				return true;
			}
			return false;
	}

	public String getRecordCounts(String managerID) {
		Request request = new Request();
		request.requestType = Request.GCREQ;
		request.managerID = managerID;
		message1.setMessage("Manager " +managerID + " requested for record count.");
	
			String response = UDPhandlingClient(request);
			if(!response.startsWith("false")) {	
				System.out.println("Count Result: " + response);
				message1.setMessage("Manager " + managerID + ": Count Result:- " + response);
				return response;
			}
			else {
				return "Could not get response from " + managerID.substring(0,3) + " leader.";
			}
		}
	

	


	public boolean createSRecord(String managerID, String firstName, String lastName, String coursesRegistered, boolean status,
			String statusDate)  {
		Request request = new Request();
		request.requestType = Request.CSR;
		request.managerID = managerID;
		request.recordID = GenerateID.getInstance().generateNewID("SR");
		request.fName = firstName;
		request.lName = lastName;
		request.courseRegistered = coursesRegistered;
		request.status = status;
		request.statusDate = statusDate;
		message1.setMessage("Manager "+ managerID + ": Student record " + request.recordID + " has sent request to  " + managerID.substring(0,3) + " leader.");
			String response = UDPhandlingClient(request);
			if(response.startsWith("true")) {
				message1.setMessage("Manager " + managerID + " created Student Record: " + request.recordID);
				return true;
			}
			return false;
	}

	
	public boolean editRecord(String managerID, String recordID, String fieldName, String newValue) {
		Request request = new Request();
		request.requestType = Request.EREQ;
		request.managerID = managerID;
		request.recordID = recordID;
		request.fieldName = fieldName;
		request.newValue = newValue;

		message1.setMessage("Manager "+ managerID + ": sent edit request of Record ID: " + request.recordID + " to  " + managerID.substring(0,3) + " leader.");
			String response = UDPhandlingClient(request);
			if(response.startsWith("true")) {
				message1.setMessage(managerID + " edited RecordID :- " + recordID + " changed (" + fieldName + ") to (" + newValue + ")");
				return true;
			}
		message1.setMessage(managerID + " failed to edit RecordID:- " + recordID);
		return false;
	}

	
	
	public static void main(String [] args) {
		try {
			
			ORB objectorb = ORB.init(args, null);
		
			POA cobraimplroot = POAHelper.narrow(objectorb.resolve_initial_references("RootPOA"));
			cobraimplroot.the_POAManager().activate();

			
			org.omg.CORBA.Object objectofreference = objectorb.resolve_initial_references("NameService");
			
			NamingContextExt refncincorba = NamingContextExtHelper.narrow(objectofreference);

			
			FrontEnd fEnd = new FrontEnd();
			fEnd.setORB(objectorb);
			
			org.omg.CORBA.Object objectreference2 = cobraimplroot.servant_to_reference(fEnd);
			FrontEndImplementation objectreference3 = FrontEndImplementationHelper.narrow(objectreference2);
			
			String name = "FrontEnd";
			NameComponent nameobject[] = refncincorba.to_name(name);
			refncincorba.rebind(nameobject, objectreference3);
			System.out.println("Front End services started.");
			(new Thread(fEnd)).start();
			
			objectorb.run();
		} catch (Exception e) {
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}
	}

	@Override
	public boolean transferRecord(String managerID, String recordID, String remoteCenterServerName) {
		Request request = new Request();
		request.requestType = Request.TREQ;
		request.managerID = managerID;
		request.recordID = recordID;
		request.receivingServer = remoteCenterServerName;
			String response = UDPhandlingClient(request);
			if(response.startsWith("true")) {
				message1.setMessage("Manager " + managerID + ": transferred Record ID :- " + request.recordID + " to " + remoteCenterServerName);
				System.out.println("There is successful transfer of Record ID:-" + request.recordID +" to " + remoteCenterServerName);
				return true;
			}
		message1.setMessage("The transfer of Record ID:-" + request.recordID + " is unsuccesful.");
		System.out.println("The transferring of Record ID:- " + request.recordID + " to " + remoteCenterServerName + " server is unsuccessful.");
		return false;
	}
	
	
	public void run() {
		System.out.println("At port number: " + this.ListeningportUDP +  " Front End UDP socket is listening");
		FrontEnd.isMTL1Alive = true;
		FrontEnd.isMTL2Alive = true;
		FrontEnd.isMTL3Alive = true;
		FrontEnd.isLVL1Alive = true;
		FrontEnd.isLVL2Alive = true;
		FrontEnd.isLVL3Alive = true;
		FrontEnd.isDDO1Alive = true;
		FrontEnd.isDDO2Alive = true;
		FrontEnd.isDDO3Alive = true;
		int tmp1 = 0;
		int tmp2 = 0;
		int tmp3 = 0;
		int tmp4 = 0;
		int tmp5 = 0;
		int tmp6 = 0;
		int tmp7 = 0;
		int tmp8 = 0;
		int tmp9 = 0;
		
		checkAliveStatus();
		
		DatagramSocket datatransferSocket = null;
		ReliableServerSocket relSocket = null; 
		String heartBeat = "  ";
		try {
			datatransferSocket = new DatagramSocket(PortAssignment.UDPPortHeartBeat_FE);
			relSocket = new ReliableServerSocket(new DatagramSocket(9999),2000);
			byte[] myBuffer = new byte[50];
			Socket s = relSocket.accept();
			InputStream is = s.getInputStream();
			while (true) {
				DatagramPacket receivedPacket = new DatagramPacket(myBuffer, myBuffer.length);
				is.read();
				while (true) {
					try {

						datatransferSocket.receive(receivedPacket);
						heartBeat = new String(receivedPacket.getData());
						
						String[] mySplit = heartBeat.split("!");

						int portNumber = Integer.parseInt(mySplit[2].trim());

						if (portNumber == PortAssignment.UDPPortHeartBeat_MTL1) {
							heartBeatInfoMap.put(PortAssignment.UDPPortHeartBeat_MTL1, mySplit[1].trim());
							FrontEnd.isMTL1Alive = true;
							tmp1++;
							System.out.println("Status of MTL1: " + isMTL1Alive);
							if (mySplit[1].trim() != null) {
								FrontEnd.isMTL1Alive = true;
							}
						}else if ((tmp2-tmp1)>1 || (tmp3-tmp1)>1){
							FrontEnd.isMTL1Alive = false;
							leader_status.put("MTLServer1", -1);
							tmp1 = 0;
							tmp2 = 0;
							tmp3 = 0;
						}
							

						if (portNumber == PortAssignment.UDPPortHeartBeat_MTL2) {
							heartBeatInfoMap.put(PortAssignment.UDPPortHeartBeat_MTL2, mySplit[1].trim());
							FrontEnd.isMTL2Alive = true;
							tmp2++; 
							if (mySplit[1].trim() != null) {
								FrontEnd.isMTL2Alive = true;
							}
						} else if ((tmp1-tmp2)>1 || (tmp3-tmp2)>1){
							FrontEnd.isMTL2Alive = false;
							leader_status.put("MTLServer2", -1);
							tmp1 = 0;
							tmp2 = 0;
							tmp3 = 0;
						}
							
						
						if (portNumber == PortAssignment.UDPPortHeartBeat_MTL3) {
							heartBeatInfoMap.put(PortAssignment.UDPPortHeartBeat_MTL3, mySplit[1].trim());
							isMTL3Alive = true;
							tmp3++;
							if (mySplit[1].trim() != null) {
								isMTL3Alive = true;
							}
						}else if ((tmp1-tmp3)>1 || (tmp2-tmp3)>1){
							FrontEnd.isMTL3Alive = false;
							leader_status.put("MTLServer3", -1);
						    tmp1 = 0;
							tmp2 = 0;
							tmp3 = 0;
						}
						
						
						if (portNumber == PortAssignment.UDPPortHeartBeat_LVL1) {
							heartBeatInfoMap.put(PortAssignment.UDPPortHeartBeat_LVL1, mySplit[1].trim());
							FrontEnd.isLVL1Alive = true;
							tmp4++;
							System.out.println("Status of LVL1: " + isLVL1Alive);
							if (mySplit[1].trim() != null) {
								FrontEnd.isLVL1Alive = true;
							}
						}else if ((tmp5-tmp4)>1 || (tmp6-tmp4)>1){
							FrontEnd.isLVL1Alive = false;
							leader_status.put("LVLServer1", -1);
							tmp4 = 0;
							tmp5 = 0;
							tmp6 = 0;
						}
							

						if (portNumber == PortAssignment.UDPPortHeartBeat_LVL2) {
							heartBeatInfoMap.put(PortAssignment.UDPPortHeartBeat_LVL2, mySplit[1].trim());
							FrontEnd.isLVL2Alive = true;
							tmp5++; 
							if (mySplit[1].trim() != null) {
								FrontEnd.isLVL2Alive = true;
							}
						} else if ((tmp4-tmp5)>1 || (tmp6-tmp5)>1){
							FrontEnd.isLVL2Alive = false;
							leader_status.put("LVLServer2", -1);
							tmp4 = 0;
							tmp5 = 0;
							tmp6 = 0;
						}
							
						
						if (portNumber == PortAssignment.UDPPortHeartBeat_LVL3) {
							heartBeatInfoMap.put(PortAssignment.UDPPortHeartBeat_LVL3, mySplit[1].trim());
							FrontEnd.isLVL3Alive = true;
							tmp6++;
							if (mySplit[1].trim() != null) {
								FrontEnd.isLVL3Alive = true;
							}
						}else if ((tmp4-tmp6)>1 || (tmp5-tmp6)>1){
							FrontEnd.isLVL3Alive = false;
							leader_status.put("LVLServer3", -1);
						    tmp4 = 0;
							tmp5 = 0;
							tmp6 = 0;
						}
						
						
						
						if (portNumber == PortAssignment.UDPPortHeartBeat_DDO1) {
							heartBeatInfoMap.put(PortAssignment.UDPPortHeartBeat_DDO1, mySplit[1].trim());
							FrontEnd.isDDO1Alive = true;
							tmp7++;
							System.out.println("Status of DDO1: " + isDDO1Alive);
							if (mySplit[1].trim() != null) {
								FrontEnd.isDDO1Alive = true;
							}
						}else if ((tmp8-tmp7)>1 || (tmp9-tmp7)>1){
							FrontEnd.isDDO1Alive = false;
							leader_status.put("DDOServer1", -1);
							tmp7 = 0;
							tmp8 = 0;
							tmp9 = 0;
						}
							

						if (portNumber == PortAssignment.UDPPortHeartBeat_DDO2) {
							heartBeatInfoMap.put(PortAssignment.UDPPortHeartBeat_DDO2, mySplit[1].trim());
							FrontEnd.isDDO2Alive = true;
							tmp8++; 
							if (mySplit[1].trim() != null) {
								FrontEnd.isDDO2Alive = true;
							}
						} else if ((tmp7-tmp8)>1 || (tmp9-tmp8)>1){
							FrontEnd.isDDO2Alive = false;
							leader_status.put("DDOServer2", -1);
							tmp7 = 0;
							tmp8 = 0;
							tmp9 = 0;
						}
							
						
						if (portNumber == PortAssignment.UDPPortHeartBeat_DDO3) {
							heartBeatInfoMap.put(PortAssignment.UDPPortHeartBeat_DDO3, mySplit[1].trim());
							FrontEnd.isDDO3Alive = true;
							tmp9++;
							if (mySplit[1].trim() != null) {
								FrontEnd.isDDO3Alive = true;
							}
						}else if ((tmp7-tmp9)>1 || (tmp8-tmp9)>1){
							FrontEnd.isDDO3Alive = false;
							leader_status.put("DDOServer3", -1);
						    tmp7 = 0;
							tmp8 = 0;
							tmp9 = 0;
						}
						
						System.out.println(heartBeatInfoMap.get(PortAssignment.UDPPortHeartBeat_MTL1));
						System.out.println(heartBeatInfoMap.get(PortAssignment.UDPPortHeartBeat_MTL2));
						System.out.println(heartBeatInfoMap.get(PortAssignment.UDPPortHeartBeat_MTL3));	
						System.out.println(heartBeatInfoMap.get(PortAssignment.UDPPortHeartBeat_LVL1));
						System.out.println(heartBeatInfoMap.get(PortAssignment.UDPPortHeartBeat_LVL2));
						System.out.println(heartBeatInfoMap.get(PortAssignment.UDPPortHeartBeat_LVL3));	
						System.out.println(heartBeatInfoMap.get(PortAssignment.UDPPortHeartBeat_DDO1));
						System.out.println(heartBeatInfoMap.get(PortAssignment.UDPPortHeartBeat_DDO2));
						System.out.println(heartBeatInfoMap.get(PortAssignment.UDPPortHeartBeat_DDO3));	
					}
					catch (SocketTimeoutException e) {
						System.out.println("Socket Timeout Exception.");
						e.printStackTrace();
					}
				}
			}
		}
		catch (SocketException ex) {
			System.out.println("SocketException" + ex.getMessage());
		} catch (IOException e) {
			System.out.println("IOException :" + e.getMessage());

		}
	}

			
		
	
	
	public void checkAliveStatus() {

		System.out.println("Checking if all are alive.");
		TimerTask task = new TimerTask() {

			@Override
			public void run() { 
				System.out.println("Initial alive status " + FrontEnd.isMTL1Alive +", " + FrontEnd.isMTL2Alive + ", " + FrontEnd.isMTL3Alive + ", " + FrontEnd.isLVL1Alive + ", " + FrontEnd.isLVL2Alive + ", " + FrontEnd.isLVL3Alive + ", " + FrontEnd.isDDO1Alive + ", " + FrontEnd.isDDO2Alive + ", " + FrontEnd.isDDO3Alive);
				if (FrontEnd.isMTL1Alive == false && FrontEnd.MTLLeadPort == PortAssignment.UDPPortMTLServer1) {
					System.out.println("");
					BullyAlgorithm obj = new BullyAlgorithm();

					String newLeader = obj.MTLElection(leader_status, "MTLServer1");
					System.out.println("New Leader is: " + newLeader);
					message1.setMessage("Leader MTL1 went down. Running Election Algorithm.");
					if (newLeader.equals("MTLServer2")) {
						message1.setMessage("MTL2 is new MTLLeadPort having UDP port: " + FrontEnd.MTLLeadPort); 
					}
					else {
						message1.setMessage("MTL3 is new MTLLeadPort having UDP port: " + FrontEnd.MTLLeadPort); 
					}
					
				} 

				if (FrontEnd.isMTL2Alive == false && FrontEnd.MTLLeadPort == PortAssignment.UDPPortMTLServer2) {
					System.out.println("");
					BullyAlgorithm obj = new BullyAlgorithm();

					String newLeader = obj.MTLElection(leader_status, "MTLServer2");
					System.out.println("New Leader is: " + newLeader);
					message1.setMessage("Leader MTL2 went down. Running Election Algorithm.");
					if (newLeader.equals("MTLServer3")) {
						message1.setMessage("MTL3 is new MTLLeadPort having UDP port: " + FrontEnd.MTLLeadPort); 
					}
				} 
				
				
				
				
				if (FrontEnd.isLVL1Alive == false && FrontEnd.LVLLeadPort == PortAssignment.UDPPortLVLServer1) {
					System.out.println("");
					BullyAlgorithm obj = new BullyAlgorithm();

					String newLeader = obj.LVLElection(leader_status, "LVLServer1");
					System.out.println("New Leader is: " + newLeader);
					message1.setMessage("Leader LVL1 went down. Running Election Algorithm.");
					if (newLeader.equals("LVLServer2")) {
						message1.setMessage("LVL2 is new LVLLeadPort having UDP port: " + FrontEnd.LVLLeadPort); 
					}
					else {
						message1.setMessage("LVL3 is new LVLLeadPort having UDP port: " + FrontEnd.LVLLeadPort); 
					}
				} 

				if (FrontEnd.isLVL2Alive == false && FrontEnd.LVLLeadPort == PortAssignment.UDPPortLVLServer2) {
					System.out.println("");
					BullyAlgorithm obj = new BullyAlgorithm();

					String newLeader = obj.LVLElection(leader_status, "LVLServer2");
					System.out.println("New Leader is: " + newLeader);
					message1.setMessage("Leader LVL2 went down. Running Election Algorithm.");
					if (newLeader.equals("LVLServer3")) {
						message1.setMessage("LVL3 is new LVLLeadPort having UDP port: " + FrontEnd.LVLLeadPort); 
					}
				} 
				
				
				
				if (FrontEnd.isDDO1Alive == false && FrontEnd.DDOLeadPort == PortAssignment.UDPPortDDOServer1) {
					System.out.println("");
					BullyAlgorithm obj = new BullyAlgorithm();

					String newLeader = obj.DDOElection(leader_status, "DDOServer1");
					System.out.println("New Leader port: " + newLeader);
					message1.setMessage("Leader DDO1 went down. Running Election Algorithm.");
					if (newLeader.equals("DDOServer2")) {
						message1.setMessage("DDO2 is new DDOLeadPort having UDP port: " + FrontEnd.DDOLeadPort); 
					}
					else {
						message1.setMessage("DDO3 is new DDOLeadPort having UDP port: " + FrontEnd.DDOLeadPort); 
					}
				} 

				if (FrontEnd.isDDO2Alive == false && FrontEnd.DDOLeadPort == PortAssignment.UDPPortDDOServer2) {
					System.out.println("");
					BullyAlgorithm obj = new BullyAlgorithm();

					String newLeader = obj.DDOElection(leader_status, "DDOServer2");
					System.out.println("New Leader port: " + newLeader);
					message1.setMessage("Leader DDO2 went down. Running Election Algorithm.");
					if (newLeader.equals("DDOServer3")) {
						message1.setMessage("DDO3 is new DDOLeadPort having UDP port: " + FrontEnd.DDOLeadPort); 
					}
				} 
			}
		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(task, 4000, 4000);
	}
	

	public synchronized String UDPhandlingClient(Request request) {
		String backmessage = "";
		DatagramSocket datatransferobj = null;
		ReliableSocket relSocket = null;
		try {
			synchronized(request) {
			while(FrontEnd.MTLLeadPort == 0  ||  FrontEnd.LVLLeadPort == 0 || FrontEnd.DDOLeadPort == 0) {
				System.out.println("Leader ports not initialized.");
			};
			
				ByteArrayOutputStream streamGoingOut = new ByteArrayOutputStream();
				ObjectOutput output = new ObjectOutputStream(streamGoingOut);
				output.writeObject(request);
				output.close();
				if(request.managerID.startsWith("MTL"))
					System.out.println("Record ID: " + request.recordID + " sent to MTL leader listening on port: " + FrontEnd.MTLLeadPort + 
												" through front end port: " + this.ListeningportUDP);
				else if(request.managerID.startsWith("LVL"))
					System.out.println("Record ID: " + request.recordID + " sent to LVL leader listening on port: " + FrontEnd.LVLLeadPort + 
													" through front end port: " + this.ListeningportUDP);
				else
					System.out.println("Record ID: " + request.recordID + " sent to DDO leader listening on port: " + FrontEnd.DDOLeadPort + 
													" through front end port: " + this.ListeningportUDP);
				
				
				InetAddress ahost = InetAddress.getByName("localhost");
				datatransferobj = new DatagramSocket(this.ListeningportUDP);
				byte[] msg = streamGoingOut.toByteArray();
				relSocket = new ReliableSocket(new DatagramSocket());
				relSocket.connect(new InetSocketAddress(ahost,9999));
				if(request.managerID.startsWith("MTL"))
					System.out.println("Record with request type: " + request.requestType + " sent to MTL leader listening on port: " + FrontEnd.MTLLeadPort);
				else if(request.managerID.startsWith("LVL"))
					System.out.println("Record with request type: " + request.requestType + " sent to LVL leader listening on port: " + FrontEnd.LVLLeadPort);
				else
					System.out.println("Record with request type: " + request.requestType + " sent to DDO leader listening on port: " + FrontEnd.DDOLeadPort);
				
				DatagramPacket response = null;
				if(request.managerID.startsWith("MTL"))
					response = new DatagramPacket(msg, msg.length, ahost, FrontEnd.MTLLeadPort);
				else if(request.managerID.startsWith("LVL"))
					response = new DatagramPacket(msg, msg.length, ahost, FrontEnd.LVLLeadPort);
				else 
					response = new DatagramPacket(msg, msg.length, ahost, FrontEnd.DDOLeadPort);
				
				datatransferobj.setSoTimeout(5000);
				datatransferobj.send(response);
			
				byte[] gatheringdata = new byte[65536];
				DatagramPacket responseofdatagather = new DatagramPacket(gatheringdata, gatheringdata.length);
				datatransferobj.receive(responseofdatagather);
				relSocket.write(msg, 0, msg.length);
				backmessage = new String(responseofdatagather.getData());
			} 
		}catch(SocketTimeoutException e) {
			message1.setMessage("No response from " + request.managerID.substring(0,3) + " leader. Sending request again.");
			if(datatransferobj !=null) {
				datatransferobj.close();
			}
			return UDPhandlingClient(request);
		}
		
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(datatransferobj !=null) {
				datatransferobj.close();
			}
		}
		return backmessage;
	}

}
