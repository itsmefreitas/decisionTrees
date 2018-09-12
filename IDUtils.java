import java.util.*;
import java.io.*;

public class IDUtils
{
    
    public static float isNumeric(String str)
    {
        try
        {
            return Float.parseFloat(str);
        }
        catch (NumberFormatException e)
        {
            return Integer.MIN_VALUE;
        }
    }

    public static int getAttIndex(String [][] data, String id) {
        
        for (int i=1; i<data[0].length; i++)
        {
            if (data[0][i].compareTo(id) == 0) return i;
        }
        
        return 0;
    }

  public static int [] dimensions(String filename, BufferedReader br) {
        
    int lines = 0;
    int columns = 0;
    
    int [] size = new int [2];
    
    try {
        br = new BufferedReader(new FileReader(filename));
        
        while (br.readLine() != null) {
            if (columns == 0) columns = br.readLine().split(",").length;
            lines++;
        }
    }
    catch (FileNotFoundException e) { System.out.println("File not found!"); System.exit(1); }
    catch (IOException e) { System.out.println("I/O exception!"); System.exit(1); }
    
    size[0] = lines+1;
    size[1] = columns;
    
    br = null;
    
    return size;
  }

  public static String [][] parse(String filename) {
    
    String line = "";
    
    BufferedReader br = null;
    
    int [] d = dimensions(filename,br);
    
    String [][] parsedTable = new String [d[0]][d[1]];
    
    try {
        
        br = new BufferedReader(new FileReader(filename));
        int i = 0;
        
        while ((line = br.readLine()) != null) {
            
            String lnInfo [] = line.split(",");
            
            for (int j=0; j<d[1]; j++) {
                
                parsedTable[i][j] = lnInfo [j];
            }
            
            i++;
        }
        
    }
    catch (FileNotFoundException e) { /*e.printStackTrace();*/ System.out.println("File not found!"); System.exit(1); }
    catch (IOException e) { System.out.println("I/O exception!"); System.exit(1); }
    finally {
        
        if (br!=null) {
            try { br.close(); }
            catch (IOException e) { System.out.println("I/O exception!"); System.exit(1); }
        }
    }
    
    return parsedTable;
  }

  public static void printTree(IDNode t, int tabs, int ord, String [] data) {
        
    String format = "%-"+tabs+"s";
    
    if (t.label != null) 
    {
        if (tabs > 2) System.out.printf(format, "");
        System.out.println("<"+t.label+">");
        
        int aIndex = -1;
        
        for (int i=0; i<data.length; i++) {
            
            if (data[i].compareTo(t.label) == 0) aIndex = i;
        }
        
    }
    
    for (IDNode n : t.subTrees) {
        
        System.out.printf(format, "");
        
        if (n.classValue != null) 
        {
            if (n.label != null) System.out.print(n.label+": ");
            System.out.print(n.classValue+" ("+n.nExamples+")");
            System.out.println();
        }
        else if (n.classValue == null)
        {
            System.out.print(n.arch+":");
            System.out.println();
            printTree(n,tabs*=2,ord, data);
        }
    }
    
}

}