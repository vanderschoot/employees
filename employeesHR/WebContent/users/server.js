
$(function() {
	$('#dlgServer').form('load',{
		server:window.parent.$("#serverhost").val(),
		port:window.parent.$("#serverport").val(),
		project:window.parent.$("#serverproject").val(),
	});

	$('#dlgServer').dialog('open').dialog('setTitle','Server URL');  
});


function saveServer() {
	var URL;
	window.parent.$('#serverhost').val($('#server').val());
	window.parent.$('#serverport').val($('#port').val());
	window.parent.$('#serverproject').val($('#project').val());
	URL = "http://" + $('#server').val() + ":" + $('#port').val() + "/" + $('#project').val();
	window.parent.$('#serverurl').val(URL);
}

