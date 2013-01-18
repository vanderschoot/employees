jQuery(document).ready(function() {
	$('#frmsrv\\:server').val(getCookie("server"));
	$('#frmsrv\\:port').val(getCookie("port"));
	$('#frmsrv\\:project').val(getCookie("project"));
});

function saveServer() {
	var URL;
	URL = "http://" + $('#frmsrv\\:server').val() + ":" + $('#frmsrv\\:port').val() + "/" + $('#frmsrv\\:project').val();
	
	setCookie("server", $('#frmsrv\\:server').val());
	setCookie("port", $('#frmsrv\\:port').val());
	setCookie("project", $('#frmsrv\\:project').val());
	setCookie("serverurl", URL);
	return true;
}
