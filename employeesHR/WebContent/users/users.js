var url;
var newrecord = false;

//Bereid de easui tabel voor op juiste url
$(function() {
	var serverurl = window.parent.$('#serverurl').val();
	$('#dg').datagrid({
		url:serverurl + '/usr/list'
	});
});

// Bereid de roles combobox voor op juiste url
function loadRolesCombo(){
	var serverurl = window.parent.$('#serverurl').val();
	$('#roles').combobox('clear');
	$('#roles').combobox('reload', serverurl + '/role/cblist');
}

function newUser(){  
	var serverurl = window.parent.$('#serverurl').val();
    $('#dlg').dialog('open').dialog('setTitle','New User');  
    $('#fm').form('clear');  
    loadRolesCombo();
	newrecord = true;
    url = serverurl + '/usr/create';  
}  
function editUser(){  
	var serverurl = window.parent.$('#serverurl').val();
    var row = $('#dg').datagrid('getSelected');  
    if (row){  
        $('#dlg').dialog('open').dialog('setTitle','Edit User');  
        $('#fm').form('load',row);  
        loadRolesCombo();

        //Haal de userroles op en stel deze als geselecteerd in in de roles combobox
        $('#roles').combobox('clear');
        jQuery.support.cors = true;
        $.ajax(
            {
                type: "POST",
                url: serverurl + '/usrl/list?userId=' + row['userId'],
                dataType: "json",
                success: function (data) {
                    console.log("edituser data received , data = " + data);
                    $.each(data.rows, function (i, item) {
                        console.log("i=" + i + " / item = " + item);
                        $('#roles').combobox('select',item.role.roleId); 
                    });
                },
                error: function (msg, url, line) {
                    alert('ERROR, msg = ' + msg + ', url = ' + url + ', line = ' + line);
                }
        });

    	newrecord = false;
        url = serverurl + '/usr/update';  
    }  
}

function updateUserRoles(userId, roleList) {
	var serverurl = window.parent.$('#serverurl').val();
	var okstatus = false;
	//remove roles not in selected roleList
    $.ajax(
        {
            type: "POST",
            async: false,
            url: serverurl + '/usrl/delroles/' + userId + '/' + roleList,
            dataType: "json",
            success: function (result) {
                 console.log("delroles return received , result = " + result);
                 if (result.success) {
                    console.log("result=ok");
                    okstatus = true;
                } else {  
                    console.log("result=not ok, message: " + result.message);
                    $.messager.show({title: 'Error', msg: result.message});  
                }
            },
            error: function (msg, url, line) {
                $.messager.show({title: 'Error', msg: msg });  
            
            }
    });  
  
	if (okstatus) {
		// Create roles in selected roleList
	    $.ajax(
	            {
	                type: "POST",
	                async: false,
	                url: serverurl + '/usrl/createroles?userId=' + userId + '&roleList=' + roleList,
	                dataType: "json",
	                success: function (result) {
	                    console.log("createroles return received , result = " + result);
	                    //var result = eval('('+result+')');  
	                    if (result.success) {  
	                        console.log("result=ok");
	                    } else {
	                        console.log("result=not ok, msg = " + result.message);
	                        $.messager.show({  
	                            title: 'Error', msg: result.message  
	                        });  
	                    }  
	                },
	                error: function (msg, url, line) {
	                    alert('ERROR, msg = ' + msg + ', url = ' + url + ', line = ' + line);
	                }
	    });  
	}	
}

function saveUser() { 
    var row;  
    var userId;
	var urlrequest="";
	
	if (newrecord) {
		urlrequest = url;
	} else {
		row = $('#dg').datagrid('getSelected');
		userId = row['userId'];
		urlrequest = url + '/' + userId;
	}
	
	urlrequest = urlrequest + 
	'?userName=' + $('#userName').val() +
	'&email=' + $('#email').val() +
	'&password=' + $('#password').val();

	// Create current selected roleList
    var roleList = $('#roles').combobox('getValues').join(',');
    console.log("roleList = " + roleList);

	//Save user data
    console.log("update, url = " + urlrequest);
    jQuery.support.cors = true;
    $('#fm').form('submit',{  
        url: urlrequest,
        onSubmit: function(){  
            return $(this).form('validate');  
        },  
        success: function(resultdata){  
            var result = eval('('+resultdata+')');  
            if (result.success){  
        		if (newrecord) {
        			userId = parseInt(result.message);
        			console.log("new user created: userid = " + userId);
        		} else {
        			console.log("user updated: userid = " + userId);        			
        		}
            	updateUserRoles(userId, roleList);
                $('#dlg').dialog('close');      // close the dialog  
                $('#dg').datagrid('reload');    // reload the data  
            } else {  
                ok = false;
                $.messager.show({  
                    title: 'Error',  
                    msg: result.message  
                });  
            }  
        }  
    });   
} 

function removeUser(){  
	var serverurl = window.parent.$('#serverurl').val();
    var row = $('#dg').datagrid('getSelected');  
    if (row){  
        $.messager.confirm('Confirm','Weet u dit zeker?',function(r){  
            if (r){  
                $.post(serverurl + '/usr/delete/'+row['userId'],{id:row['userId']},function(result){  
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

