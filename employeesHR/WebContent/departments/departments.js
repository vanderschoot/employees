var url;
var newrecord = false;

$(function() {
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
	
	//Bereid de easui tabel voor op juiste url
	var serverurl = window.parent.$('#serverurl').val();
	$('#dg').datagrid({
		url:serverurl + '/dep/list'
	});
	
});


function link2Employees(value,row,index){  
    return '<a href="../employees/employees.html?departmentId=' + row.departmentId + '">Medewerkers-></a>';  
}
function newDepartment(){  
	var serverurl = window.parent.$('#serverurl').val();
    $('#dlg').dialog('open').dialog('setTitle','New Department');  
    $('#fm').form('clear');  
	newrecord = true;
    url = serverurl + '/dep/create';  
}  
function editDepartment(){  
	var serverurl = window.parent.$('#serverurl').val();
    var row = $('#dg').datagrid('getSelected');  
    if (row){  
        $('#dlg').dialog('open').dialog('setTitle','Edit Department');  
        $('#fm').form('load',row);  
    	newrecord = false;
        url = serverurl + '/dep/update';  
    }  
}
function saveDepartment(){ 
    var row = $('#dg').datagrid('getSelected');  
	var urlrequest="";
	if (newrecord) {
		urlrequest = url;
	} else {
		urlrequest = url + '/' + row['departmentId'];
	}
	urlrequest = urlrequest + 
	'?name=' + $('#name').val() + 
	'&address=' + 	$('#address').val() + 
	'&budget=' + $('#budget').val();
	
    $('#fm').form('submit',{  
        url: urlrequest,
        onSubmit: function(){  
            return $(this).form('validate');  
        },  
        success: function(result){  
            var result = eval('('+result+')');  
            if (result.success){  
                $('#dlg').dialog('close');      // close the dialog  
                $('#dg').datagrid('reload');    // reload the data  
            } else {  
                $.messager.show({  
                    title: 'Error',  
                    msg: result.msg  
                });  
            }  
        }  
    });  
} 
function removeDepartment(){  
	var serverurl = window.parent.$('#serverurl').val();
    var row = $('#dg').datagrid('getSelected');  
    if (row){  
        $.messager.confirm('Confirm','Weet u dit zeker?',function(r){  
            if (r){  
                $.post(serverurl + '/dep/delete/' + row['departmentId'],{id:row['departmentId']},function(result){  
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


