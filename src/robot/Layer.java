package robot;

import java.util.ArrayList;
import java.util.List;

public class Layer 
{
    public Perceptron[] perceptron;

    public Layer(Perceptron[] perceptron) 
    {
	this.perceptron = perceptron;
    }
	
    public List<Double> sum(List<Double> example) 
    {		   
        List<Double> listSum = new ArrayList<>();
        double s;
        
	for(int j = 0; j < perceptron.length; j++) 
        { 
            s = perceptron[j].sum(example);
            listSum.add(s);
        }	
	
        return listSum;
    }
	    
    public List<Double> delta(List<Double> result) 
    {
	List<Double> listDelta = new ArrayList<>();
        
        for(int j =0; j < result.size(); j++)
        {
            double d = perceptron[j].delta(result.get(j));
            listDelta.add(d);
        }			   
            
        return listDelta;
    }
	   
    public void correctWeigths(List<Double> sum)
    {
        for(int i = 0; i < perceptron.length; i++)
        {
	    perceptron[i].correctWeigths(sum);
        }	   
    }
}