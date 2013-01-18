var auth;
var userName;

function createToken(user, password) {
	  userName = user;
	  var tok = user + ':' + password;
	  var hash = Base64.encode(tok);
	  return "Basic " + hash;
}

function register() {
	var messages = "";
	var serverurl = getServerUrl();
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
		
		var data = ajaxcall_withcredentials(serverurl + '/control/register.html' + queryparams, auth);
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
}

function askpasswordREST() {
	var serverurl = getServerUrl();
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
		
		var data = ajaxcall_withcredentials(serverurl + '/control/security/askpassword.html' + queryparams, auth);
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
}

function loginREST() {
	var serverurl = getServerUrl();
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
		
		var data = ajaxcall_withcredentials(serverurl + '/control/security/login.html', auth);
      	if (data) {
      		if (data.success) {
              	console.log("login ready, roles = " + data.message);
      			$("#div-login").addClass("hide");
      			$("#div-logout").removeClass("hide");
      			$("#usernameLoggedIn").html(userName);
      			data.message.split(",").forEach(function(item) {enableMenuRoles(item);});
      			setCookie("user", $("#inp1-username").val());
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

function logoutREST() {
	var serverurl = getServerUrl();
	var user = getCookie("user");
	var data = ajaxcall_withcredentials(serverurl + '/control/security/logout.html?userName=' + user, auth);
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

function checkroleREST(user, role) {
	  var serverurl = getServerUrl();
	  var authorized = false;
	  var data = ajaxcall_withcredentials(serverurl + '/control/security/checkrole.html?user=' + user + '&role=' + role, auth);
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

function loggedinREST() {
	var serverurl = getServerUrl();
	var authorized = false;
	var user = getCookie("user");
	if (user) {
		var data = ajaxcall_withcredentials(serverurl + '/control/security/loggedin.html?userName=' + user, auth);
	    if (data) {
	    		if (data.success) {
	            	console.log("logged in");
	            	authorized = true;
	      			$("#div-login").addClass("hide");
	      			$("#div-logout").removeClass("hide");
	      			$("#usernameLoggedIn").html(user);
          			data.message.split(",").forEach(function(item) {enableMenuRoles(item);});
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

