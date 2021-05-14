package com.example.viewtypes;

public class HeaderItem implements ItemTypeInterfaсe{
    private String headerText;

    public HeaderItem(String headerText) {
        this.headerText = headerText;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    @Override
    public int getType() {
        return  ItemTypeInterfaсe.HEADER;
    }
}