package dod.service;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.util.Pool;

import java.util.Map;
import java.util.Set;

/**
 * Created by manas.kirti on 08/12/17.
 */
@Singleton
public class PricingService {

    private Pool<Jedis> jedisPool;

    @Inject
    public PricingService(Pool<Jedis> jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void updateListingPrice(String listingId, String price){
        try (Jedis jedis = jedisPool.getResource()) {
            //jedis.zadd(listingId, System.currentTimeMillis(), price);
            Set<Tuple> responses=jedis.zrangeWithScores(listingId, 0, -1);
            Map<String, Double> updatedPriceSet= Maps.newHashMap();
            updatedPriceSet.put(price, Double.parseDouble(Long.toString(System.currentTimeMillis())));
            for(Tuple t : responses){
                double timeStamp = t.getScore();
                //millis in 1month
                if (System.currentTimeMillis() - timeStamp < 7000000){
                    updatedPriceSet.put(t.getElement(), t.getScore());
                }
            }
            jedis.del(listingId);
            jedis.zadd(listingId, updatedPriceSet);
        }
    }

    public Long getLeastPriceInRange(String listingId, Long timeInMillis){
        Set<String>responses=null;
        long curTime = System.currentTimeMillis();
        try (Jedis jedis = jedisPool.getResource()) {
            responses = jedis.zrangeByScore(listingId, ((curTime - timeInMillis)>=0 ? (curTime - timeInMillis) : 0), curTime);
        }
        if(responses==null || responses.isEmpty()) return null;
        Long min = Long.MAX_VALUE;
        for(String t : responses){
            long l = Long.parseLong(t);
            if( l< min) min=l;
        }
        return min;
    }

    public Double getLeastPriceInRangeWithCurTime(String listingId, Long timeInMillis, Long curTime){
        Set<Tuple>responses=null;
        try (Jedis jedis = jedisPool.getResource()) {
            responses = jedis.zrangeWithScores(listingId, 0, -1);
        }
        if(responses==null || responses.isEmpty()) return null;
        for(Tuple t : responses){
            long priceTime = Long.parseLong(t.getElement());
            Double price = t.getScore();
            if(curTime <= timeInMillis + priceTime ) {
                return price;
            }
        }
        return null;
    }
}
