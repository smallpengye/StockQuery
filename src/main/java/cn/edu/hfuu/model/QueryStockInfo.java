package cn.edu.hfuu.model;

import java.util.Date;

/**
 * Created by huzhipeng on 2017/4/9.
 */
public class QueryStockInfo {

    private Date date;
    private String openingprice;

    private String maxprice;
    private String minprice;
    private String closingprice;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOpeningprice() {
        return openingprice;
    }

    public void setOpeningprice(String openingprice) {
        this.openingprice = openingprice;
    }

    public String getMaxprice() {
        return maxprice;
    }

    public void setMaxprice(String maxprice) {
        this.maxprice = maxprice;
    }

    public String getMinprice() {
        return minprice;
    }

    public void setMinprice(String minprice) {
        this.minprice = minprice;
    }

    public String getClosingprice() {
        return closingprice;
    }

    public void setClosingprice(String closingprice) {
        this.closingprice = closingprice;
    }


}
