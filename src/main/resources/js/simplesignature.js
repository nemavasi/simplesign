

////////////////////////////////////////
// тонкий момент
// во всплывающем окне
// отображаем только данные из подписи
// больше ничего
////////////////////////////////////////


var simplesign = {};
simplesign.module = [];

simplesign.module = (function () {

    var visible = false;

    ////////////////////////////////////////
    // события по нажатию на кнопки
    ////////////////////////////////////////
    var callServerRest = function(obj, id) {



        console.log("obj: " + obj + " id: " +  id);

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
            '</li>';

        var tableObj = AJS.$("div#signDetailDiv ul");

        // // удалим все строки кроме заголовка
        // var rows = AJS.$("div#signDetailDiv ul li");
        // var rowsSize = rows.size();
        // for( var i = rowsSize - 1; i > 0; i--) {
        //     rows[i].remove();
        // }
        // // почистим сообщение статуса
        // // AJS.$("div#signDetailDiv div .status-bad").text("");
        // AJS.$("div#signDetailDiv div.sign-total").text("")


        // добавление строк

        // заголовок
        var rowStr = rowTemplate.replace("__objectId__", "summ_sign");
        rowStr = rowStr.replace("__object__", "заголовок");
        rowStr = rowStr.replace("__status__", "&nbsp;");

        // добавляем строку
        tableObj.append(rowStr);
        // привязываем событие
        AJS.$(tableObj[tableObj.size() - 1]).find(".sign-bthcheck button").bind("click", function() {
            callServerRest("summ", "", 123);
        });

        // описание
        rowStr = rowTemplate.replace("__objectId__", "descr_sign");
        rowStr = rowStr.replace("__object__", "описание");
        rowStr = rowStr.replace("__status__", "&nbsp;");

        tableObj.append(rowStr);


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

                console.log(data);


                // for (var i = 0; i < dataLength; i++) {
                //     strMess = strMess + '<li>' + data[i] + '</li>';
                // }
                //
                // strMess = '<ul>' + strMess +'</ul>';

                // var myFlag = AJS.flag({
                //     title: "Загружены вложения",
                //     type: 'success',
                //     body: strMess,
                // });


                // обновление окна задачи
                // JIRA.trigger(JIRA.Events.REFRESH_ISSUE_PAGE, [JIRA.Issue.getIssueId()]);

                // console.log(data);
                // user = data.username;
            },
            error: function(data) {
                // var myFlag = AJS.flag({
                //     type: 'error',
                //     body: 'Ошибка загрузки',
                // });

            },
        });





        // вложения
        rowStr = rowTemplate.replace("__objectId__", "aatach_121212");
        rowStr = rowStr.replace("__object__", "Вложение вложение вложение вложение вложение вложение ");
        rowStr = rowStr.replace("__status__", "&nbsp;");

        tableObj.append(rowStr);


        rowStr = rowTemplate.replace("__objectId__", "aatach_232323");
        rowStr = rowStr.replace("__object__", "Вложение вложение вложение вложение вложение вложение ");
        rowStr = rowStr.replace("__status__", "&nbsp;");

        tableObj.append(rowStr);


        rowStr = rowTemplate.replace("__objectId__", "aatach_343434");
        rowStr = rowStr.replace("__object__", "Вложение вложение вложение вложение вложение вложение ");
        rowStr = rowStr.replace("__status__", "&nbsp;");

        tableObj.append(rowStr);


        return true;

    }


    ////////////////////////////////////////
    // отображение всплывающего окна
    ////////////////////////////////////////
    var checkSumClick = function() {


        // всплывающее окно
        var objSignDetailDiv = AJS.$("#signDetailDiv");

        if (visible) {
            objSignDetailDiv.css("display", "none");
            visible = false;
            return true;
        }

        if (!visible) {
            // отрисовать заголовок
            renderPopupWindow();

            objSignDetailDiv.css("top", 45);
            objSignDetailDiv.css("left", 100);
            objSignDetailDiv.show("fast");
            visible = true;
            return true;
        }

    }

    return {
        checkSumClick:checkSumClick
    };



}());

