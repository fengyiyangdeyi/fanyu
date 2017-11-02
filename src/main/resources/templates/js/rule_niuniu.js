function delRule() {
    $.post("delRule"
        , function (data) {
            if (data.retCode == 200) {
                window.location.href = "rule";
            } else {
                alert("网络错误！");
            }
        }
    );
}

function editTemplates() {
    var minBankerScore = $("#minBankerScore").val();
    var banker=$("#banker").val();
    var minBet=$("#minBet").val();
    var maxBet=$("#maxBet").val();
    var minsuoha=$("#minsuoha").val();
    var maxsuoha=$("#maxsuoha").val();
    var szcvalue=$("#szcvalue").val();
    var ylcvalue=$("#ylcvalue").val();
    var rtcvalue=$("#rtcvalue").val();
    var timeout=$("#timeout").val();
    var xcType = $("input[name='xcType']:checked").val();
    $.post("/templates/editTemplates"
        , {
            "minBankerScore": minBankerScore,
            "banker":banker,
            "minBet":minBet,
            "maxBet":maxBet,
            "minsuoha":minsuoha,
            "maxsuoha":maxsuoha,
            "szcvalue":szcvalue,
            "ylcvalue":ylcvalue,
            "xcType":xcType,
            "rtcvalue":rtcvalue,
            "timeout":timeout
        }
        , function (data) {
            if (data.retCode == 200) {
                window.location.href = "rule";
            } else {
                alert(data.errorMsg);
            }
        }
    );
}

window.onload=function () {
    $.get("/templates/getAllUser"
        , function (data) {
            if (data.retCode == 200) {
                var jsonObj = data.data;
                var optionstring = "";
                for (var j = 0; j < jsonObj.length; j++) {
                    optionstring += "<option value=\"" + jsonObj[j].username + "\" >" + jsonObj[j].nickname + "</option>";
                }
                $("#banker").html("<option value=''>请选择...</option> " + optionstring);
                var form = layui.form;
                form.render(); //更新全部
                $.get("/templates/getNiuNiuTemplates"
                    , function (data) {
                        if (data.retCode == 200) {
                            var jsonObj = data.data;
                            $("input[name='xcType'][value="+jsonObj.xcType+"]").attr("checked",true);
                            $("#minBankerScore").val(jsonObj.minBankerScore);
                            // console.log(jsonObj.banker);
                            var A=jsonObj.banker;
                            if(!(A==null||A==undefined||A=="")){
                                $("#banker").find("option[value="+jsonObj.banker+"]").attr("selected",true);
                            }
                            $("#minBet").val(jsonObj.minBet);
                            $("#maxBet").val(jsonObj.maxBet);
                            $("#minsuoha").val(jsonObj.minsuoha);
                            $("#maxsuoha").val(jsonObj.maxsuoha);
                            $("#szcvalue").val(jsonObj.szcvalue);//上庄抽
                            $("#ylcvalue").val(jsonObj.ylcvalue);
                            $("#rtcvalue").val(jsonObj.rtcvalue);
                            $("#timeout").val(jsonObj.timeout);
                            var form = layui.form;
                            form.render(); //更新全部
                        } else {
                            alert("网络错误！");
                        }
                    }
                );
            } else {
                alert("网络错误！");
            }
        }
    );
}



