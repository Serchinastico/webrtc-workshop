package org.etaoin.webrtcdemo.signaling;

import android.util.Log;
import org.etaoin.webrtcdemo.signaling.callback.GetParticipantsResponseCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergio Gutiérrez
 */
public class GetParticipantsRequest extends BaseRequest<String, Void, List<String>> {

	private static final String LOG_TAG = "GetParticipantsRequest";

	private GetParticipantsResponseCallback callback;

	public GetParticipantsRequest(GetParticipantsResponseCallback callback) {
		this.callback = callback;
	}

	@Override
	protected List<String> doInBackground(String... strings) {
		List<String> participants = new ArrayList<String>();

		try {
			String rawResponse = executeGet(strings[0]);
			JSONArray jsonResponse = new JSONArray(rawResponse);
			for (int i = 0 ; i < jsonResponse.length(); i++) {
				participants.add(jsonResponse.getString(i));
			}
			Log.v(LOG_TAG, "Getting participants returned: " + rawResponse);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Unexpected response from server");
		}

		return participants;
	}

	@Override
	protected void onPostExecute(List<String> success) {
		super.onPostExecute(success);

		callback.onParticipantsReceived(success);
	}

	@Override
	protected String getMethodName() {
		return "getparticipants";
	}

}
