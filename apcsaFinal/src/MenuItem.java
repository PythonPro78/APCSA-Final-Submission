import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

abstract class MenuItem
{
	private static ArrayList<String> allIds = new ArrayList<String>();
	
    protected int x, y;
    protected int width, height;
    
    protected String id;
    
    // GETTERS
    public String getId()
    {
    	return id;
    }
    
    public int getX()
    {
    	return x;
    }
    
    public int getY()
    {
    	return y;
    }
    
    public int getWidth()
    {
    	return width;
    }
    
    public int getHeight()
    {
    	return height;
    }
    
    // SETTERS
    public void setPosition(float x, float y)
    {
    	this.x = percentConvertX(x);
    	this.y = percentConvertY(y);
    	
    	center();
    }
    
    public void setSize(float width, float height)
    {
    	x += this.width/2;
    	y += this.height/2;
    	
    	this.width = percentConvertX(width);
    	this.height = percentConvertY(height);
    	
    	center();
    }
    
    // BRAIN METHODS
    protected int percentConvertX(float num)
    {
    	return (int) (num * Main.SCREEN_SIZE.width);
    }
    
    protected int percentConvertY(float num)
    {
    	return (int) (num * Main.SCREEN_SIZE.height);
    }

    public void draw(Graphics2D g2D)
    {
    	g2D.setColor(Color.BLUE);
    	g2D.fillRect(x, y, width, height);
    }
    
    protected void center()
    {
    	x -= width/2;
    	y -= height/2;
    }
    
    protected static boolean duplicateId(String id)
    {
    	for (int i = 0; i < allIds.size(); i ++)
    	{
    		if (id == allIds.get(i))
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
}
