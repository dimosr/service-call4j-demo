<html>
    <head>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    </head>
    <body>
        <h1>ServiceCall-4j Demo Interface</h1>

        <h2>Sample requests</h2>
        <form id="input-form">
              Input: <input id="input" type="number">
              <input type="submit" value="Make request">
        </form>
        <div>Result: <span id="result"></span></div>

        <script>
            $("#input-form").submit(function( event ) {
                    var input = $("#input").val();
                    $.ajax({
                        url: "http://localhost:8080/ServiceCall4j-Demo/" + input,
                        dataType:'json',
                    })
                    .done(function( data ) {
                        $("#result").empty();
                        if(data.hasOwnProperty("error")) {
                            $("#result").append("Error!!");
                        } else {
                            $("#result").append(data.result);
                        }
                    });
                    event.preventDefault();
            });
        </script>
    </body>
</html>