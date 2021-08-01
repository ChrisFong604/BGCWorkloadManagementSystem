var currentTab = 0;

var week_periods = [];
var num_resources = 0;
var project_name = "";

var total_resources = {};

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
	x[currentTab].style.display = "none";
	// Increase or decrease the current tab by 1:
	currentTab += n;
	console.log(currentTab);
	//Wipe html from form pt 2
	if (currentTab == 0) {
		document.getElementById("weeks").innerHTML = "";
		document.getElementById("resources").innerHTML = "";
	}

	if (currentTab == 1) {
		week_periods = findweeks(
			document.getElementById("start").value,
			document.getElementById("end").value
		);

		project_name = document.getElementById("name").value;
		document.getElementById("project-name").innerHTML = project_name;

		create_weekly_distribution();
		addResource();
		num_resources = 1;
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

	offset_date = new Date();
	const offset = offset_date.getTimezoneOffset();
	startdate = new Date(startdate.getTime() + offset * 60 * 1000);
	enddate = new Date(enddate.getTime() + offset * 60 * 1000);

	let day_diff =
		Math.round(enddate.getTime() - startdate.getTime()) / (1000 * 60 * 60 * 24);

	let week_dates = [];

	while (day_diff > 0) {
		let last_day = new Date(enddate);
		last_day.toLocaleString("en-CA", {
			timeZone: "Canada/Pacific",
		});
		last_day.setDate(last_day.getDate() - day_diff);
		last_day = last_day.toISOString().split("T")[0];
		week_dates.push(last_day);
		day_diff -= 7;
	}
	enddate.toLocaleString("en-CA", {
		timeZone: "Canada/Pacific",
	});

	week_dates.push(enddate.toISOString().split("T")[0]);
	return week_dates;
}

//Creates a table with all the weeks specified as well as inputs for each employee resource
function create_weekly_distribution() {
	let table = document.getElementById("weeks");
	table.innerHTML = "<th><h1>Resource Name</h1></th>";
	for (let i = 0; i < week_periods.length; i++) {
		const week = document.createElement("TH");
		week.innerHTML = "<h1>" + week_periods[i] + "</h1>";

		table.appendChild(week);
	}
}

function addResource() {
	num_resources++;
	let resource_table = document.getElementById("resources");
	let new_resource = resource_table.insertRow(-1);

	let name_input = document.createElement("TH");

	name_input.innerHTML =
		'<input name="resource-name" placeholder="resource name"/>';
	new_resource.appendChild(name_input);

	for (let i = 0; i < week_periods.length; i++) {
		weekly_input = document.createElement("TD");
		weekly_input.innerHTML =
			"<input type='number' name='resource-capacity' class='week" +
			" " +
			(i + 1).toString() +
			"'/>";
		new_resource.appendChild(weekly_input);
	}
}

function submitHandler() {
	var resources = document.getElementsByName("resource-capacity");
	var resource_names = document.getElementsByName("resource-name");

	var weeks = week_periods.length;
	//we create a JSON object with a key for each resources name and assigned work capacity
	for (var i = 0; i < resource_names.length; i++) {
		let work_capacities = {};
		for (var j = 0; j < weeks; j++) {
			work_capacities[week_periods[j]] = resources[i * weeks + j].value;
		}
		total_resources[resource_names[i].value] = work_capacities;
	}

	//JSON for sum of work capacity for each week
	let weekly_capacities = {};
	for (let i = 0; i < week_periods.length; i++) {
		let capacity_inputs = document.getElementsByClassName(
			"week " + (i + 1).toString()
		);
		let capacity_sum = 0;
		for (let j = 0; j < capacity_inputs.length; j++) {
			capacity_sum += parseInt(capacity_inputs[j].value);
		}
		weekly_capacities[week_periods[i]] = capacity_sum;
	}

	//Inserts values into invisible inputs that will be grabbed from form submission
	document.getElementById("resource-id").value =
		JSON.stringify(total_resources);
	document.getElementById("weekly-capacities-id").value =
		JSON.stringify(weekly_capacities);
	document.getElementById("project-form").submit();
}
