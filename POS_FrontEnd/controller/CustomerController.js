let baseUrl="http://localhost:8082/posapplayerd/"


getAllCustomers();  //Formeka enakota customersla tika load welaa thiyenna


//NuttonEvents
//Add Customer
$("#btnCustomer").click(function () {
    let formData = $("#customerForm").serialize();

    $.ajax({
        url:baseUrl+"customer",
        method:"post",
        data:formData,
        dataType: "json",
        success:function (res) {
            //Invoked if the response status code is in 200 range
            console.log("Success Method Invoked");
            console.log(res);
            alert(res.message);
            getAllCustomers();
        },
        error:function (error) {
            //Invokes if status code range is 500 range or 400 range
            console.log("Error Method Invoked");
            console.log(JSON.parse(error.responseText));
            alert(JSON.parse(error.responseText).message);

        }
    });

});


//Delete customer

$("#btnCusDelete").click(function () {
    let id = $("#txtCustomerID").val();
    $.ajax({
        url:baseUrl+"customer?id="+id,
        method:"delete",
        success:function (resp) {
            getAllCustomers();
            alert(resp.message);
        },
        error:function (error) {
            let message= JSON.parse(error.responseText).message;
            alert(message);
        }
    });
});

//Update customer
$("#btnUpdate").click(function () {
    let cusId = $("#txtCustomerID").val();
    let cusName = $("#txtCustomerName").val();
    let cusAddress = $("#txtCustomerAddress").val();
    let cusSalary = $("#txtCustomerSalary").val();

    var customerOb={
        id:cusId,
        name:cusName,
        address:cusAddress,
        salary:cusSalary
    }

    $.ajax({
        url:baseUrl+"customer",
        method:"put",
        contentType:"application/json",  //JS Object ekak JSON ekak widiyata yawanawa nam meya daanna oona
        data: JSON.stringify(customerOb),   //JS Object ekak JSON ekak widiyata yawanawa nam aniwaaryayen "   JSON.stringify()  ", ekata daala ywanna oona..
        dataType: "json",
        success:function (resp) {
            getAllCustomers();
            alert(resp.message);
        },
        error:function (error) {
            let message= JSON.parse(error.responseText).message;
            alert(message);
        }
    });
});

//GetAll customer
$("#btnGetAll").click(function () {
    getAllCustomers();
});
//GetAll customer function
function getAllCustomers() {
    $("#tblCustomer").empty();
    $.ajax({
        url:baseUrl+"customer",
        dataType :"json",
        success:function (res) {
            // console.log(res)
            for (let c of res.data) {
                let cusID=c.id;
                let cusName=c.name;
                let cusAddress=c.address;
                let cusSalary=c.salary;

                let row= "<tr><td>"+cusID+"</td><td>"+cusName+"</td><td>"+cusAddress+"</td><td>"+cusSalary+"</td></tr>";
                $("#tblCustomer").append(row);
            }
            bindRowClickEvents();
            setTextFieldValues("","","","");
        },
        error:function (error) {
            let message= JSON.parse(error.responseText).message;
            alert(message);
        }
    });
}


//Bind events for the table rows function
function bindRowClickEvents() {
    $("#tblCustomer>tr").click(function () {
        let id = $(this).children(":eq(0)").text();
        let name = $(this).children(":eq(1)").text();
        let address = $(this).children(":eq(2)").text();
        let salary = $(this).children(":eq(3)").text();

        //Setting table details values to text fields

        $('#txtCustomerID').val(id);
        $('#txtCustomerName').val(name);
        $('#txtCustomerAddress').val(address);
        $('#txtCustomerSalary').val(salary);
    });
}

//Set text fields values function
function setTextFieldValues(id, name, address, salary) {
    $("#txtCustomerID").val(id);
    $("#txtCustomerName").val(name);
    $("#txtCustomerAddress").val(address);
    $("#txtCustomerSalary").val(salary);
}

