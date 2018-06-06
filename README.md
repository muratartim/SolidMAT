# SolidMAT
Finite element analysis software. SolidMAT is an object-oriented software system which has been designed, implemented and extended for structural and lifetime analysis of small-to-medium scaled structural engineering applications. The software system enables the user to configure, start and monitor the different successive steps involved in the analysis procedure.

## Development languages
The software is mainly written in Java (90%), including user interfaces, analysis modules and data visualisations. Some of the linear equation solvers are written in Fortran and comipled into standalone executables which are called by the main application when needed. Some C++ libraries are included into the developed software package in order to be used in fatigue analysis. Because whole software package has been developed in Java programming language, native interfaces needed to be implemented to establish connection between the package and the libraries. This has been realized by the use of Java Native Interface (JNI).

## Screenshots
Following screenshots demonstrate some of the 3D modeling, data visualisation and analysis capabilities of SolidMAT.

![sc3](https://user-images.githubusercontent.com/13915745/40977697-f7a41cf0-68d1-11e8-8f38-0fa9070e04fc.jpg)

![flow](https://user-images.githubusercontent.com/13915745/40979015-e7d89ce4-68d4-11e8-9ae8-fc15e63ac658.jpg)
