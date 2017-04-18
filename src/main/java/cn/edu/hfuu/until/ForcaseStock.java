package cn.edu.hfuu.until;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static java.lang.Math.pow;
import static java.lang.Math.random;

/**
 * Created by huzhipeng on 2017/4/16.
 */

public class ForcaseStock {

    public List<Double> getForcaseStock(List<String> closingPrices,int days){
        List<Double> closePrices=stringToInteger(closingPrices);
        Double average=getAverage(closePrices);
        //保留小数点后两位
        BigDecimal bg = new BigDecimal(average);
        average = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println("average"+average);
        //方差也保留小数点后两位
        Double variance=getVariance(closePrices,average);
        BigDecimal bg2 = new BigDecimal(variance);
        variance = bg2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println("variance"+variance);

        //对数据进行预处理
        List<Double> Pretreatmentdate=Pretreatment(closePrices,average,variance);
        List<Double> firstNeuron= getNeuron(Pretreatmentdate);
        List<Double> forcasedata=new ArrayList<Double>();
        for(int i=0;i<days;i++){
           Random random=new Random();
           int x=random.nextInt(firstNeuron.size());
           forcasedata.add(firstNeuron.get(x)/10);
        }

        return forcasedata;
    }

    //strin转double便于计算
    public static List<Double> stringToInteger(List<String> closingPrices){
        List<Double> closingnPricesInteger =new ArrayList<Double>();
        for(String s:closingPrices){
            Double double1=new Double(s);
            closingnPricesInteger.add(double1);
        }
        return closingnPricesInteger;
    }

    //计算平均值
    public static Double getAverage( List<Double> closePrices){
        Double sum=(double) 0;
        Double average=(double)0;
        for(Double i:closePrices){
            sum+=i;
        }
        average=sum/closePrices.size();
        return  average;
    }
    //计算方差
    public static double getVariance(List<Double> closePrices, Double average){
        Double sum=(double)0;
        for(Double i:closePrices){
          sum+= Math.pow(i-average,2);
        }
        double variance=sum/closePrices.size();

        return  variance;
    }

    //数据预处理
    public static List<Double> Pretreatment(List<Double> closePrices,double average,double variance){

        List<Double> pretreatdate=new ArrayList<Double>();
        for(Double d: closePrices){

            BigDecimal bg = new BigDecimal((d-average)/variance);
            Double predate = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            pretreatdate.add(predate);
        }
        return pretreatdate;
    }

    //构造神经元
    public static  List<Double> getNeuron(List<Double> pretreatment){
        //第一层神经元
        List<Double> firstNeuron=new ArrayList<Double>();
        Double sum=new Double(0);
        for(int i=1;i<pretreatment.size();i++){
            Double s=pretreatment.get(i);
            sum+=s;
            if(i%4==0){
                Double d1=new Double(sum/4);
                firstNeuron.add(d1);
                sum=(double) 0;
              //  Double sum=new Double(0);
            }
        }

        return firstNeuron;
    }



    public List<Double> getForcasePrices(String lastDayPrice,List<Double> riseAndFall){

        List<Double> forcasePrise=new ArrayList<>();
        Double lastPrice=new Double(lastDayPrice);
        for(Double raf :riseAndFall){
            lastPrice+=raf;
            Double price=new Double(lastPrice);
            forcasePrise.add(price);
            lastPrice=price;
        }

        return forcasePrise;
    }
}
