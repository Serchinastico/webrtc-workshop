<?php

/**
 * Class responsible for operating with the requests stored in persistence.
 * @author Sergio Gutiérrez
 */
class RequestBackend {
	
	private $mysqli;
	
	public function __construct() {
		$credentials = SqlConfig::getCredentials();
		$this->mysqli = new mysqli($credentials['host'], $credentials['user'], $credentials['password'], $credentials['table']);
	}

	public function sendRequest($from, $to, $type, $request) {
		$query = 'INSERT INTO `requests` (`from_user`, `to_user`, `type`, `request`) ' .
				 'VALUES ("' . $from . '", "' . $to . '", "' . $type . '", "' . $request . '");';
		$success = $this->mysqli->query($query);
		return $success;
	}
	
	public function getMessages($name, $lastReadRequest) {
		$requests = array();
		$lastNewRequest = $lastReadRequest;
		$query = 'SELECT `id`, `from_user`, `type`, `request` FROM `requests`' . 
				 'WHERE `to_user` = "' . $name . '" AND `id` > ' . $lastReadRequest . ' ' . 
				 'ORDER BY 1;';
		$result = $this->mysqli->query($query);
		if ($result->num_rows > 0) {
			while ($row = $result->fetch_assoc()) {
				$requests[] = array(
					'from' => $row['from_user'],
					'type' => $row['type'], 
					'request' => $row['request']
				);
				$lastNewRequest = $row['id'];
			}
		}

		// Updating sessions from request backend... wtf?
		$query = 'UPDATE `sessions` SET `last_request` = ' . $lastNewRequest . ' ' .
				 'WHERE `name` = "' . $name . '";';
		$result = $this->mysqli->query($query);

		return $requests;
	}
}