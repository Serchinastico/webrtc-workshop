package org.etaoin.webrtcdemo.communication;

import android.content.Context;
import org.etaoin.webrtcdemo.communication.observer.CallModelObserver;
import org.etaoin.webrtcdemo.media.PeerConnectionWrapper;
import org.etaoin.webrtcdemo.media.callback.ConnectionStateCallback;
import org.etaoin.webrtcdemo.media.callback.CreateAnswerCallback;
import org.etaoin.webrtcdemo.media.callback.CreateOfferCallback;
import org.etaoin.webrtcdemo.media.callback.IceCandidateCallback;
import org.etaoin.webrtcdemo.signaling.Message;
import org.etaoin.webrtcdemo.signaling.SignalingController;
import org.etaoin.webrtcdemo.signaling.callback.GetMessagesResponseCallback;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Sergio Gutiérrez
 */
public class CommunicationController implements CreateOfferCallback, CreateAnswerCallback, IceCandidateCallback, ConnectionStateCallback, GetMessagesResponseCallback {

	private static CommunicationController instance = null;
	private Context context;
	private SignalingController signalingController;
	private PeerConnectionWrapper peerConnectionWrapper;
	private CallModel model;

	private CommunicationController(Context context) {
		this.context = context;
		this.signalingController = new SignalingController();
		this.model = new CallModel();
		this.peerConnectionWrapper = new PeerConnectionWrapper();
	}

	public static CommunicationController getInstance(Context context) {
		if (instance == null) {
			instance = new CommunicationController(context);
		}
		return instance;
	}

	public void startReadingMessages(final String from) {
		model.setCurrentUser(from);

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				signalingController.getMessages(from, CommunicationController.this);
			}
		};

		Timer readingMessagesTimer = new Timer();
		readingMessagesTimer.schedule(task, 0, 1000);
	}

	public void call(String from, String to) {
		model.setCurrentUser(from);
		model.setOutgoingCall(to);
		peerConnectionWrapper.createOffer(context, this, this, this);
	}

	public void hangUp() {
		signalingController.sendTerminate(model.getMe(), model.getCounterpart());
		model.setFinishedCall();
	}

	public void subscribe(CallModelObserver observer) {
		model.subscribe(observer);
	}

	public void unsubscribe(CallModelObserver observer) {
		model.unsubscribe(observer);
	}

	public CallModel getModel() {
		return model;
	}

	public void onConnectionStateChange(String state) {
		if (state.equals("CONNECTED")) {
			model.setOngoingCall();
		}
	}

	public void onOfferCreated(String offer) {
		model.addSignalingLog(">>>>>>>>\r\n" + offer + ">>>>>>>>\r\n");
		signalingController.sendOffer(model.getMe(), model.getCounterpart(), offer);
	}

	public void onAnswerCreated(String answer) {
		model.addSignalingLog(">>>>>>>>\r\n" + answer + ">>>>>>>>\r\n");
		signalingController.sendAnswer(model.getMe(), model.getCounterpart(), answer);
	}

	public void onNewIceCandidate(String candidate) {
		model.addSignalingLog(">>>>>>>>\r\n" + candidate + ">>>>>>>>\r\n");
		signalingController.sendIceCandidate(model.getMe(), model.getCounterpart(), candidate);
	}

	public void onNewMessages(List<Message> messages) {
		for (Message message : messages) {
			if (message.getType().equals("offer")) {
				model.setIncomingCall(message.getFrom());
				peerConnectionWrapper.createAnswer(context, message.getContent(), this, this, this);
			} else if (message.getType().equals("answer")) {
				model.setEstablishingCall();
				peerConnectionWrapper.setRemoteDescription(message.getContent());
			} else if (message.getType().equals("candidate")) {
				peerConnectionWrapper.addIceCandidate(message.getContent());
			} else if (message.getType().equals("terminate")) {
				model.setFinishedCall();
				peerConnectionWrapper.finish();
			}
			model.addSignalingLog("<<<<<<<<\r\n" + message.getContent() + "\r\n<<<<<<<<\r\n");
		}
	}

}
