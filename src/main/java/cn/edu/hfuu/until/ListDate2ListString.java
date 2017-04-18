package cn.edu.hfuu.until;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huzhipeng on 2017/4/17.
 */
public class ListDate2ListString {
    public static List<String> getListString(List<Date> alldate){
        List<String> allString=new ArrayList<String>();
        for(Date d:alldate){
            String s=d.toString();
            allString.add(s);
        }
        return allString;
    }
}
