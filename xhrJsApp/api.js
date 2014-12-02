/**
 * Api client for https://github.com/Serchinastico/webrtc-workshop/tree/master/server
 * @author Iván Mosquera Paulo
 */
var Api = {
    _server: "http://23.94.244.200/experiments/webrtc-workshop/",
    startSession: function (sessionId, onSuccess) {
        console.log(">> startSession('" + sessionId + "')");
        $.get(this._server + "?method=startSession&name=" + sessionId, onSuccess);
    },

    getParticipants: function (sessionId, onSuccess) {
        console.log(">> getParticipants('" + sessionId + "')");
        $.get(this._server + "?method=getParticipants&name=" + sessionId, onSuccess);
    },

    getMessages: function (sessionId, onSuccess) {
        console.log(">> getMessages('" + sessionId + "')");
        $.get(this._server + "?method=getMessages&name=" + sessionId, onSuccess);

    },

    sendOffer: function (sessionId, to, offer, onSuccess) {
        console.log(">> sendOffer('" + sessionId + "','" + to + "','" + offer + "')");
        $.post(this._server + "?method=sendOffer", {
            name: sessionId,
            to: to,
            offer: offer
        },
        onSuccess);
    },

    sendAnswer: function (sessionId, to, answer, onSuccess) {
        console.log(">> sendAnswer('" + sessionId + "','" + to + "','" + answer + "')");
        $.post(this._server + "?method=sendAnswer", {
            name: sessionId,
            to: to,
            answer: answer
        },
        onSuccess);
    },

    sendIceCandidate: function (sessionId, to, icecandidate, onSuccess) {
        console.log(">> sendIceCandidate('" + sessionId + "','" + to + "','" + icecandidate + "')");
        $.post(this._server + "?method=sendIceCandidate", {
            name: sessionId,
            to: to,
            icecandidate: icecandidate
        },
        onSuccess);
    },

    sendTerminate: function (sessionId, to, onSuccess) {
        console.log(">> sendOffer('" + sessionId + "','" + to + "')");
        $.post(this._server + "?method=sendTerminate", {
            name: sessionId,
            to: to
        },
        onSuccess);
    }


}
