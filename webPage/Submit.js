function submit() {
	var formData = new FormData();
	var firstNameNode = document.getElementById("firstName");
	var lastnameNode = document.getElementById("lastname");
	var emailNode = document.getElementById("email");
	var contentNode = document.getElementById("content");

	formData.append("firstName", firstNameNode.value);
	formData.append("lastName", lastnameNode.value);
	formData.append("email", emailNode.value);
	formData.append("content", contentNode.value);

	alert("Submited");
	var request = new XMLHttpRequest();
	request.open("POST", "contact");
	request.send(formData);
}

