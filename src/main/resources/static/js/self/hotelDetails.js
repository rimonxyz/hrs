/*<![CDATA[*/

var onRoomItemClick = function (element, id) {
    var alreadyExists = isAlreadyExists(id);
    toggleBackground(element, alreadyExists);

    if (!alreadyExists) {
        var roomId = $(element).find("#roomId").text();
        var roomNumber = $(element).find("#roomNumber").text();
        var roomPrice = $(element).find("#roomPrice").text();
        var markup = "<tr><td>" + roomId + "</td><td>" + roomNumber + "</td><td>" + roomPrice + "</td></tr>";
        $("#bookingTable tbody").append(markup);
    } else {
        removeRow(id);
    }
}

var removeRow = function (id) {
    $('#bookingTable tbody tr').each(function () {
        var eId = $(this).find("td:first-child").text();
        if (id == eId)
            $(this).remove();
    });
}
var toggleBackground = function (element, alreadyExists) {
//        console.log(element);
    if (alreadyExists)
        $(element).css("background-color", "transparent");
    else
        $(element).css("background-color", "green");
}
var isAlreadyExists = function (id) {
    exists = false;
    $('#bookingTable tbody tr td:first-child').each(function () {
        if (id == $(this).text()) {
            exists = true;
        }
    });
    return exists;
}

/*]]>*/
