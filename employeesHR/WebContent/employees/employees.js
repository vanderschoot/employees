var url;
var newrecord;
var request = {}; // global voor de request parameters

// Bereid de easyui tabelgrid voor op juiste url: met of zonder request parameter departmentId
//haal de request parameters op uit de url context
$(document).ready(function(){
	var serverurl = window.parent.$('#serverurl').val();
	var urlreq = serverurl + '/emp/listform';
	var userName = $('#usernameLoggedIn', window.parent.document).html();
	var authorized = false;
	if (userName) {
		authorized = checkroleREST(userName, "User");
		if (authorized) {
			$('#btnnew').linkbutton('enable');
			$('#btnedit').linkbutton('enable');
			$('#btnremove').linkbutton('enable');
		} else {
			$('#btnnew').linkbutton('disable');
			$('#btnedit').linkbutton('disable');
			$('#btnremove').linkbutton('disable');
		}
	}
	
	//zoek of request parameter departmentId is meegegeven
	var pairs = location.search.substring(1).split('&');
	for (var i = 0; i < pairs.length; i++) {
	  var pair = pairs[i].split('=');
	  request[pair[0]] = pair[1];
	}
	if (request['departmentId'] == null) request['departmentId'] = '-1';

	//als request parameter departmentId is gevuld geef dan mee aan de url in de easyui url
	$('#dg').datagrid({
		  url:urlreq,
		  onLoadError: function(){
				alert("Geen gegevens beschikbaar. Wellicht niet geauthoriseerd?");
		  },
		  queryParams:{
			  departmentId: request['departmentId']
		  },
		  onBeforeLoad: function(){
			if (userName) {
				return true;
			} else { 
				alert("Niet ingelogd!");
				return true;
			}
		  } 
	});	

}); 

// Bereid de department combobox voor op juiste url
function loadDepartmentCombo(){
	var serverurl = window.parent.$('#serverurl').val();
	$('#department').combobox('reload', serverurl + '/dep/cblist');
}

function newEmployee(){  
	var serverurl = window.parent.$('#serverurl').val();
    $('#dlg').dialog('open').dialog('setTitle','New Employee');  
    $('#fm').form('clear');  
    loadDepartmentCombo();
	newrecord = true;
    url = serverurl + '/emp/create';  
}  
function editEmployee(){  
	var serverurl = window.parent.$('#serverurl').val();
    var row = $('#dg').datagrid('getSelected');  
    if (row){  
        $('#dlg').dialog('open').dialog('setTitle','Edit Employee');  
        $('#fm').form('load',row);  
        loadDepartmentCombo();
        $('#birthDate').datebox('setValue',formatDate(new Date(getDateFromFormat(row.birthDate,"y-M-d")),"dd-MM-yyyy"));
        
        try {
          	$('#department').combobox('select',row.department.departmentId); 
        } catch (err) {
        	$('#department').combobox('clear'); 
        }
        
    	newrecord = false;
        url = serverurl + '/emp/update';  
    }  
}
function saveEmployee(){ 
    var row = $('#dg').datagrid('getSelected');  
	var urlrequest="";
	if (newrecord) {
		urlrequest = url;
	} else {
		urlrequest = url + '/' + row['employeeId'];
	}
	urlrequest = urlrequest + 
	'?firstName=' + $('#firstName').val() + 
	'&lastName=' + 	$('#lastName').val() + 
	'&birthDate=' + formatDate(new Date(getDateFromFormat($('#birthDate').datebox('getValue'),'d-M-y')),'yyyy-MM-dd') +
	'&departmentId=' + $('#department').combobox('getValue');
	
    $('#fm').form('submit',{  
        url: urlrequest,
        onSubmit: function(){  
            return $(this).form('validate');  
        },  
        success: function(result){  
            if (result) {
	            var result = eval('('+result+')');  
	            if (result.success){  
	                $('#dlg').dialog('close');      // close the dialog  
	                $('#dg').datagrid('reload');    // reload the data  
	            } else {  
	                $.messager.show({title: 'Error',  msg: result.message});  
	            }  
            } else {
                $.messager.show({title: 'Error',  msg: "Gegevens niet opgeslagen. Wellicht niet geauthoriseerd?"});  
            }
        }  
    });  
} 
function removeEmployee(){  
	var serverurl = window.parent.$('#serverurl').val();
    var row = $('#dg').datagrid('getSelected');  
    if (row){  
        $.messager.confirm('Confirm','Weet u dit zeker?',function(r){  
            if (r){  
                $.post(serverurl + '/emp/delete/' + row['employeeId'],{id:row['employeeId']},function(result){  
                    if (result.success){  
                        $('#dg').datagrid('reload');    // reload the data  
                    } else {  
                        $.messager.show({   // show error message  
                            title: 'Error',  
                            msg: result.msg  
                        });  
                    }  
                },'json');  
            }  
        });  
    }  
}  
// zet juiste format department name ipv departmentid in de tabel
function formatDepartment(val,rec){  
	return rec.department == null ? "":rec.department.name; 
}  
//zet juiste date format in de tabel
function formatDatum(val,rec){ 
    return formatDate(new Date(getDateFromFormat(rec['birthDate'],"y-M-d")),"dd-MM-yyyy");	
//    return formatDate(new Date(getDateFromFormat(rec['birthDate'],"y-M-dTm:m:m+s:s")),"dd-MM-yyyy");	
}  
//zet juiste date format voor datebox datum picker
$.fn.datebox.defaults.formatter = function(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	return (d < 10 ? ('0' + d) : d) + '-' + (m < 10 ? ('0' + m) : m) + '-' + y;
};

$.fn.datebox.defaults.parser = function(s) {
	if (s) {
		var a = s.split('-');
		var d = parseInt(a[0],10);
		var m = parseInt(a[1],10);
		var y = parseInt(a[2],10);
		//if (d>31) {var t = y;y = d;d = t;}
	    if (!isNaN(y) && !isNaN(m) && !isNaN(d)){  
	        dt = new Date(y,m-1,d);
	        return dt; 
	    } else {  
	        return new Date();  
	    }  			
	} else {
		return new Date();
	}
};