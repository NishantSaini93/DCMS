package Servers;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;



@WebServiceClient(name = "MTLService", targetNamespace = "http://Servers/", wsdlLocation = "http://localhost:8080/MTL?wsdl")
public class MTLService extends Service{
	private final static QName qName = new QName("http://Servers/", "MTLService");
	private final static URL WSDL_LOCATION;
    private final static WebServiceException MTL_EXCEPTION;
	
    
    static {
    	URL dataurl = null;
        WebServiceException exceptiondata = null;
        try {
            dataurl = new URL("http://localhost:8080/MTL?wsdl");
        } catch (MalformedURLException e) {
        	exceptiondata = new WebServiceException(e);
        }
        WSDL_LOCATION = dataurl;
        MTL_EXCEPTION = exceptiondata;
    }
    public MTLService(URL wsdllocationdata, QName nameofservice) {
		super(wsdllocationdata, nameofservice);
		
	}	
	protected MTLService() throws MalformedURLException {
		super(locationdata(), qName);
	}
	
	
	public MTLService(URL wsdllocationdata, WebServiceFeature... WSFdata) {
        super(wsdllocationdata, qName, WSFdata);
    }


	
	 public MTLService(WebServiceFeature... WSFdata) {
	        super(locationdata(), qName, WSFdata);
	    }

	   public MTLService(URL wsdllocationdata) {
	        super(wsdllocationdata, qName);
	    }
	   public MTLService(URL wsdllocationdata, QName nameofservice, WebServiceFeature... WSFdata) {
			super(wsdllocationdata, nameofservice, WSFdata);
			
		}
	    


	@WebEndpoint(name = "MTLServicePort")
    public IDCMSInterface getMTLServicePort(WebServiceFeature... Wsfdata) {
        return super.getPort(new QName("http://Servers/", "MTLServicePort"), IDCMSInterface.class, Wsfdata);
    }
	
	
	@WebEndpoint(name = "MTLServicePort")
    public IDCMSInterface getMTLServicePort() {
        return super.getPort(new QName("http://Servers/", "MTLServicePort"), IDCMSInterface.class);
    }
	
	
	private static URL locationdata() {
		if(MTL_EXCEPTION!=null) {
			throw new WebServiceException();
		}
		return WSDL_LOCATION;
	}
}
