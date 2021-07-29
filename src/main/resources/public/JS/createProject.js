var currentTab = 0;
var num_weeks = [];
var project_name = "";
showTab(currentTab);

function showTab(Tab) {
	// This function will display the specified tab of the form ...
	var x = document.getElementsByClassName("tab");
	x[Tab].style.display = "block";
	// ... and fix the Previous/Next buttons:
	if (Tab == 0) {
		document.getElementById("prevBtn").style.display = "none";
	} else {
		document.getElementById("prevBtn").style.display = "inline";
	}
	if (Tab == x.length - 1) {
		document.getElementById("nextBtn").style.display = "none";
	} else {
		document.getElementById("nextBtn").style.display = "inline";
		document.getElementById("nextBtn").innerHTML = "Next";
	}
	// ... and run a function that displays the correct step indicator:
	fixStepIndicator(Tab);
}

function nextPrev(n) {
	//n is the incrementor (either 1 or -1)
	// This function will figure out which tab to display
	var x = document.getElementsByClassName("tab");
	// Exit the function if any field in the current tab is invalid:
	if (n == 1 && !validateForm()) return false;
	// Hide the current tab:

	if (currentTab == 0) {
		num_weeks = findweeks(
			document.getElementById("start").value,
			document.getElementById("end").value
		);
	}

	x[currentTab].style.display = "none";
	// Increase or decrease the current tab by 1:
	currentTab = currentTab + n;

	if (currentTab == 1) {
		project_name = document.getElementById("name").value;
		let projects = document.getElementsByClassName("project-name");

		for (let i = 0; i < projects.length; i++) {
			projects[i].innerHTML = project_name;
		}

		create_weekly_distribution();
	}

	// if you have reached the end of the form... :
	if (currentTab >= x.length) {
		document.getElementById("regForm").submit();
		return false;
	}
	// Otherwise, display the correct tab:
	showTab(currentTab);
}

function validateForm() {
	// This function deals with validation of the form fields
	let valid = true;
	let tabs = document.getElementsByClassName("tab");
	let inputs = tabs[currentTab];

	if (currentTab == 0) {
		let start = new Date(document.getElementById("start"));
		let end = new Date(document.getElementById("end"));
		if (end < start) {
			valid = false;
		}
	}

	if (valid) {
	} else {
		document.getElementById("error").innerHTML("Invalid Input!");
	}
	return valid; // return the valid status
}

function fixStepIndicator(n) {
	// This function removes the "active" class of all steps...
	var i,
		x = document.getElementsByClassName("step");
	for (i = 0; i < x.length; i++) {
		x[i].className = x[i].className.replace(" active", "");
	}
	//... and adds the "active" class to the current step:
	x[n].className += " active";
}

/*
	Manipulation with dates functions
*/

function findweeks(first, second) {
	//Receives two date strings, and performs arithmetic to find difference. The second date must ALWAYS be later
	startdate = new Date(first);
	enddate = new Date(second);
	let day_diff =
		Math.round(enddate.getTime() - startdate.getTime()) / (1000 * 60 * 60 * 24);

	console.log("day diff = " + day_diff);
	let week_dates = [];

	while (day_diff > 0) {
		let last_day = new Date(enddate);
		last_day.setDate(last_day.getDate() - day_diff);
		week_dates.push(last_day);
		day_diff -= 7;
	}
	week_dates.push(enddate);
	return week_dates;
}

//Creates a table with all the weeks specified as well as inputs for each employee resource
function create_weekly_distribution() {
	let table = document.getElementById("weeks");
	table.innerHTML = "<th>Resource Name</th>";
	for (let i = 0; i < num_weeks.length; i++) {
		const week = document.createElement("TH");
		const node = document.createTextNode(num_weeks[i]);

		week.appendChild(node);
		table.appendChild(week);
	}
	addResource();
}

function addResource() {
	let table = document.getElementById("work-table");
	let new_resource = table.insertRow(-1);

	let name_input = document.createElement("th");

	name_input.innerHTML = '<input placeholder="resource name"/>';
	new_resource.appendChild(name_input);

	for (let i = 0; i < num_weeks.length; i++) {
		weekly_input = document.createElement("th");
		weekly_input.innerHTML = "<input placeholder='week" + " " + i + 1 + "' />";
		new_resource.appendChild(weekly_input);
	}
}
