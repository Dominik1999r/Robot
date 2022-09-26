package robot;

import java.util.List;

public class Perceptron 
{
    public double [] weigths;
    public double constLearning;
    public double sum;
    public double delta;
    public double weightForBias;
   
    public Perceptron(double[] weigths, double weightForBias,  double constLearning) 
    {
	this.weigths = weigths;
	this.weightForBias = weightForBias;
	this.constLearning = constLearning;
    }
    
    public Perceptron() {}	
    
    public double sigmoida(double x) 
    {
        double r;
        
	r = (1 / (1 + Math.pow(Math.E, -x)));
        
	return r;
    }
    
    public Double sum(List<Double> example) 
    {	
    	double out = 0;
    	double s = 0 ;
        
 	for(int j = 0; j < weigths.length; j++) 
        { 	 
            out += example.get(j)*weigths[j];
 	}	

 	out += weightForBias;
	s = sigmoida(out);
 	this.sum = s;
 		
 	return s;
   }
 
    public double delta(double result) 
    {
 	double d = (sum - result) * sum * (1 - sum);	
        
 	this.delta = d;
         
 	return d;
    }
    
    public void correctWeigths(List<Double> sum) 
    {
    	for(int i = 0; i < weigths.length; i++) weigths[i] = weigths[i] - constLearning * delta * sum.get(i);	
    	weightForBias = weightForBias - constLearning * delta * 1;
        
    }
}
