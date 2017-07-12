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

        <h2>Make sample request</h2>
        <form id="request-form">
              Request Input: <input id="request-input" type="number">
              <input type="submit" value="Make request">
        </form>
        <div>Response: <span id="request-result"></span></div>

        <h2>Make a batch of requests</h2>
        <form id="load-form">
              Number of requests: <input id="load-input" type="number">
              With random input between: <input id="load-input-min" type="number"> and <input id="load-input-max" type="number">
              <input type="submit" value="Create requests">
        </form>
        <div>
            Progress: <span id="load-result"></span>
        </div>

        <h2>Simulate constant traffic</h2>
        <form id="rps-form">
              (RPS) Requests per second: <input id="rps-input" type="number">
              With random input between: <input id="rps-input-min" type="number"> and <input id="rps-input-max" type="number">
              <input type="submit" value="Create steady traffic">
        </form>
        <div>
            Current traffic: <span id="rps-result">-</span> requests per second
            <button id="abort-load" type="button">Abort</button>
        </div>


        <h2>Dependency Configuration</h2>
        <form id="configuration-form">
              Introduce failures: <input id="configuration-failures-input" type="checkbox">
              Response latency in milliseconds: <input id="configuration-latency-input" type="number">
              <input type="submit" value="Update configuration">
        </form>

        <script>
            var schedulerID;

            $("#request-form").submit(function( event ) {
                    var input = $("#request-input").val();
                    showLoader();
                    $.ajax({
                        url: "http://localhost:8080/ServiceCall4j-Demo/" + input,
                        dataType:'json',
                    })
                    .done(function( data ) {
                        hideLoader();
                        $("#request-result").html(data.result);
                    });
                    event.preventDefault();
            });

            $("#load-form").submit(function( event ) {
                var loadRequests = $("#load-input").val();
                var rangeMin = parseInt($("#load-input-min").val());
                var rangeMax = parseInt($("#load-input-max").val());
                var completedRequests = 0;
                for(var i = 0; i < loadRequests; i++) {
                    var randomInput = getRandomNumberInRange(rangeMin, rangeMax);
                    $.ajax({
                        url: "http://localhost:8080/ServiceCall4j-Demo/" + randomInput,
                        dataType:'json',
                    })
                    .done(function( data ) {
                        completedRequests++;
                        $("#load-result").html(completedRequests + " of " + loadRequests + " requests completed");
                    });
                }
                event.preventDefault();
            });

            $("#rps-form").submit(function( event ) {
                var rpsRate = parseInt($("#rps-input").val());
                var rangeMin = parseInt($("#rps-input-min").val());
                var rangeMax = parseInt($("#rps-input-max").val());
                $("#rps-result").html(rpsRate);
                schedulerID = setInterval(function() {
                    executeSteadyRequestsRate(rpsRate, rangeMin, rangeMax);
                }, 1000);
                event.preventDefault();
            });

            $("#abort-load").click(function( event ) {
                clearInterval(schedulerID);
                $("#rps-result").html("-");
            })

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

            function executeSteadyRequestsRate(requestsPerSecond, rangeMin, rangeMax) {
                for(var i = 0; i < requestsPerSecond; i++) {
                    var randomInput = getRandomNumberInRange(rangeMin, rangeMax);
                    $.ajax({
                        url: "http://localhost:8080/ServiceCall4j-Demo/" + randomInput,
                        dataType:'json',
                    })
                }
            }

            function getRandomNumberInRange(rangeBottom, rangeTop) {
                return Math.floor( Math.random() * (rangeTop - rangeBottom + 1) ) + rangeBottom;
            }
        </script>

        <div id="loader">
            <div></div>
        </div>
    </body>
</html>