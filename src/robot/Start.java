package robot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Random;
import javax.swing.JFrame;

public class Start 
{
    
    public static void main(String[] args) 
    {
        int[] numberOfPerceptrons = {2, 11, 4, 2};
	Random random = new Random();
	final double constantLearning = 0.1;
		
	Layer[] layer = new Layer[numberOfPerceptrons.length];

	Perceptron [] perceptron = new Perceptron[numberOfPerceptrons[0]];
	for(int j = 0; j < numberOfPerceptrons[0]; j++) 
        {                  
            double [] weights = new double[2];
            for(int i = 0 ; i < weights.length ; i++ ) 
            {
                            
		weights[i] = random.nextDouble();
            }
                        
            double weightForBias = random.nextDouble();
            Perceptron p = new Perceptron(weights, weightForBias, constantLearning);	
            perceptron[j] = p;                     
	}
		
	Layer layer1 = new Layer(perceptron);
	layer[0] = layer1;
		
	for(int k = 1; k < numberOfPerceptrons.length; k++) 
        {		    
            Perceptron[] perceptron2 = new Perceptron[numberOfPerceptrons[k]];
            for(int j = 0; j < numberOfPerceptrons[k]; j++)
            {
		
                double[] weights = new double[numberOfPerceptrons[k-1]];
		for(int i = 0; i < weights.length; i++) 
                {                 
                    weights[i] = random.nextDouble();
		}
		
                double weightForBias = random.nextDouble();
		Perceptron p = new Perceptron(weights, weightForBias, constantLearning);	
		perceptron2[j] = p;
            }
			
            Layer layer2 = new Layer(perceptron2);
            layer[k] = layer2;
		
	} 
		
	final Examples examples = new Examples(layer);
	examples.learning();
        System.out.println("Gotowy");
	GUI(examples);
    }
	
    public static void GUI(final Examples examples) 
    {           
        JFrame frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
	final Line line = new Line();
	line.addMouseMotionListener(new MouseMotionAdapter() 
        {
            @Override
	    public void mouseMoved(MouseEvent evt) 
            {                  
	        SingleExample t = examples.findResult(evt.getX(), evt.getY());
	        Point firstArmPoint = examples.getFirstArmPoint(t.alfa);
	        Point secondArmPoint = examples.getSecondArmPoint(t.alfa, t.beta);
                
	        line.clearLines();
	        line.addLine(30, 150, firstArmPoint.x, firstArmPoint.y, secondArmPoint.x, secondArmPoint.y);
	    }      
	});
	    
	line.setPreferredSize(new Dimension(350, 300));
	frame.getContentPane().add(line, BorderLayout.CENTER);
	frame.pack();
	frame.setVisible(true);
    }
}
