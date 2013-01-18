var auth;
var userName;

function createToken(user, password) {
	  userName = user;
	  var tok = user + ':' + password;
	  var hash = Base64.encode(tok);
	  return "Basic " + hash;
}

function registerme() {
	var messages = "";
	var serverurl = getServerUrl();
	$('#reg-messages').html("");
	if ((serverurl == null) || (serverurl == '')) {
		messages = messages + "server is niet ingesteld</br>";		
	}
	if ($('#frmsec2\\:inp2-email').val() ==  '') {
		messages = messages + "email is niet ingevuld</br>";
	} else {
		if (!validateEmail($('#frmsec2\\:inp2-email').val())) {
			messages = messages + "email heeft onjuist formaat</br>";			
		}
	}
	if ($('#frmsec2\\:inp2-username').val() ==  '') {
		messages = messages + "gebruikersnaam is niet ingevuld</br>";
	}
	if ($('#frmsec2\\:inp2-password').val() ==  '') {
		messages = messages + "wachtwoord is niet ingevuld</br>";
	}
	if ($('#frmsec2\\:inp2-password2').val() ==  '') {
		messages = messages + "wachtwoord 2 is niet ingevuld</br>";
	}
	if ( ($('#frm2\\:inp-password').val() !=  '') && ($('#frm2\\:inp2-password2').val() !=  '')) {
		if ($('#frm2\\:inp2-password').val() != $('#frm2\\:inp2-password2').val()) {
			messages = messages + "wachtwoorden zijn niet gelijk</br>";
		}
	}
	if (messages != "") {
		messages = "<p style='color:red'>" + messages + "</p>";
		$('#reg-messages').html(messages);
	} else {		
		var queryparams = 	"&userName=" + $('#frmsec2\\:inp2-username').val() + 
							"&email=" + $('#frmsec2\\:inp2-email').val() + 
							"&password=" + $('#frmsec2\\:inp2-password').val();		
		var data = ajaxcall_withcredentials(serverurl + '/SecurityController?action=register' + queryparams, auth);
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
	}
	return false;
}

function askpassword() {
	var serverurl = getServerUrl();
	var messages = "";
	$('#pwr-messages').html("");
	if ((serverurl == null) || (serverurl == '')) {
		messages = messages + "server is niet ingesteld</br>";		
	}
	if ($('#frmsec3\\:inp3-email').val() ==  '') {
		messages = messages + "email is niet ingevuld</br>";
	} else {
		if (!validateEmail($('#frmsec3\\:inp3-email').val())) {
			messages = messages + "email heeft onjuist formaat</br>";			
		}
	}
	if ($('#frmsec3\\:inp3-username').val() ==  '') {
		messages = messages + "gebruikersnaam is niet ingevuld</br>";
	}
	if (messages != "") {
		messages = "<p style='color:red'>" + messages + "</p>";
		$('#pwr-messages').html(messages);
	} else {
		var queryparams = 	"&userName=" + $('#frmsec3\\:inp3-username').val() + 
							"&email=" + $('#frmsec3\\:inp3-email').val();
		var data = ajaxcall_withcredentials(serverurl + '/SecurityController?action=askpassword' + queryparams, auth);
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
	}
	return false;
}

function logmein() {
	var serverurl = getServerUrl();
	var messages = "";
	$('#login-messages').html("");
	if ((serverurl == null) || (serverurl == '')) {
		messages = messages + "server is niet ingesteld</br>";		
	}
	if ($('#frmsec1\\:inp1-username').val() ==  '') {
		messages = messages + "gebruikersnaam is niet ingevuld</br>";
	}
	if ($('#frmsec1\\:inp1-password').val() ==  '') {
		messages = messages + "wachtwoord is niet ingevuld</br>";
	}
	if (messages != "") {
		messages = "<p style='color:red'>" + messages + "</p>";
		$('#login-messages').html(messages);
  		alert("login fout : " + messages);
		return false;
	} else {
		auth = createToken($("#frmsec1\\:inp1-username").val(),$("#frmsec1\\:inp1-password").val());
		var data = ajaxcall_withcredentials(serverurl + '/SecurityController?action=login', auth);
      	if (data) {
      		if (data.success) {
              	console.log("login ready, roles = " + data.message);
      			$("#div-login").addClass("hide");
      			$("#div-logout").removeClass("hide");
      			$("#usernameLoggedIn").html(userName);
      			data.message.split(",").forEach(function(item) {enableMenuRoles(item);});
      			setCookie("user", $("#frmsec1\\:inp1-username").val());
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
	}
}

function logout() {
	  var serverurl = getServerUrl();
	  var user = getCookie("user");
	  var messages = "";
	  $('#login-messages').html("");
	  if ((serverurl == null) || (serverurl == '')) {
			messages = messages + "server is niet ingesteld</br>";		
      }	
	  if (messages != "") {
		messages = "<p style='color:red'>" + messages + "</p>";
		$('#login-messages').html(messages);
	  } else { 
			var data = ajaxcall_withcredentials(serverurl + '/SecurityController?action=logout', auth);
			if (data) {
		  		if (data.success) {
		          	console.log("logout ready");
		  			$("#div-login").removeClass("hide");
		  			$("#div-logout").addClass("hide");
		  			disableMenuRoles("Administrator", "User");
		  			setCookie("user", null);
		  		} else {              			
		      		alert("uitlog fout : " + data.message);
		  		}
		  	} else {
		  		alert("geen logout terugkoppeling ontvangen");
		  	}
	  }
}

function checkrole(user, role) {
	  var serverurl = getServerUrl();
	  var authorized = false;
	  
	  var data = ajaxcall_withcredentials(serverurl + '/SecurityController?action=checkrole&user=' + user + '&role=' + role, auth);
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
	  return authorized;
}

function loggedin(user) {
	  var serverurl = getServerUrl();
	  var authorized = false;
	  var user = getCookie("user");
	  if (user) {
			var data = ajaxcall_withcredentials(serverurl + '/SecurityController?action=loggedin&userName=' + user, auth);
		    if (data) {
		    		if (data.success) {
		            	console.log("logged in");
		            	authorized = true;
		      			$("#div-login").addClass("hide");
		      			$("#div-logout").removeClass("hide");
		      			$("#usernameLoggedIn").html(user);
	          			data.roles.split(",").forEach(function(item) {enableMenuRoles(item);});
		      			setCookie("user", data.userName);
		    		} else {              			
		            	console.log("not logged in");
		      			setCookie("user", null);
		    		}
		    } else {
		    		console.log("no login feedback available");
		    }
	  }
	  return authorized;
}
