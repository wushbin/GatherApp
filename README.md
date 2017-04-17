DukeGather App
===================================
This app is build for gathering student's to call a duke safe ride.

Pre-requisites
--------------
- Android SDK v25
- Android Build Tools v25.0.2
- Android Support Repository v25.2.0

Getting Started
---------------
- This project uses the Gradle build system. To build this project, use the "gradlew build" command or use "Import Project" in Android Studio. 
- We recommend using Android studio to import this project, because we have not exported a .apk file that can be installed in your phone.
- Gradle version should be 2.14.1
- Android Plugin Version should be 2.2.3
- There are some bug reported on the newest version of gradle, so now we can just use the gradle version as mentioned above. 

Overall structure
-----------------
- There are 7 mian interfaces in our app, they are:
 1. Post board (MainActivity), display all post groups, which can jump to search, create new post, in group chatting interface.
 1. Search (SearchActivity), search for certain groups in post board, which can jump to post board.
 1. Create new post (CreatePostActivity), create a new post in post board, which can jump to post board.
 1. In group chatting (InGroupActivity), allow users to chat in the group, which can jump to edit post, group member.
 1. Edit post (EditPostActivity), allow owner of the group to change group information.
 1. Group member (GroupInfoActivity), allow group members to find information (email, name, photo) about other group members.
 1. Setting (SettingActivity), allow users to change his/her own name, email and upload his photo.

Key APIs into the back end
--------------------------
- We are using Firebase as back end for our app. We also have used some APIs for images displaying
- APIs includes:
* Authencation
 1. com.firebaseui:firebase-ui-auth:1.2.0
 1. com.google.firebase:firebase-auth
* Real time database
 1. com.google.firebase:firebase-database:10.2.1
 2. com.google.firebase:firebase-core:10.2.1
* Firebase storage
 1. com.google.firebase:firebase-storage:10.2.1
* Message Dependency
 1. com.google.firebase:firebase-messaging:10.2.1
* Image displaying
 1. com.github.bumptech.glide:glide:3.6.1

Team Assignment Allocation
------------------------

| task name                         | Description                                                                                                                                                              | owner                     |
|-----------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------|
| Post board | Build post board UI and retrieve data from firebase| Shengbin Wu|
| Setting | Build setting UI and update user name, email, photo into firebase| Shangxing Sun|
|Create new Post | Build create new post UI and upload new post into firebase| Shangxing Sun,Shengbin Wu|
|In group chatting | Build in group chatting UI and upload user message into firebase| Shengbing Wu|
|Edit post| Build edit post UI and update group information into firebase| Shengbing Wu|
|Group member| Build group member info UI and display group member's info| Shengbing Wu|
| search function                   | implement the search function of the post in firebase | Shangxing Sun, Shihao Li  |
| UI function furnished             | UI refinement and debugging |Shihao Li|
|firebase Implementation| Implement the app with firebase| Shengbin Wu|
|firebase authentication| Implement the authentication |Shangxing Sun| 
| Document  | Finish design and test document | Shihao Li, Shangxing Sun|
| Presentation| Preparing presentation powerpoint and other document| Shangxing Sun, Shihao Li, Shengbing Wu|

