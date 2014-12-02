<?php

	require_once('SqlConfig.php');
	require_once('SessionBackend.php');
	require_once('RequestBackend.php');
	require_once('GetMessagesRequest.php');
	require_once('GetParticipantsRequest.php');
	require_once('SendMessageRequest.php');
	require_once('StartSessionRequest.php');
	require_once('RequestFactory.php');

	// Allow fiddle.net origin
	// TODO take domain for config
	header("Access-Control-Allow-Origin: http://fiddle.jshell.net");

	$method = isset($_GET['method']) ? $_GET['method'] : NULL;
	$requestFactory = new RequestFactory();


	$request = $requestFactory->createRequest($method, $_GET, $_POST);
	if (!empty($request)) {
		try {
			$response = $request->execute();
			if (!empty($response)) {
				echo json_encode($response);
			}
		} catch (InvalidArgumentException $e) {
			header('HTTP/1.1 400 Bad Request', TRUE, 400);
		}
	} else{
		header('HTTP/1.1 400 Bad Request', TRUE, 400);
	}
?>
