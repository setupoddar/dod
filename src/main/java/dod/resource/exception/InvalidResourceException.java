package dod.resource.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by setu.poddar on 22/02/15.
 */
public class InvalidResourceException extends WebApplicationException {

    public InvalidResourceException() {
        super(Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"" + "requested resource not found, please validate the data and try again." + "\"}").type(MediaType.APPLICATION_JSON).build());
    }

    public InvalidResourceException(String message) {
        super(Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"" + message + "\"}").type(MediaType.APPLICATION_JSON).build());
    }
}
