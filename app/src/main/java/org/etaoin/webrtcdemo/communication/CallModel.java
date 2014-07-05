package org.etaoin.webrtcdemo.communication;

import org.etaoin.webrtcdemo.communication.observer.CallModelObserver;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sergio Gutiérrez
 */
public class CallModel {

	private Set<CallModelObserver> observers;
	private CallState state;
	private String signalingLog;
	private String currentUser;
	private String from;
	private String to;

	public CallModel() {
		this.observers = new HashSet<CallModelObserver>();
		this.state = CallState.IDLE;
		this.signalingLog = "";
	}

	public void subscribe(CallModelObserver observer) {
		observers.add(observer);
	}

	public void unsubscribe(CallModelObserver observer) {
		observers.remove(observer);
	}

	public void addSignalingLog(String log) {
		signalingLog += log;

		for (CallModelObserver observer : observers) {
			observer.onSignalingLogUpdate(signalingLog);
		}
	}

	public String getSignalingLog() {
		return signalingLog;
	}

	public void setOutgoingCall(String to) {
		this.from = currentUser;
		this.to = to;
		this.state = CallState.RINGING_OUTGOING;

		for (CallModelObserver observer : observers) {
			observer.onStateChange(state);
		}
	}

	public void setIncomingCall(String from) {
		this.from = from;
		this.to = currentUser;
		this.state = CallState.RINGING_INCOMING;

		for (CallModelObserver observer : observers) {
			observer.onStateChange(state);
		}
	}

	public void setEstablishingCall() {
		this.state = CallState.ESTABLISHING;

		for (CallModelObserver observer : observers) {
			observer.onStateChange(state);
		}
	}

	public void setOngoingCall() {
		if (this.state.equals(CallState.RINGING_INCOMING)) {
			this.state = CallState.ONGOING_INCOMING;
		} else {
			this.state = CallState.ONGOING_OUTGOING;
		}

		for (CallModelObserver observer : observers) {
			observer.onStateChange(state);
		}
	}

	public void setFinishedCall() {
		this.state = CallState.IDLE;

		for (CallModelObserver observer : observers) {
			observer.onStateChange(state);
		}
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public String getMe() {
		String me;

		if (state.equals(CallState.RINGING_INCOMING) || state.equals(CallState.ONGOING_INCOMING)) {
			me = to;
		} else {
			me = from;
		}

		return me;
	}

	public String getCounterpart() {
		String counterpart;

		if (state.equals(CallState.RINGING_INCOMING) || state.equals(CallState.ONGOING_INCOMING)) {
			counterpart = from;
		} else {
			counterpart = to;
		}

		return counterpart;
	}
}
