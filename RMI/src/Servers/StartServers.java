package Servers;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class StartServers {
	/**
	 * Starts the servers at locations MTL, LVL and DDO
	 */
	public static void main(String[] args) throws RemoteException, MalformedURLException{
		// TODO Auto-generated method stub
		DDOServer.startDDOServer();
		LVLServer.startLVLServer();
		MTLServer.startMTLServer();
	}

}
