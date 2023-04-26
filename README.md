## In.java

Used to facilitate input streaming to the graph API. 

The user can construct an ```In``` object by passing the path of a file which contains data to construct the graph. The format of this data is explained at the top of the Graph.java file.

## Graph.java

The graph API has two constructors of which only one is of importance. 

To create the a graph, the user must pass an ```In``` object to the graph constructor. The constructor will interpret the file associated to the ```In``` object and create the graph object.

Once constructed, the degeneracy, depth and colouring can be computed and fetched using the following getters:

*Degeneracy*
```
int getDegeneracy()
```
*Colouring number*
```
int getColouring()
```
*Colouring groups (not recommended for large graphs)*
```
void displayColouring()
```
*Vertices depth* and *Specific vertex depth*
```
int[] getDepth()
int getDepth(int v)
```

## GraphTester.java

This class is used as a default Graph API manipulator. Various graphs of various sizes can be initialised and timed on various actions. See documentation.

## Compilation & Execution
To compile and run the program the user must run the makefile (make in the command line), and execute one of the following commands: 

*Single execution*
```
java src.GraphTester
```

*Multiple executions*

```
java src.GraphTester [n]
```

This initialises the graphs, runs the algorithms ```n``` times and calculates the average time taken for each action.

By default the program creates four graphs of increasing size and displays a report for each one. The last graph is very big (|V| = 160k, |E| = 6Mil), it takes around 5 minutes to execute. This code can be commented out in file **src/GraphTester.java** *L.89*.
## Author

Thomas O'Cuilleanain.
