package cn.edu.hfuu.dao;

import cn.edu.hfuu.model.QueryStockInfo;
import cn.edu.hfuu.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by huzhipeng on 2017/3/15.
 */
@Transactional(readOnly = true)
 //   @Transactional
public interface StockDao extends JpaRepository<Stock,Integer>,JpaSpecificationExecutor<Stock> {

    //  @Query("select date,openingprice ,closingprice,maxprice,minprice from Stock where stockname = ?1 order by  date")
    //  List<QueryStockInfo>  queryStockByname(@Param("stockname") String stockname);

    @Query("from Stock where stockname = ?1 order by date")
    List<Stock> findByStockname(String stockname);

    @Query("from Stock where stocknum = ?1 order by date")
    List<Stock> findByStocknum(String stocknum);

    @Query("from Stock where stocknum like %:stockname%")
    List<Stock> findByStockname2(@Param("stockname") String stockname);

    @Query("select count(*) from Stock where stockname=?1")
    int countByStockName(String stockname);

    @Query("select count(*) from Stock where stocknum=?1")
    int countByStockNum(String stocknum);

    //通过stockname查找一条日期最大的数据
    @Query("from Stock where stockname=?1 and date=(select max(date) from Stock where stockname=?1)")
    Stock findMaxDateStockByStockName(String stockname);

    //通过stocknum查找一条日期最大的数据
    @Query("from Stock where stocknum=?1 and date=(select max(date) from Stock where stocknum=?1)")
    Stock findMaxDateStockByStockNum(String stocknum);


    //只有开始时间
    @Query("from Stock where stocknum=?1 and date> ?2 ")
    List<Stock> startFindByStockNum(String stocknum, Date startdate);

    @Query("from Stock where stockname=?1 and date>?2")
    List<Stock> startFindByStockName(String stocknum, Date startdate);

    //只有结束时间
    @Query("from Stock where stocknum=?1 and date<?2 ")
    List<Stock> endFindByStockNum(String stocknum, Date enddate);

    @Query("from Stock where stockname=?1 and date<?2 ")
    List<Stock> endFindByStockName(String stocknum, Date enddate);

    //既有开始时间，也有结束时间
    @Query("from Stock where stocknum=?1 and date>?2 and  date<?3")
    List<Stock> startendFindByStockNum(String stocknum, Date startdate, Date enddate);

    @Query("from Stock where stockname=?1 and date>?2 and date<?3")
    List<Stock> startendFindByStockName(String stockname,Date startdate, Date enddate);


    //根据时间查询单个股票
    @Query("from Stock where stockname=?1 and date=?2")
    Stock findStockByNameAndTime(String name ,Date date);

    @Query("from Stock where stocknum=?1 and date=?2")
    Stock findStockByNumAndTime(String name ,Date date);

    //查询某个时间段内的收盘价
    @Query("select closingprice from Stock where stockname=?1 and date>?2 and date<?3 order by date")
    List<String> findClosePricesByName(String stockname,Date startdate,Date enddate);

    @Query("select closingprice from Stock where stockNum=?1 and date>?2 and date<?3 order by date")
    List<String> findClosePricesByNum(String stockname,Date startdate,Date enddate);

    //查询时间段内的所有日期
    @Query("select date from Stock where stockname=?1 and date>?2 and date<?3 order by date")
    List<Date>   findAllDate(String stockname, Date startdate,Date enddate);
}