# CubeSolver
To run the CubeSolver JAR executable, download this repository and open ```CubeSolverExecutable\CubeSolver.jar```. You need Java installed for the program to run. The only files the JAR needs are those in the ```CubeSolverExecutable``` folder.
## Useful commands (execute from from root directory):
compile:
```
javac -d classes src\cubesolver\cube\piece\*.java src\cubesolver\cube\*.java src\cubesolver\*.java src\cubevisual\*.java
```
run:
```
java -cp classes cubevisual.CubeApp
```
build jar:
```
jar cfm CubeSolverExecutable\CubeSolver.jar MANIFEST.MF -C classes cubesolver -C classes cubevisual
```
## License
Licensed under the [MIT License](LICENSE).
