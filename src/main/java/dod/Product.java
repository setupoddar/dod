package dod;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class Product {

    String id;
    String lid;
    List<String> keyFeatures;
    List<String> tags;

    public Product(String id, List<String> keyFeatures) {
        this.id = id;
        this.keyFeatures = keyFeatures;
    }

    public Product(String pid, String lId) {
        this.id = pid;
        this.lid = lId;
        this.keyFeatures = new ArrayList<String>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getKeyFeatures() {
        return keyFeatures;
    }

    public void setKeyFeatures(List<String> keyFeatures) {
        this.keyFeatures = keyFeatures;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }
}
