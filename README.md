###Light-weight Dead Code Detector###
***

####Build Incomplete Call Graph using AndroGuard####
1. You can find how to install androGuard [here](https://code.google.com/archive/p/androguard/wikis/Installation.wiki), after install androGuard, excute
```
./apkviewer.py -i ./sample.apk -o ./sample.gexf
```
where "sample" is the name the Apk you want to test. After that, you will get a sample.gexf file, which contains the call graph information.


___

####Installation####
1. Install jdk7, eclipse and Adt plugin.
2. Import SEFinalProject into eclipse. Select this project, right click -> run as -> run configuration -> arguments, set the arguments as input.
3. Rename \*.gexf got from AndroGuard to \*.xml, and copy the *.xml file into SEFinalProject/bin directory, and change the file name in Main.java(line 156)
4. copy the source code of the app that waiting to be detested into SEFinalProject/input file, and change the path in Main.java(line 151)
5. copy the manifest.xml and main.xml into SEFinalProject/bin directory, and change the file name in Main.java(line 19,20) if necessary
6. run
___

####Note####
There are some anonymous classes in java file, current implementation can not deal with this problem. So I used source code that decompilered from APK file which has separate the anonymous class. Because it is easy to correspond the decompilered code to the real source code, I think it does not have an effect on dead code detecting.