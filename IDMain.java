import java.util.*;
import java.io.*;
import java.math.*;

public class IDMain {
    
        public static void main (String args[]) {
        
        Scanner in = new Scanner(System.in);
        
        System.out.print("*** Filename of the training set (as filename.csv): ");
        String fileName = in.next();
        
        String dataset [][] = IDUtils.parse(fileName);
        
        System.out.print("*** Do you wish to discretize the numeric values [Y/N]? ");
        String disc = in.next();
        
        if (disc.compareTo("Y") == 0) IDMath.discretize(dataset);
        
        LinkedList<Integer> a = new LinkedList <Integer>();
        LinkedList<String> attributes = new LinkedList<String>();
        
        //Iterate over all the lines in the CSV, containing data, this is to be changed later upon ID3 implementation.
        for (int i=1; i<dataset.length; i++) a.add(i);
        for (int j=1; j<dataset[0].length-1; j++) attributes.add(dataset[0][j]);
        
        IDNode n = new IDNode();
        
        IDAlg.ID3(dataset,dataset[0][dataset[0].length-1],attributes,a,n);
        
        IDUtils.printTree(n,2,0,dataset[0]);
        
        System.out.println();
        
        System.out.print("*** Filename of input examples (as filename.csv): ");
        String inputFileName = in.next();

        String example [][] = IDUtils.parse(inputFileName);
        
        if (disc.compareTo("Y") == 0) IDMath.discretize(example);
        
        for (int i=0; i<example.length; i++)
        {
            String ex = "";
            for (int j=0; j<example[0].length; j++)
            {
                if (j<example[0].length-1) ex+=(example[i][j]+",");
                else {
                    ex += example[i][j];
                }
            }
            IDAlg.makeDecision(ex,n,dataset);

            System.out.println();
        }
        
    }
}