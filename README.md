# Change-Application
Change application is a javaFX-based desktop application that runs on Windows, Mac OS X and Linux. 

As a GUI (graphical user interface), it visualizes the tree structure of model instances. The application converts model instances from XMI (XML Metadata Interchange) format to a visual tree view. Each model instance has its own attributes, attribute values, containing features and feature types, which are hierarchically displayed as tree items in the tree view. Three case study models for the Vitruv framework are are Ecore meta models based on EMF (Eclipse Modeling Framework) for demonstrating MDSD (Model-Driven Software Development) technologies, that are families, persons and insurance meta model, which can be found on the [Vitruv-DSLs](https://github.com/vitruv-tools/Vitruv-DSLs) (The languages for specifying consistency preservation rules). The case studies for the Vitruv framework [Vitruv-CaseStudies](https://github.com/vitruv-tools/Vitruv-CaseStudies), particularly consist of an example application of Vitruv for component-based systems.

The correspondence between these three models can be shown in the application. When the mouse clicks on an instance, all corresponding instances in the other tree views are selected in gray color. This allows you to quickly find all the correspondences. For the correspondence example, a family member is a person who has purchased an insurance. Based on this relationship, three instances are derived from the families, persons and insurance meta model, and correspond to each other and are consistent through change.

## Usage

1. Open the `tools.vitruv.framework.visualization` project in Eclipse IDE.

2. Run the application:
Right click on any file under the `tools.vitruv.framework.visualization.src` package, select `Run as Java Application`. Or select the `run` menu item and choose `Run as Java Application`.

## Project Development
The application depends on the following tools:
+ Eclipse Modeling Framework (EMF) as the modeling environment to process the EMF-model instances
+ JavaFX as a software platform for creating and delivering desktop applications


