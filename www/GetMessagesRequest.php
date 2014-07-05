<?php

/**
 * Request class to retrieve the pending messages for a given user.
 * @author Sergio Gutiérrez
 */
class GetMessagesRequest {

	private $name;

	public function __construct($name) {
		$this->name = $name;
	}

	public function execute() {
		$this->assertValidParams();

		$sessionBackend = new SessionBackend();
		$lastReadRequest = $sessionBackend->getLastReadRequest($this->name);
		$requestBackend = new RequestBackend();
		return $requestBackend->getMessages($this->name, $lastReadRequest);
	}

	private function assertValidParams() {
		if (empty($this->name)) {
			throw new InvalidArgumentException("name is a mandatory parameter");
		}
	}

}
