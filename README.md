iG Protection is short for Image Gesture Protection which uses OpenCV and Handwave to take gesture inputs from front facing camera and use them to make a unique encryption key to be used to encrypt target data file using Blowfish symmetric key encryption Algorithm. To run the project, you need an android phone and android studio to build the app.

How to build and run the app:

1. Load the project in Android Studio.

2. Build the app.

3. Copy the app in your android phone.

4. Make sure that Installation from Unknown Sources is checked in the phone.

5. Install the app and run.

6. After opening the app, provide a combination of up to 5 gestures (left, right, up or down), which you can see being recorded in the phone.

7. Then select the file that need to be encrypted, which then gets saved in the 'gesture' folder in the root directory.

8. To decrypt the file, select the file from the Home menu of the app, provide the same gestures as provided to encrypt the file.

9. After decryption, the unlocked file is saved in 'gesture_unlock' folder in the root directory.


# NewHandwave

Handwave was originally intended to be a library that would use a phone's front facing camera to detect a gesture that happens overtop the phone. Generally done with your hand, you can wave your hand up, down, left, right, and further out then towards your phone which registers as a click. The motions are simplified to implement in your project with an interface that may be passed in via parameters. See the usage examples below.

Usage on a seperate project:

1. Download the library to your computer

2. Open the project you want to add this to

3. File -> New -> Import Module

4. Check Both -> OK -> Wait for the libraries to index

5. Right Click your app module -> Open Module Settings

6. Click the dependencies tab

7. Click the + icon and select the module option

8. Add Both the openCV module and the NewHandwave module

9. The library is now usable in your project
