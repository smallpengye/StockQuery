/**
 * Created by huzhipeng on 2017/3/19.
 */
var app = angular.module('myapp1', []);
app.controller('myInfo', function ($scope,$http) {

    $http({
        url:"/getstockcollection"
    }).success(function (data) {
        debugger;
        $scope.stocks=data;
    })


    $scope.goindex=function () {
        window.location.href = '../html/index.html';
    }
    $scope.submit = function() {

        alert("dd");
        $('#myform').ajaxForm({
            success: function (datasource) {
                if(datasource=="notexcel"){
                    alert("请上传excel文件")
                }else{
                    alert("上传成功")
                }

            }
        }).submit();
    }
    $scope.canclecollect=function (index) {
        var stockName=$scope.stocks[index];
        $scope.stocks.splice(index,1);
        data1={"stockName":stockName};
        $http({
            method:"POST",
            data:data1,
            url:"/canclecollection"
        }).success(function(data){
            alert(data);
        });


    }







})