var currentTab = 0;
var num_weeks = [];
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
		console.log(
			findweeks(
				document.getElementById("start").value,
				document.getElementById("end").value
			)
		);
	}
	x[currentTab].style.display = "none";
	// Increase or decrease the current tab by 1:
	currentTab = currentTab + n;
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

	let week_dates = [];

	while (day_diff >= 7) {
		let last_day = new Date(enddate);
		week_dates.push(last_day.setDate(last_day.getDate() - day_diff));
		day_diff -= 7;
	}
	if (day_diff > 0) {
		week_dates.push(enddate.getDate() + day_diff);
	}
	return week_dates;
}
