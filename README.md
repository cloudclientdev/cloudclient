![Logo](/Logo.png)

# Cloud Client
Cloud Client is an open source Minecraft PVP Client for 1.7.10 and 1.8.9 using the Forge API.

## Downloading
- Download the mod from the Releases section here
- Download the mod using the installer [Installer](https://github.com/CloudClientDev/cloudinstaller)

## Workspace Setup
1. Clone or download the repository either using git or the zip download.
2. Open the folder, either `1.7.10/cloudclient` or `1.8.9/cloudclient` and copy the path.
3. Open a command proment or terminal and change the directory to the copied path.
```
cd C:\User\Desktop\cloudclient-main\<version>\cloudclient
```
4. Creating the workspace for your IDE <br>
- IntelliJ IDEA
```
gradlew setupDecompWorkspace idea
```
- Eclipse
```
gradlew setupDecompWorkspace eclipse
```
5. Open the project with your preferred IDE. Don't import it as a gradle project.
6. To get Mixins working in a Dev Environment, add the args below, to your program arguments.
```
--tweakClass org.spongepowered.asm.launch.MixinTweaker --mixin mixins.cloudmc.json
```

## Building
In order to build the project you will first need to setup your workspace, which you can do above.
1. Open either the `1.7.10/cloudclient` or `1.8.9/cloudclient` folder and copy the path.
2. Open a command proment or terminal and change the directory to the copied path.
```
cd C:\User\Desktop\cloudclient-main\<version>\cloudclient
```
3. Make a build
```
gradlew build
```
You will find the new build in
```
C:\User\Desktop\cloudclient-main\<version>\build\libs
```
4. Copy the .jar file and paste it into your mods folder and launch forge for your version.

## Contributions
Feel free to fork this project, make any changes you would like to add and finally make a pull request.

## License
This project is licensed under the GNU Lesser General Public License v3.0

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
- superblaubeere27 (Font Renderer) https://github.com/superblaubeere27
- LaVache-FR (AnimationUtil) https://github.com/LaVache-FR
- Moulberry (MotionBlur) https://github.com/Moulberry
