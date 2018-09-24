package Servers;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;



@WebServiceClient(name = "DDOService", targetNamespace = "http://Servers/", wsdlLocation = "http://localhost:8080/DDO?wsdl")
public class DDOService extends Service
{
	
	private final static URL WSDL_LOCATION;
	private final static QName qName = new QName("http://Servers/", "DDOService");
    private final static WebServiceException DDO_EXCEPTION;
	
    
    static 
    {
    	URL dataurl = null;
        WebServiceException exceptiondata = null;
        try 
        {
            dataurl = new URL("http://localhost:8080/DDO?wsdl");
        } 
        catch (MalformedURLException e)
        {
        	exceptiondata = new WebServiceException(e);
        }
        WSDL_LOCATION = dataurl;
        DDO_EXCEPTION = exceptiondata;
    }
		
    public DDOService(WebServiceFeature... WSFdata) 
    {
        super(locationdata(), qName, WSFdata);
    }
	
	
	public DDOService(URL wsdllocationdata, QName nameofserver, WebServiceFeature... WSFdata)
	{
		super(wsdllocationdata, nameofserver, WSFdata);
		
	}


	protected DDOService() throws MalformedURLException {
		super(locationdata(), qName);
	}
	
	   public DDOService(URL wsdllocationdata, WebServiceFeature... WSFdata) {
	        super(wsdllocationdata, qName, WSFdata);
	    }

	public DDOService(URL wsdllocationdata, QName nameofservice) 
	{
		super(wsdllocationdata, nameofservice);
		
	}
	
	

	   public DDOService(URL wsdllocationdata) 
	   {
	        super(wsdllocationdata, qName);
	    }

	 


	@WebEndpoint(name = "DDOServicePort")
    public IDCMSInterface getDDOServicePort(WebServiceFeature... WSFdata) {
        return super.getPort(new QName("http://Servers/", "DDOServicePort"), IDCMSInterface.class, WSFdata);
    }
	
	
	@WebEndpoint(name = "DDOServicePort")
    public IDCMSInterface getDDOServicePort() {
        return super.getPort(new QName("http://Servers/", "DDOServicePort"), IDCMSInterface.class);
    }
	
	
	private static URL locationdata() {
		if(DDO_EXCEPTION!=null) {
			throw new WebServiceException();
		}
		return WSDL_LOCATION;
	}
}
