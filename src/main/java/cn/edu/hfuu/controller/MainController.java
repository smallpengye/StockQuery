package cn.edu.hfuu.controller;

import cn.edu.hfuu.dao.StockDao;
import cn.edu.hfuu.dao.UserDao;
import cn.edu.hfuu.model.QueryStockInfo;
import cn.edu.hfuu.model.Stock;
import cn.edu.hfuu.model.User;
import cn.edu.hfuu.service.MainControllerService;
import cn.edu.hfuu.service.UserService;
import cn.edu.hfuu.until.ForcaseStock;
import cn.edu.hfuu.until.ListStock2EchartsData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class MainController {


    @Autowired
    private UserDao userDao;
    @Autowired
    private StockDao stockDao;
    @Autowired
    private UserService userService;
    @Autowired
    private MainControllerService mainControllerService;


    @RequestMapping("/register")
    @ResponseBody
    public Object test1(String userName, String userPsd) {
        User user = new User();
        List<User> users=userDao.findByUsername2(userName);
        if(users.size()==0){
            user.setUsername(userName);
            user.setPassword(userPsd);
            user.setRoles("user");
            userDao.save(user);
            return "success";
        }else{
            return "fail";
        }

    }


    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        String filetype = file.getOriginalFilename();
        String type = filetype.substring(filetype.indexOf("."));
        String path=this.getClass().getResource("/").getPath();
        File f = new File(path+ "public/uploadfile/");
        if(!f.exists()){
            f.mkdirs();
        }
        if(type.equalsIgnoreCase(".xlsx") || type.equalsIgnoreCase(".xls")) {
            String filename=file.getOriginalFilename();
            file.transferTo(new File(f+"/"+filename));
            FileInputStream inputStream=new FileInputStream(new File(f+"/"+filename));
            String statu=mainControllerService.saveexeldata(inputStream);
        } else {
            return "notexcel";
        }
      return "sucess";
    }


    //查询股票
    @RequestMapping("/querystok")
    @ResponseBody
    public Object test3(@RequestBody JSONObject jsonObject) {
        List<List<Object>> datas = new ArrayList<List<Object>>();
        datas=mainControllerService.queryStock(jsonObject);
        return datas;
    }


    //查询具体的单日信息
    @RequestMapping("/getDetailedInfo")
    @ResponseBody
    public Object getDetailedInfo(@RequestBody JSONObject jsonObject){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(jsonObject.get("date").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Date date=new Date.(jsonObject.get("date").toString());
        String name=jsonObject.get("name").toString();
        Stock stock=new Stock();
        if(name.contains("sh")||name.contains("SH")){
             stock =stockDao.findStockByNumAndTime(name,date);
        }else{
             stock =stockDao.findStockByNameAndTime(name,date);
        }


        return stock;
    }

    @RequestMapping("/getalluser")
    @ResponseBody
    public Object getalluser(){

        List<User> users=userDao.findAll();
        List<Map> usersAuth=new ArrayList<>();
        for(User user:users){
            Map<String,String> userAuth=new HashMap<>();
            userAuth.put("name",user.getUsername());
            userAuth.put("roles",user.getRoles());
            usersAuth.add(userAuth);
        }
        return usersAuth;
    }

    @RequestMapping("/collectionStock")
    @ResponseBody
    public String collectionStock(@RequestBody JSONObject jsonObject){
        String stockNameorNum= jsonObject.get("stockname").toString();
        String userName=jsonObject.get("userName").toString();
        User user= userDao.findOneByUsername(userName);
        int num=0;
        Stock stock=new Stock();
        if(stockNameorNum.contains(".sh")){
          // num=stockDao.countByStockNum(stock);
            stock= stockDao.findMaxDateStockByStockNum(stockNameorNum);
        }else{
          //  num=stockDao.countByStockName(stock);
            stock= stockDao.findMaxDateStockByStockName(stockNameorNum);
        }

        JSONArray oldFocusStock=new JSONArray();
        if(stock!=null){
            String focusStock= user.getFocusstock();
            if(focusStock!=null) {
                oldFocusStock = JSONArray.parseArray(focusStock);
                Iterator it =oldFocusStock.iterator();
                while(it.hasNext()){
                    JSONObject jsonObject1=(JSONObject)it.next();
                    if(stockNameorNum.equals(jsonObject1.get("stockNum"))||stockNameorNum.equals(jsonObject1.get("stockName"))){
                        return "已经收藏过了哦";
                    }
                }
            }
            JSONObject focusstockJSON=new JSONObject();
            focusstockJSON.put("stockName", stock.getStockname());
            focusstockJSON.put("stockNum", stock.getStocknum());

            oldFocusStock.add(focusstockJSON);
            user.setFocusstock(oldFocusStock.toString());
            userDao.save(user);
            return "收藏成功了哟，可以去用户中心看哟！";

        }else{
            return "查询的股票不存在呢！";
        }
    }

    @RequestMapping("/getuserInfo")
    @ResponseBody
    public Object getuserInfo() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user= userDao.findOneByUsername(userDetails.getUsername());

        String focusstock=user.getFocusstock();
        JSONArray focusstockJSON=JSONArray.parseArray(focusstock);
        String s=null;
        if(focusstockJSON!=null&&focusstockJSON.size()>0){
            JSONObject j= (JSONObject) focusstockJSON.get(0);
            s=j.get("stockName").toString();
        }
        /* Iterator it=focusstockJSON.iterator();
        while (it.hasNext()){
            JSONObject focusstockinfo= (JSONObject) it.next();
            //if(focusstockinfo.get("stockName").equals(""))

        }*/
        Map map=new HashMap<String,String>();
        map.put("stockName",s);
        map.put("userName",userDetails.getUsername());
        map.put("roles",user.getRoles());

        return map;
    }

    @RequestMapping("/canclecollection")
    @ResponseBody
    public String canclecollection(@RequestBody JSONObject jsonObject){
        String stockName=jsonObject.get("stockName").toString();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String userName= userDetails.getUsername();
        User user=userDao.findOneByUsername(userName);
        JSONArray focusstock= (JSONArray) JSONArray.parse(user.getFocusstock());
//        Iterator it=focusstock.iterator();
//        while(it.hasNext()){
//            JSONObject jsonObject1= (JSONObject) it.next();
//            if(jsonObject1.get("stockName").toString();
//        }
        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<focusstock.size();i++){
            JSONObject jsonObject1= (JSONObject) focusstock.get(i);
            if(!stockName.equals(jsonObject1.get("stockName"))){
                jsonArray.add(jsonObject1);
            }
        }
        user.setFocusstock(jsonArray.toString());
        userDao.save(user);
        return "取消收藏成功咯！";


    }

    //查询用户收藏的股票
    @RequestMapping("/getstockcollection")
    @ResponseBody
    public List<String> getstockcollection(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String userName=userDetails.getUsername();
        User user =userDao.findOneByUsername(userName);
        String focusstock=user.getFocusstock();
        JSONArray focusstockArray=JSONArray.parseArray(focusstock);
        if(focusstockArray==null||focusstockArray.size()==0){
            return null;
        }
        Iterator it=focusstockArray.iterator();
        List<String> stocks=new ArrayList<String>();
        while(it.hasNext()){
            JSONObject jsonObject= (JSONObject) it.next();
            stocks.add(jsonObject.get("stockName").toString());
        }
        return stocks;
    }

    @RequestMapping("/saveroles")
    @ResponseBody
    public String saveroles(@RequestBody JSONObject jsonObject){
        try {
            String name = jsonObject.get("userName").toString();
            String roles = jsonObject.get("roles").toString();
            User user = userDao.findOneByUsername(name);
            user.setRoles(roles);
            userService.updataroles(roles, name);
            return "更新成功";
        }catch (Exception e){
            return "更新失败";
        }
    }

    @RequestMapping("/deleteuser")
    @ResponseBody
    public  String deleteuser(@RequestBody JSONObject jsonObject){
        try {
            String name = jsonObject.get("name").toString();
            userService.deleteuserByName(name);
            return "删除成功";
        }catch (Exception e){
            return "删除失败";
        }
    }

    @RequestMapping("/user")
    public Object test5(@AuthenticationPrincipal User user) {
        return user;
    }

    @RequestMapping("/testurl")
    public String  testurl(HttpServletResponse response) throws IOException {

        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("html/test1");

        response.setStatus(302);
        response.addHeader("Location","http://baidu.com");
       // response.sendRedirect("/html/test1.html");
        return "dasds";
    }

    @RequestMapping("/forcaseStock")
    @ResponseBody
    public Object forcaseStock(@RequestBody  JSONObject jsonObject){
        String stockname=jsonObject.get("stockName").toString();
        String startdate=jsonObject.get("startDate").toString();
        String enddate=jsonObject.get("endDate").toString();
        String customdays="";
        if(jsonObject.containsKey("customdays")){
             customdays = jsonObject.get("customdays").toString();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date= null;
        try {
            date = sdf.parse(startdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date1= null;
        try {
            date1 = sdf.parse(enddate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<String> s=new ArrayList<String>();
        if(stockname.contains("SH")||stockname.contains("sh")){
             s = stockDao.findClosePricesByNum(stockname, date, date1);
        }else {
             s = stockDao.findClosePricesByName(stockname, date, date1);
        }
        ForcaseStock forcaseStock=new ForcaseStock();
        //最后一天的日期
        //自定义查询存在，且长度小于30则日期添加30天
        int amount=0;
        if(customdays.equals("")){
             amount=30;
        }else{
            Integer maxday=Integer.parseInt(customdays);
            if(maxday>30){
                amount=maxday;
            }else {
                amount=30;
            }
        }
        //获取所有日期
        //List<String> alldates=mainControllerService.getAllDate(startdate,enddate,amount);
          List<String> alldates=mainControllerService.getAllDate2(stockname,startdate,enddate,amount);
        //String lastDay=
        //最后一天的价格
        String lastDayPrices=s.get(s.size()-1);
        //未来七天的价格上升与下降的价格
        List<Double> riseandfall7= forcaseStock.getForcaseStock(s,7);
        //根据数据最后一天的价格和上升下降的价格获取新的七天价格
        List<Double> forcasePrices7=forcaseStock.getForcasePrices(lastDayPrices,riseandfall7);

        List<Double> riseandfall14= forcaseStock.getForcaseStock(s,14);
        List<Double> forcasePrices14=forcaseStock.getForcasePrices(lastDayPrices,riseandfall14);
        List<Double> riseandfall30= forcaseStock.getForcaseStock(s,30);
        List<Double> forcasePrices30=forcaseStock.getForcasePrices(lastDayPrices,riseandfall30);

        Map map=new HashMap()<String,List<String>>;
        //forcaseStock.getForcaseStock(s,14);
        return forcasePrices;
    }
}