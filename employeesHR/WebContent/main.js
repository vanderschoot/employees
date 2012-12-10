var auth;
var userName;

function createTokenREST(user, password) {
	  userName = user;
	  var tok = user + ':' + password;
	  var hash = Base64.encode(tok);
	  return "Basic " + hash;
}

function getserverHost() {
	return window.serverhost;
}
function getserverPort() {
	return window.serverport;
}
function getserverProject() {
	return window.serverproject;
}

function registerREST() {
	var messages = "";
	var serverurl = $('#serverurl').val();
	$('#reg-messages').html("");
	if ($('#inp2-email').val() ==  '') {
		messages = messages + "email is niet ingevuld</br>";
	} else {
		if (!validateEmail($('#inp2-email').val())) {
			messages = messages + "email heeft onjuist formaat</br>";			
		}
	}
	if ($('#inp2-username').val() ==  '') {
		messages = messages + "gebruikersnaam is niet ingevuld</br>";
	}
	if ($('#inp2-password').val() ==  '') {
		messages = messages + "wachtwoord is niet ingevuld</br>";
	}
	if ($('#inp2-password2').val() ==  '') {
		messages = messages + "wachtwoord 2 is niet ingevuld</br>";
	}
	if ( ($('#inp-password').val() !=  '') && ($('#inp2-password2').val() !=  '')) {
		if ($('#inp2-password').val() != $('#inp2-password2').val()) {
			messages = messages + "wachtwoorden zijn niet gelijk</br>";
		}
	}
	if (messages != "") {
		messages = "<p style='color:red'>" + messages + "</p>";
		$('#reg-messages').html(messages);
	} else {		
		var queryparams = 	"?userName=" + $('#inp2-username').val() + 
							"&email=" + $('#inp2-email').val() + 
							"&password=" + $('#inp2-password').val();
		
		  $.ajax({	type: "POST",
              url: serverurl + '/security/register' + queryparams,
              dataType: "json",
      	      beforeSend : function(req) {
        	    	req.withCredentials = true;
        	    	req.setRequestHeader('Authorization', auth);
      	    },
            success: function (data) {
              	if (data) {
              		if (data.success) {
    	              	console.log("registratie gereed");
                  		alert("registratie uitgevoerd. " + data.message);
              		} else {              			
                  		alert("registratie fout : " + data.message);
              		}
              	} else {
              		alert("geen registratie terugkoppeling ontvangen");
              	}
              },
              error: function (xhr, errorstatus, errorThrown) {
                  alert('ERROR, xhr = ' + xhr + ', errorstatus = ' + errorstatus + ', errorThrown = ' + errorThrown);
                  console.log("ajax call=not ok, errorstatus= " + errorstatus + " xhr.status= " + xhr.status + " errorThrown= " + errorThrown);                
              }
          });
	}
}

function askpasswordREST() {
	var serverurl = $('#serverurl').val();
	var messages = "";
	$('#reg-messages').html("");
	if ($('#inp3-email').val() ==  '') {
		messages = messages + "email is niet ingevuld</br>";
	} else {
		if (!validateEmail($('#inp3-email').val())) {
			messages = messages + "email heeft onjuist formaat</br>";			
		}
	}
	if ($('#inp3-username').val() ==  '') {
		messages = messages + "gebruikersnaam is niet ingevuld</br>";
	}
	if (messages != "") {
		messages = "<p style='color:red'>" + messages + "</p>";
		$('#pwr-messages').html(messages);
	} else {
		var queryparams = 	"?userName=" + $('#inp3-username').val() + 
		"&email=" + $('#inp3-email').val();
		$.ajax({	type: "POST",
		url: serverurl + '/security/askpassword' + queryparams,
		dataType: "json",
		beforeSend : function(req) {
		req.withCredentials = true;
		req.setRequestHeader('Authorization', auth);
		},
		success: function (data) {
		if (data) {
			if (data.success) {
		  	console.log("password verzonden");
				alert("password verzonden. " + data.message);
			} else {              			
				alert("probleem opgetreden: " + data.message);
			}
		} else {
			alert("geen terugkoppeling ontvangen");
		}
		},
		error: function (xhr, errorstatus, errorThrown) {
		alert('ERROR, xhr = ' + xhr + ', errorstatus = ' + errorstatus + ', errorThrown = ' + errorThrown);
		console.log("ajax call=not ok, errorstatus= " + errorstatus + " xhr.status= " + xhr.status + " errorThrown= " + errorThrown);                
		}
		});
	}
}

