<?php

/**
 * Basic configuration class for storing the SQL credentials.
 * @author Sergio Gutiérrez
 */
class SqlConfig {
	
	public static function getCredentials() {
		return array(
			'host' => 'localhost',
			'user' => 'workshop',
			'password' => '12345678',
			'table' => 'workshop'
		);
	}

}
