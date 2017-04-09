/**
 * Created by sayemkcn on 4/9/17.
 */
$(document).ready(function () {
    var roomsTable = $('#roomsTable').DataTable({
        "sAjaxSource": "http://localhost:8080/hotel/rooms",
        "sAjaxDataProp": "",
        "order": [[ 0, "asc" ]],
        "aoColumns": [
            { "mData": "id"},
            { "mData": "roomNumber" },
            { "mData": "floorNumber" },
            { "mData": "discount" }
        ]
    })
});