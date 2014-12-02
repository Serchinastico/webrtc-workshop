package org.etaoin.webrtcdemo.signaling;

import org.etaoin.webrtcdemo.signaling.callback.GetMessagesResponseCallback;
import org.etaoin.webrtcdemo.signaling.callback.GetParticipantsResponseCallback;
import org.etaoin.webrtcdemo.signaling.callback.StartSessionResponseCallback;

/**
 * @author Sergio Gutiérrez
 */
public class SignalingController {

	public void startSession(String name, StartSessionResponseCallback callback) {
		StartSessionRequest request = new StartSessionRequest(callback);
		request.execute(name);
	}

	public void getParticipants(String name, GetParticipantsResponseCallback callback) {
		GetParticipantsRequest request = new GetParticipantsRequest(callback);
		request.execute(name);
	}

	public void getMessages(String name, GetMessagesResponseCallback callback) {
		GetMessagesRequest request = new GetMessagesRequest(callback);
		request.execute(name);
	}

	public void sendOffer(String name, String to, String offer) {
		SendOfferRequest request = new SendOfferRequest();
		request.execute(name, to, offer);
	}

	public void sendAnswer(String name, String to, String answer) {
		SendAnswerRequest request = new SendAnswerRequest();
		request.execute(name, to, answer);
	}

	public void sendIceCandidate(String name, String to, String candidate) {
		SendIceCandidateRequest request = new SendIceCandidateRequest();
		request.execute(name, to, candidate);
	}

	public void sendTerminate(String name, String to) {
		SendTerminateRequest request = new SendTerminateRequest();
		request.execute(name, to);
	}

}
