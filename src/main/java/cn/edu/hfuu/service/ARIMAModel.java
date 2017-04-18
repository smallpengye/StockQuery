/*
package cn.edu.hfuu.service;

import oracle.jvm.hotspot.jfr.StreamWriter;

import java.util.Date;

*/
/**
 * Created by huzhipeng on 2017/4/13.
 *//*

public class ARIMAModel {

        public static int size=500;
        int p,q;
        Date startDate=new Date();
        Date endDate=new Date();
        Date headDate=new Date();

        double[] data=new double[size];
        double[] dataY=new double[size];
        double[] fan=new double[size];
        double[] seta=new double[size];
        int length,dataLength=0,dataYLength=1,fanLength=1,setaLength=1;
        static int maxSize=300;
        double[] Result=new double[size];
        double last;

        public ARIMAModel(){}
        public ARIMAModel(Date StartDate,Date EndDate)
        {
            startDate=StartDate;
            endDate=EndDate;
            TimeSpan ts=startDate-endDate;
            length=Math.abs(ts.Days);
            headDate=startDate.AddDays((0-150));
            read();
        }

        public void setPQ(int i,int j)
        {
            p=i;
            q=j;
        }
        void read()
        {
            String strCon = " Provider = Microsoft.Jet.OLEDB.4.0 ; Data Source =data.xls;Extended Properties=Excel 8.0" ;
            OleDbConnection myConn = new OleDbConnection ( strCon ) ;
            string strCom = " SELECT * FROM [Sheet1$] " ;
            myConn.Open ( ) ;

            OleDbDataAdapter myCommand = new OleDbDataAdapter ( strCom , myConn ) ;

            DataSet dataSet = new DataSet ( ) ;

            myCommand.Fill (dataSet , "[Sheet1$]" ) ;
            foreach(DataTable table in dataSet.Tables)
            {
                foreach(DataRow row in table.Rows)
                {
                    try
                    {
                        if(row[0]!=null&&row[1]!=null)
                        {
                            String nowday=(String)row[0];
                            double price=(double)row[1];
                            DateTimeFormatInfo   dtFI   =   new   DateTimeFormatInfo();
                            dtFI.ShortDatePattern   =   "yy/MM/dd ";
                            DateTime nowDay=DateTime.Parse(nowday,dtFI);
                            TimeSpan ts=headDate-nowDay;
                            TimeSpan ts2=startDate-nowDay;

                            if(ts.Days<=0&&ts2.Days>0)
                            {
                                dataLength++;
                                data[dataLength]=price;
                            }
                        }
                    }
                    catch(Exception e){}
                }
            }
            tiaozheng();
            myConn.Close ( ) ;
        }


        void tiaozheng()
        {
            double count;
            last=data[dataLength];
            for(int i=dataLength;i>1;i--)
            {
                data[i]=data[i]-data[i-1];
            }
        }



     public double computeDataAverage()
     {
        double count=0;
        for(int i=1;i<=dataLength;i++)
        {
            count+=data[i];
        }
        count=count/dataLength;
        return count;
     }



        double computeDataVariance(int parameter)
        {
            double variance=0;
            double average;
            average=computeDataAverage();
            for(int i=1;i<=dataLength-parameter;i++)
            {
                variance+=(data[i]-average)*(data[i+parameter]-average);
            }
            variance=variance/(dataLength);

            return variance;
        }




    public void computeFan()
        {
            double[] variance=new double[maxSize];
            double variancet=computeDataVariance(0);
            fan[0]=-1;
            for(int i=0;i<=p+q;i++)
            {
                variance[i]=computeDataVariance(i)/variancet;
            }
            fanLength=p;
            for(int i=1;i<=p;i++)
            {
                fan[i]=0;
                for(int j=1;j<=p;j++)
                {
                    fan[i]+=10*variance[Math.abs(q+i-j)]*variance[q+j];
                }
            }

        }


        public void computeDataY()
        {
            computeFan();
            dataYLength=dataLength;
            int start;
            for(int i=1;i<=p;i++)
            {
                dataY[i]=data[i];
            }

            for(int i=p+1;i<=dataYLength;i++)
            {
                dataY[i]=data[i];
                for(int j=1;j<=p;j++)
                {
                    dataY[i]=dataY[i]-fan[j]*data[i-j];
                }
            }
        }


        double YAverage;

        public void computeDataYAverage()
        {
            computeDataY();

            double count=0;
            for(int i=p+1;i<=dataYLength;i++)
            {
                count+=dataY[i];
            }
            count=count/(dataYLength-p);
            YAverage=count;

        }


        public double computeDataYVariance(int parameter)
        {
            double variance=0;
            double average=0;
            average=YAverage;
            for(int i=p+1;i<=dataYLength-parameter;i++)
            {
                variance+=(dataY[i]-average)*(dataY[i+parameter]-average);
            }
            variance=variance/(dataYLength-p-parameter);
            return variance;
        }


        public void computeSeta()
        {
            double[] variance=new double[maxSize];
            computeDataYAverage();
            double variancet=computeDataYVariance(0);
            for(int i=1;i<=p+q;i++)
            {
                variance[i]=computeDataYVariance(i)/variancet;
            }
            setaLength=q;
            for(int i=1;i<=q;i++)
            {
                seta[i]=0;
                for(int j=1;j<=q;j++)
                {
                    seta[i]+=10*variance[Math.abs(p+i-j)]*variance[p+j];
                }
            }
        }


        double[] segama=new double[size];
        int segamaLength=1;

        public void computeSegama()
        {
            segamaLength=dataLength;
            for(int i=p+1;i<=segamaLength;i++)
            {
                double count=0;
                for(int j=1;j<=p;j++)
                {
                    count+=fan[j]*data[i-j];
                }
                segama[i]=(data[i]-count)*10;
            }
        }


        double computeSegamaVariange()
        {
            computeSegama();
            double average=0;
            for(int i=p+1;i<=segamaLength;i++)
            {
                average+=segama[i];
            }

            average=average/(segamaLength-p);
            double variance=0;
            for(int i=p+1;i<=segamaLength;i++)
            {
                variance+=(segama[i]-average)*(segama[i]-average);
            }
            variance=variance/(segamaLength-p);
            return variance;
        }


        void computePAndQ()
        {
            double minDeta=10000000,deta,variange;
            int finalP=1,finalQ=1;
            for(int i=1;i<=10;i++)
            {
                for(int j=1;j<=10;j++)
                {
                    this.setPQ(i,j);
                    computeFan();
                    computeSeta();
                    variange=computeSegamaVariange();
                    deta=Math.log(variange)+2*(p+q+1)/(dataLength*1.0);
                    if(deta<=minDeta)
                    {
                        minDeta=deta;
                        finalP=i;
                        finalQ=j;
                    }
                }
            }
            setPQ(finalP,finalQ);
        }

        public void forecast()
        {
            double[] result=new double[size];
            double count=0;
            computePAndQ();
            computeFan();
            computeSeta();
            computeSegamaVariange();
            for(int i=1;i<=length;i++)
            {
                int I=dataLength+1;
                result[i]=0;
                for(int j=1;j<=q;j++)
                {
                    result[i]+=fan[j]*data[I-j];
                }
                for(int j=1;j<=p;j++)
                {
                    result[i]+=seta[j]*segama[I-j];
                }
                Result[i]=last+(result[i]*last)%50;
                last=Result[i];
                dataLength++;
                data[dataLength]=result[i];
                segamaLength++;
                count=0;
                for(int j=1;j<=p;j++)
                {
                    count+=fan[j]*data[segamaLength-j];
                }
                segama[segamaLength]=((data[dataLength]-count)-segama[segamaLength-1])%500;
            }
        }

        public void writerFile(String url)
        {
            Date day=new Date();
            day=startDate;
            StreamWriter sw=new StreamWriter(url);
            String str;
            for(int i=1;i<length;i++)
            {
                str=day.ToShortDateString();
                sw.WriteLine("{0}",str);
                Math.Round(Result[i],2);
                sw.WriteLine(Result[i].ToString("F2"));
                day=day.AddDays(1);
            }

            sw.Close();
        }


}
*/
