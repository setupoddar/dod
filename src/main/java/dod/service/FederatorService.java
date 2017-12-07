package dod.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class FederatorService {

    private static final String URL = "http://10.47.1.95:25290/sherlock/v1/stores/tyy/4io/iterator?query-guide=true&q=[QUERY]&client.tag=mobile-app-retail&products.count=60&products.type=listing&geoBrowse=enabled&query-stub=true&products.start=[START]&redirection=true&smart-classify=true";

    public Map<String, String> getFederatorResponse(String query, Integer start) {
        Map<String, String> prodcutListing = new HashMap();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(URL.replace("[QUERY]", query).replace("[START]", Integer.toString(start)));
        httpGet.addHeader("x-flipkart-client", "hack");
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
            JSONArray array = (JSONArray) ((JSONObject) ((JSONObject) jsonObject.get("RESPONSE")).get("products")).get("listing-ids");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jo = (JSONObject) array.get(i);
                prodcutListing.put(jo.getString("product-id"), jo.getString("listing-id"));
            }
            return prodcutListing;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
