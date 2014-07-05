Droidcon-WebRTC
===============

Sample project used in the small talk I gave about WebRTC in Droidcon Madrid


The code is divided in two different parts:
  * _www_ contains all the chat server code. It includes the schema for the tables needed to store session and requests information in SQL format. 
  * _app_ contains the sample code for the android app. Keep in mind that it has been simplified to the maximum and in general it's still very unstable, actually, I highly discourage any brave programmer to try to compile it. Instead just read it and keep it as a reference to know how to work with the API provided by WebRTC and how the interactions between peers should be. I also included a stripped version (no video) of the WebRTC library.

