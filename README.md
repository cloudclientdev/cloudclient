# Cloud Client
Cloud Client is an open source Minecraft PVP Client for 1.7.10 and 1.8.9 using the Forge API.

## Downloading
- Download the mod from the Releases section here
- Download the mod using the installer (https://github.com/CloudClientDev/cloudinstaller)

## Workspace Setup
1. Clone or download the repository either using git or the zip download
2. Open the folder, either 1.7.10 or 1.8.9 and copy the path
3. Open a command proment or terminal and change the directory to the copied path (for example: "cd C:\User\Desktop\cloudclient-main\1.8.9")
4. Type in "gradlew setupDecompWorkspace" and either add "idea" if using intelliJ IDEA or "eclipse" if using Eclipse (IntelliJ IDEA is recommended)
5. Wait for everything to finish and open the project using your preferred IDE from above. Do not import the project as a gradle project
6. Lastly add "--tweakClass org.spongepowered.asm.launch.MixinTweaker --mixin mixins.cloudmc.json" to your programm arguments

## Building
In order to build the project you will first need to setup your workspace, which you can do above.
1. Open either the 1.7.10 or 1.8.9 folder and copy the path
2. Open a command proment or terminal and change the directory to the copied path (for example: "cd C:\User\Desktop\cloudclient-main\1.8.9")
3. Type in "gradlew build". This will make a new build in C:\User\Desktop\cloudclient-main\1.8.9\build\libs
4. Copy the .jar file and paste it into your mods folder in order to launch the mod

## License
This project is licensed under the GNU GPL-3.0 License

Permissions:
- Modification 
- Distribution 
- Private use

Conditions:
- License and copyright notice
- State changes 
- Disclose source 
- Same license 

This project uses code from:
- superblaubeere27 (Font Renderer) https://github.com/superblaubeere27 (MIT license, compatible with GPL-3.0)
- LaVache-FR (AnimationUtil) https://github.com/LaVache-FR (GPL-3.0 License)
- Moulberry (MotionBlur) https://github.com/Moulberry (Creative Commons Public License, compatible with GPL-3.0)
- makamys (1.7.10 Template) https://github.com/makamys/forge-mod-template (LGPL, compatible with GPL-3.0 License)
