package org.etaoin.webrtcdemo.signaling.callback;

import java.util.List;

/**
 * @author Sergio Gutiérrez
 */
public interface GetParticipantsResponseCallback {

	public void onParticipantsReceived(List<String> participants);

}
