package cn.edu.hfuu.service;

import cn.edu.hfuu.dao.StockDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Created by huzhipeng on 2017/3/26.
 */
@Service
public class StockService  {
    @Autowired
    StockDao stockDao;
    public List<Integer> getStockClosingPrice(String stock , Date startdate, Date enddate){

        return null;
    }
}
