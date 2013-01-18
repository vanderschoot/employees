function validateEmail(email) { 
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    var res = re.test(email);
    return res;
} 

var today = new Date();
var expiry = new Date(today.getTime() + 30 * 24 * 3600 * 1000); // plus 30 days

function setCookie(name, value)
{
  document.cookie=name + "=" + escape(value) + "; path=/; expires=" + expiry.toGMTString();
}

function getCookie(name)
{
  var re = new RegExp(name + "=([^;]+)");
  var value = re.exec(document.cookie);
  return (value != null) ? unescape(value[1]) : null;
}

function ajaxcall_withcredentials(url, auth) {
$.ajax({type: "GET",
    url: url,
    async: false,
    dataType: "json",
	    beforeSend : function(req) {
	    	req.withCredentials = true;
	        req.setRequestHeader('Authorization', auth);
	    },
    success: function (data) {
    	window.res = data;
    },
    error: function (xhr, errorstatus, errorThrown) {
    	window.res.success = false;
    	window.res.message = errorThrown;
        console.log("ajax call=not ok, errorstatus= " + errorstatus + " xhr.status= " + xhr.status + " errorThrown= " + errorThrown);                
    }
});
return window.res;
}

function ajaxcall(url) {
}
