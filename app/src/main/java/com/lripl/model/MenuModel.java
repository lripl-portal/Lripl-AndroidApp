package com.lripl.model;

/**
 * Created by anupamchugh on 22/12/17.
 */

public class MenuModel {

    public String menuName, menuId;
    public boolean hasChildren, isGroup;
    public boolean isItemSelected;

    public MenuModel(String menuName, String menuId, boolean isGroup, boolean hasChildren) {
        this.menuName = menuName;
        this.menuId = menuId;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
    }

    public boolean isItemSelected() {
        return isItemSelected;
    }

    public void setItemSelected(boolean itemSelected) {
        isItemSelected = itemSelected;
    }
}
