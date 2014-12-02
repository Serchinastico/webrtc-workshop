<?php

/**
 * Factory to create requests depending on the provided method.
 * @author Sergio Gutiérrez
 */
class RequestFactory {

	public function createRequest($method, $getParams, $postParams) {
		$request = NULL;
		$method = strtolower($method);

		switch ($method) {
			case "startsession":
				$request = new StartSessionRequest(
					isset($getParams['name']) ? $getParams['name'] : NULL);
				break;
			case "getparticipants":
				$request = new GetParticipantsRequest(
					isset($getParams['name']) ? $getParams['name'] : NULL);
				break;
			case "getmessages":
				$request = new GetMessagesRequest(
					isset($getParams['name']) ? $getParams['name'] : NULL);
				break;
			case "sendoffer":
				$request = new SendMessageRequest(
					isset($postParams['name']) ? $postParams['name'] : NULL,
					isset($postParams['to']) ? $postParams['to'] : NULL,
					'offer',
					isset($postParams['offer']) ? $postParams['offer'] : NULL);
				break;
			case "sendanswer":
				$request = new SendMessageRequest(
					isset($postParams['name']) ? $postParams['name'] : NULL,
					isset($postParams['to']) ? $postParams['to'] : NULL,
					'answer',
					isset($postParams['answer']) ? $postParams['answer'] : NULL);
				break;
			case "sendicecandidate":
				$request = new SendMessageRequest(
					isset($postParams['name']) ? $postParams['name'] : NULL,
					isset($postParams['to']) ? $postParams['to'] : NULL,
					'candidate',
					isset($postParams['icecandidate']) ? $postParams['icecandidate'] : NULL);
				break;
			case "sendterminate":
				$request = new SendMessageRequest(
					isset($postParams['name']) ? $postParams['name'] : NULL,
					isset($postParams['to']) ? $postParams['to'] : NULL,
					'terminate');
				break;
		}

		return $request;
	}

}
