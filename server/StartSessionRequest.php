<?php

/**
 * Request class to start a session in the chat room.
 * @author Sergio Gutiérrez
 */
class StartSessionRequest {

	private $name;

	public function __construct($name) {
		$this->name = $name;
	}

	public function execute() {
		$this->assertValidParams();

		$sessionBackend = new SessionBackend();
		$sessionBackend->startSession($this->name);
	}

	private function assertValidParams() {
		if (empty($this->name)) {
			throw new InvalidArgumentException("name is a mandatory parameter");
		} elseif (strlen($this->name) < 3) {
			throw new InvalidArgumentException("name is too short");
		} elseif (strlen($this->name) > 64) {
			throw new InvalidArgumentException("name is too long");
		}
	}

}
