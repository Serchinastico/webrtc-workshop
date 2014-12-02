isCaller = true;
mySid = "ivmos1";
otherSid = "ivmos2";

Api.startSession(mySid);

error = function (error) {
    console.log(error);
}

init = function (onSuccess) {
    pc = new webkitRTCPeerConnection({
        "iceServers": [{
            "url": "stun:stun.l.google.com:19302"
        }]
    });
    iceCandidates = [];

    var v = document.getElementById('v');

    pc.onaddstream = function (event) {
        console.log("onaddstream");
        console.log(event);
        v.src = URL.createObjectURL(event.stream);
        v.play();
    };

    pc.onicecandidate = function (event) {
        if (event.candidate) {
            Api.sendIceCandidate(mySid, otherSid, event.candidate.candidate);
        }
    };

    navigator.webkitGetUserMedia({
        video: true,
        audio: true
    },
    onSuccess,
    error);
}

if (isCaller) {
    init(

    function (localMediaStream) {
        pc.addStream(localMediaStream);
        pc.createOffer(function (offer) {
            pc.setLocalDescription(
            offer,

            function () {
                Api.sendOffer(mySid, otherSid, offer.sdp);
            });
        });
    });
}

receiveOffer = function (offerSdp) {
    init(

    function (localMediaStream) {
        pc.addStream(localMediaStream);
        pc.setRemoteDescription(new RTCSessionDescription({
            type: "offer",
            sdp: offerSdp
        }),

        function () {
            pc.createAnswer(function (answer) {
                pc.setLocalDescription(answer);
                Api.sendAnswer(mySid, otherSid, answer.sdp);
            },
            error, {
                mandatory: {
                    OfferToReceiveAudio: true,
                    OfferToReceiveVideo: true
                }
            });

        },
        error);
    });
}

receiveAnswer = function (answerSdp) {
    pc.setRemoteDescription(new RTCSessionDescription({
        type: "answer",
        sdp: answerSdp
    }));
}

addIceCandidate = function (candidateSdp) {
    pc.addIceCandidate(new RTCIceCandidate({
        candidate: candidateSdp
    }));
}

setInterval(function () {
    Api.getMessages(mySid, function (data) {
        console.log(data);
        try {
            data = JSON.parse(data);
        } catch (e) {
            return;
        }

        for (var i = 0; i < data.length; i++) {
            switch (data[i].type) {
                case "offer":
                    receiveOffer(data[i].request, otherSid);
                    break;
                case "answer":
                    receiveAnswer(data[i].request);
                    break;
                case "candidate":
                    addIceCandidate(data[i].request);
                    break;

            };
        }

    });
}, 4000);
