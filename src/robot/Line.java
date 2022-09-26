package robot;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JComponent;

public class Line extends JComponent
{
    private static final long serialVersionUID = 1L;

    private static class Line2 
    {
	final int x1; 
	final int y1;
	final int x2;
	final int y2;   

	public Line2(int x1, int y1, int x2, int y2) 
        {
	    this.x1 = x1;
	    this.y1 = y1;
	    this.x2 = x2;
	    this.y2 = y2;
	}               
    }

    private final ArrayList<Line2> lines = new ArrayList<>();

    public void addLine(int x1, int x2, int x3, int x4, int x5, int x6) 
    {	
	lines.add(new Line2(x1,x2,x3,x4));       
	lines.add(new Line2(x3,x4,x5,x6));
	repaint();
    }

    public void clearLines() 
    {
	lines.clear();
	repaint();
    }

    @Override
    public void paint(Graphics g) 
    {
	for (int i = 0; i < lines.size(); i++) 
        {
            Line2 line = lines.get(i);
            
            g.setColor(Color.RED);
	    g.drawLine(line.x1, line.y1, line.x2, line.y2);
	}
    }
}
