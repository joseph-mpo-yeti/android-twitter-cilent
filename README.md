# Project 2 - Part 2 - *SimpleTweet* - 

**SimpleTweet** is an android app that allows a user to view his Twitter timeline and post a new tweet. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: **11** hours spent in total

## User Stories

The following **required** functionality is completed:

- [x] User can **compose and post a new tweet**
    - [x] User can click a “Compose” icon in the Action Bar on the top right
    - [x] User can then enter a new tweet and post this to twitter
    - [x] User is taken back to home timeline with **new tweet visible** in timeline
    - [x] Newly created tweet should be manually inserted into the timeline and not rely on a full refresh
    - [x] User can **see a counter with total number of characters left for tweet** on compose tweet page

The following **optional** features are implemented:

- [x] User is using **"Twitter branded" colors and styles**
- [x] User can click links in tweets launch the web browser
- [ ] User can **select "reply" from detail view to respond to a tweet**
- [x] The "Compose" action is moved to a FloatingActionButton instead of on the AppBar
- [ ] Compose tweet functionality is build using modal overlay
- [x] Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.org/android/Using-Parceler).
- [ ] User can **open the twitter app offline and see last loaded tweets**. Persisted in SQLite tweets are refreshed on every application launch. While "live data" is displayed when app can get it from Twitter API, it is also saved for use in offline mode.
- [ ] When a user leaves the compose view without publishing and there is existing text, prompt to save or delete the draft. If saved, the draft should then be **persisted to disk** and can later be resumed from the compose view.
- [ ] Enable your app to receive implicit intents from other apps. When a link is shared from a web browser, it should pre-fill the text and title of the web page when composing a tweet.

The following **additional** features are implemented:

- [x] User can **sign out of Twitter**
- [x] User can **view single image** in new Activity
- [x] User can **swipe to view all embedded images** in a tweet

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<div style="display: inline-flex;">
  <img src='./tw2_1.gif' title='Video Walkthrough 1' width='260' alt='Video Walkthrough 1' style="margin-right: 10px" />
  <img src='./tw2_2.gif' title='Video Walkthrough 2' width='260' alt='Video Walkthrough 2' style="margin-right: 10px" />
  <img src='./tw2_3.gif' title='Video Walkthrough 3' width='260' alt='Video Walkthrough 3' />
</div>

<div style="display: inline-flex;">
  <img src='./tw2_4.gif' title='Video Walkthrough 4' width='260' alt='Video Walkthrough 4' style="margin-right: 10px" />
  <img src='./tw2_5.gif' title='Video Walkthrough 5' width='260' alt='Video Walkthrough 5' style="margin-right: 10px" />
  <img src='./tw2_6.gif' title='Video Walkthrough 6' width='260' alt='Video Walkthrough 6' />
</div>

## Notes

Building the the optional features requires a lot of reading and research and it is time consuming, but worth it.
My codebase is growing very large and I am a little worried about best practices to better manage it and optimize the app.


* [scribe-java](https://github.com/fernandezpablo85/scribe-java) - Simple OAuth library for handling the authentication flow.
* [Android Async HTTP](https://github.com/codepath/AsyncHttpClient) - Simple asynchronous HTTP requests with JSON parsing
* [codepath-oauth](https://github.com/thecodepath/android-oauth-handler) - Custom-built library for managing OAuth authentication and signing of requests
* [Glide](https://github.com/bumptech/glide) - Used for async image loading and caching them in memory and on disk.
* [Room](https://developer.android.com/training/data-storage/room/index.html) - Simple ORM for persisting a local SQLite database on the Android device
* [Parceler](https://github.com/johncarl81/parceler) Parcelable library for Android

## License

    Copyright 2021 Joseph Mpo Yeti

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


----

# Project 2 - *SimpleTweet*

**SimpleTweet** is an android app that allows a user to view his Twitter timeline. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: **8** hours spent in total

## User Stories

The following **required** functionality is completed:

- [x] User can **sign in to Twitter** using OAuth login
- [x]	User can **view tweets from their home timeline**
- [x] User is displayed the username, name, and body for each tweet
- [x] User is displayed the [relative timestamp](https://gist.github.com/nesquena/f786232f5ef72f6e10a7) for each tweet "8m", "7h"
- [x] User can refresh tweets timeline by pulling down to refresh

The following **optional** features are implemented:

- [x] User can view more tweets as they scroll with infinite pagination
- [x] Improve the user interface and theme the app to feel "twitter branded"
- [x] Links in tweets are clickable and will launch the web browser
- [x] User can tap a tweet to display a "detailed" view of that tweet
- [x] User can see embedded image media within the tweet detail view
- [ ] User can watch embedded video within the tweet
- [ ] User can open the twitter app offline and see last loaded tweets
- [ ] On the Twitter timeline, leverage the CoordinatorLayout to apply scrolling behavior that hides / shows the toolbar.

The following **additional** features are implemented:

- [ ] List anything else that you can get done to improve the app functionality!

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='./tw_demo_1.gif' title='Video Walkthrough 1' width='300' alt='Video Walkthrough 1' />
<img src='./tw_demo_2.gif' title='Video Walkthrough 2' width='300' alt='Video Walkthrough 2' />
<img src='./tw_demo_3.gif' title='Video Walkthrough 3' width='300' alt='Video Walkthrough 3' />

## Notes

I wish I had more time to work on it.

## Open-source libraries used

* [scribe-java](https://github.com/fernandezpablo85/scribe-java) - Simple OAuth library for handling the authentication flow.
* [Android Async HTTP](https://github.com/codepath/AsyncHttpClient) - Simple asynchronous HTTP requests with JSON parsing
* [codepath-oauth](https://github.com/thecodepath/android-oauth-handler) - Custom-built library for managing OAuth authentication and signing of requests
* [Glide](https://github.com/bumptech/glide) - Used for async image loading and caching them in memory and on disk.
* [Room](https://developer.android.com/training/data-storage/room/index.html) - Simple ORM for persisting a local SQLite database on the Android device
* [Parceler](https://github.com/johncarl81/parceler) Parcelable library for Android

## License

    Copyright 2021 Joseph Mpo Yeti

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
