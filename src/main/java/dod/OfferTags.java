package dod;

import dod.dal.dao.ListingDAO;
import dod.dal.model.Listing;
import dod.dal.model.Offer;
import dod.dal.model.Tag;

import javax.inject.Inject;
import java.util.*;

public class OfferTags {

    private ListingDAO listingDAO;

    @Inject
    public OfferTags(ListingDAO listingDAO) {
        this.listingDAO = listingDAO;
    }



    private  Map<String, String> getOfferTagsForListing(List<String> listingIds) throws Exception {
        Map<String, SortedSet<Tag>> listingIdToTags = new HashMap<>();
        List<Listing> listings = listingDAO.getProducts(listingIds);
        for(Listing listing: listings) {
            Set<Offer> offerSet = listing.getOffers();
            Comparator<Tag> tagComparator= new Comparator<Tag>(){
                @Override public int compare(Tag t1, Tag t2) {
                    if(t1.getStartTime().compareTo(t2.getStartTime()) > 0)
                        return 1;
                    return -1;
            }};
            SortedSet<Tag> tagForListing = new TreeSet<>(tagComparator);
            for(Offer offer : offerSet) {
                Set<Tag> tags = offer.getTags();
                for (Tag tag : tags) {
                    tagForListing.add(tag);
                }
            }
            listingIdToTags.put(listing.getId(), tagForListing);
        }


        Map<String, String> listingIdToTag = new HashMap<>();
        for (String listingId : listingIds) {
            if(listingIdToTags.get(listingId) == null)
                continue;
            for(Tag tag: listingIdToTags.get(listingId)){
                if(startOrEndTimeWithinLimits(tag)) {
                    listingIdToTag.put(listingId, tag.getTag());
                    break;
                }
            }

        }

        return listingIdToTag;
    }

    private  boolean startOrEndTimeWithinLimits(Tag tag) {
        Date date = new Date();
        if ( date.after(tag.getStartTime())   && date.before(tag.getEndTime())  ) {
            return true;
        }
        return false;
    }

}
