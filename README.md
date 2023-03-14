![Logo](/screenshots/Logo.png)

# Cloud Client
Cloud Client is an open source Minecraft PvP Client for 1.7.10 and 1.8.9 which uses the Forge API.

## Downloading
- Download the mod's `.jar` file from the [Releases](https://github.com/CloudClientDev/cloudclient/releases) section (Windows, MacOS & Linux)
- Download the mod using the [Installer](https://github.com/CloudClientDev/cloudinstaller/) (Windows only)

<details>
<summary><h2>Screenshots</h2></summary>

### Home/Title Screen
![TitleScreen](/screenshots/TitleScreen.png)

### HUD Editor
![TitleScreen](/screenshots/HudEditor.png)

### Mod Menu
![TitleScreen](/screenshots/ModMenu.png)
</details>

<details>
<summary><h2>Compatible Mods</h2></summary>

<h3>(WIP!)<h3>
</details>

<br/>

## Building
### Windows
1. Open either the `1.7.10\cloudclient`, `1.8.9\cloudclient` or `1.12.2\cloudclient` folder and copy the path.
2. Open a command prompt or terminal and change the directory (`cd`) to the copied path. Example path if using GitHub Desktop:
```
cd C:\Users\%USERNAME%\Documents\GitHub\cloudclient\<version>\cloudclient
```
3. Make a build
```
gradlew build
```
&nbsp;&nbsp;&nbsp;&nbsp;or `.\gradlew.bat build` if you do not have Gradle installed.

&nbsp;&nbsp;&nbsp;&nbsp;You will find the new build in `build\libs`.

4. Copy the `.jar` file and paste it into your mods folder and launch Forge for your version.

<br/>

## Workspace Setup
### Windows
1. Clone or download the repository, either using `git`, Git GUI's such as GitHub Desktop or GitKraken, or the [zip download](https://github.com/CloudClientDev/cloudclient/archive/refs/heads/development.zip).
2. Open the folder, either `1.7.10\cloudclient`, `1.8.9\cloudclient` or `1.12.2\cloudclient` and copy the path.
3. Open a command prompt or terminal and change the directory (`cd`) to the copied path. Example path if using GitHub Desktop:
```
cd C:\Users\%USERNAME%\Documents\GitHub\cloudclient\<version>\cloudclient
```
4. Creating the workspace for your IDE <br/>
- IntelliJ IDEA
```
gradlew setupDecompWorkspace idea
```
- Eclipse
```
gradlew setupDecompWorkspace eclipse
```
5. Open the project with your preferred IDE. Don't import it as a Gradle project.
6. To get Mixins working in a Dev Environment, add the arguments below, to your program arguments.
```
--tweakClass org.spongepowered.asm.launch.MixinTweaker --mixin mixins.cloudmc.json
```

## Contributions
Feel free to fork this project, make changes and submit a pull request to the `development` branch.

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
- [superblaubeere27](https://github.com/superblaubeere27)'s Font Renderer
- [LaVache-FR](https://github.com/LaVache-FR/)'s AnimationUtil (https://github.com/LaVache-FR/AnimationUtil)
- [Moulberry](https://github.com/Moulberry)'s MotionBlur
