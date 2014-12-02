isCaller = true;

error = function (error) {
    console.log(error);
}

if (isCaller) {
    pc = new webkitRTCPeerConnection({
        "iceServers": []
    });

    navigator.webkitGetUserMedia({
        video: true,
        audio: true
    },

    function (localMediaStream) {
        pc.addStream(localMediaStream);
        pc.createOffer(function (offer) {
            pc.setLocalDescription(
            offer,

            function () {
                console.log(offer.sdp);
                console.log("Paste on callee:");
                console.log("receiveOffer(atob('" + btoa(offer.sdp) + "'))");
            });
        });
    },
    error

    );
}

receiveOffer = function (offerSdp) {
    var pc = new webkitRTCPeerConnection({
        "iceServers": []
    });

    navigator.webkitGetUserMedia({
        video: true,
        audio: true
    },

    function (localMediaStream) {
        pc.addStream(localMediaStream);
        pc.setRemoteDescription(new RTCSessionDescription({
            type: "offer",
            sdp: offerSdp
        }),

        function () {
            pc.createAnswer(function (answer) {
                pc.setLocalDescription(answer);
                console.log(answer.sdp);
                console.log("Paste on caller:");
                console.log("receiveAnswer(atob('" + btoa(answer.sdp) + "'))");
            },
            error, {
                mandatory: {
                    OfferToReceiveAudio: true,
                    OfferToReceiveVideo: true
                }
            });

        },
        error);
    },

    error);
}
