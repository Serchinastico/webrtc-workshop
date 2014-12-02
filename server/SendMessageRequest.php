<?php

/**
 * Request class to send a (signaling) message to another user.
 * @author Sergio Gutiérrez
 */
class SendMessageRequest {

	private $name;
	private $to;
	private $type;
	private $message;

	public function __construct($name, $to, $type, $message = '') {
		$this->name = $name;
		$this->to = $to;
		$this->type = $type;
		$this->message = $message;
	}

	public function execute() {
		$this->assertValidParams();

		$requestBackend = new RequestBackend();
		$requestBackend->sendRequest($this->name, $this->to, $this->type, $this->message);
	}

	private function assertValidParams() {
		if (empty($this->name)) {
			throw new InvalidArgumentException("name is a mandatory parameter");
		} elseif (empty($this->to)) {
			throw new InvalidArgumentException("to is a mandatory parameter");
		} elseif ($this->type != 'terminate' && empty($this->message)) {
			throw new InvalidArgumentException("offer/candidate/answer/terminate is a mandatory parameter");
		}
	}

}
