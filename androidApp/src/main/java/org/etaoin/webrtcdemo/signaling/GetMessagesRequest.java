package org.etaoin.webrtcdemo.signaling;

import android.util.Log;
import org.etaoin.webrtcdemo.signaling.callback.GetMessagesResponseCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergio Gutiérrez
 */
public class GetMessagesRequest extends BaseRequest<String, Void, List<Message>> {

	private static final String LOG_TAG = "GetMessagesRequest";

	private GetMessagesResponseCallback callback;

	public GetMessagesRequest(GetMessagesResponseCallback callback) {
		this.callback = callback;
	}

	@Override
	protected List<Message> doInBackground(String... strings) {
		List<Message> messages = new ArrayList<Message>();

		try {
			String rawResponse = executeGet(strings[0]);
			if (!rawResponse.equals("")) {
				JSONArray jsonResponse = new JSONArray(rawResponse);
				for (int i = 0; i < jsonResponse.length(); i++) {
					JSONObject jsonMessage = jsonResponse.getJSONObject(i);
					messages.add(new Message(
							jsonMessage.getString("from"),
							jsonMessage.getString("type"),
							jsonMessage.getString("request")
					));
				}
			}
			Log.v(LOG_TAG, "Getting requests returned: " + rawResponse);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Unexpected response from server");
		}

		return messages;
	}

	@Override
	protected void onPostExecute(List<Message> messages) {
		super.onPostExecute(messages);

		callback.onNewMessages(messages);
	}

	@Override
	protected String getMethodName() {
		return "getmessages";
	}
}
