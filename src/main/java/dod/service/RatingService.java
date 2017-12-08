package dod.service;

import com.google.inject.Inject;
import dod.dal.dao.ProductDAO;
import dod.Product;

import java.util.*;
import java.util.stream.Collectors;

public class RatingService {

    protected ProductDAO productDAO;
    private Integer threshold = 4;

    @Inject
    public RatingService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Map<String, Product> getRatingTags(Map<String, Product> productIds) {
        List<dod.dal.model.Product> products = productDAO.getProducts(productIds.keySet().stream().collect(Collectors.toList()));
        for (dod.dal.model.Product product : products) {
            if (product.getRating() > threshold) {

                if (productIds.get(product.getId()).getTags() == null)
                    productIds.get(product.getId()).setTags(new ArrayList());
                productIds.get(product.getId()).getTags().add("4+ Rated");
            }
        }
        return productIds;
    }
}