function loginREST() {
	var serverurl = $('#serverurl').val();
	var messages = "";
	$('#login-messages').html("");
	if ($('#inp1-username').val() ==  '') {
		messages = messages + "gebruikersnaam is niet ingevuld</br>";
	}
	if ($('#inp1-password').val() ==  '') {
		messages = messages + "wachtwoord is niet ingevuld</br>";
	}
	if (messages != "") {
		messages = "<p style='color:red'>" + messages + "</p>";
		$('#login-messages').html(messages);
	} else {
		auth = createTokenREST($("#inp1-username").val(),$("#inp1-password").val());
		$.ajax({	type: "POST",
	            url: serverurl + '/security/login',
	            dataType: "json",
	      	    beforeSend : function(req) {
	      	    	req.withCredentials = true;
	      	        req.setRequestHeader('Authorization', auth);
	      	    },
	            success: function (data) {
	              	if (data) {
	              		if (data.success) {
	                      	console.log("login ready");
	              			$("#div-login").addClass("hide");
	              			$("#div-logout").removeClass("hide");
	              			$("#usernameLoggedIn").html(userName);
	              			data.message.split(",").forEach(function(item) {enableMenuRoles(item);});
	              		} else {              			
	                  		alert("login fout : " + data.message);
	                  		if (data.message.indexOf("already been authenticated") > 0) {
	                          	console.log("already been authenticated");
	                  			$("#div-login").addClass("hide");
	                  			$("#div-logout").removeClass("hide");                  			
	                  		}
	              		}
	              	} else {
	              		alert("geen login terugkoppeling ontvangen");
	              	}
	              },
	              error: function (xhr, errorstatus, errorThrown) {
	                  alert('ERROR, xhr = ' + xhr + ', errorstatus = ' + errorstatus + ', errorThrown = ' + errorThrown);
	                  console.log("ajax call=not ok, errorstatus= " + errorstatus + " xhr.status= " + xhr.status + " errorThrown= " + errorThrown);                
	              }
		});
	}
}

function logoutREST() {
	  var serverurl = $('#serverurl').val();
	  $.ajax({	type: "POST",
	              url: serverurl + '/security/logout',
	              dataType: "json",
	      	      beforeSend : function(req) {
	        	    	req.withCredentials = true;
	        	    	req.setRequestHeader('withCredentials', 'true');
	        	    	req.setRequestHeader('Authorization', auth);
	      	    },
	            success: function (data) {
	              	if (data) {
	              		if (data.success) {
	    	              	console.log("logout ready");
	              			$("#div-login").removeClass("hide");
	              			$("#div-logout").addClass("hide");
	              			disableMenuRoles("Administrator", "User");
	              		} else {              			
	                  		alert("uitlog fout : " + data.message);
	              		}
	              	} else {
	              		alert("geen logout terugkoppeling ontvangen");
	              	}
	              },
	              error: function (xhr, errorstatus, errorThrown) {
	                  alert('ERROR, xhr = ' + xhr + ', errorstatus = ' + errorstatus + ', errorThrown = ' + errorThrown);
	                  console.log("ajax call=not ok, errorstatus= " + errorstatus + " xhr.status= " + xhr.status + " errorThrown= " + errorThrown);                
	              }
	          });
}
function checkroleREST(user, role) {
	  var serverurl = window.parent.$('#serverurl').val();
	  var authorized = false;
	  $.ajax({	type: "GET",
		  		async:false,
	            url: serverurl + '/security/checkrole/' + user + '/' + role,
	            dataType: "json",
	      	    beforeSend : function(req) {
	        	  	req.withCredentials = true;
	        	  	req.setRequestHeader('Authorization', auth);
	      	    },
	            success: function (data) {
	              	if (data) {
	              		if (data.success) {
	    	              	console.log("role is assigned");
	    	              	authorized = true;
	              		} else {              			
	    	              	console.log("role is not assigned");
	              		}
	              	} else {
	              		console.log("no role feedback available");
	              	}
	              },
	              error: function (xhr, errorstatus, errorThrown) {
	                  console.log("ajax call=not ok, errorstatus= " + errorstatus + " xhr.status= " + xhr.status + " errorThrown= " + errorThrown);                
	              }
	          });
	  return authorized;
}

function validateEmail(email) { 
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    var res = re.test(email);
    return res;
} 
