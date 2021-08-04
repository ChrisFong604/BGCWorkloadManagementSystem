window.addEventListener("load", getResources);
        
function getResources() {

    let table = document.getElementById("myTable");

    //var res = [[${project.resources}]];

    // converting JSON object to JS object
    var obj = JSON.parse(res);

    // get names/resources
    var resources = Object.keys(obj);
    console.log(resources);

    // get weeks/capacities
    var capacities = Object.values(obj);
    console.log(capacities);

    // for header
    var week = Object.keys(capacities[0]);
    console.log(week);

    var thead = document.getElementById("table-header");
    for (i = 0; i < week.length; i++) {
        var th = document.createElement("th");
        th.innerHTML = week[i];
        thead.appendChild(th);
        //console.log(week[i]);
    }

    
    for (i = 0; i < resources.length; i++) {

        // add row
        let row = table.insertRow(-1);
        
        // Name
        let colName = row.insertCell(0);
        colName.innerHTML = resources[i];

        // capacities/visual
        for (j = 0; j < week.length; j ++) {
            let col = row.insertCell(j+1);
            var caps = Object.values(capacities[i]);
            console.log(caps[j]);
            col.innerHTML = caps[j];
        }

    }
}


