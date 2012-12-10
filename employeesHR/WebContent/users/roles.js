var url;
var newrecord = false;

//Bereid de easui tabel voor op juiste url
$(function() {
	var serverurl = window.parent.$('#serverurl').val();
	$('#dg').datagrid({
		url:serverurl + '/role/list'
	});
});


function newRole(){  
	var serverurl = window.parent.$('#serverurl').val();
    $('#dlg').dialog('open').dialog('setTitle','New Role');  
    $('#fm').form('clear');  
	newrecord = true;
    url = serverurl + '/role/create';  
}  
function editRole(){  
	var serverurl = window.parent.$('#serverurl').val();
    var row = $('#dg').datagrid('getSelected');  
    if (row){  
        $('#dlg').dialog('open').dialog('setTitle','Edit Role');  
        $('#fm').form('load',row);  
    	newrecord = false;
        url = serverurl + '/role/update';  
    }  
}
function saveRole(){ 
    var row = $('#dg').datagrid('getSelected');  
	var urlrequest="";
	if (newrecord) {
		urlrequest = url;
	} else {
		urlrequest = url + '/' + row['roleId'];
	}
	urlrequest = urlrequest + 
	'?name=' + $('#name').val();
	
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
function removeRole(){  
	var serverurl = window.parent.$('#serverurl').val();
    var row = $('#dg').datagrid('getSelected');  
    if (row){  
        $.messager.confirm('Confirm','Weet u dit zeker?',function(r){  
            if (r){  
                $.post(serverurl + '/role/delete/'+row['roleId'],{id:row['roleId']},function(result){  
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
