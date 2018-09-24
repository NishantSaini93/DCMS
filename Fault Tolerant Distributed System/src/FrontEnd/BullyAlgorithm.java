package FrontEnd;

import java.util.HashMap;

import Ports.PortAssignment;

public class BullyAlgorithm {
	
	public String MTLElection(HashMap<String, Integer> leader_status , String downServer){
		System.out.println("Running MTL Election Algorithm");
		
		String newLeader = "";
		if(FrontEnd.isMTL1Alive == false && downServer.equals("MTLServer1")) {
				FrontEnd.leader_status.put("MTLServer2", 10);
				FrontEnd.MTLLeadPort = PortAssignment.UDPPortMTLServer2;
				FrontEnd.leader_status.put("LVLServer2", 10);
				FrontEnd.LVLLeadPort = PortAssignment.UDPPortLVLServer2;
				FrontEnd.leader_status.put("DDOServer2", 10);
				FrontEnd.DDOLeadPort = PortAssignment.UDPPortDDOServer2;
				FrontEnd.leader_status.put("LVLServer1", FrontEnd.server1ID);
				FrontEnd.leader_status.put("DDOServer1", FrontEnd.server1ID);
				newLeader = "MTLServer2";
		}
		
		else if(FrontEnd.isMTL2Alive == false && downServer.equals("MTLServer2")) {
				FrontEnd.leader_status.put("MTLServer3", 10);
				FrontEnd.MTLLeadPort = PortAssignment.UDPPortMTLServer3;
				FrontEnd.leader_status.put("LVLServer3", 10);
				FrontEnd.LVLLeadPort = PortAssignment.UDPPortLVLServer3;
				FrontEnd.leader_status.put("DDOServer3", 10);
				FrontEnd.DDOLeadPort = PortAssignment.UDPPortDDOServer3;
				FrontEnd.leader_status.put("LVLServer2", FrontEnd.server2ID);
				FrontEnd.leader_status.put("DDOServer2", FrontEnd.server2ID);
				newLeader = "MTLServer3";
		}
		
		
			System.out.println("New MTL Leader ID is: " + newLeader);
			return newLeader;
	}
	
	
	public String LVLElection(HashMap<String, Integer> leader_status , String downServer){
		System.out.println("Running LVL Election Algorithm");
      
		String newLeader = "";
		if(FrontEnd.isLVL1Alive == false && downServer.equals("LVLServer1")) {
				FrontEnd.leader_status.put("LVLServer2", 10);
				FrontEnd.LVLLeadPort = PortAssignment.UDPPortLVLServer2;
				FrontEnd.leader_status.put("MTLServer2", 10);
				FrontEnd.MTLLeadPort = PortAssignment.UDPPortMTLServer2;
				FrontEnd.leader_status.put("DDOServer2", 10);
				FrontEnd.DDOLeadPort = PortAssignment.UDPPortDDOServer2;
				FrontEnd.leader_status.put("MTLServer1", FrontEnd.server1ID);
				FrontEnd.leader_status.put("DDOServer1", FrontEnd.server1ID);
				newLeader = "LVLServer2";
		}
		
		else if(FrontEnd.isLVL2Alive == false && downServer.equals("LVLServer2")) {
				FrontEnd.leader_status.put("LVLServer3", 10);
				FrontEnd.LVLLeadPort = PortAssignment.UDPPortLVLServer3;
				FrontEnd.leader_status.put("MTLServer3", 10);
				FrontEnd.MTLLeadPort = PortAssignment.UDPPortMTLServer3;
				FrontEnd.leader_status.put("DDOServer3", 10);
				FrontEnd.DDOLeadPort = PortAssignment.UDPPortDDOServer3;
				FrontEnd.leader_status.put("MTLServer2", FrontEnd.server2ID);
				FrontEnd.leader_status.put("DDOServer2", FrontEnd.server2ID);
				newLeader = "LVLServer3";
		}
		
		
			System.out.println("New LVL Leader ID is: " + newLeader);
			return newLeader;
	}
	
	
	public String DDOElection(HashMap<String, Integer> leader_status , String downServer){
		System.out.println("Running DDO Election Algorithm");
      
		String newLeader = "";
		if(FrontEnd.isDDO1Alive == false && downServer.equals("DDOServer1")) {
				FrontEnd.leader_status.put("DDOServer2", 10);
				FrontEnd.DDOLeadPort = PortAssignment.UDPPortDDOServer2;
				FrontEnd.leader_status.put("MTLServer2", 10);
				FrontEnd.MTLLeadPort = PortAssignment.UDPPortMTLServer2;
				FrontEnd.leader_status.put("LVLServer2", 10);
				FrontEnd.LVLLeadPort = PortAssignment.UDPPortLVLServer2;
				FrontEnd.leader_status.put("MTLServer1", FrontEnd.server1ID);
				FrontEnd.leader_status.put("LVLServer1", FrontEnd.server1ID);
				newLeader = "DDOServer2";
			
		}
		
		else if(FrontEnd.isDDO2Alive == false && downServer.equals("DDOServer2")) {
				FrontEnd.leader_status.put("DDOServer3", 10);
				FrontEnd.DDOLeadPort = PortAssignment.UDPPortDDOServer3;
				FrontEnd.leader_status.put("MTLServer3", 10);
				FrontEnd.MTLLeadPort = PortAssignment.UDPPortMTLServer3;
				FrontEnd.leader_status.put("LVLServer3", 10);
				FrontEnd.LVLLeadPort = PortAssignment.UDPPortLVLServer3;
				FrontEnd.leader_status.put("MTLServer2", FrontEnd.server2ID);
				FrontEnd.leader_status.put("LVLServer2", FrontEnd.server2ID);
				newLeader = "DDOServer3";
			
		}
		
		
			System.out.println("New DDO Leader ID is: " + newLeader);
			return newLeader;
	}
	
}
