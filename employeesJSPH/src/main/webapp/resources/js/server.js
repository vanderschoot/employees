jQuery(document).ready(function() {
	$('#server').val(getCookie("server"));
	$('#port').val(getCookie("port"));
	$('#project').val(getCookie("project"));
});

function saveServer() {
	var URL;
	URL = "http://" + $('#server').val() + ":" + $('#port').val() + "/" + $('#project').val();
	
	setCookie("server", $('#server').val());
	setCookie("port", $('#port').val());
	setCookie("project", $('#project').val());
	setCookie("serverurl", URL);
	return true;
}

function getServerUrl() {
	return getCookie("serverurl");
}