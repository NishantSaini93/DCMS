package Servers;


public class StartServers {
	/**
	 * Starts the servers at locations MTL, LVL and DDO
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DDOServer.startDDOServer();
		LVLServer.startLVLServer();
		MTLServer.startMTLServer();
	}

}
