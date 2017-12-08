package dod.resource;

import com.google.inject.Inject;
import dod.service.PricingService;
import io.dropwizard.jersey.params.LongParam;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
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

    private final Provider<PricingService> pricingServiceProvider;

    @Inject
    public ApplicationResource(Provider<PricingService> pricingServiceProvider) {
        this.pricingServiceProvider = pricingServiceProvider;
    }

    @GET
    @Path("/price/low/listingId/{listingId}/time/{days}")
    public Long getLowestPriceInXDaysTag(@PathParam("listingId")String listingId, @PathParam("days")LongParam days){
        log.info("getLowestPriceInXDaysTag for listing id {} and days {}", listingId, days.get());
        //TODO Change it to proper day to millis conversion
        return pricingServiceProvider.get().getLeastPriceInRange(listingId, days.get() * 1000000);
    }

    @POST
    @Path("/price/bulk")
    public Map<String, String> getBulkLowestPriceTag(List<String> listingId){
        log.info("getLowestPriceInXDaysTag for listing ids {} ", listingId.toString());
        return pricingServiceProvider.get().getTagsForListings(listingId);
    }
}
