// let baseUrl="http://localhost:8082/posapplayerd/"



getAllItems();  //Formeka enakota customersla tika load welaa thiyenna


//NuttonEvents
//Add Item
$("#btnItem").click(function () {
    let formData = $("#itemForm").serialize();

    $.ajax({
        url:"http://localhost:8082/posapplayerd/item",
        method:"post",
        data:formData,
        dataType: "json",
        success:function (res) {

            alert(res.message);
            getAllItems();
        },
        error:function (error) {
            //Invokes if status code range is 500 range or 400 range
            console.log("Error Method Invoked");
            console.log(JSON.parse(error.responseText));
            alert(JSON.parse(error.responseText).message);

        }
    });

});


//Delete Item

$("#btnItemDelete").click(function () {
    let code = $("#itemCode").val();
    $.ajax({
        url:"http://localhost:8082/posapplayerd/item?code="+code,
        method:"delete",
        success:function (resp) {
            getAllItems();
            alert(resp.message);
        },
        error:function (error) {
            let message= JSON.parse(error.responseText).message;
            alert(message);
        }
    });
});

//Update Item
$("#btnItemUpdate").click(function () {
    let code = $("#itemCode").val();
    let name = $("#itemName").val();
    let qty = $("#itemQty").val();
    let price = $("#itemPrice").val();

    var itemOb={
        code:code,
        description:name,
        qtyOnHand:qty,
        unitPrice:price
    }

    $.ajax({
        url:"http://localhost:8082/posapplayerd/item",
        method:"put",
        contentType:"application/json",  //JS Object ekak JSON ekak widiyata yawanawa nam meya daanna oona
        data: JSON.stringify(itemOb),   //JS Object ekak JSON ekak widiyata yawanawa nam aniwaaryayen "   JSON.stringify()  ", ekata daala ywanna oona..
        dataType: "json",
        success:function (resp) {
            getAllItems();
            alert(resp.message);
        },
        error:function (error) {
            let message= JSON.parse(error.responseText).message;
            alert(message);
        }
    });
});

//GetAll Item
$("#btnItemGetAll").click(function () {
    getAllItems();
});
//GetAll customer function
function getAllItems() {
    $("#tblItem").empty();
    $.ajax({
        url:"http://localhost:8082/posapplayerd/item",
        success:function (res) {
            // console.log(res)
            for (let c of res.data) {
                let code=c.code;
                let description=c.description;
                let qtyOnHand=c.qtyOnHand;
                let unitPrice=c.unitPrice;

                let row= "<tr><td>"+code+"</td><td>"+description+"</td><td>"+qtyOnHand+"</td><td>"+unitPrice+"</td></tr>";
                $("#tblItem").append(row);
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
    $("#tblItem>tr").click(function () {
        let code = $(this).children(":eq(0)").text();
        let name = $(this).children(":eq(1)").text();
        let qty = $(this).children(":eq(2)").text();
        let price = $(this).children(":eq(3)").text();

        //Setting table details values to text fields

        $('#itemCode').val(code);
        $('#itemName').val(name);
        $('#itemQty').val(qty);
        $('#itemPrice').val(price);
    });
}

//Set text fields values function
function setTextFieldValues(code, name, qty, price) {
    $('#itemCode').val(code);
    $('#itemName').val(name);
    $('#itemQty').val(qty);
    $('#itemPrice').val(price);
}
