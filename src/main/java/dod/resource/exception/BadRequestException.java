package dod.resource.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by setu.poddar on 06/05/15.
 */
public class BadRequestException extends WebApplicationException {

    public BadRequestException(Integer code, String message ) {
        super(Response.status(Response.Status.BAD_REQUEST).entity("{\"error_message\":\"" + message + "\",\"error_code\":\"" + code + "\"}").type(MediaType.APPLICATION_JSON).build());
    }
}
