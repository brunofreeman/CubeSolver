# CubeSolver
To run the CubeSolver JAR executable, ensure Java is installed, download this repository, and open ```CubeSolverExecutable\CubeSolver.jar```. The only files the JAR needs are those in the ```CubeSolverExecutable``` folder.
## Useful commands (execute from from root directory):
### Windows
compile:
```
javac -d classes src\cubesolver\cube\piece\*.java src\cubesolver\cube\*.java src\cubesolver\*.java src\cubevisual\*.java
```
run:
```
java -cp classes cubevisual.CubeApp
```
build JAR:
```
jar cfm CubeSolverExecutable\CubeSolver.jar MANIFEST.MF -C classes cubesolver -C classes cubevisual
```
### MacOS
compile:
```
javac -d classes src/cubesolver/cube/piece/*.java src/cubesolver/cube/*.java src/cubesolver/*.java src/cubevisual/*.java
```
run:
```
java -cp classes cubevisual.CubeApp
```
build JAR:
```
jar cfm CubeSolverExecutable/CubeSolver.jar MANIFEST.MF -C classes cubesolver -C classes cubevisual
```
JAR Note: if any files are changed in ```src\resources```, they must also be changed in ```CubeSolverExecutable\src\resources```.
## License
Licensed under the [MIT License](LICENSE).
