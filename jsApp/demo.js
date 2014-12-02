error = function (error) {
    console.log(error);
}

navigator.webkitGetUserMedia({
    video: true,
    audio: true
},

function (stream) {
    console.log("success callback");
},
error);
