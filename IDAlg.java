import java.util.*;

public class IDAlg {

public static LinkedList <String> nodes = new LinkedList<String>();
public static LinkedList <String> splittedAttributes = new LinkedList<String>();

public static void ID3(String [][] data, String target, LinkedList<String> att, LinkedList<Integer> range, IDNode n) {
    
    String root = "";
    
    int targetIndex = -1;
    
    for (int i=1; i<data[0].length; i++) if (data[0][i].compareTo(target) == 0) targetIndex = i;
    
    LinkedList <String> possibleValues = new LinkedList<String>();
    
    // Get the line position where the class value is to add it to the leaf nodes we may generate.
    int firstRange = -1;
    
    for (Integer r : range) {
        
        if (!possibleValues.contains(data[r][targetIndex])) {
            possibleValues.add(data[r][targetIndex]);
            firstRange = r;
        }
    }
    
    //If this happens, then we have either all YES or all NO!
    //If all nodes are positive OR all nodes are negative.
    if (possibleValues.size() == 1) {
        
        // Get the column index where the parent attribute is located, so we can know what to label our node.
        int lIndex = -1;
        
        for (int i=1; i<data[0].length; i++) {
            
            if (data[0][i].compareTo(n.parent.label) == 0) {
                lIndex = i;
                break;
            }
        }
        
        n.label = data[firstRange][lIndex];
        
        // Since we only have one class value, then we simply 
        n.classValue = data[firstRange][targetIndex];
        
        n.nExamples = range.size();
        
        return;
    }
    
    //If we have expanded all the attributes, then there's nothing else to see.
    if (splittedAttributes.size() == data[0].length-2) {
        
        /* We've gone down through ALL the attributes possible to explore, so we ought to return the single-node tree with the label = target value that occurs most frequently in the examples set*/
        
        int i = 0;
        int setSize = possibleValues.size();
        int [] counter = new int [setSize];
        
        for (String val : possibleValues) {
            
            for (Integer r : range) {
                if (data[r][targetIndex].compareTo(val) == 0) counter[i]+=1;
            }
            i++;
        }
        
        int max = -1;
        
        for (int j=0; j<counter.length; j++) {
            
            if (max < counter[j]) {
                max = counter[j];
                i = j;
            }
        }
        
        // What's the maximum class value for the range we're handling.
        String maxCValue = possibleValues.get(i);
        
        for (Integer r : range) {
            
            if (maxCValue.compareTo(data[r][targetIndex]) == 0) {
                firstRange = r;
                break;
            }
        }
        
        int lIndex = -1;
        
        for (int k=1; k<data[0].length; k++) {
            
            if (data[0][k].compareTo(n.parent.label) == 0) {
                lIndex = k;
                break;
            }
        }
        
        n.label = data[firstRange][lIndex];
        n.classValue = maxCValue;
        n.nExamples = max;
        
        return;
    }
    
    else {
        
        LinkedHashMap<String,Double> q = new LinkedHashMap<String,Double>();
        
        //In which lines am I looking for attributes?
        //for (Integer k : range) System.out.println(k);
        
        for (String s : att) {

            q.put(s,IDMath.entropy(data,range,data[0][data[0].length-1],s));
            //System.out.println(s+" >> "+entropy(data,range,data[0][data[0].length-1],s));
        }

        double bestE = Double.MAX_VALUE;
        String bestKey = null;
        int bestBranchFactor = Integer.MAX_VALUE;

        //All possible values the attribute can assume (ex: for Price [$,$$,$$$])
        LinkedList<String> vis = new LinkedList<String>();
        
        for (Map.Entry<String,Double> entry : q.entrySet()) {
            
            int branchFactor = 0;
            String entryKey = entry.getKey();
            double entryVal = entry.getValue();
            
            int keyIndex = -1;
            // Figuring out which position of the table holds the attribute's values...
            for (int i=1; i<data[0].length; i++)
                if (data[0][i].compareTo(entryKey) == 0) keyIndex = i;
            
            for (int r=1; r<data.length; r++)
                if(!vis.contains(data[r][keyIndex])) {
                    vis.add(data[r][keyIndex]);
                    branchFactor++;
                }
            
            if (entryVal < bestE) {
                bestE = entryVal;
                bestKey = entryKey;
            }
            
            else if (entryVal == bestE && branchFactor < bestBranchFactor) {
                bestKey = entryKey;
                bestBranchFactor = branchFactor;
            }
            
            vis.clear();
            
        }
        
        //vis.clear();
        
        // Figuring out which position of the table holds the attribute's values...
        int attIndex = -1;
        for (int i=1; i<data[0].length; i++)
            if (data[0][i].compareTo(bestKey) == 0) attIndex = i;
        
        //We figured out the attribute to split on (with the least entropy)
        splittedAttributes.add(bestKey);
        //System.out.println(bestKey);
        
        n.label = bestKey;
        
        //*** IMPORTANT QUESTION: Determining all the possible values (Vis) I can have for a given attribute. Should I go over the whole set or just the ones in the range?? YES!
        
        for (int r=1; r<data.length; r++)
            if(!vis.contains(data[r][attIndex])) vis.add(data[r][attIndex]);
        
        LinkedList <LinkedList<Integer>> sets = new LinkedList<LinkedList<Integer>>();
        
        for (String vi : vis) {
            
            //System.out.println(":: "+vi);
            
            LinkedList <Integer> subset = new LinkedList<Integer>();
            
            for (Integer r : range) {
                if (data[r][attIndex].compareTo(vi) == 0) subset.add(r);
                firstRange = r;
            }
            
            //TODO: adicionar um nó folha, trata-se de uma classe maioritária!
            if (subset.size() != 0) sets.add(subset);
            else {
                IDNode l = new IDNode(n, vi, data[firstRange][targetIndex]);
                n.subTrees.add(l);
            }
        }
        
        for (LinkedList <Integer> set : sets) {

            LinkedList <String> attMinusA = new LinkedList<String>();

            for (String s : att) if (s.compareTo(bestKey) != 0) attMinusA.add(s);
            
            IDNode subTree = new IDNode(n, null, null);
            
            ID3(data,target,attMinusA,set,subTree);
            
            if (subTree.subTrees.size() > 0) subTree.arch = data[set.peek()][attIndex];
            
            n.subTrees.add(subTree);
        }
        
    }
}

public static LinkedList <IDNode> treeQueue = new LinkedList<IDNode>();

public static void makeDecision(String example, IDNode n, String [][] data)
{
    
    String info [] = example.split(",");
    
    boolean classFound = false;
    
    if (n.classValue != null) {
        
        System.out.print(info[0]+":.. "+n.classValue);
        classFound = true;
    }
    
    int attIndex = -1;
    
    for (int i=1; i<data[0].length; i++)
    {
        if (n.label.compareTo(data[0][i]) == 0) attIndex = i;
    }
    
    if (n.classValue == null)
    {
    
        for (IDNode k : n.subTrees)
        {
            if (k.label.compareTo(info[attIndex]) == 0 && k.classValue != null)
            {
                System.out.print(info[0]+": "+k.classValue);
                classFound = true;
            }
        }
        
        // If not, we ought to move to the next sub-tree and look up from there.
        if (!classFound)
        {
            for (IDNode k : n.subTrees)
            {
                if (k.classValue == null)
                {
                    if (k.arch != null && k.arch.compareTo(info[attIndex]) == 0)
                    {
                        makeDecision(example, k, data);
                    }
                }
            }
        }
    
    }
}

}