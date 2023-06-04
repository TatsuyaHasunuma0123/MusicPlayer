![imga screenshot](./images/img1.png) ![imgb screenshot](./images/img2.png) ![imgc screenshot](./images/img3.png)

# 🎵 MusicPlayer  

## 🎥 Demo
[Here](https://drive.google.com/file/d/1YzpoULIvCi_bBl6aMO38xbyxH8ObpGdJ/view?usp=sharing) is a demonstration video.  
Since this is being executed on an emulator, there will be no sound. However, if you run it on an actual device, there will be sound.

## My other App is here .
- 📝[Canvas](https://github.com/TatsuyaHasunuma0123/Canvas)
- 📆[Twitter Calender](https://github.com/TatsuyaHasunuma0123/TwitterCalendar)
- 🕸️[Discussion Web](https://github.com/TatsuyaHasunuma0123/Discuss)
- 🗾[population_graph](https://github.com/TatsuyaHasunuma0123/population_graph)
  
## Usage
- Install the latest version from the [official AndroidStudio app](https://developer.android.com/studio). 
- **clone** this repositories and open project with AndroidStudio.  
**:collision: werning**: There is a possibility that it may not work depending on your environment.

```
git clone https://github.com/TatsuyaHasunuma0123/MusicPlayer
```
**:collision: werning**: API Level >> 23.  
Change the minSdkVersion in `app/src/build.gradle` to the API level of your actual device.

-Set music on `res/raw` and images `drawable` and add code `MainActivity.onCreate`  
😞 I have to　implement this function in my app...
 
## Button Guides
- ⏯️ pause and play
- ⏭️ track change
- ✖️ change speed
- ➡️ Skip or rewind 10 seconds
- ➖ By moveing sideways this, you can play from your favorite position
