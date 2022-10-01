> This project can't build without twitch developer settings. but you can run this app with apk file on release note. if you want to build this app, you should create your own twitch app in Twitch Developer console. and set OAuth redirection URL https://towitch/home

</br>

Using [Twitch Api](https://dev.twitch.tv/docs/api/), I made a **twitch clone app**.

</br>

![alt](demo.gif)

## Feature
- Twitch Sign in
- Show Live stream whoes user follow
- Show random Live Streams
- Show Broadcasters who user follow
- Show Top popular games on twitch
- Show Just Chatting streams
- Show Clips
- Show Categories

> This app can't play video or show chat. but just shows a thumbnail of the video. cuz Twitch API does not support it.

## Tech Stack
- Jetpack compose
- dagger-hilt
- coroutine
- retrofit
- glide
- deep link (for twitch auth redirection)

