# decisionTrees

*** Readme file for decisionTree.java

    > In order to compile the project:
        1. Open terminal;
        2. cd in terminal to the directory containing 'decisionTree.java';
        3. run the command 'javac decisionTree.java'.
        
    > Running the generated binary:
    
        1. In terminal and in the directory of the compiled java code, execute the command 'java decisionTree';
        2. You will be asked to direct the application to a .csv file, containing the training dataset *;
        3. After that, you can feed the program another .csv file in order to evaulate the tree and give you a class value *.
        
        | Notes about the .csv files:
            (a) There are sample ones located in the sub-folder 'datasets';
            (b) The training set file should have attributes [X0,...,Xn] and a class variable Xn+1;
            (c) As for the input .csv file, it should have the same attributes [X0,...,Xn] minus the class value column.