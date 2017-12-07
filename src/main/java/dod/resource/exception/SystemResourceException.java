package dod.resource.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by setu.poddar on 09/03/15.
 */
public class SystemResourceException extends WebApplicationException {

    public SystemResourceException(Integer code, String message ) {
        super(Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"" + message + "\",\"code\":\"" + code + "\"}").type(MediaType.APPLICATION_JSON).build());
    }

}
