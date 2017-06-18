<html>
    <%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

    <head>
        <c:url value="/resources/css/loader.css" var="loaderCSS" />
        <c:url value="/resources/js/loader.js" var="loaderJS" />
        <c:url value="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js" var="jqueryJS" />
        <link href="${loaderCSS}" rel="stylesheet" />
        <script src="${loaderJS}"></script>
        <script src="${jqueryJS}"></script>
    </head>
    <body>
        <h1>ServiceCall-4j Demo Interface</h1>

        <h2>Sample requests</h2>
        <form id="request-form">
              Input: <input id="request-input" type="number">
              <input type="submit" value="Make request">
        </form>
        <div>Result: <span id="request-result"></span></div>

        <h2>Dependency Configuration</h2>
        <form id="configuration-form">
              Introduce failures: <input id="configuration-failures-input" type="checkbox">
              Response latency in milliseconds: <input id="configuration-latency-input" type="number">
              <input type="submit" value="Update configuration">
        </form>

        <script>
            $("#request-form").submit(function( event ) {
                    var input = $("#request-input").val();
                    showLoader();
                    $.ajax({
                        url: "http://localhost:8080/ServiceCall4j-Demo/" + input,
                        dataType:'json',
                    })
                    .done(function( data ) {
                        hideLoader();
                        $("#request-result").empty();
                        $("#request-result").append(data.result);
                    });
                    event.preventDefault();
            });

            $("#configuration-form").submit(function( event ) {
                    var data = {};
                    data["introduceFailures"] = $("#configuration-failures-input").prop("checked") ? true : false;
                    data["latencyInMillis"] = $("#configuration-latency-input").val();
                    $.ajax({
                        type: "POST",
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        url: "http://localhost:8080/ServiceCall4j-Demo/configure",
                        data: JSON.stringify(data),
                    })
                    event.preventDefault();
            });
        </script>

        <div id="loader">
            <div></div>
        </div>
    </body>
</html>