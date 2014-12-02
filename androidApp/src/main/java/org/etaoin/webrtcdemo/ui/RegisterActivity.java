package org.etaoin.webrtcdemo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import org.etaoin.R;
import org.etaoin.webrtcdemo.signaling.SignalingController;
import org.etaoin.webrtcdemo.signaling.callback.StartSessionResponseCallback;

/**
 * @author Sergio Gutiérrez
 */
public class RegisterActivity extends Activity implements StartSessionResponseCallback {

	private Button registerButton;
	private EditText nicknameInput;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_screen);

		mapGui();
	}

	private void mapGui() {
		registerButton = (Button) findViewById(R.id.register_button);
		nicknameInput = (EditText) findViewById(R.id.nickname_input);
	}

	@Override
	protected void onResume() {
		super.onResume();

		registerButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				String nickname = nicknameInput.getText().toString();
				if (nickname.length() > 0) {
					SignalingController signalingController = new SignalingController();
					signalingController.startSession(nickname, RegisterActivity.this);
				}
			}
		});
	}

	public void onResponse() {
		// We show the participants screen
		Intent intent = new Intent(this, ParticipantsActivity.class);
		intent.putExtra(ParticipantsActivity.NICKNAME_EXTRA, nicknameInput.getText().toString());
		startActivity(intent);
	}

}

