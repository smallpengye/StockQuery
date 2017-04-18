package cn.edu.hfuu.until;

import cn.edu.hfuu.dao.StockDao;
import cn.edu.hfuu.model.Stock;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by huzhipeng on 2017/3/26.
 */


public class SaveSheetStock {

    public static Stock row2Stock(Row row,String stockname,String stocknum){
        Stock stock=new Stock();
        stock.setStockname(stockname);
        stock.setStocknum(stocknum);
        stock.setDate(row.getCell(0).getDateCellValue());
        stock.setOpeningprice(row.getCell(1).toString());
        stock.setMaxprice(row.getCell(2).toString());
        stock.setMinprice(row.getCell(3).toString());
        stock.setClosingprice(row.getCell(4).toString());
        stock.setRiseandfall(row.getCell(5).toString());
        stock.setChangerate(row.getCell(6).toString());
        stock.setAverage(row.getCell(7).toString());
        return stock;
    }


}
