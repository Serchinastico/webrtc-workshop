<?php

/**
 * Class responsible for operating with the sessions stored in persistence.
 * @author Sergio Gutiérrez
 */
class SessionBackend {
	
	private $mysqli;
	
	public function __construct() {
		$credentials = SqlConfig::getCredentials();
		$this->mysqli = new mysqli($credentials['host'], $credentials['user'], $credentials['password'], $credentials['table']);
	}
	
	public function startSession($name) {
		$query = 'INSERT INTO `sessions` (`time`, `name`, `last_request`) ' .
				 'VALUES (' . time() . ', "' . $name . '", 0);';
		$success = $this->mysqli->query($query);
		return $success;
	}

	public function endSession($name) {
		$query = 'DELETE FROM `sessions` WHERE `name` = "' . $name . '";';
		$this->mysqli->query($query);
	}

	public function getParticipants($name) {
		$participants = array();

		$query = 'SELECT `name` FROM `sessions` WHERE `name` != "' . $name . '";';
		$result = $this->mysqli->query($query);
		if ($result->num_rows > 0) {
			while ($row = $result->fetch_assoc()) {
				$participants[] = $row['name'];
			}
		}

		return $participants;
	}

	public function getLastReadRequest($name) {
		$lastReadRequest = -1;
		$query = 'SELECT `last_request` FROM `sessions` WHERE `name` = "' . $name . '";';
		$result = $this->mysqli->query($query);
		if ($result->num_rows == 1) {
			$row = $result->fetch_assoc();
			$lastReadRequest = $row['last_request'];
		}
		return $lastReadRequest;
	}
}