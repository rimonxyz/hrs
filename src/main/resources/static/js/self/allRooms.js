var roomIds = [];

$("#dateFilter").on("change", function (form) {
    $("#dateFilterForm").submit();
});
$("#categoryFilter").on("change", function (form) {
    $("#catFilterForm").submit();
});

// room box click actions
function onRoomPanelClick(id, toggle) {
    console.log(id);
    loadRoomDetails(id);
    if (toggle)
        toggleVisibility();
}

var toggleVisibility = function () {
    var isVisible = $("#containerPanel").is(":visible");
    if (isVisible) {
        $("#roomListDivLarge").show(500);
        $("#containerPanel").hide(500);
    } else {
        $("#roomListDivLarge").hide(500);
        $("#containerPanel").show(500);
    }
}

var loadRoomDetails = function (id) {
    $.get("/rest/rooms/" + id, function (room, status) {
        console.log(room);
        $("#dtlsRoomId").text(id);
        $("#dtlsCategory").text(room.category.name);
        $("#dtlsDiscountPercentage").text(room.discountPercentage);
        $("#dtlsPrice").text("৳" + room.price);
        $("#dtlsFloorNumber").text("Floor: " + room.floorNumber + getPostFix(room.floorNumber));
        $("#dtlsRoomNumber").text("Room Number: " + room.roomNumber);
        // Fascilities
        if (room.category.facilities.frontFace) $("#dtlsIsFrontFace").attr('class', 'glyphicon glyphicon-ok');
        else $("#dtlsIsFrontFace").attr('class', 'glyphicon glyphicon-remove');

        if (room.category.facilities.withCorridor) $("#dtlsIsWithCorridor").attr('class', 'glyphicon glyphicon-ok');
        else $("#dtlsIsWithCorridor").attr('class', 'glyphicon glyphicon-remove');

        if (room.category.facilities.breakfast) $("#dtlsIsBreakfast").attr('class', 'glyphicon glyphicon-ok');
        else $("#dtlsIsBreakfast").attr('class', 'glyphicon glyphicon-remove');

        if (room.category.facilities.internet) $("#dtlsIsWifi").attr('class', 'glyphicon glyphicon-ok');
        else $("#dtlsIsWifi").attr('class', 'glyphicon glyphicon-remove');

        if (room.category.facilities.airConditioned) $("#dtlsIsAirConditioned").attr('class', 'glyphicon glyphicon-ok');
        else $("#dtlsIsAirConditioned").attr('class', 'glyphicon glyphicon-remove');

        if (room.category.facilities.tv) $("#dtlsIsTv").attr('class', 'glyphicon glyphicon-ok');
        else $("#dtlsIsTv").attr('class', 'glyphicon glyphicon-remove');

        if (room.category.facilities.geyser) $("#dtlsIsGeyser").attr('class', 'glyphicon glyphicon-ok');
        else $("#dtlsIsGeyser").attr('class', 'glyphicon glyphicon-remove');

        $("#dtlsImg1").attr('src', '/rooms/' + id + "/image/" + 0);
        $("#dtlsImg2").attr('src', '/rooms/' + id + "/image/" + 1);
        $("#dtlsImg3").attr('src', '/rooms/' + id + "/image/" + 2);
    })
}

var getPostFix = function (number) {
    if (number === 1)
        return "st";
    else if (number === 2)
        return "nd";
    else if (number === 3)
        return "rd";
    else return "th";
};

// BOOKING
$("#bookNowButton").on("click", function () {
    var id = $("#dtlsRoomId").text();
    if (!alreadyExists(localStorage.getItem("roomIds"),id)){
        roomIds.push(id);
        alert("Room "+id+" is added to the cart!");
    }else
        alert("Room "+id+" is already added to the cart!");
    localStorage.setItem("roomIds",roomIds);

});

var alreadyExists = function (ids,id) {
    for (var i=0;i<ids.length;i++){
        if (ids[i] === id)
           return true;
    }
    return false;
}

// checkout button click action
$("#checkoutButton").on('click',function () {
    $("#modalRoomId").text($("#dtlsRoomId").text());
});

// click booking button
$("#modalAddBookingButton").on("click", function () {
    var booking = {};
    booking.ids = localStorage.getItem("roomIds");
    if (booking.ids.length<1) {
        alert("You haven't added any room yet!");
        return;
    }
    booking.startDate = $("#modalBookingDate").val();
    booking.endDate = $("#modalCheckoutDate").val();
    if (!Date.parse(booking.startDate) || !Date.parse(booking.endDate)){
        alert("You must enter booking date.")
        return;
    }
    sendToServer(booking);
    // bookingList.push(booking);
    // localStorage.setItem("bookingList", bookingList);
    // $('#bookingModal').modal('toggle');
    // $('#checkoutButton').attr('hidden',false);
    // console.log(localStorage.getItem("bookingList"));
});

var sendToServer = function (data) {
    console.log(JSON.stringify(data));
    $.ajax({
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(data),
        url: "/bookings/create",
        statusCode: {
            200: function (xhr) {
                console.log(xhr.responseText);
                window.location = "/hotels";
            },
            403: function (xhr) {
                window.location = "/login";
            },
            400: function (xhr) {
                window.location = "/login";
            },
            226: function (xhr) {
                $("#messageArea").text("One or more Rooms are already booked.");
            }
        }
        // success: function (msg) {
        //     console.log(msg);
        //     window.location = "/hotels";
        // },
        // error : function(e) {
        //     console.log('Error: ' + e);
        // }

    });
}