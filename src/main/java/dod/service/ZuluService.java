package dod.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dod.Product;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;


public class ZuluService {

    private static final String URL = "http://10.47.1.8:31200/views?viewNames=product_attributes%2Cproduct_listing_info%2Cproduct_attributes_big%2Cproduct_meta_info%2Cproduct_base_info&entityIds=[FSN]";

    public Map<String, Product> getResponse(Map<String, String> productListingMap) {
        if (productListingMap.keySet().size() < 10) {
            return getZuluObjects(productListingMap.keySet(), productListingMap);
        } else {
            Map<String, Product> prodcutListing = new HashMap();
            Set<String> fsns = new HashSet<String>();
            Iterator<String> it = productListingMap.keySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                i++;
                fsns.add(it.next());
                if (i % 10 == 0) {
                    prodcutListing.putAll(getZuluObjects(fsns, productListingMap));
                    fsns.clear();
                }
            }
            return prodcutListing;
        }
    }

    private Map<String, Product> getZuluObjects(Set<String> productIds, Map<String, String> productListingMap) {
        Map<String, Product> prodcutListing = new HashMap();
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String uri = URL;
        Iterator<String> it = productIds.iterator();

        while (it.hasNext()) {
            String key = it.next();
            if (it.hasNext())
                uri = uri.replace("[FSN]", key + ",[FSN]");
            else
                uri = uri.replace("[FSN]", key);
        }
        HttpGet httpGet = new HttpGet(uri);
        httpGet.addHeader("z-clientid", "zulu.admin");
        httpGet.addHeader("z-requestid", "abc");
        httpGet.addHeader("z-timestamp", "123");
        httpGet.addHeader("content-type", "application/json");
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
            JSONArray array = ((JSONArray) jsonObject.getJSONArray("entityViews"));
            for (int i = 0; i < array.length(); i++) {

                JSONObject p = array.getJSONObject(i);
                if (p.has("view")) {
                    JSONObject v = p.getJSONObject("view");
                    if (v.has("key_features")) {
                        JSONArray jo = v.getJSONArray("key_features");
                        List<String> arrayList = new ArrayList(jo.length());
                        for (int j = 0; j < jo.length(); j++) {
                            arrayList.add(jo.getString(j));
                        }
                        prodcutListing.put(p.getString("entityId"), new Product(p.getString("entityId"), arrayList));
                    } else if(v.has("listings")){
                        JSONArray jo = v.getJSONArray("listings");
                        for (int k = 0; k < jo.length(); k++) {
                            JSONObject l = jo.getJSONObject(k);
                            String lId = l.getString("listing_id");
                            String pid = lId.substring(3,19);
                            if(lId.equals(productListingMap.get(pid))){
                                if(prodcutListing.get(pid) != null){
                                    prodcutListing.get(pid).setLid(lId);
                                } else {
                                    prodcutListing.put(pid, new Product(pid,lId));
                                }

                            }
                        }
                    }
                }
            }
            return prodcutListing;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        System.out.println(om.writeValueAsString(new ZuluService().getResponse(new FederatorService().getFederatorResponse("iphone", 0))));
    }
}
