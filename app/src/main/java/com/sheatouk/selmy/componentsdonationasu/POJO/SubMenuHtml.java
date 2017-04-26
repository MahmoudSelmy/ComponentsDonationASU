package com.sheatouk.selmy.componentsdonationasu.POJO;

import java.util.List;

/**
 * Created by pc on 25/04/2017.
 */

public class SubMenuHtml {
    String name;
    List<SubMenuRowHtml> rows;

    public SubMenuHtml() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubMenuRowHtml> getRows() {
        return rows;
    }

    public void setRows(List<SubMenuRowHtml> rows) {
        this.rows = rows;
    }
}
