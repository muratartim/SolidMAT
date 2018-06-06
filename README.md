# SolidMAT
SolidMAT is an object-oriented software system which has been designed and developed for finite element and lifetime analysis of small-to-medium scaled structural applications. The software system enables the user to perform different linear analyses of the following types:
- Linear static (displacement & stress),
- Modal (free vibration),
- Linear transient (time history),
- Linear buckling (stability),
- Fatigue (lifetime).

The user can configure, start and monitor the different successive steps involved in these analysis procedures. The major features of the software package can be highlighted as follows,
- Preprocessing (discretization and mesh generation, material information, boundary conditions and etc.),
- Processing (starting and monitoring the demanded analysis type),
- Postprocessing (displaying the resulting values in various formats such as tables, 2D and 3D plots and etc.).

## Screenshots
Following screenshots demonstrate some of the 3D modeling, data visualisation and analysis capabilities of SolidMAT.

![sc3](https://user-images.githubusercontent.com/13915745/40977697-f7a41cf0-68d1-11e8-8f38-0fa9070e04fc.jpg)

## Development languages
The software is mainly written in Java (90%), including user interfaces, analysis modules and data visualisations. Some of the linear equation solvers are written in Fortran and comipled into standalone executables which are called by the main application when needed. 

Some C++ libraries are included into the developed software package in order to be used in fatigue analysis. Because whole software package has been developed in Java programming language, native interfaces needed to be implemented to establish connection between the package and the libraries. This has been realized by the use of Java Native Interface (JNI).

## Software architecture
Following sub-sections detail the overall software architecture employed for the development process.

### Package structure
Project has been partitioned into four major packages for simultaneous development. The <b>fea</b> package contains packages necessary for the Finite Element Analysis, the <b>mesh</b> package contains automatic mesh generation classes, the <b>output</b> package includes classes for visualizer and table output writer classes, respectively. The <b>gui</b> package contains all the user interface classes.



### UML class diagrams

### Java native interfaces (JNI)

## Software usage

### Element types

### Linear equation solvers

### Mesh generators
