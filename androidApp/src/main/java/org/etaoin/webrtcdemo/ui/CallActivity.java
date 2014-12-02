package org.etaoin.webrtcdemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import org.etaoin.R;
import org.etaoin.webrtcdemo.communication.CallModel;
import org.etaoin.webrtcdemo.communication.CallState;
import org.etaoin.webrtcdemo.communication.CommunicationController;
import org.etaoin.webrtcdemo.communication.observer.CallModelObserver;

/**
 * @author Sergio Gutiérrez
 */
public class CallActivity extends Activity implements CallModelObserver {

	private TextView webrtcLog;
	private Button hangUpButton;
	private CommunicationController communicationController;
	private CallModel callModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_screen);

		mapGui();
		hookListeners();

		communicationController = CommunicationController.getInstance(this);
		callModel = communicationController.getModel();
		communicationController.subscribe(this);
		TextView counterpartName = (TextView) findViewById(R.id.counterpart_name);
		counterpartName.setText(callModel.getCounterpart());
		webrtcLog.setText(callModel.getSignalingLog());
	}

	private void mapGui() {
		webrtcLog = (TextView) findViewById(R.id.webrtc_log);
		hangUpButton = (Button) findViewById(R.id.hang_up);
	}

	private void hookListeners() {
		hangUpButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				communicationController.hangUp();
			}
		});
	}

	public void onStateChange(CallState state) {
		if (state.equals(CallState.ONGOING_INCOMING) || state.equals(CallState.ONGOING_OUTGOING)) {
			runOnUiThread(new Runnable() {
				public void run() {
					findViewById(R.id.call_window).setBackgroundColor(getResources().getColor(R.color.established_call_green));
				}
			});
		} else if (state.equals(CallState.IDLE)) {
			communicationController.unsubscribe(this);
			finish();
		}
	}

	public void onSignalingLogUpdate(final String log) {
		runOnUiThread(new Runnable() {
			public void run() {
				webrtcLog.setText(log);
			}
		});
	}
}
