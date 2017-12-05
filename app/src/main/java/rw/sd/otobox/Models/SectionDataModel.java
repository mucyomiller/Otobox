package rw.sd.otobox.Models;

/**
 * Created by miller on 11/28/17.
 */

import java.util.ArrayList;

public class SectionDataModel {
    private String headerTitle;
    private String headerSubTitle;
    private ArrayList<Product> allItemsInSection;


    public SectionDataModel() {

    }
    public SectionDataModel(String headerTitle,String headerSubTitle,ArrayList<Product> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.headerSubTitle = headerSubTitle;
        this.allItemsInSection = allItemsInSection;
    }



    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getHeaderSubTitle() {return headerSubTitle;}

    public void setHeaderSubTitle(String headerSubTitle) {this.headerSubTitle = headerSubTitle;}


    public ArrayList<Product> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<Product> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


}