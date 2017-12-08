package dod.resource;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.sun.org.apache.bcel.internal.generic.TABLESWITCH;
import dod.Product;
import dod.service.FederatorService;
import dod.service.OfferTags;
import dod.service.PricingService;
import dod.service.RatingService;
import dod.service.ZuluService;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Provider;

import javax.ws.rs.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.*;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final RatingService ratingService;

    private ZuluService zuluService;
    private FederatorService federatorService;

    private ObjectMapper objectMapper;
    private Provider<OfferTags> offerTagsProvider;

    @Inject
    public ApplicationResource(ZuluService zuluService, FederatorService federatorService, ObjectMapper objectMapper, Provider<PricingService> pricingServiceProvider, RatingService ratingService, Provider<OfferTags> offerTagsProvider) {
        this.zuluService = zuluService;
        this.federatorService = federatorService;
        this.objectMapper = objectMapper;
        this.pricingServiceProvider = pricingServiceProvider;
        this.ratingService = ratingService;
        this.offerTagsProvider = offerTagsProvider;
    }

    @GET
    @Timed(name = "tag")
    @UnitOfWork
    public String getSubstores(@Context UriInfo uriInfo) throws JsonProcessingException {
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        Map<String, String> pL = federatorService.getFederatorResponse(String.valueOf(queryParameters.getFirst("query")), Integer.valueOf(String.valueOf(queryParameters.getFirst("start"))));
        Map<String, Product> map = zuluService.getResponse(pL);
        List<String> lids = map.entrySet().stream().map(entry -> entry.getValue().getLid()).collect(Collectors.toCollection(LinkedList::new));
        Map<String, String> pricingTags = getBulkLowestPriceTag(lids);
        Map<String, String>  offerTag = getTimeTagsForProduct(lids);
        for (String s : map.keySet()) {
            Product product = map.get(s);
            String tag = pricingTags.get(product.getLid());
            if (tag != null) {
                if (product.getTags() == null)
                    product.setTags(new ArrayList<String>());
                product.getTags().add(tag);
                if(offerTag.get(s) != null)
                product.getTags().add(offerTag.get(s));
            }
        }

        map = ratingService.getRatingTags(map);
        return objectMapper.writeValueAsString(map);
    }

    @GET
    @Path("/price/low/listingId/{listingId}/time/{days}")
    public Long getLowestPriceInXDaysTag(@PathParam("listingId") String listingId, @PathParam("days") LongParam days) {
        log.info("getLowestPriceInXDaysTag for listing id {} and days {}", listingId, days.get());
        //TODO Change it to proper day to millis conversion
        return pricingServiceProvider.get().getLeastPriceInRange(listingId, days.get() * 1000000);
    }

    @POST
    @Path("/price/bulk")
    public Map<String, String> getBulkLowestPriceTag(List<String> listingId) {
        log.info("getLowestPriceInXDaysTag for listing ids {} ", listingId.toString());
        List<String> actuals = Lists.newArrayList();
        actuals.addAll(listingId.stream().filter(s -> s != null && !s.isEmpty()).collect(Collectors.toList()));
        return pricingServiceProvider.get().getTagsForListings(actuals);
    }

    @GET
    @Path("/time/tags")
    public Map<String, String> getTimeTagsForProduct(List<String> listingId){
        try {
            log.info("getTimeTagsForProduct for listing ids {} ", listingId.toString());
            List<String> actuals = Lists.newArrayList();
            actuals.addAll(listingId.stream().filter(s -> s != null && !s.isEmpty()).collect(Collectors.toList()));
            return offerTagsProvider.get().getOfferTagsForListing(actuals);
        } catch(Exception ex) {
            log.error("Exception occured in getTimeTagsForProduct "+ex);
            return (new HashMap<>());
        }
    }
}
