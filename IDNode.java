import java.util.*;

public class IDNode {
    
  public IDNode parent;
  public String label;
  public String arch;
  public LinkedList <IDNode> subTrees = new LinkedList<IDNode>();
  public String classValue;
  public int nExamples;
  
  IDNode () {
      
      this.parent = null;
      this.label = null;
      this.arch = null;
      this.classValue = null;
      this.nExamples = 0;
  }
  
  IDNode (IDNode parent, String label, String classValue) {
      
      this.parent = parent;
      this.label = label;
      this.classValue = classValue;
  }
  
  IDNode (IDNode parent, String label, LinkedList <IDNode> subTrees, String classValue) {
      
      this.parent = parent;
      this.label = label;
      this.classValue = classValue;
      
      for (IDNode n : subTrees) {
          
          this.subTrees.add(n);
      }
  }
  
}