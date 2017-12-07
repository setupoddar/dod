package dod.service;

import com.google.inject.Inject;
import dod.dal.dao.ProductDAO;

import java.util.List;
import java.util.Map;

public class RatingService {

    protected ProductDAO productDAO;

    @Inject
    public RatingService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Map<String,String> getRatingTags(List<String> productIds){


        productDAO.getProducts(productIds);
        return null;

    }
}
