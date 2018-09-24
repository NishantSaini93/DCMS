package Servers;
/**
 * Contains Center server methods and data for Laval location
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import RecordManagement.Record;
import Servers.IRMIInterface;
import Services.GenerateID;
import Services.LogEvent;

public class LVLServer extends UnicastRemoteObject implements IRMIInterface, Runnable{
	int sPort = 0;
	int UDP_Port = 0;
	HashMapRecord rec;
	String managerID = "";
	LogEvent message1=null;
//	private static final long serialVersionUID = 98599739739L;

	protected LVLServer(int s1Port, int UDP_Port1) throws RemoteException {
		super();
		sPort = s1Port;
		rec = new HashMapRecord();
		UDP_Port = UDP_Port1;
		message1= new LogEvent("LVL");
		try {
		rec.createRecord(new Record("SR99999", "Ashish", "Sharma", new String [] { "Distributed Systems"}, true, 
				new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2018")));
		rec.createRecord(new Record("TR99998", "Pradeep", "Verma", "Concordia", "123456", 
				"C#", "LVL"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean createTRecord(String firstName, String lastName, String address, String phone, String specialization,
			String location) throws RemoteException {
		String teacherID = GenerateID.getInstance().generateNewID("TR");
		Record tRecord = new Record(teacherID, firstName, lastName, address, phone, specialization, 
				location);
		if (!rec.createRecord(tRecord))
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
		if(managerID.substring(0, 3).equalsIgnoreCase("LVL")){
			this.managerID = managerID;		
			message1.setMessage("Manager " +managerID +" signed in. ");
			return true;
		}
		return false;
	}

	public String getRecordCounts() throws RemoteException {
		message1.setMessage("Manager " +managerID + " requested for record count.");
		String result = "LVL" + " :- " + rec.fetchRecordCount();
		result += " MTL :- " + UDPClient(9213) + " DDO :- " + UDPClient(9215);
		return result;
	}

	public void signOut(){
		this.managerID = "";
		message1.setMessage("Manager " + managerID +" has signed out. ");
	}
	
	public boolean createSRecord(String firstName, String lastName, String[] courseRegisterd, boolean status,
			Date statusDate) throws RemoteException {
		String studentID = GenerateID.getInstance().generateNewID("SR");
		Record sRecord = new Record(studentID, firstName, lastName, courseRegisterd, status, 
				statusDate);

		if (!rec.createRecord(sRecord))
		{
			message1.setMessage(managerID +" failed to create " + sRecord.toString("SR"));
			return false;
		}
		
		else{
			message1.setMessage(managerID +" created " + sRecord.toString("SR"));
			return true;	
		}		
	}

	public boolean editRecord(String recordID, String fieldName, String newValue) throws Exception {

		boolean editResult = rec.editRecord(recordID, fieldName, newValue);
		if (!editResult) {
			message1.setMessage(managerID + " failed to edit RecordID:- " + recordID);
			
		}
		else{
			message1.setMessage(managerID + " edited RecordID :- " + recordID + " changed (" + fieldName + ") to (" + newValue + ")");
		}
		return editResult;
	}

		// Servers startup*****
	public static void startLVLServer() throws RemoteException, MalformedURLException{
		Registry reg = LocateRegistry.createRegistry(8759);
		LVLServer lvl = new LVLServer(8759, 9214);
		Naming.rebind("rmi://localhost:" + lvl.sPort + "/LVL", lvl);
		System.out.println("*********LVL Server started*********");
		(new Thread(lvl)).start();
	}

	public void run() {
		System.out.println("UDP Socket for LVL is listening on port number: " + this.UDP_Port);
		DatagramSocket dataSocket = null;
		try {
			dataSocket = new DatagramSocket(this.UDP_Port);
			byte[] buf = new byte[65536];
			while (true) {
				DatagramPacket req1 = new DatagramPacket(buf, buf.length);
				dataSocket.receive(req1);
				String recCount = String.valueOf(rec.fetchRecordCount());
				DatagramPacket response = new DatagramPacket(recCount.getBytes(), recCount.getBytes().length,
						req1.getAddress(), req1.getPort());
				dataSocket.send(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method is used to communicate with other servers.
	 * @param port
	 * @return response
	 */
	public String UDPClient(int port) {
		System.out.println("Request for service at port number:- " + port);
		String strResponse = null;
		DatagramSocket dataSocket = null;
		try {
			dataSocket = new DatagramSocket();
			InetAddress newHost = InetAddress.getByName("localhost");
			DatagramPacket request = new DatagramPacket("LVL".getBytes(), "LVL".getBytes().length,
					newHost, port);
			dataSocket.send(request);
			byte[] buf = new byte[65536];
			DatagramPacket response = new DatagramPacket(buf, buf.length);
			dataSocket.receive(response);
			byte[] result = response.getData();
			strResponse = new String(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResponse;
	}

}
