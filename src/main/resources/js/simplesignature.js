
var simplesign = {};
simplesign.module = [];

simplesign.module = (function () {

    var visible = false;

    var checkSumClick = function() {

        // всплывающее окно
        var objSignDetailDiv = AJS.$("#signDetailDiv");

        if (visible) {
            objSignDetailDiv.css("display", "none");

            visible = false;
            return true;
        }

        if (!visible) {

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

