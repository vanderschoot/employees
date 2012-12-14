var selectedEmployeeId;

/* CRUD Functies voor Employee */

function getEmployeeListJQM() {
    var  urlrequest;
    if (selectedDepartmentId) {
    	urlrequest= serverurl + "/emp/listu?departmentId=" + selectedDepartmentId; 
	} else {
    	urlrequest= serverurl + "/emp/listu"; 		
	}
    $.ajax(
        {
            type: "GET",
            url: urlrequest,
            dataType: "json",
            success: function (data) {
            	console.log('data received : ' + data);
            	$("#emplist").empty();
                $.each(data, function (i, item) {
                    $("#emplist").append('<li id="' + item['employeeId'] + '"><a href="employeeEditJQM.html">' + item.firstName + ' ' + item.lastName + '</a></li>');
                });
                $("#emplist").listview("refresh");
        		$("li").on("vclick", function (event) {selectedEmployeeId = $(this).attr("id");});
            },
            error: function (jqXHR, textStatus, errorThrown) {
          	  console.log('error trapped in error: function(jqXHR, textStatus, errorThrown)');
          	  console.log('XHRstatus = ' + jqXHR.status + ' XHRreadyState = ' + jqXHR.readyState + ', textStatus = ' + textStatus + ', errorThrown = ' + errorThrown);
            }
        });
}

function getEmployeeJQM() {	  
	console.log('parameter found, employeeId = ' + selectedEmployeeId);
	// Lees eerst de lijst departments voor de foreign key selectmenu 
    $.ajax(
    {
        type: "POST",
        url: serverurl + "/dep/cblistu",
        dataType: "json",
        success: function (data) {
        	console.log('cb data received : ' + data);
        	$("#department").empty();
            $.each(data, function (i, item) {
                $("#department").append('<option value="' + item.id + '">' + item.name + '</option>');
            });
            $("#department").select("refresh");
         },
        error: function (jqXHR, textStatus, errorThrown) {
      	  console.log('error trapped in error: function(jqXHR, textStatus, errorThrown)');
      	  console.log('XHRstatus = ' + jqXHR.status + ' XHRreadyState = ' + jqXHR.readyState + ', textStatus = ' + textStatus + ', errorThrown = ' + errorThrown);
        }
    });		
	if (selectedEmployeeId != null) {
	    $.ajax(
        {
            type: "POST",
            url: serverurl + "/emp/showu/" + selectedEmployeeId,
            dataType: "json",
            success: function (data) {
            	console.log('data received : ' + data);
            	$("#firstName").val(data.rows.firstName);
            	$("#lastName").val(data.rows.lastName);
            	$("#birthDate").val(formatDate(new Date(getDateFromFormat(data.rows.birthDate,"y-M-dTm:m:m+s:s")),"dd-MM-yyyy"));
            	$("#department").val(data.rows.department.departmentId).attr('selected', true).siblings('option').removeAttr('selected');
            	$("#department").selectmenu("refresh", true);
            },
            error: function (jqXHR, textStatus, errorThrown) {
          	  console.log('error trapped in error: function(jqXHR, textStatus, errorThrown)');
          	  console.log('XHRstatus = ' + jqXHR.status + ' XHRreadyState = ' + jqXHR.readyState + ', textStatus = ' + textStatus + ', errorThrown = ' + errorThrown);
            }
	    });		
	} else {
		console.log("employeeId niet gevonden, wellicht add");
	}
}

function saveEmployee() {
    var  urlrequest;
    if (selectedEmployeeId) {
    	urlrequest= serverurl + "/emp/updateu/" + selectedEmployeeId; 
	} else {
    	urlrequest= serverurl + "/emp/createu"; 		
	}
	urlrequest = urlrequest + 
	"?firstName=" + $("#firstName").val() + 
	"&lastName=" + 	$("#lastName").val() + 
	"&birthDate=" + formatDate(new Date(getDateFromFormat($("#birthDate").val(),"d-M-y")),"yyyy-MM-dd") + 
	"&departmentId=" + $("#department").val();
	
    $.ajax(
    {
        type: "POST",
        url: urlrequest,
        dataType: "json",
        success: function (data) {
        	console.log("data received : " + data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
          alert("Fout opgetreden in opslag");
      	  console.log('error trapped in error: function(jqXHR, textStatus, errorThrown)');
      	  console.log('XHRstatus = ' + jqXHR.status + ' XHRreadyState = ' + jqXHR.readyState + ', textStatus = ' + textStatus + ', errorThrown = ' + errorThrown);
        }
    });
}

function deleteEmployee() {
    var  urlrequest = serverurl + "/emp/deleteu/" + selectedEmployeeId;
    
    $.ajax(
    {
        type: "POST",
        url: urlrequest,
        dataType: "json",
        success: function (data) {
        	console.log("data received : " + data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
          alert("Fout opgetreden in verwijderen");
      	  console.log('error trapped in error: function(jqXHR, textStatus, errorThrown)');
      	  console.log('XHRstatus = ' + jqXHR.status + ' XHRreadyState = ' + jqXHR.readyState + ', textStatus = ' + textStatus + ', errorThrown = ' + errorThrown);
        }
    });
}

