package robot;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Examples 
{
    public static final int N = 10000; 
    public static final int BREAK = 100000000;
    public static final int INTERVAL = 10000;
    public int armLength = 75;
    
    Random rand = new Random();
    public Layer layer[];
    
    public Examples(Layer[] layer) 
    {
    	this.layer = layer;
    }
    
    public Point countXY(Point poinXY, Point centerPoint, double alfa) 
    {
	double rad = alfa * (Math.PI / 180);
        double cosRad = Math.cos(rad);
        double sinRad = Math.sin(rad);
        
        int x = (int) (cosRad * (poinXY.x - centerPoint.x) - sinRad *
                (poinXY.y - centerPoint.y) + centerPoint.x);
        int y = (int) (sinRad * (poinXY.x - centerPoint.x) + cosRad *
        	(poinXY.y - centerPoint.y) + centerPoint.y);
        Point point = new Point(x,y);
        
        return point;
    } 
	
    public Point getSecondArmPoint(double alfa, double beta) 
    {		
	beta = -180 + beta ;
        Point center = new Point(30, 150); 
	
        Point firstArm = countXY(new Point(30, 150 - armLength), center, alfa);
        Point tempPoint = countXY(new Point(30, 150 - 2 * armLength), center, alfa); 
	Point secondPoint = countXY(new Point(tempPoint.x, tempPoint.y),
                                        new Point(firstArm.x, firstArm.y), beta);
	
        return secondPoint;
    } 
    	
    public Point getFirstArmPoint(double alfa) 
    {
	Point center = new Point(30, 150);
	Point newPoint = countXY(new Point(30, 150 - armLength), center, alfa);
	
        return newPoint;
    }
	
    public void learning() 
    {    
	double[][] listOfExamples = loadFileToArray("test.txt");	  	 
	int r = 1;
	double error = 0;
        int k = 0;
        
	while(true) 
        {
            r = rand.nextInt(N - 1) + 0;
            SingleExample singleExample = new SingleExample(listOfExamples[r][0],listOfExamples[r][1],
                                                            listOfExamples[r][2],listOfExamples[r][3]);
            countThrough(singleExample);	
            countBack(singleExample);	
            correctWeigths(singleExample);
				
            if (k % INTERVAL == 0)
            {
		error = countError(listOfExamples);
                System.out.println("Blad: " + error);
                if(error < 25) break;
            }

            if(k > BREAK) break;	
            k++;
        }  
    }
   
    public void countThrough(SingleExample singleExample) 
    {
	List<Double> list = new ArrayList<>();
	list.add(singleExample.x);
	list.add(singleExample.y);
		
        for(int i = 0; i < layer.length; i++) 
        {
            list = layer[i].sum(list);
	}
    }
	
    public void countBack(SingleExample singleExample) 
    {
	List<Double> list = new ArrayList<>();
	list.add(singleExample.alfa);
	list.add(singleExample.beta);
	layer[layer.length-1].delta(list);		
		
	for(int i = layer.length-2; i >= 0; i--)
        {
            for(int j =0; j < layer[i].perceptron.length; j++)
            {
		for (int k = 0; k < layer[i+1].perceptron.length; k++) 
                {
                    layer[i].perceptron[j].delta += layer[i+1].perceptron[k].delta *
                                                    layer[i+1].perceptron[k].weigths[j];
		}
		layer[i].perceptron[j].delta *= layer[i].perceptron[j].sum *
						(1 - layer[i].perceptron[j].sum);
            }
	}
    }
	
    public void correctWeigths(SingleExample singleExample) 
    {
        List<Double> listSum = new ArrayList<>();
        listSum.add(singleExample.x);
        listSum.add(singleExample.y);

	for(int i = 0; i < layer.length; i++) 
        {
            layer[i].correctWeigths(listSum);
            listSum.clear();
            
            for(int j = 0; j < layer[i].perceptron.length; j++)
            {
		listSum.add(layer[i].perceptron[j].sum);
            }
	}		
    }
	
    public SingleExample findResult(int x, int y) 
    {
	double tx = ((double) x / 150) * 0.8 + 0.1;
	double ty = ((double) y / 300) * 0.8 + 0.1;
		
	SingleExample test = new SingleExample();
	test.x = tx;
	test.y = ty;	

	countThrough(test);
	double alfa = layer[layer.length-1].perceptron[0].sum;
	double beta =  layer[layer.length-1].perceptron[1].sum;
		
	alfa=(alfa - 0.1) / 0.8 * 180;
	beta=(beta - 0.1) / 0.8 * 180;
	test.alfa = alfa;
	test.beta = beta;
        
	return test;
    }
	
    private double countError(double[][] examples) 
    {
        double error = 0;
        
        for (int n = 0; n < examples.length; n++)  
        {
            SingleExample singleExample = new SingleExample(examples[n][0], examples[n][1],
            examples[n][2], examples[n][3]);
            error += countSingleExampleError(singleExample);
        }
        
        return error * 0.5;
    }

    public double countSingleExampleError(SingleExample singleExample) 
    {
        double result = 0;
        double[] t = new double[] {singleExample.alfa, singleExample.beta};
        
        countThrough(singleExample);
        
        for (int k = 0; k < layer[layer.length-1].perceptron.length; k++)  
        {
              result += (Math.pow(layer[layer.length-1].perceptron[k].sum - t[k], 2));
        }
        
        return result;
    }
	
    public double[][] loadFileToArray(String name) 
    {
	int i = 0;

	File file = new File(name);
	try {
            Scanner in = new Scanner(file);
            
            while(in.hasNextLine()) 
            {
                String line = in.nextLine();
                i++;
            }
			
            in.close();
            
            Scanner in2 = new Scanner(file);
            double [][] example = new double[i][4];
            int c=0;
			
            while(in2.hasNextLine()) 
            {
                String line = in2.nextLine();	
                String[] s = line.split(" ");
                
		for(int j=0 ; j<4 ; j++) 
                {
                    example[c][j] = Double.parseDouble(s[j]);
		}
                
		c++;
            }
	
            in2.close();
			
	    return example;
            
        } catch (FileNotFoundException e) {
		e.printStackTrace();
		return null;
            }
    }
}
