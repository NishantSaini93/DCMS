package Servers;
/**
 * The Java RMI interface which is implemented by the servers at various location
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface IRMIInterface extends Remote{
	public boolean signIn (String mgID) throws RemoteException;
	public void signOut () throws RemoteException;
	public boolean createTRecord(String fName, String lName, String address, String phone, 
			String specialization, String location) throws RemoteException;
	public String getRecordCounts() throws RemoteException;
	public boolean editRecord(String recordID, String fieldName, String newValue) throws Exception;
	public boolean createSRecord(String fName, String lName, String[] coursesRegistered, boolean status,
			Date statusDate) throws RemoteException;
}
