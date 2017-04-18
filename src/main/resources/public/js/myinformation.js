/**
 * Created by 志鹏 on 2016/10/21.
 */
$(document).ready(function () {


    $.ajax({
        type: "POST",
        url: "myinformation1",
        success: function (rsp) {
           var userName=rsp.data.userName
            
            $("#userName").text("欢迎登录"+userName);


        }, error: function (data) {
            alert("数据发送失败");
        }
    });


})