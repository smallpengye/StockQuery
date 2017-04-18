/**
 * Created by 志鹏 on 2016/11/26.
 */
var app = angular.module('myApp', []);
app.controller('checkCtrl', function ($scope) {
    $scope.button1=function() {
        $scope.alertmsg = "hello";
    }
})

