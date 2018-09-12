import java.util.*;

public class IDMath {
  
  public static double entropy(String [][] dataset, LinkedList<Integer> range, String colClass, String a) {
    
    double e = 0.0;
    
    int cIndex = -1;
    int aIndex = -1;
    
    for (int i=0; i<dataset[0].length; i++) {
      
      if (dataset[0][i].compareTo(colClass) == 0) cIndex = i;
      if (dataset[0][i].compareTo(a) == 0) aIndex = i;
    }
    
    LinkedList <String> aVals = new LinkedList<String>();
    LinkedList <String> cVals = new LinkedList<String>();
    
    for (Integer j : range)  {
      
      if (!aVals.contains(dataset[j][aIndex])) aVals.add(dataset[j][aIndex]);
      if (!cVals.contains(dataset[j][cIndex])) cVals.add(dataset[j][cIndex]);
    }
    
    int [][] eTable = new int [aVals.size()+1][cVals.size()+1];
    
    int x = 0;  
    
    for (String av : aVals) {
      
      int y = 0;
      
      for (String cv : cVals) {
        
        for (Integer i : range) {
          
          if (dataset[i][aIndex].compareTo(av) == 0 && dataset[i][cIndex].compareTo(cv) == 0) eTable[x][y]++;
          
        }
        
        y++;
      }
      
      x++;
    }
    
    for(int k=0; k<eTable.length-1; k++) {
      for (int j=0; j<eTable[k].length-1; j++) {
        eTable[k][eTable[k].length-1] += eTable[k][j];
        eTable[eTable.length-1][j] += eTable[k][j];
      }
      eTable[eTable.length-1][eTable[k].length-1] += eTable[k][eTable[k].length-1];
      
    }
    
    double [] prob = new double [eTable.length];
    double [] entr = new double [eTable.length];
    
    for (int i=0; i<prob.length-1; i++) {
      
      prob[i] = (double)eTable[i][eTable[i].length-1]/(double)eTable[eTable.length-1][eTable[i].length-1];
      
      for (int j=0; j<eTable[i].length-1; j++) {
        
        double temp = (double)eTable[i][j]/(double)eTable[i][eTable[i].length-1];
        
        //Careful! Domain of f(x) = log(x) function is all positive real numbers, the else "handles" NaNs.
        if (temp != 0) entr[i] += temp*(Math.log(temp)/Math.log(2));
        else entr[i] = 0;
      }
      
      entr[i]*=-1;
      
      e += prob[i]*entr[i];
      
    }
    
    return e;
  }
  
  public static void discretize(String [][] data)
  {
    
    LinkedList<Integer> attToDiscretize = new LinkedList<Integer>();
    
    for (int i=1; i<data[1].length; i++)
    {
      if(IDUtils.isNumeric(data[1][i]) != Integer.MIN_VALUE)
      {
        
        attToDiscretize.add(i);
      }
    }
    
    for (Integer j : attToDiscretize)
    {
      
      float vals [] = new float [data.length];
      
      for (int i=1; i<data.length; i++)
      {
        
        vals[i] = IDUtils.isNumeric(data[i][j]);
      }
      
      float valMax = getMax(vals);
      float valMin = getMin(vals);
      
      float amp = valMax - valMin;
      
      float step = (float)Math.log(amp);
      float next = valMin;
      
      int nIntervals = 0;
      
      while (next < valMax)
      {
        nIntervals++;
        next += step;
      }
      
      next = valMin;
      
      String [] newClasses = new String [nIntervals];
      
      for (int l=0; l<newClasses.length; l++) {
        
        double temp = next+step;
        String n = String.format("%.2f",next);
        String t = String.format("%.2f",temp);
        newClasses[l] ="["+n+"-"+t+"[";
        next += step;
      }
      
      for (int k=1; k<data.length; k++)
      {
        next = valMin;
        for (int l=0; l<newClasses.length; l++)
        {    
          double cValue = IDUtils.isNumeric(data[k][j]);
          
          // If we still didn't get to the last discretized attribute value.
          if (l<newClasses.length-1)
          {
            if(cValue >= next && cValue < next+step) {
              data[k][j] = newClasses[l];
              //System.out.println(newClasses[l]);
            }
          }
          
          // If we got to the last attribute value, then we check to see if the value "fits" in there.
          else
          {
            if(cValue > next && cValue <= next+step) {
              data[k][j] = newClasses[l];
            }
          }
          
          next += step;
        }
        
      }
    }
  }
  
  public static float getMin(float [] vals) {
    
    float min = Integer.MAX_VALUE;
    
    for (int i=1; i<vals.length; i++)
    {
      
      if (vals[i] < min) min = vals[i];
    }
    
    return min;
  }
  
  public static float getMax(float [] vals) {
    
    float max = Integer.MIN_VALUE;
    
    for (int i=1; i<vals.length; i++)
    {
      
      if (vals[i] > max) max = vals[i];
    }
    
    return max;
  }
  
}