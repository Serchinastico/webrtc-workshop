package org.etaoin.webrtcdemo.communication;

/**
 * @author Sergio Gutiérrez
 */
public enum CallState {
	IDLE,
	RINGING_OUTGOING,
	RINGING_INCOMING,
	ESTABLISHING,
	ONGOING_OUTGOING,
	ONGOING_INCOMING,
}
