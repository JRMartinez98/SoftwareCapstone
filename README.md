# Hearts
Capstone Project - Thomas Haines &amp; Jose Martinez
 
 
                                                        FA-RE
___________________________________________________________________________________________________
 
FA-RE is an Android app that focuses on helping students study for Theory of Computing.
This app comes from the idea that you can find many apps that help you learn how to code
but none of these help you study Theory, a very important course in the Computer Science
major.
 
Our app features a Multiple Choice section that focuses on different topics such as 
  - Context-Free Grammar
  - Regular Languages
  - Push Down Automata
and more.
 
The app also features a profile section, allowing the user to set their own name and keep
track of their performance in the different testing methods.
 
On top of this, if the user believes that more could be added for questioning or wanted to add
their own question set, they can! FA-RE obtains the questions displayed through a .txt file 
that follows a certain layout and the app will contain a section for creating multiple choice 
questions in-app, eliminating the need for a txt editor app in order to make question sets.


*FA-RE is only compatible with Android devices running on Android 8.1 (API Level 27) or higher.
If you cannot update an Android device to firmware 8.1 or higher, you will not be able to use this app.*
 
___________________________________________________________________________________________________

When opening the app you are greeted with a Main Menu and are asked to provide your name 
for your profile. The Main Menu offers the user 3 different options:

- Testing: Section for the user to be tested on the different sections from Theory of Computing.
    - Has 3 different options: testing on regular languages, CFGs and testing on user generated files.
    The User Generated section contains an in-app file browser for the folder that contains the
    user generated text files. After the user is tested, they are taken to their profile so they can view
    the stats. Each question provides instant feedback on their result so they are aware what needs to be reviewed.
    
- User Profile: View the user's profile with stats from their testing.
    - Contains the name of the user and the stats that the user has obtained through testing. After being tested, 
    the user is taken to the profile in order to view their final score. The stats are written to the user profile 
    file.
    
- Question Creator: allows for in-app creation of questions that the user can be tested on.
    - Requires the user to specify a file name and allows them to choose from appending to file or full overwrite.
    After choosing a file the user is able to add questions and are not allowed to write to file until at least one
    question is added. Questions cannot be added until a question, 4 choices and an answer are provided. 
    - Once a question is added, it can be viewed at the bottom to check for any errors but it cannot be edited. The
    user is required to back out and restart. Once all questions have been added and the user hits "FINISH" the activity is
    restarted in order to clear everything
    
    
