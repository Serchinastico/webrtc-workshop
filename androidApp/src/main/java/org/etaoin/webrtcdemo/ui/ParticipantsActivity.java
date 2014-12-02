package org.etaoin.webrtcdemo.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.etaoin.R;
import org.etaoin.webrtcdemo.communication.CallState;
import org.etaoin.webrtcdemo.communication.CommunicationController;
import org.etaoin.webrtcdemo.communication.observer.CallModelObserver;
import org.etaoin.webrtcdemo.signaling.SignalingController;
import org.etaoin.webrtcdemo.signaling.callback.GetParticipantsResponseCallback;

import java.util.List;

/**
 * @author Sergio Gutiérrez
 */
public class ParticipantsActivity extends ListActivity implements GetParticipantsResponseCallback, CallModelObserver {

	public static final String NICKNAME_EXTRA = "nickname";

	private String nickname;
	private SignalingController signalingController;
	private CommunicationController communicationController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		nickname = getIntent().getStringExtra(NICKNAME_EXTRA);

		// Start polling for incoming messages
		communicationController = CommunicationController.getInstance(this);
		communicationController.startReadingMessages(nickname);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Get the list of participants in the chat room
		signalingController = new SignalingController();
		signalingController.getParticipants(nickname, this);

		communicationController.subscribe(this);
	}

	public void onParticipantsReceived(List<String> participants) {
		setListAdapter(new ArrayAdapter<String>(this, R.layout.participant_item, participants));

		ListView listView = getListView();

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				// Make a call to the selected user
				String participant = ((TextView) view).getText().toString();
				communicationController.call(nickname, participant);
			}
		});
	}

	public void onStateChange(CallState state) {
		// We are expecting two different state transitions here:
		//  - from IDLE to RINGING_OUTGOING
		//  - from IDLE to RINGING_INCOMING
		// In either case we go directly to the call window
		if (state.equals(CallState.RINGING_INCOMING) || state.equals(CallState.RINGING_OUTGOING)) {
			Intent intent = new Intent(ParticipantsActivity.this, CallActivity.class);
			startActivity(intent);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

		communicationController.unsubscribe(this);
	}

	public void onSignalingLogUpdate(String log) {
		// Empty
	}
}
