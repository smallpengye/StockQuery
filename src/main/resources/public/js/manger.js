/**
 * Created by huzhipeng on 2017/3/19.
 */
var app = angular.module('myapp2', []);
app.controller('manger', function ($scope,$http) {

    //获取用户收藏的股票
    $http({
        url:"/getstockcollection"
    }).success(function (data) {
        debugger;
        $scope.stocks=data;
    })

    //管理员获取所有用户
    $http({
        url:"/getalluser"
    }).success(function (data) {
        debugger;
        $scope.users=data;
    })

    $scope.goindex=function () {
        window.location.href = '../html/index.html';
    }
    $scope.submit = function() {

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

    var allroles=new Array("user","admin","member");
    $scope.changroles=function (index) {
        debugger;
        for(var o in allroles){
            if($scope.users[index].roles==allroles[o]){
                if(o==0) {
                    $scope.users[index].roles = allroles[1];
                    break;
                }else if(o==1){
                    $scope.users[index].roles = allroles[2];
                    break;
                }else if(o==2){
                    $scope.users[index].roles = allroles[0];
                    break;
                }
            }
        }

    }

 //   $scope.users=[{"name":"huzi","roles":"admin"},{"name":"huzi","roles":"admin"},{"name":"huzi","roles":"admin"}]
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


    var currentindex;
    $scope.deleteroles=function (index) {
        ying2.style.display = "block";
        $scope.currentname2=$scope.users[index].name;
        currentindex=index;
        //    $scope.currentroles=$scope.users[index].roles


    }
    $scope.cancle2=function () {
        ying2.style.display= "none";
    }
    $scope.confirm2=function () {
        debugger;
        var name=$scope.currentname2;
        $scope.users.splice(currentindex,1);
        data={"name":name}
        $http({
            url:"/deleteuser",
            data:data,
            method:"POST"
        }).success(function(data1){
            ying2.style.display= "none";
            alert(data1);
        })

    }
    var ying = document.getElementById("ying");
    $scope.saveroles=function (index) {
        ying.style.display = "block";
        $scope.currentname=$scope.users[index].name,
        $scope.currentroles=$scope.users[index].roles
    }

    $scope.cancle=function () {
        ying.style.display= "none";
        cancelBubble = true;
    }
    $scope.confirm=function () {
        debugger;
        data={
                    "userName":$scope.currentname,
                    "roles":$scope.currentroles
                }
                $http({
                    url:"/saveroles",
                    data:data,
                    method:"POST"
                }).success(function(data){
                    ying.style.display= "none";
                    alert(data);
                })
    }



})