package dod.service;

import com.google.inject.Inject;
import dod.dal.dao.ProductDAO;
import dod.dal.model.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingService {

    protected ProductDAO productDAO;
    private Integer threshold = 4;

    @Inject
    public RatingService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Map<String, String> getRatingTags(List<String> productIds) {
        Map<String, String> productRatingMap = new HashMap<String, String>();
        List<Product> products = productDAO.getProducts(productIds);
        for (Product product : products) {
            if (product.getRating() > threshold)
                productRatingMap.put(product.getId(), "4+ Rated");
        }
        return productRatingMap;
    }
}
