function headcount() {
        //var str = [[${range}]]

        // Date: y/m/d
        //let str = "2021-07-10";/

    // START DATE
    let st = str.start.split("-");
        
    // END DATE
    let y = str.end.split("-");
    
    // Date: y/m/d
    
    var now = new Date(y[0],y[1]-1,y[2]);
    var foralert = new Date(st[0], st[1]-1, st[2]);

    if (foralert > now){
        alert("Start date is later than end date!");
    }
    //console.log("Y -------------> " + y);
    //console.log("NOW -------------> " + now);


    let dateobjects = [];
    // emp start date
    let dateobjects2 = [];
    //pemp start date
    let dateobjects3 = [];
    // emp end date
    let dateobjects4 = [];
    // pemp end date
    let dateobjects5 = [];

    //console.l
    
    var daysOfYear = [];
    for (var d = new Date(st[0], st[1]-1, st[2]); d <= now; d.setDate(d.getDate() + parseInt(7))) {
        
        //console.log("D " + d);
        let x = d.getDate();
        let y = d.getMonth(); // 0
        let z = d.getFullYear();        
        let c = new Date(z,y,x);
        dateobjects.push(c);
        let c1 = c.getDate();
        let c2 = c.getMonth();
        let c3 = c.getFullYear();
        daysOfYear.push(c3 + "-" + parseInt(c2+1).toString() + "-" + c1);
        //console.log("C " + c);
        //console.log("DAYS OF YEAR " + c3 + "-" + parseInt(c2+1).toString() + "-" + c1);
    }




    let empsd = [];
    //var theList = [[${employees}]]
    let hemp = [];
    let pemp = [];
    let empst = [];
    let emped = [];
    let pempsd = [];
    let pemped = [];
    let pempst = [];
    //var theList2 = [[${employees2}]]


    for (i = 0; i < theList.length; i++) {
        empsd.push(theList[i].start);
        empst.push(theList[i].status);
        emped.push(theList[i].end);
        //console.log(test[i]);
    }
    /*
    let st = str.start.split("-");
    */

    for (i = 0; i < theList.length; i++){
        //console.log("EMPSD " + empsd[i]);
        let o = empsd[i].split("-");
        let p = new Date(o[0],o[1]-1,o[2]);
        dateobjects2.push(p);
        //console.log("DATEOBJECT2" + p);
    }

    for (i = 0; i < theList.length; i++){
        let o = emped[i].split("-");
        let p = new Date(o[0],o[1]-1,o[2]);
        dateobjects4.push(p);
    }


    for (i = 0; i < theList2.length; i++){
        pempsd.push(theList2[i].start);
        pempst.push(theList2[i].status);
        pemped.push(theList2[i].end);
    }

    for (i = 0; i < theList2.length; i++){
        //console.log("EMPSD " + empsd[i]);
        let o = pempsd[i].split("-");
        let p = new Date(o[0],o[1]-1,o[2]);
        dateobjects3.push(p);
        //console.log("DATEOBJECT2" + p);
    }

    for (i = 0; i < theList2.length; i++){
        //console.log("EMPSD " + empsd[i]);
        let o = pemped[i].split("-");
        let p = new Date(o[0],o[1]-1,o[2]);
        dateobjects5.push(p);
        //console.log("DATEOBJECT2" + p);
    }
    
    //creating hired employee list to match daysofyear
    for (i = 0; i < daysOfYear.length; i++){
        hemp.push(0);
        //console.log(hemp[i]);
    }
    
    //creating projected employee list
    for (i = 0; i < daysOfYear.length; i++){
        pemp.push(0);
        //console.log(hemp[i]);
    }

    for (i = 0; i < dateobjects2.length; i++){
        if (dateobjects2[i] < dateobjects[0] && dateobjects4[i] > dateobjects[0]){

            if (empst[i] == true){
                hemp[0] += 1;
                pemp[0] += 1;
            }

            else{
                pemp[0] += 1;
            }
            
            console.log("DATEOBJECT1 " + dateobjects[0]);
            console.log("DATEOBJECT2 " + dateobjects2[i]);
        }
    }

    console.log("PRINTING HEMP 0");
    for (i = 0; i < hemp.length; i++){
        console.log(hemp[i]);
    }
    
    console.log("PRINTING PEMP 0");
    for (i = 0; i < pemp.length; i++){
        console.log(pemp[i]);
    }

    // first for loop
    for (i = 0; i < empsd.length; i++){

        console.log(" EMPSD ---------------------->>>>>>>>>>>>>>>>>>>> " + empsd[i]);

        for(j = 0; j < dateobjects.length; j++){

            if (j == 0){
                
                if ((dateobjects2[i].getTime() == dateobjects[0].getTime()) && (empst[i] == true)){
                    hemp[0] += 1;
                    console.log("ADDING TO HEMP 1"); 
                    break;
                    
                    
                }

                else if ((dateobjects2[i] > dateobjects[0] && dateobjects2[i] < dateobjects[1]) && (empst[i] == true)){
                    hemp[1] += 1;
                    console.log("ADDING TO HEMP 2"); 
                    break;
                    
                }
            }

            else if ((dateobjects2[i].getTime() == dateobjects[j].getTime()) && (empst[i] == true)){
                hemp[j] += 1;
                
                console.log("ADDING TO HEMP 3"); 
                break;
                
            }

            else if ((dateobjects2[i] > dateobjects[j-1] && dateobjects2[i] < dateobjects[j]) && (empst[i] == true)){
                hemp[j-1] += 1;          
                console.log("ADDING TO HEMP 4 " + j);  
                break;
                   
            }
        }
    }

    // second for loop
    for (i = 0; i < pempsd.length; i++){

        for(j = 0; j < dateobjects.length; j++){

            if (j == 0){

                if (dateobjects3[i].getTime() == dateobjects[0].getTime()){
                    pemp[0] += 1;
                    console.log("ADDING TO PEMP 1");
                    break;
                }

                else if (dateobjects3[i] > dateobjects[0] && dateobjects3[i] < dateobjects[1]){
                    pemp[1] += 1;
                    console.log("ADDING TO PEMP 2 " + 1);
                    break;
                
                   
                }
            }

            else if (dateobjects3[i].getTime() == dateobjects[j].getTime()){
                pemp[j] += 1;
                console.log("ADDING TO PEMP 3");
                break;
                
                
            }

            

            else if ((dateobjects3[i] > dateobjects[j-1] && dateobjects3[i] < dateobjects[j])){
                pemp[j-1] += 1;   
                console.log("ADDING TO PEMP 4 " + j);
                break;
               
                           
            }
        }
    }
    
    // third for loop
    for (i = 0; i < emped.length; i++){

        for(j = 0; j < dateobjects.length; j++){

            if (j == 0){
        
                if ((dateobjects4[i].getTime() == dateobjects[0].getTime()) && (empst[i] == true)){
                    hemp[0] -= 1;
                    console.log("SUBTRACTING FROM HEMP 1");
                    break;
                    
                }

                else if ((dateobjects4[i] > dateobjects[0] && dateobjects4[i] < dateobjects[1]) && (empst[i] == true)){
                    hemp[1] -= 1;
                    console.log("SUBTRACTING FROM HEMP 2");
                    break;
                    
                }
            }

            else if ((dateobjects4[i].getTime() == dateobjects[j].getTime()) && (empst[i] == true)){
                hemp[j] -= 1;
                console.log("HIi"); 
                console.log("SUBTRACTING FROM HEMP 3");
                break;
               
            }

    
            else if ((dateobjects4[i] > dateobjects[j-1] && dateobjects4[i] < dateobjects[j]) && (empst[i] == true)){
                hemp[j] -= 1;          
                console.log("HI"); 
                console.log("SUBTRACTING FROM HEMP 4");
                break;
                   
            }
        }
    }

    // fourth for loop
    for (i = 0; i < pemped.length; i++){

        for(j = 0; j < dateobjects.length; j++){

            if (j == 0){

                if (dateobjects5[i].getTime() == dateobjects[0].getTime()){
                    pemp[0] -= 1;
                    console.log("SUBTRACTING FROM PEMP 1");
                    break;
                    
                }

                else if (dateobjects5[i] > dateobjects[0] && dateobjects5[i] < dateobjects[1]){
                    pemp[1] -= 1;
                    console.log("SUBTRACTING FROM PEMP 2");
                    break;
                    
                    
                }
            }

            else if (dateobjects5[i].getTime() == dateobjects[j].getTime()){
                pemp[j] -= 1;
                console.log("SUBTRACTING FROM PEMP 3");
                break;
               
                
            }

    

            else if ((dateobjects5[i] > dateobjects[j-1] && dateobjects5[i] < dateobjects[j])){

                console.log("DATEOBJECTS5 ----------->>>>> " + dateobjects5[i]);
                pemp[j] -= 1;   
                
                console.log("SUBTRACTING FROM PEMP 4 " + j); 
                break;
                         
            }
        }
    }

    console.log("PRINTING HEMP 1");
    for (i = 0; i < hemp.length; i++){
        console.log(hemp[i]);
    }
    
    console.log("PRINTING PEMP 1");
    for (i = 0; i < pemp.length; i++){
        console.log(pemp[i]);
    }
    
    for (i = 0; i < hemp.length; i++){
        if (i == 0){
            hemp[0] = hemp[0];
        }
        else{
            hemp[i] = hemp[i-1] + hemp[i];
        }
        
    }
    for (i = 0; i < pemp.length; i++){
        if (i == 0){
            pemp[0] = pemp[0];
        }
        else{
            pemp[i] = pemp[i-1] + pemp[i];
        }
        
    }

    for (i = 0; i <empst.length; i++){
        console.log("EMPLOYEE STATUS -------------------> " + empst[i]);
    }
    
    console.log("PRINTING HEMP 2");
    for (i = 0; i < hemp.length; i++){
        console.log(hemp[i]);
    }
    
    console.log("PRINTING PEMP 2");
    for (i = 0; i < pemp.length; i++){
        console.log(pemp[i]);
    }

    /*
    console.log("TEST[0] " + test[0]);
    console.log("DAYSOFYEAR[0] " + daysOfYear[0]);
    if (test[0] == daysOfYear[0]){
        console.log("YAYYY");
        
    }
    console.log(typeof test[0]);
    console.log(typeof daysOfYear[0]);
    console.log("BELOW IS START DATE");
    console.log(theList[0].start);
    console.log(theList[0].role);
    */

    

    let headcount = document.getElementById('headcount').getContext('2d');
    let myChart = new Chart(headcount, {
        //type of line we want to create
        type: 'line',
         //the data of our dataset
        data: {
            labels: daysOfYear, //[july 3, jan 10, july 17, jan 22],
            datasets: [{
                label: 'Headcount: Planning',
                
                data: pemp, //[3,3,3,4]
                backgroundColor:'orange',
                borderColor: 'orange',
                fill: false,
                pointRadius: 5
            },
            {
                label: 'Headcount: Confirmed',
                data: hemp,
                backgroundColor:'blue',
                borderColor: 'blue',
                fill: false,
                pointRadius: 5
            }]
        },
        options: {
            scales:{
                yAxes:{
                    beginAtZero: true,
                    suggestedMin: 0,
                    title: {
                        display: true,
                        text: "Headcount",
                        font: {
                            size: 25
                        },
                    }
                },
                xAxes:{
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
                    text: 'Headcount View',
                    font: {
                            size: 50
                    }
                }
            }
        }
    });
}