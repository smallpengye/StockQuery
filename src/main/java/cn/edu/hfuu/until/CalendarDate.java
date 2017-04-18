package cn.edu.hfuu.until;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by huzhipeng on 2017/4/17.
 */
public class CalendarDate {
    public static String getNewDate(String str,int amount){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //String str = "2016-05-23";
        // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
        Date date = sdf.parse(str, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        calendar.add(Calendar.DATE, amount);
        Date date1 = calendar.getTime();
        String out = sdf.format(date1);
        //System.out.println(out);
        return out;
    }
}
