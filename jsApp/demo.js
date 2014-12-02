error = function (error) {
    console.log(error)
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
            console.log("Offer: \n", offer);
            console.log(offer.sdp);
        });
    });
},

error);
