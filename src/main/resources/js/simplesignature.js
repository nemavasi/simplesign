

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

    var packetProcess = false;
    var packetObjects = [];


    ////////////////////////////////////////
    // события по нажатию на кнопки - запрос подписи с сервера
    ////////////////////////////////////////
    // objname - название объекта
    // issieid - идентификатор задачи
    // attachmentid - идентификатор задачи
    // var getSignForObject = function(obj, asyncFlag = true) {
    var getSignForObject = function(obj) {

        // asyncFlag = false;
        // console.log("asyncFlag");
        // console.log(asyncFlag);

        // если заблокировано то ничего не делаем, выходим
        if (lockEvents) {
            return true;
        }

        // блокируем на время работы функции
        lockEvents = true;


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


        // return true;

        // значения objId
        // summ_sign
        // descr_sign
        // attach_sign_10000
        // attach_sign_10002
        // attach_sign_10001


        AJS.$("div#" + objId).parent().find(".sign-spin").css("display", "block");


        AJS.$.ajax({
            url: restUrl,
            type: 'get',
            dataType: 'json',
            // data: JSON.stringify(jsonObj),
            async: true,
            // async: asyncFlag,
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

            },
            error: function(data) {

            },
            complete: function() {
                AJS.$("div#" + objId).parent().find(".sign-spin").css("display", "none");
                lockEvents = false;

                // если запущена пакетная обработка то надо вызвать функцию еще раз по списку объектов
                if (packetProcess) {
                    // начинаем пакетную обработку
                    if (packetObjects.length > 0) {
                        getSignForObject(packetObjects.pop());
                    } else {

                        // проверить состав вложений напоследок
                        checkAttachments();

                        // сбросить флаг пакетной обработки
                        packetProcess = false;
                    }

                }
            },
        });

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
            async: false,
            // async: true,
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


        return true;

    }


    ////////////////////////////////////////
    // проверка всех компонентов подписи
    ////////////////////////////////////////
    var checkAll = function() {

        // очистим массив
        packetObjects = [];
        // установим флаг пакетной обработки - флаг нужно будет сбросить в конце обработки в функции
        packetProcess = true;


        // кнопки для передачи в функцию
        var buttons = AJS.$("div#signDetailDiv ul li div.sign-bthcheck button");

        // заполняем массив
        for (var i = buttons.size() - 1; i > 0; i--) {
            packetObjects.push(buttons[i]);
        }

        // начинаем пакетную обработку
        if (packetObjects.length > 0) {
            getSignForObject(packetObjects.pop());
        }

    }

    ////////////////////////////////////////
    // проверка того является ли список вложений неизмененным
    ////////////////////////////////////////
    var checkAttachments = function() {

        var messages = []; // сообщения об ошибках

        ////////////////////////////////////////
        // 1 - получить список имен вложений из нашей таблицы
        var attachsSigned = [];

        var divs = AJS.$("div#signDetailDiv ul li div.sign-name");

        for (var i = 3; i < divs.size(); i++) {
            attachsSigned.push(divs[i].innerText);
        }

        // console.log(attachsSigned);

        ////////////////////////////////////////
        // 2 - получить список имен вложений из задачи
        var attachsIssue = [];

        var responseObj = AJS.$.ajax({
            url: AJS.params.baseURL + "/rest/api/2/issue/" + AJS.$("input#signissueid").val(),
            type: 'get',
            dataType: 'json',
            async: false,
        });

        if (responseObj.statusText == "success") {
            var jsonObj = JSON.parse(responseObj.responseText);

            var attachsArr = jsonObj.fields.attachment;
            var attachsArrLength = jsonObj.fields.attachment.length;

            for (var i = 0; i < attachsArrLength; i++) {
                // arrElem = {};
                // arrElem.id = attachsArr[i].id;
                // arrElem.filename = attachsArr[i].filename;
                // arrElem.size = attachsArr[i].size;
                //
                // attachs.push(arrElem);
                attachsIssue.push(attachsArr[i].filename);
            }
        }

        // console.log(attachsIssue);

        // 3 - проверить что вложения из подписи есть в задаче
        for (var i = 0; i < attachsSigned.length; i++) {
            if (attachsIssue.indexOf(attachsSigned[i]) < 0) {
                messages.push("вложение подписи не найдено во вложениях задачи " + attachsSigned[i].substring(0, 10));
            }
        }

        // 4 - проверить что вложения из задачи есть в подписи
        for (var i = 0; i < attachsIssue.length; i++) {
            if (attachsSigned.indexOf(attachsIssue[i]) < 0) {
                messages.push("вложение задачи не найдено во вложениях подписи " + attachsIssue[i].substring(0, 10));
            }
        }

        // 5 - проверить количество элементов в массивах
        if (attachsIssue.length != attachsSigned.length) {
            messages.push("вложения в подписи и в задаче различны");
        }


        // 6 - сформировать сообщения об итогах проверки
        var msg = "";

        for (var i = 0; i < messages.length; i++) {
            msg = msg + messages[i] + "\n";
        }

        // console.log(msg);

        AJS.$("div#signDetailDiv div.sign-total").text(msg);
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
        checkSumClick:checkSumClick,
        checkAll:checkAll,
        // checkAttachments:checkAttachments,
        // callServerRest:callServerRest
    };



}());

