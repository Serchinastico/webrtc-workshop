package org.etaoin.webrtcdemo.media;

import android.content.Context;
import android.util.Log;
import org.etaoin.webrtcdemo.media.callback.ConnectionStateCallback;
import org.etaoin.webrtcdemo.media.callback.CreateAnswerCallback;
import org.etaoin.webrtcdemo.media.callback.CreateOfferCallback;
import org.etaoin.webrtcdemo.media.callback.IceCandidateCallback;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnection.IceConnectionState;
import org.webrtc.PeerConnection.IceGatheringState;
import org.webrtc.PeerConnection.IceServer;
import org.webrtc.PeerConnection.Observer;
import org.webrtc.PeerConnection.SignalingState;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.SessionDescription.Type;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergio Gutiérrez
 */
public class PeerConnectionWrapper implements Observer {

	private static final String LOG_TAG = "PeerConnectionWrapper";

	private PeerConnection peerConnection = null;
	private CreateOfferCallback createOfferCallback;
	private CreateAnswerCallback createAnswerCallback;
	private IceCandidateCallback iceCandidateCallback;
	private ConnectionStateCallback connectionStateCallback;
	private MediaStream localStream;

	public void createOffer(Context context, final CreateOfferCallback createOfferCallback,
			IceCandidateCallback iceCandidateCallback, ConnectionStateCallback connectionStateCallback) {
		this.createOfferCallback = createOfferCallback;
		this.iceCandidateCallback = iceCandidateCallback;
		this.connectionStateCallback = connectionStateCallback;

		if (createPeerConnection(context)) {
			MediaConstraints offerConstraints = new MediaConstraints();
			offerConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
			offerConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "false"));
			peerConnection.createOffer(new OfferObserver(), offerConstraints);
		}
	}

	public void createAnswer(Context context, String offer, CreateAnswerCallback createAnswerCallback,
			IceCandidateCallback iceCandidateCallback, ConnectionStateCallback connectionStateCallback) {
		this.createAnswerCallback = createAnswerCallback;
		this.iceCandidateCallback = iceCandidateCallback;
		this.connectionStateCallback = connectionStateCallback;

		if (createPeerConnection(context)) {
			SessionDescription offerDescription = new SessionDescription(Type.OFFER, offer);
			peerConnection.setRemoteDescription(new RemoteDescriptionObserver(), offerDescription);
		}
	}

	public void setRemoteDescription(String answer) {
		SessionDescription answerDescription = new SessionDescription(Type.ANSWER, answer);
		peerConnection.setRemoteDescription(new RemoteDescriptionObserver(), answerDescription);
	}

	public void addIceCandidate(String candidate) {
		// We are assuming audio candidates but the type should be sent through our signaling system
		peerConnection.addIceCandidate(new IceCandidate("audio", 0, candidate));
	}

	public void finish() {
		if (peerConnection != null) {
			peerConnection.close();
		}
	}

	private class OfferObserver implements SdpObserver {

		public void onCreateSuccess(SessionDescription sessionDescription) {
			peerConnection.setLocalDescription(this, sessionDescription);
			createOfferCallback.onOfferCreated(sessionDescription.description);
		}

		public void onSetSuccess() {}

		public void onCreateFailure(String s) {
			Log.e(LOG_TAG, "Error creating the offer: " + s);
		}
		public void onSetFailure(String s) {}

	}
	private class AnswerObserver implements SdpObserver {

		public void onCreateSuccess(SessionDescription sessionDescription) {
			peerConnection.setLocalDescription(this, sessionDescription);
			createAnswerCallback.onAnswerCreated(sessionDescription.description);
		}

		public void onSetSuccess() {}

		public void onCreateFailure(String s) {
			Log.e(LOG_TAG, "Error creating the answer: " + s);
		}
		public void onSetFailure(String s) {}

	}
	private class RemoteDescriptionObserver implements SdpObserver {

		public void onCreateSuccess(SessionDescription sessionDescription) {}

		public void onSetSuccess() {
			if (peerConnection.getLocalDescription() == null) {
				MediaConstraints answerConstraints = new MediaConstraints();
				answerConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
				answerConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "false"));
				peerConnection.createAnswer(new AnswerObserver(), answerConstraints);
			}
		}

		public void onCreateFailure(String s) {}
		public void onSetFailure(String s) {
			Log.e(LOG_TAG, "Error setting the remote description: " + s);
		}

	}

	public void onSignalingChange(SignalingState signalingState) {}

	public void onIceConnectionChange(IceConnectionState iceConnectionState) {
		connectionStateCallback.onConnectionStateChange(iceConnectionState.toString());
	}

	public void onIceGatheringChange(IceGatheringState iceGatheringState) {}

	public void onIceCandidate(IceCandidate iceCandidate) {
		if (iceCandidate != null) {
			iceCandidateCallback.onNewIceCandidate(iceCandidate.sdp);
		}
	}

	public void onError() {}

	public void onAddStream(MediaStream mediaStream) {}

	public void onRemoveStream(MediaStream mediaStream) {}

	public void onDataChannel(DataChannel dataChannel) {}

	public void onRenegotiationNeeded() {}

	private boolean createPeerConnection(Context context) {
		boolean success = false;

		if (PeerConnectionFactory.initializeAndroidGlobals(context)) {
			PeerConnectionFactory factory = new PeerConnectionFactory();
			List<IceServer> iceServers = new ArrayList<IceServer>();
			iceServers.add(new IceServer("stun:stun.l.google.com:19302"));
			// For TURN servers the format would be:
			// new IceServer("turn:url", user, password)

			MediaConstraints mediaConstraints = new MediaConstraints();
			mediaConstraints.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "false"));
			mediaConstraints.optional.add(new MediaConstraints.KeyValuePair("RtpDataChannels", "true"));
			peerConnection = factory.createPeerConnection(iceServers, mediaConstraints, this);

			localStream = factory.createLocalMediaStream("WEBRTC_WORKSHOP_NS");
			localStream.addTrack(factory.createAudioTrack("WEBRTC_WORKSHOP_NSa1",
					factory.createAudioSource(new MediaConstraints())));
			peerConnection.addStream(localStream, new MediaConstraints());
			success = true;
		}

		return success;
	}
}
