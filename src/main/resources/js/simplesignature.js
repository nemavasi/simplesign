

////////////////////////////////////////
// тонкий момент
// во всплывающем окне
// отображаем только данные из подписи
// больше ничего
////////////////////////////////////////


var simplesign = {};
simplesign.module = [];

simplesign.module = (function () {

    var visible = false; // переменная - видимость всплытого окна
    // TODO: реализовать блокировки чтобы предотвратить нажатия кнопок во время выполнения действия
    // TODO: реализовать массовую проверку по кнопке ПроверитьВсе

    var lockEvents = false; // признак блокировки


    ////////////////////////////////////////
    // события по нажатию на кнопки - запрос подписи с сервера
    ////////////////////////////////////////
    // objname - название объекта
    // issieid - идентификатор задачи
    // attachmentid - идентификатор задачи
    var getSignForObject = function(obj) {



        var issueId = AJS.$("input#signissueid");

        // сначала нужно определить тип объекта
        var objId = AJS.$(obj).parent().parent().find("div.sign-name").attr("id");

        var objName = "";
        var objAttachId= "*";

        switch (objId) {
            case "summ_sign":
                objName = "summary";
                break;
            case "descr_sign":
                objName = "description";
                break;
            default:
                // console.log();
                if (objId.substring(0, 11) == "attachsign_") {
                    objName = "attachment";
                    objAttachId = objId.substring(11);
                }

        }

        var restUrl = AJS.params.baseURL + "/rest/simplesignrest/1.0/message/getsignature/" + AJS.$("input#signissueid").val() + "/" + AJS.$("input#signfieldid").val() + "/" + objName + "/" + objAttachId;


        // console.log(url);

        // return true;

        // значения objId
        // summ_sign
        // descr_sign
        // attach_sign_10000
        // attach_sign_10002
        // attach_sign_10001



        AJS.$.ajax({
            url: restUrl,
            type: 'get',
            dataType: 'json',
            // data: JSON.stringify(jsonObj),
            async: true,
            contentType: "application/json; charset=utf-8",
            success: function(data) {


                if (data.signhash == data.realhash) {
                    AJS.$("div#" + objId).parent().find(".sign-status").text("OK");
                    AJS.$("div#" + objId).parent().find(".sign-status").removeClass("status-bad");
                    AJS.$("div#" + objId).parent().find(".sign-status").addClass("status-ok");
                } else {
                    AJS.$("div#" + objId).parent().find(".sign-status").text("ОШИБКА");
                    AJS.$("div#" + objId).parent().find(".sign-status").removeClass("status-ok");
                    AJS.$("div#" + objId).parent().find(".sign-status").addClass("status-bad");
                }



                // var dataLength = data.length;
                // var strMess = "";
                //
                // // console.log(data);
                //
                // for (var i = 0; i < dataLength; i++) {
                //
                //     rowStr = rowTemplate.replace("__objectId__", "attach_sign_" + data[i].id);
                //     rowStr = rowStr.replace("__object__", data[i].name);
                //     rowStr = rowStr.replace("__status__", "&nbsp;");
                //
                //     tableObj.append(rowStr);
                //
                //
                //     rows = AJS.$("div#signDetailDiv ul li");
                //     AJS.$(rows[rows.size() - 1]).find(".sign-bthcheck button").bind("click", function() {
                //         getSignForObject(this);
                //     });
                //}
                //
            },
            error: function(data) {
                // var myFlag = AJS.flag({
                //     type: 'error',
                //     body: 'Ошибка загрузки',
                // });

            },
        });





        // // console.log("objname: " + objname + " issueId: " +  issueId + " attachmentid: " + attachmentid);
        // console.log(" event log ");
        // console.log(obj);
        // console.log(objId);
        // console.log(AJS.$(obj).parent().parent().find("div.sign-name"));

        // console.log(" ======================== ");
        // for (var i = 0; i < arguments.length; i++) {
        //     console.log( "Привет, " + arguments[i] );
        // }

    }


    ////////////////////////////////////////
    // отрисовка элементов всплывающего окна
    ////////////////////////////////////////
    var renderPopupWindow = function() {
        var rowTemplate = '<li>' +
            '<div id="__objectId__" class="sign-name">__object__</div>' +
            '<div class="sign-status status-ok">__status__</div>' +
            '<div class="sign-bthcheck"><button>проверить</button></div>' +
            '<div class="sign-spin" style="display: none">' +
            '<img src="' + AJS.params.baseURL + '/download/resources/ru.hlynov.oit.simplesign.simplesignature:simplesignature-resources/images/spin.gif"></img>' +
            '</div>';

            '</li>';

        var tableObj = AJS.$("div#signDetailDiv ul");
        // var tableObjRows = AJS.$("div#signDetailDiv ul li");

        // удалим все строки кроме заголовка
        var rows = AJS.$("div#signDetailDiv ul li");
        var rowsSize = rows.size();
        for( var i = rowsSize - 1; i > 0; i--) {
            rows[i].remove();
        }
        // почистим сообщение статуса
        // AJS.$("div#signDetailDiv div .status-bad").text("");
        AJS.$("div#signDetailDiv div.sign-total").text("");


        // добавление строк

        // заголовок
        var rowStr = rowTemplate.replace("__objectId__", "summ_sign");
        rowStr = rowStr.replace("__object__", "заголовок");
        rowStr = rowStr.replace("__status__", "&nbsp;");

        // добавляем строку
        tableObj.append(rowStr);
        // привязываем событие
        rows = AJS.$("div#signDetailDiv ul li");
        AJS.$(rows[rows.size() - 1]).find(".sign-bthcheck button").bind("click", function() {
            getSignForObject(this);

            // callServerRest(this);
        });

        // описание
        rowStr = rowTemplate.replace("__objectId__", "descr_sign");
        rowStr = rowStr.replace("__object__", "описание");
        rowStr = rowStr.replace("__status__", "&nbsp;");

        tableObj.append(rowStr);
        rows = AJS.$("div#signDetailDiv ul li");

        AJS.$(rows[rows.size() - 1]).find(".sign-bthcheck button").bind("click", function() {
            getSignForObject(this);

            // callServerRest(this);
        });


        // return true;

        // тут надо делать вызов рест для того чтобы получить имена файлов во вложениии
        // которые находятся в подписи
        AJS.$.ajax({
            url: AJS.params.baseURL + "/rest/simplesignrest/1.0/message/getattachnames/" + AJS.$("input#signissueid").val() + "/" + AJS.$("input#signfieldid").val(),
            type: 'get',
            dataType: 'json',
            // data: JSON.stringify(jsonObj),
            async: true,
            contentType: "application/json; charset=utf-8",
            success: function(data) {

                var dataLength = data.length;
                var strMess = "";

                // console.log(data);

                for (var i = 0; i < dataLength; i++) {

                    rowStr = rowTemplate.replace("__objectId__", "attachsign_" + data[i].id);
                    rowStr = rowStr.replace("__object__", data[i].name);
                    rowStr = rowStr.replace("__status__", "&nbsp;");

                    tableObj.append(rowStr);


                    rows = AJS.$("div#signDetailDiv ul li");
                    AJS.$(rows[rows.size() - 1]).find(".sign-bthcheck button").bind("click", function() {
                        getSignForObject(this);
                    });


                }
                //
            },
            error: function(data) {
                // var myFlag = AJS.flag({
                //     type: 'error',
                //     body: 'Ошибка загрузки',
                // });

            },
        });



        //
        //
        // // вложения
        // rowStr = rowTemplate.replace("__objectId__", "aatach_121212");
        // rowStr = rowStr.replace("__object__", "Вложение вложение вложение вложение вложение вложение ");
        // rowStr = rowStr.replace("__status__", "&nbsp;");
        //
        // tableObj.append(rowStr);
        //
        //
        // rowStr = rowTemplate.replace("__objectId__", "aatach_232323");
        // rowStr = rowStr.replace("__object__", "Вложение вложение вложение вложение вложение вложение ");
        // rowStr = rowStr.replace("__status__", "&nbsp;");
        //
        // tableObj.append(rowStr);
        //
        //
        // rowStr = rowTemplate.replace("__objectId__", "aatach_343434");
        // rowStr = rowStr.replace("__object__", "Вложение вложение вложение вложение вложение вложение ");
        // rowStr = rowStr.replace("__status__", "&nbsp;");
        //
        // tableObj.append(rowStr);
        //

        return true;

    }


    ////////////////////////////////////////
    // отображение всплывающего окна
    ////////////////////////////////////////
    var checkSumClick = function() {

        // если заблокировано то ничего не делаем, выходим
        if (lockEvents) {
            return true;
        }

        // блокируем на время работы функции
        lockEvents = true;


        // всплывающее окно
        var objSignDetailDiv = AJS.$("#signDetailDiv");

        if (visible) {
            objSignDetailDiv.css("display", "none");
            visible = false;

            // разблокируем по завершении
            lockEvents = false;

            return true;
        }

        if (!visible) {
            // отрисовать заголовок
            renderPopupWindow();

            objSignDetailDiv.css("top", 45);
            objSignDetailDiv.css("left", 100);
            objSignDetailDiv.css("display", "block");
            // objSignDetailDiv.show("fast");
            visible = true;

            // разблокируем по завершении
            lockEvents = false;

            return true;
        }

        // разблокируем по завершении
        lockEvents = false;

    }

    return {
        checkSumClick:checkSumClick
        // callServerRest:callServerRest
    };



}());

