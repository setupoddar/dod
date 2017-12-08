package dod.resource;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import dod.service.FederatorService;
import dod.service.ZuluService;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.Map;

/**
 * Created by setu.poddar on 26/05/17.
 */
@Path("/tag")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/tag", description = "Application up Related APIs")
@Slf4j
public class ApplicationResource {


    private ZuluService zuluService;

    private FederatorService federatorService;

    private ObjectMapper objectMapper;

    @Inject
    public ApplicationResource(ZuluService zuluService, FederatorService federatorService, ObjectMapper objectMapper) {
        this.zuluService = zuluService;
        this.federatorService = federatorService;
        this.objectMapper = objectMapper;
    }

    @GET
    @Timed(name = "tag")
    @UnitOfWork
    public String getSubstores(@Context UriInfo uriInfo) throws JsonProcessingException {
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        Map<String, String> pL = federatorService.getFederatorResponse(String.valueOf(queryParameters.getFirst("query")), Integer.valueOf(String.valueOf(queryParameters.getFirst("start"))));
        return objectMapper.writeValueAsString(zuluService.getResponse(pL).values());
    }

}
