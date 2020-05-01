package com.example.xml_200415_jy;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "response")
public class TrainData {
    @ElementList(name = "items")
    @Path("body")
    private List<Item> itemList;

    List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}


