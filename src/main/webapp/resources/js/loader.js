function showLoader() {
    $("#loader").show()
}

function hideLoader() {
    $("#loader").hide()
}

/* Used to execute asynchronous processing, showing loader during the execution */
function executeAsyncProcessWithLoader(callbackFn) {
    showLoader();
    callbackFn();
    hideLoader();
}

/* Used to execute synchronous processing, showing loader during the execution */
function executeSyncProcessWithLoader(callbackFn) {
    showLoader();
    setTimeout(function() {
		callbackFn();
    	hideLoader();
	}, 50);
}