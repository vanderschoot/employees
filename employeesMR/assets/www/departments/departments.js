var selectedDepartmentId;

/* CRUD Functies voor Department */

function getDepartmentListJQM() {	  
    $.ajax(
        {
            type: "POST",
            url: serverurl + "/dep/listu",
            dataType: "json",
            success: function (data) {
            	console.log('data received : ' + data);
            	$("#dplist").empty();
                $.each(data, function (i, item) {
                    $("#dplist").append('<li id="' + item.departmentId + 
                    					'"><a href="departmentEditJQM.html?departmentId=' + item.departmentId + 
                    					'">' + item.name + '</a></li>');
                });
                $("#dplist").listview("refresh");
        		$("li").on("vclick", function (event) {selectedDepartmentId = $(this).attr("id");});
            },
            error: function (jqXHR, textStatus, errorThrown) {
          	  console.log('error trapped in error: function(jqXHR, textStatus, errorThrown)');
          	  console.log(	'XHRstatus = ' + jqXHR.status + ' XHRreadyState = ' + jqXHR.readyState + 
          			  		', textStatus = ' + textStatus + ', errorThrown = ' + errorThrown);
            }
        });
}

function getDepartmentJQM() {	  
	if (selectedDepartmentId != null) {
		console.log('parameter found, departmentId = ' + selectedDepartmentId);
	    $.ajax(
        {
            type: "POST",
            url: serverurl + "/dep/showu/" + selectedDepartmentId,
            dataType: "json",
            success: function (data) {
            	console.log('data received : ' + data);
            	$("#name").val(data.rows.name);
            	$("#address").val(data.rows.address);
            	$("#budget").val(data.rows.budget);
             },
            error: function (jqXHR, textStatus, errorThrown) {
          	  console.log('error trapped in error: function(jqXHR, textStatus, errorThrown)');
          	  console.log('XHRstatus = ' + jqXHR.status + ' XHRreadyState = ' + jqXHR.readyState + ', textStatus = ' + textStatus + ', errorThrown = ' + errorThrown);
            }
	    });		
	} else {
		console.log("departmentId niet gevonden, wellicht add");
	}
}

function saveDepartment() {
    var  urlrequest;
    if (selectedDepartmentId) {
    	urlrequest= serverurl + "/dep/updateu/" + selectedDepartmentId; 
	} else {
    	urlrequest= serverurl + "/dep/createu"; 		
	}
	urlrequest = urlrequest + 
	"?name=" + $("#name").val() + 
	"&address=" + 	$("#address").val() + 
	"&budget=" + $("#budget").val();

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

function deleteDepartment() {
    var  urlrequest = serverurl + "/dep/deleteu/" + selectedDepartmentId;
    
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


