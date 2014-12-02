error = function (error) {
    console.log(error);
};

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
        pc.setLocalDescription(offer, function () {
            console.log(offer.sdp);
            console.log("Paste on callee:");
            console.log("receiveOffer(atob('" + btoa(offer.sdp) + "'))");
        });
    });
},

error);
