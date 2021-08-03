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

var daysOfYear = [];
for (var d = new Date(st[0], st[1]-1, st[2]); d <= now; d.setDate(d.getDate() + parseInt(7))) {

    //console.log("D " + d);
    let x = d.getDate();
    let y = d.getMonth(); // 0
    let z = d.getFullYear();
    let c = new Date(z,y,x);

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
        },

        {
            label: 'aaaaa',
            stack: 'Stack 0',
            data: [1, 2, 3, 4, 5, 6, 7], //[3,3,3,4]
            backgroundColor:'green',
            borderColor: 'green',
            fill: false,
            pointRadius: 5,
        },
        {
            label: 'bbbbbbbb',
            stack: 'Stack 0',
            data: [1, 2, 3, 4, 5, 6, 7],
            backgroundColor:'blue',
            borderColor: 'blue',
            fill: false,
            pointRadius: 5,
        }]
    },
    options: {
        scales:{

            yAxes:
                {
                beginAtZero: true,
                suggestedMin: 0,
                //stacked: true,
                title: {
                    display: true,
                    text: "# of people?",
                    font: {
                        size: 25
                    },
                }
            },

            xAxes:{
                stacked: true,
                title: {
                    display: true,
                    text: "Projects",
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

for (i = 0; i < 3; i++){
    var r = Math.floor(Math.random() * 255);
    var g = Math.floor(Math.random() * 255);
    var b = Math.floor(Math.random() * 255);
    var newDataset = {
        label: "HI" + i,
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
}