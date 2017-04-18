package cn.edu.hfuu.until;

import cn.edu.hfuu.model.Stock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huzhipeng on 2017/4/9.
 */
public class ListStock2EchartsData {
    public static List<List<Object>> ListStock2EchartsData(List<Stock> stocks){
        List<List<Object>> datas=new ArrayList<List<Object>>();

        for(Stock stock :stocks){
            List<Object>  data=new ArrayList<>();
            data.add(stock.getDate().toString());
            data.add(Float.parseFloat(stock.getOpeningprice()));
            data.add(Float.parseFloat(stock.getClosingprice()));
            data.add(Float.parseFloat(stock.getMaxprice()));
            data.add(Float.parseFloat(stock.getMinprice()));
            datas.add(data);
        }

        return  datas;
    }
}
