jQuery(document).ready(function() {
	$('.toplevel li').hover(
	    function () {
	      $('.sublevel', this).show();
	    },
	    function () {
	      $('.sublevel', this).hide();
	    }
	);
	//Bind click function to all <a href> elements to disable links with attribute 'disabled'
	//This will be used to enable/disable menu items dependent on role authorization
	$('a').click(function() {
		         return ($(this).attr('disabled')) ? false : true;
	});

	disableMenuRoles("Administrator", "User");
	enableMenuRoles("Anonymous");
	loggedinREST();

});

function enableMenuRoles() {
	for (var i=0; i< arguments.length; i++) {
		var sel = ".role" + arguments[i];
		$(sel).removeAttr("disabled");
	}	
}

function disableMenuRoles() {
	for (var i=0; i< arguments.length; i++) {
		var sel = ".role" + arguments[i];
		$(sel).attr("disabled","disabled");
	}	
}
