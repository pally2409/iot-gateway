package schooldomain.neu.pallaksingh.connecteddevices.labs.module07;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class TempResourceHandler extends CoapResource {

	public TempResourceHandler() {
		super("Temperature", true);
		// TODO Auto-generated constructor stub
	}
	
	public void handleGET(CoapExchange ce) {
		ce.respond(ResponseCode.VALID, "GET WORKED");
	}
	
	public void handlePOST(CoapExchange ce) {
        ce.respond(ResponseCode.VALID, "POST_REQUEST_SUCCESS");
    }
}
