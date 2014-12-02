<?php

/**
 * Request class to get the other participants name in the chat room.
 * @author Sergio Gutiérrez
 */
class GetParticipantsRequest {

	private $name;

	public function __construct($name) {
		$this->name = $name;
	}

	public function execute() {
		$this->assertValidParams();

		$sessionBackend = new SessionBackend();
		return $sessionBackend->getParticipants($this->name);
	}

	private function assertValidParams() {
		if (empty($this->name)) {
			throw new InvalidArgumentException("name is a mandatory parameter");
		}
	}

}
