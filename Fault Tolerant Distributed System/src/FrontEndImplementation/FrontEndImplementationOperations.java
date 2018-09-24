package FrontEndImplementation;


/**
* FrontEndImplementation/FrontEndImplementationOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from FrontEndImplementation.idl
* Thursday, July 26, 2018 5:58:19 o'clock PM EDT
*/

public interface FrontEndImplementationOperations 
{
  boolean createTRecord (String managerID, String fName, String lName, String address, String phone, String specialization, String location);
  String getRecordCounts (String managerID);
  boolean editRecord (String managerID, String recordID, String fieldName, String newValue);
  boolean createSRecord (String managerID, String fName, String lName, String coursesRegistered, boolean status, String statusDate);
  boolean transferRecord (String managerID, String recordID, String remoteCenterServerName);
} // interface FrontEndImplementationOperations
