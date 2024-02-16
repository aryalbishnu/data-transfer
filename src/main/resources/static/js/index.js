/**
 * 子画面参照処理
 */
$(document).ready(function() {
    $("#txtName").blur(function() {
        let popup;
        let query = $("#txtName").val();
        if (query == "") {            
        } else {
            //sending request to server
            let url = `http://localhost:8080/search/${query}`;
            fetch(url, {method: "POST"}).then((Response) => {
                return Response.json();
            })
                .then((data) => {
                    //data....
                    if (data == null || data.length === 0) {
                        alert('名前が存在しない。')
                        $("#txtMessage").html("");
                    } else if(data.length === 1) {
                        $("#txtMessage").html(`${data[0].email}`);
                    } else {      
                        let arrayListString = encodeURIComponent(JSON.stringify(data));
                        let url = `/nameSearch?arrayList=${arrayListString}`;
                        popup = window.open(url, "blank", "width=600,height=600");
                       // window.opener.document.getElementById("formName").val("aaaaa");
                        popup.focus();
                        return false;
                    }
                })
        }
    });
});

/**
 * 子画面から返却処理
 */
function nameSubmit() {
    var selectedGender = $("input[name='selectedUser']:checked").val();
    if (window.opener != null && !window.opener.closed) {
        var txtName = window.opener.document.getElementById("txtMessage");
        txtName.innerHTML = selectedGender;
    }
    window.close();
}


