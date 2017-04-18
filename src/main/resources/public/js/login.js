/**
 * Created by 志鹏 on 2016/8/3.
 */
$(document).ready(function () {
    $("#loginBtn").on("click", function () {
        var userName = $("#userName").val()
        var userPsd = $("#userPsd").val()
        var date = {"userName": userName, "userPsd": userPsd}
        $.ajax({
            type: "post",
            datatype: "JSON",
            data: date,
            url: "login123",
            success: function (rsp) {
                if (rsp.data.success == true) {

                    // alert("login success");
                    location.href = "myinformation";
                    
                } else {
                    alert("login erroreeeeeee; erroinfo:" + rsp.data.info);
                }
            }, error: function (data) {
                alert("数据发送失败" + userName);
            }
        });
    })

    $("#regBtn").on("click", function () {
    //    location.href = "http://r159398a65.imwork.net/register";
        window.location.href='register.html'
    })
})