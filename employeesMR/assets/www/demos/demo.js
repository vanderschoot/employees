/* Demo van een basic Ajax aanroep, zonder jQuery */
function getDepartmentsAjax() {
	var req = new XMLHttpRequest();
	req.onreadystatechange = function () {
		//console.log("test2, readyState = " + this.readyState + " / status = " + this.status);
		if (this.readyState == 4) {
			if (this.status == 200) {
    	 		var data = JSON.parse(this.responseText);
				//alert("test, status = " + this.status +  " / response = " + this.responseText + " / data = " + data);
   	 			var ul = document.getElementById("dplist");
    	 		//console.log("test 3, length=" + data.rows.length);
    	 		for (var i=0; i < data.length; i++) {
	    	 		var item = document.createElement("li");
	    	 		item.innerHTML = data[i].name;
    	 			ul.appendChild(item);
    	 	    }
    	    } else if (this.status == 0) { 
				//console.log("test 4, status = " + this.status +  " / response = " + this.responseText);
    	    }
	     }
	};				
	req.open("POST", serverurl + "/dep/listu", true);
	req.send();        
 }

/* Demo van een Ajax aanroep met jQuery */
function getDepartmentsJQ() {	  
    //jQuery.support.cors = true;
    $.ajax(
        {
            type: "POST",
            url: serverurl + "/dep/listu",
            dataType: "json",
            success: function (data) {
            	console.log('data received : ' + data);
  				//alert("test, data = " + data +  " / length = " + data.length);
                $.each(data, function (i, item) {
                    $("#dplist").append('<li id="' + i + '">' + item.name + '</li>');
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
          	  console.log('error trapped in error: function(jqXHR, textStatus, errorThrown)');
          	  console.log('XHRstatus = ' + jqXHR.status + ' XHRreadyState = ' + jqXHR.readyState + ', textStatus = ' + textStatus + ', errorThrown = ' + errorThrown);
            }
        });
}
