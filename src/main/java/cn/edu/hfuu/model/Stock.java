package cn.edu.hfuu.model;

import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by huzhipeng on 2017/3/15.
 */
@Entity
@Repository
@Table(name="stocks")
public class Stock{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stocknum;

    private String stockname;


    private Date date;

    private String openingprice;

    private String maxprice;
    private String minprice;
    private String closingprice;
    private String riseandfall;

    private  String changerate;
    private String average;
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

    public String getRiseandfall() {
        return riseandfall;
    }

    public void setRiseandfall(String riseandfall) {
        this.riseandfall = riseandfall;
    }

    public String getChangerate() {
        return changerate;
    }

    public void setChangerate(String changerate) {
        this.changerate = changerate;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStocknum() {
        return stocknum;
    }

    public void setStocknum(String stocknum) {
        this.stocknum = stocknum;
    }

    public String getStockname() {
        return stockname;
    }

    public void setStockname(String stockname) {
        this.stockname = stockname;
    }

}
