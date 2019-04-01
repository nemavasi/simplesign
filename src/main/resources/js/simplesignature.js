
var simplesign = {};
simplesign.module = [];

simplesign.module = (function () {

    var visible = false;

    var checkSumClick = function() {

        if (visible) {
            AJS.$("#showSign").hide("slow");
            visible = false;
            return true;
        }

        if (!visible) {
            AJS.$("#showSign").show("slow");
            visible = true;
            return true;
        }

    }


    return {
        checkSumClick:checkSumClick
    };



}());

