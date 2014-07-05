package org.etaoin.webrtcdemo.signaling;

import org.etaoin.webrtcdemo.signaling.callback.StartSessionResponseCallback;

/**
 * @author Sergio Gutiérrez
 */
public class StartSessionRequest extends BaseRequest<String, Void, Void> {

	private StartSessionResponseCallback callback;

	public StartSessionRequest(StartSessionResponseCallback callback) {
		this.callback = callback;
	}

	@Override
	protected Void doInBackground(String... strings) {
		executeGet(strings[0]);

		return null;
	}

	@Override
	protected void onPostExecute(Void response) {
		super.onPostExecute(response);

		callback.onResponse();
	}

	@Override
	protected String getMethodName() {
		return "startsession";
	}

}
