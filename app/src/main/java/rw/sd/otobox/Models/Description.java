package rw.sd.otobox.Models;

/**
 * Created by miller on 2/5/18.
 */

public class Description {
    private String desc_name;
    private String desc_desc;

    public Description() {
    }

    public Description(String desc_name, String desc_desc) {
        this.desc_name = desc_name;
        this.desc_desc = desc_desc;
    }

    public String getDesc_name() {
        return desc_name;
    }

    public void setDesc_name(String desc_name) {
        this.desc_name = desc_name;
    }

    public String getDesc_desc() {
        return desc_desc;
    }

    public void setDesc_desc(String desc_desc) {
        this.desc_desc = desc_desc;
    }
}
