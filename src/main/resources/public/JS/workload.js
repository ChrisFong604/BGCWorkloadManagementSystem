function workload() {
    //var listOfEmps = [[${empInRange}]];

let empstatus = [];


for (i = 0; i < listOfEmps.length; i++){
    empstatus.push(listOfEmps[i].status);
    //console.log("listOfEmps.STATUS ---------------> " + listOfEmps[i].status)
}

var wclist = [];

for (i = 0; i < listOfEmps.length; i++){

    let a = wclist.push(listOfEmps[i].rampUp);

    //console.log(a);
}
/*
for (i = 0; i < wclist[1].length; i++){

    console.log("PRINTING WCLIST ");
    console.log(wclist[0][i]);
}
*/
//var range = [[${range}]];

// Date: y/m/d
//let range = "2021-07-10";/

// START DATE
let st = range.start.split("-");

// END DATE
let y = range.end.split("-");

// Date: y/m/d

var now = new Date(y[0],y[1]-1,y[2]);
var foralert = new Date(st[0], st[1]-1, st[2]);

if (foralert > now){
    alert("Start date is later than end date!");
}

let nweeks = 0;
let weeklydates = [];
var daysOfYear = [];
for (var d = new Date(st[0], st[1]-1, st[2]); d <= now; d.setDate(d.getDate() + parseInt(7))) {

    //console.log("D " + d);
    let x = d.getDate();
    let y = d.getMonth(); // 0
    let z = d.getFullYear();
    let c = new Date(z,y,x);
    weeklydates.push(c);
    let c1 = c.getDate();
    let c2 = c.getMonth();
    let c3 = c.getFullYear();
    daysOfYear.push(c3 + "-" + parseInt(c2+1).toString() + "-" + c1);
    //console.log("C " + c);
    //console.log("DAYS OF YEAR " + c3 + "-" + parseInt(c2+1).toString() + "-" + c1);
    nweeks += 1;
}

//console.log("nweeks ------------------> " + nweeks);


let pwc = [];
let hwc = [];

for (i = 0; i < nweeks; i++){
    pwc.push(0);
    hwc.push(0);
}

for (i = 0; i < wclist.length; i++){
    for (j = 0; j < wclist[i].length; j++){
        if (empstatus[i] == true){
            pwc[j] += wclist[i][j];
            hwc[j] += wclist[i][j];
            //console.log("IF STATEMENT");
            //console.log(" WCLIST[I][J] --------------------------> " + wclist[i][j]);
        }
        else{
            pwc[j] += wclist[i][j];
            //console.log("ELSE");
        }
    }
}

//let jsoncap = project[0].capacities;
//let cap = JSON.parse(jsoncap); 
//console.log(cap["2021-08-02"]);
//console.log("CAP LENGTH ----------------> " + cap.length);



let workload = document.getElementById('workload').getContext('2d');
let myChart = new Chart(workload, {
    //type of line we want to create
    type: 'bar',
    //the data of our dataset
    data: {
        labels: daysOfYear, //[july 3, jan 10, july 17, jan 22],
        datasets: [
        {
            type: 'line',
            label: 'Hired',

            backgroundColor:'blue',
            borderColor: 'blue',
            data: hwc,
        },

        {
            type: 'line',
            label: 'Projected',

            backgroundColor:'orange',
            borderColor: 'orange',
            data: pwc,
        }

        ]
    },
    options: {
        interaction: {
            mode: 'nearest'
        },
        scales:{

            yAxes:
                {
                beginAtZero: true,
                suggestedMin: 0,
                //stacked: true,
                title: {
                    display: true,
                    text: "Project and Employee Work Capacities",
                    font: {
                        size: 25
                    },
                }
            },

            xAxes:{
                stacked: true,
                title: {
                    display: true,
                    text: "Date",
                    font: {
                        size: 25
                    }
                }
            }
        },
        plugins:{
            legend:{
                position: 'bottom',
                labels:{
                    padding:50
                }
            },
            title: {
                display: true,
                text: 'Workload',
                font: {
                        size: 50
                }
            }
        }
    }
});

let a = projects[0].id;
//console.log("PROJECTS LENGTH ----------->" + projects.length);

for (i = 0; i < projects.length; i++){
    
    let counter = 0; //counter to count # of weeks
    let jsoncap = projects[i].capacities2; // json object with capacities

    let cap = JSON.parse(jsoncap);
    
    //console.log("CAP2 ---------------> " + cap["week" + (i+1).toString()]);
    let psd = projects[i].start.split("-"); //project start date
    //console.log("PSD -------->" + psd);
    let ped = projects[i].end.split("-"); //project end date
    let dpsd = new Date(psd[0],psd[1]-1,psd[2]); //date project start
    let dped = new Date(ped[0],ped[1]-1,ped[2]); //date project end date used for comparisons
    //console.log("DATE PROJECT START AND END ----------------> " + dpsd + "||||||||||" + dped );
    //console.log("PED -------->" + ped);
    for (var d = new Date(psd[0], psd[1]-1, psd[2]); d <= dped; d.setDate(d.getDate() + parseInt(7))) {
        counter += 1;
    }
    let dataarr = []; //array for saving data
    //filling dataarr with 0
    for (g = 0; g < weeklydates.length; g++){
        dataarr.push(0);
    }
    console.log("COUNTER ---------------> " + counter);
    //console.log("WHY NOT GO INTO else IF?!?!?!");
    //console.log("dpsd > weeklydates[0]" + dpsd + " > " + weeklydates[0]);
    //console.log("dped > weeklydates[weeklydates.length-1]" + dped + " > " + weeklydates[weeklydates.length-1]);
    //if the project starts and ends between graph dates
    if ((dped < weeklydates[0]) && (dpsd < weeklydates[0]) ){
        continue;
    }
    if (dpsd >= weeklydates[0] && dped <= weeklydates[weeklydates.length-1]){
        //console.log("HELLLLLLLLLLLLLLLLLLLLLLLLO");
        let k = 0;
        while (weeklydates[k] != null){
            //console.log("HELLO NOT NULL");
            if (dpsd <= weeklydates[k]){
                //console.log("INSIDE IF STATEMENT YAYAYAY")
                if (k == 0){
                    for (u = 0; u < counter; u++){
                        //console.log("INSIDE FOR LOOOOOOOP")
                        dataarr[k+u] = cap["week" + (u+1).toString()];
                        //console.log("SUPPOSED TO PRINT TWICE ----->")
                        //console.log(cap["week" + (u+1).toString()]);
                        //console.log("PRINTING AT THIS INDEX: " + k+u)
                    }
                    break;

                }
                else if(dpsd.getTime() == weeklydates[k].getTime()){
                    for (u = 0; u < counter; u++){
                        //console.log("INSIDE FOR LOOOOOOOP")
                        //console.log("GOING INSIDE ELSE ------------------------------------------> ")
                        dataarr[(k)+u] = cap["week" + (u+1).toString()];
                        //console.log("SUPPOSED TO PRINT TWICE ----->")
                        //console.log(cap["week" + (u+1).toString()]);
                        //console.log("PRINTING AT THIS INDEX: " + ((k)+u))
                    }
                    break;


                }
                else{
                    for (u = 0; u < counter; u++){
                        //console.log("INSIDE FOR LOOOOOOOP")
                        //console.log("GOING INSIDE ELSE ------------------------------------------> ")
                        dataarr[(k-1)+u] = cap["week" + (u+1).toString()];
                        //console.log("SUPPOSED TO PRINT TWICE ----->")
                        //console.log(cap["week" + (u+1).toString()]);
                        //console.log("PRINTING AT THIS INDEX: " + ((k-1)+u))
                    }
                    break;
                }
            }
            k++;
        }        
    }
    
    //if the project started after and ends after graph dates
    else if(dpsd >= weeklydates[0] && dped > weeklydates[weeklydates.length-1]){
        //console.log("GOING INSIDE ELSE IF");
        let k = 0;
        while (weeklydates[k] != null){
            //console.log("GOING INSIDE ELSE IF WHILE")
            if (dpsd <= weeklydates[k]){
                if(dpsd.getTime() == weeklydates[k].getTime() || k == 0){
                    let a = 1;
                    for (u = k; u < weeklydates.length; u++){
                        //console.log("GOING INSIDE ELSE IF WHILE FOR LOOP")
                        dataarr[u] = cap["week" + a.toString()];
                        a++;
                        //console.log("PRINTING INSIDE ELSE IF FOR LOOP");
                        //console.log("THIS IS CAP -------> " + cap["week" + a.toString()]);
                        //console.log("THIS IS THE INDEX, AND THEN A " + u + " " + a);
                    }
                    break;
                }
                else{
                    let a = 1;
                    for (u = k-1; u < weeklydates.length; u++){
                        //console.log("GOING INSIDE ELSE IF WHILE FOR LOOP")
                        dataarr[u] = cap["week" + a.toString()];
                        a++;
                        //console.log("PRINTING INSIDE ELSE IF FOR LOOP");
                        //console.log("THIS IS CAP -------> " + cap["week" + a.toString()]);
                        //console.log("THIS IS THE INDEX, AND THEN A " + u + " " + a);
                    }
                    break;
    
                }
            }
            

            k++;
        }
    }
    //if project start date is before graph and end date is within graph 
    else if((dpsd < weeklydates[0] && dped < weeklydates[weeklydates.length-1]) && (dped > weeklydates[0]) ){

        let counter2 = 0;
        for (var d = new Date(psd[0], psd[1]-1, psd[2]); d < weeklydates[0]; d.setDate(d.getDate() + parseInt(7))) {
            counter2 += 1;
        }
        let a = 0;
        //console.log("BEFORE FOR LOOP")
        console.log("THIS IS COUNTER2 -------------> " + counter2);
        //console.log("THIS IS COUNTER ------------> " + counter);
        /*for (u = counter2; u < counter; u++){
            console.log("INSIDE FOR LOOP")
            dataarr[a] = cap["week" + (u).toString()];
            console.log("THIS IS A --------------------> " + a)
            console.log("THIS IS U --------------------> " + u)
            console.log("WEEK --------------------> " + cap["week" + (u).toString()])
            a++;
        }
        */
        let u = counter2;
        console.log("THIS IS U -------------> " + u);
        console.log("THIS IS COUNTER INSIDE ELSE IF  -------------> " + counter);
        while(u < counter){
            console.log("INSIDE WHILE AND T HIS IS CAP ======>  " + cap["week" + (u).toString()])
            console.log("AND THIS IS A ======>  " + a)
            dataarr[a] = cap["week" + (u).toString()];
            u++;
            a++;
        }

        console.log("THIS IS COUNTER2 -------------> " + counter2);

    }

    else if (dpsd < weeklydates[0] && dped > weeklydates[weeklydates.length-1]){
        let counter2 = 1;
        for (var d = new Date(psd[0], psd[1]-1, psd[2]); d < weeklydates[0]; d.setDate(d.getDate() + parseInt(7))) {
            counter2 += 1;
        }
        let a = 0;
        let u = counter2;
        while (a < weeklydates.length){
            dataarr[a] = cap["week" + (u).toString()];
            u++;
            a++;
        }
    }

    var newDataset = {
        label: projects[i].name,
        type: 'bar',
        backgroundColor: projects[i].color,
        borderWidth: 1,
        data: dataarr,
        stack: 'Stack 0',
    }
    myChart.data.datasets.push(newDataset);
    myChart.update();

}

/*
for (i = 0; i < 40; i++){
    var r = Math.floor(Math.random() * 255);
    var g = Math.floor(Math.random() * 255);
    var b = Math.floor(Math.random() * 255);
    var newDataset = {
        label: "Project " + i,
        type: 'bar',
        backgroundColor: "rgb(" + r + "," + g + "," + b + ")",
        borderColor: "rgb(" + r + "," + g + "," + b + ")",
        borderWidth: 1,
        data: [1+i, 2+i, 3+i, 4+i, 5+i, 0, 0],
        stack: 'Stack 0',
    }

    // You add the newly created dataset to the list of `data`
    myChart.data.datasets.push(newDataset);
    myChart.update();
}
*/
}