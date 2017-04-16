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
- Post board (MainActivity), display all post groups, which can jump to search, create new post, in group chatting interface.
- Search (SearchActivity), search for certain groups in post board, which can jump to post board.
- Create new post (CreatePostActivity), create a new post in post board, which can jump to post board.
- In group chatting (InGroupActivity), allow users to chat in the group, which can jump to edit post, group member.
- Edit post (EditPostActivity), allow owner of the group to change group information.
- Group member (GroupInfoActivity), allow group members to find information (email, name, photo) about other group members.
- Setting (SettingActivity), allow users to change his/her own name, email and upload his photo.

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

Tak Card before April 12
------------------------

| task name                         | Description                                                                                                                                                              | owner                     |
|-----------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------|
| search function                   | implement the search function of the post in firebase | Shangxing Sun   |
| UI function furnished             | setting, create new post: For now, we use firebase authentication, the setting should be changed, there should be a datepicker in the create new post|Shihao Li|
| Message  | revised the message UI, associate the group chatting with different group| Shengbin Wu|
