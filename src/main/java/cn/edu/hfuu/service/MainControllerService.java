package cn.edu.hfuu.service;

import cn.edu.hfuu.dao.StockDao;
import cn.edu.hfuu.model.Stock;
import cn.edu.hfuu.until.CalendarDate;
import cn.edu.hfuu.until.ListDate2ListString;
import cn.edu.hfuu.until.ListStock2EchartsData;
import cn.edu.hfuu.until.SaveSheetStock;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huzhipeng on 2017/3/26.
 */
@Service
public class MainControllerService {
    @Autowired
    private StockDao stockDao;


   public String saveexeldata(FileInputStream inputStream){
       Workbook workbook= null;
       Sheet sheet=null;
       int sheetnum=0;
       try {
           workbook = WorkbookFactory.create(inputStream);
           sheetnum=workbook.getNumberOfSheets();
       } catch (IOException e) {
           e.printStackTrace();
       } catch (InvalidFormatException e) {
           e.printStackTrace();
       }


    //   sheet=workbook.getSheetAt(0);
       for(int j=0;j<sheetnum;j++) {
           sheet=workbook.getSheetAt(j);
           //rownum获取的是lastrownum的位置
           int rowNums=sheet.getLastRowNum();
           String stockname = sheet.getRow(2).getCell(2).toString();
           String stocknum = sheet.getRow(1).getCell(2).toString();
           Stock stock = null;
           for (int i = 4; i <=rowNums; i++) {
               Row sheetRow = sheet.getRow(i);
               stock = SaveSheetStock.row2Stock(sheetRow, stockname, stocknum);
               stockDao.save(stock);
           }
       }
       return "sucess";
   }

   public  List<List<Object>>  queryStock(JSONObject jsonObject){
       Date startdate=null;
       Date enddate=null;
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
       if(jsonObject.containsKey("startDate")) {
           try {
               startdate = sdf.parse(jsonObject.get("startDate").toString());
           } catch (ParseException e) {
               e.printStackTrace();
           }
       }
       if(jsonObject.containsKey("endDate")){
           try {
               enddate=sdf.parse(jsonObject.get("endDate").toString());
           } catch (ParseException e) {
               e.printStackTrace();
           }
       }
       String stock = jsonObject.get("stockName").toString();
       List<Stock> stocks = new ArrayList<Stock>();
       List<List<Object>> datas = new ArrayList<List<Object>>();

       if(startdate==null&&enddate==null) {

           if (stock.contains("SH")||stock.contains("sh")) {
               stocks = stockDao.findByStocknum(stock);
               //     stocks=stockDao.queryStockBynum(stock);
           } else {
               stocks = stockDao.findByStockname(stock);
               //  stocks=stockDao.queryStockByname(stock);
           }
           datas = ListStock2EchartsData.ListStock2EchartsData(stocks);
       }else if(startdate!=null&&enddate==null){
           if (stock.contains(".sh")) {
               stocks = stockDao.startFindByStockNum(stock,startdate);
           } else {
               stocks = stockDao.startFindByStockName(stock,startdate);
           }
           datas = ListStock2EchartsData.ListStock2EchartsData(stocks);

       }else if(startdate==null&&enddate!=null){
           if (stock.contains(".sh")) {
               stocks = stockDao.endFindByStockNum(stock,enddate);
           } else {
               stocks = stockDao.endFindByStockName(stock,enddate);
           }
           datas = ListStock2EchartsData.ListStock2EchartsData(stocks);

       }else if(startdate!=null&&enddate!=null){
           if (stock.contains(".sh")) {
               stocks = stockDao.startendFindByStockNum(stock,startdate,enddate);
           } else {
               stocks = stockDao.startendFindByStockName(stock,startdate,enddate);
           }
           datas = ListStock2EchartsData.ListStock2EchartsData(stocks);
       }else {
           return null;
       }
       return datas;
   }

    public List<String> getAllDate(String startdate ,String enddate,int amount){
       List<String> alldata=new ArrayList<String>();
       int i=0;
       while(!startdate.equals(enddate)){
           String newdate=CalendarDate.getNewDate(startdate,1);
           alldata.add(newdate);
           startdate=newdate;
       }

       for(int j=1;i<amount;i++){
           String newdate1=CalendarDate.getNewDate(startdate,j);
           alldata.add(newdate1);
       }
       return  alldata;
    }

    public List<String> getAllDate2(String stockname,String startdate ,String enddate,int amount){
        List<String> alldata=new ArrayList<String>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date startdate1=null;
        try {
             startdate1=sdf.parse(startdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date enddate1=null;
        try {
             enddate1=sdf.parse(enddate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //查询出来的所有的日期
        List<Date> alldate=stockDao.findAllDate(stockname,startdate1,enddate1);
        List<String> alldates= ListDate2ListString.getListString(alldate);
        for(int i=0;i<amount;i++){
            alldates.add("预测第"+i+"天");
        }

        return alldata;
    }
}
