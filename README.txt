Whisper Chat Application 

In order to start the backend server locally (must be done before .apk file is started):
	1. Open a CMD / Terminal to the project directory 
	2. Navigate into the backend directory 
	3. Install any requirements for the application 
		- `pip3 install -r requirement.txt`
	3. Run the python `app.py` file 
		- This is usually done through `python3 app.py`
		- The program should say that it is running on localhost port 4321
			- localhost:4321

In order to run the Whisper chat application .apk file: 
	1. Start Android Studio to the Home page
	2. Click on the Virtual Device Manager 
		- On Windows, this button will be under the "More Actions" drop down menu
		- On MacOS, this button is available under the 3 dot menu in the top right beside "Get from VCS" 
	3. If there are no virtual devices present with API level 34 or higher, click the plus button at the top to create one
	4. Launch the Android emulator by pressing the Run button 
	5. Open the file explorer to the directory containing the .apk file for the Whisper Chat Application 
	6. Drag and Drop the .apk file onto the emulator screen 
	7. After the .apk file installs, find the new application on the emulator 
	8. Double click to launch the application 