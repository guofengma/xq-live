$(function() {
    //验证input中输入的是否为数字：
    $('#accountInp').on('keyup', function() {
        $('#accountInp').val($('#accountInp').val().replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'))
    })
    // reqAjaxAsync("hx/get/1").done(function(res){
    //     console.log(res)
    // });
    $.ajax({
        type: "GET",
        url: "http://182.254.130.252/hx/get/1",
        dataType: "json",
        async: true, //默认为异步
        data: {},
        success: function(data) {
            console.log('123')
        },
        error: function(err) {
            console.log("error");
        }
    });
})

// restful
function reqAjaxAsync(url) {
    var defer = $.Deferred();
    $.ajax({
        type: "GET",
        url: "http://182.254.130.252/" + url,
        dataType: "json",
        async: true, //默认为异步
        data: {},
        success: function(data) {
            console.log('123')
            defer.resolve(data);
        },
        error: function(err) {
            console.log("error");
        }
    });
    return defer.promise();
}