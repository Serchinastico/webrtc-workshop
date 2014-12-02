package org.etaoin.webrtcdemo.signaling;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergio Gutiérrez
 */
public class SendOfferRequest extends BaseRequest<String, Void, Void> {


	@Override
	protected Void doInBackground(String... strings) {
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair("name", strings[0]));
		postParams.add(new BasicNameValuePair("to", strings[1]));
		postParams.add(new BasicNameValuePair("offer", strings[2]));
		executePost(postParams);

		return null;
	}

	@Override
	protected String getMethodName() {
		return "sendoffer";
	}

}
