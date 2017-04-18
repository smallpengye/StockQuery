/**
 * Created by 志鹏 on 2016/8/1.
 */


$(document).ready(function () {

    var limit = 8;
    var offset = $("#thispage").val()
    $("#nextpageBtn").on("click", function () {
        offset = offset + limit
    })
    var data = {"offset": offset, "limit": limit}
    $.ajax({
        type: "post",
        dataType: "json",
        //  contentType: "application/json; charset=utf-8",
        data: data,
        url: "index1",
        success: function (rsp) {
            if (rsp.data.success == true) {
                //  alert(rsp.data.map);
                var datas = rsp.data.map
                $.each(datas, function (index, item) {
                    $("#accounts").append("<  tr>" +
                        "<td id='userName'>" + item.id + "</td>" +
                        "<td id='income'>" + item.userName + "</td>" +
                        //"<td id='incomersource'>" + item.userPsd + "</td>" +
                        "<td id='pay'></td>" +
                        "<td id='payfor'></td>" +
                        "<td id='total'> </td>" +
                        "<td id='date'> </td></tr>"
                    )
                })
            }
        },
        error: function (rsp) {
            alert("请求失败");
        }
    });

})