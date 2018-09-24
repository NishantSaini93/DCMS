package Servers;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;



@WebServiceClient(name = "LVLService", targetNamespace = "http://Servers/", wsdlLocation = "http://localhost:8080/LVL?wsdl")
public class LVLService extends Service{
	private final static WebServiceException LVL_EXCEPTION;
	private final static URL WSDL_LOCATION;
	 private final static QName qName = new QName("http://Servers/", "LVLService");
   
    
    static 
    {
    	URL dataurl = null;
        WebServiceException exceptiondata = null;
        try {
            dataurl = new URL("http://localhost:8080/LVL?wsdl");
        } catch (MalformedURLException e) {
        	exceptiondata = new WebServiceException(e);
        }
        WSDL_LOCATION = dataurl;
        LVL_EXCEPTION = exceptiondata;
    }
    public LVLService(URL wsdllocationdata) {
        super(wsdllocationdata, qName);
    }

	
    public LVLService(WebServiceFeature... WSFdata) {
        super(locationdata(), qName, WSFdata);
    }

	
	public LVLService(URL wsdllocationdata, QName nameofservice, WebServiceFeature... WSFdata) {
		super(wsdllocationdata, nameofservice, WSFdata);
		
	}

	protected LVLService() throws MalformedURLException {
		super(locationdata(), qName);
	}

	public LVLService(URL wsdllocationdata, QName nameofservice) {
		super(wsdllocationdata, nameofservice);
		
	}
	
	
	   

	    public LVLService(URL wsdllocationdata, WebServiceFeature... WSFdata) {
	        super(wsdllocationdata, qName, WSFdata);
	    }



	@WebEndpoint(name = "LVLServicePort")
    public IDCMSInterface getLVLServicePort(WebServiceFeature... WFSdata) {
        return super.getPort(new QName("http://Servers/", "LVLServicePort"), IDCMSInterface.class, WFSdata);
    }
	
	
	@WebEndpoint(name = "LVLServicePort")
    public IDCMSInterface getLVLServicePort() {
        return super.getPort(new QName("http://Servers/", "LVLServicePort"), IDCMSInterface.class);
    }
	
	
	private static URL locationdata() {
		if(LVL_EXCEPTION!=null) {
			throw new WebServiceException();
		}
		return WSDL_LOCATION;
	}
}
