/**
 * Created by huzhipeng on 2017/4/15.
 */
var app = angular.module('myapp3', []);
app.controller('forcaseStock', function ($scope,$http) {

    $scope.goindex=function () {
        window.location.href = '../html/index.html';
    }

    $scope.forcaseStock=function () {
        data = {
            "stockName": $scope.stockname,
            "startDate": $scope.startdate,
            "endDate": $scope.enddate
        }

        if($scope.stockname == undefined) {
            alert("请输入所要预测的股票编号或股票名")
        } else if ($scope.startdate == undefined || $scope.enddate == undefined) {
            alert("请选择开始时间和最后时间")
        } else {
            var daya = duibi($scope.enddate, "2017-02-27");
            if (daya < 0) {
                alert("请选择2017-2-16之前的时间")
            } else {
                debugger;
                var day = duibi($scope.startdate, $scope.enddate);
                debugger

                if (day > 6) {
                    debugger;
                    getforcasedata(data);
                } else if (day < 0) {
                    alert("开始时间不能小于结束时间");
                } else {
                    alert("请至少选择七天及以上的时间");
                }
            }
        }

    }
    var getforcasedata=function (data) {
        debugger;
        if(data.stockName!=null&&data.stockName!=undefined) {
            debugger;

            $http({
                data: data,
                sync:false,
                method: 'post',
                url: '/forcaseStock',
            }).success(function (datas) {
                debugger;
                if(datas.length!=0) {

                    var myChart = echarts.init(document.getElementById('myecharts2'));
                    option = {
                        title: {
                            text: '股票预测图'
                        },
                        tooltip: {
                            trigger: 'axis'
                        },
                        legend: {
                            data:['7天','15天','30天','自定义天数']
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        toolbox: {
                            feature: {
                                saveAsImage: {}
                            }
                        },
                        xAxis: {
                            type: 'category',
                            boundaryGap: false,
                            data: datas.dates
                        },
                        yAxis: {
                            type: 'value'
                        },
                        series: [

                            {
                                name:'搜索引擎',
                                type:'line',
                                stack: '总量',
                                data:[820, 932, 901, 934, 1290, 1330, 1320]
                            }
                        ]
                    };
                    myChart.setOption(option);
                    myChart.on('click', function (params) {
                        debugger;
                        console.log(params);
                        data2={
                            date:params.name,
                            name:$scope.stockname
                        }
                        //获取点击时间的具体信息
                        $http({
                            data:data2,
                            sync:false,
                            method:'POST',
                            url:'/getDetailedInfo'
                        }).success(function (data1) {
                            debugger;
                            var date=getLocalTime(data1.date);
                            $scope.stockname1=data1.stockname;
                            $scope.stocknum=data1.stocknum;
                            $scope.stockdate=date;
                            $scope.openprice=data1.openingprice;
                            $scope.maxprice=data1.maxprice;
                            $scope.minprice=data1.minprice;
                            $scope.closeprice=data1.closingprice;
                            $scope.changerate=data1.changerate;
                            $scope.riseandfall=data1.riseandfall;
                            $scope.average=data1.average;
                        }).error(function () {
                            alert('请求出错咯！')
                        })
                    });
                }else {
                    alert("数据不存在");
                }

            })
        }

    }



    var myChart = echarts.init(document.getElementById('myecharts2'));
    option = {
        title: {
            text: '股票预测图'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:[]
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: []
        },
        yAxis: {
            type: 'value'
        },
        series: [

            {
                name:'搜索引擎',
                type:'line',
                stack: '总量',
                data:[820, 932, 901, 934, 1290, 1330, 1320]
            }
        ]
    };

    myChart.setOption(option);


    function duibi(a, b) {
        var arr = a.split("-");
        var starttime = new Date(arr[0], arr[1], arr[2]);
        var starttimes = starttime.getTime();

        var arrs = b.split("-");
        var lktime = new Date(arrs[0], arrs[1], arrs[2]);
        var lktimes = lktime.getTime();

        var day=(lktimes-starttimes)/86400000;
        debugger;

        return day;
        // if (starttimes >= lktimes) {
        //
        //     alert('开始时间大于离开时间，请检查');
        //     debugger;
        //     return false;
        // }
        // else {
        //     debugger;
        //     return true;
        // }

    }
});