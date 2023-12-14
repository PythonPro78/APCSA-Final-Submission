import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

interface ClickAction
{
	public void action();
}

public class Button extends MenuItem
{
	private static ArrayList<Button> buttons = new ArrayList<Button>();
	
    private String txt;
    private Font font;

    private Color fillColor;
    private Color highlightColor;
    
    private ClickAction actionObj;
    
    private boolean isOver;
    
    private boolean isActive;

    // CONSTRUCTORS
    public Button(String id, String txt, float x, float y, float width, float height, Font font, Color fillColor, Color highlightColor)
    {
    	if (duplicateId(id))
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	this.id = id;
    	
        this.txt = txt;
        this.font = font;
        this.fillColor = fillColor;
        this.highlightColor = highlightColor;
        
        this.x = percentConvertX(x);
        this.y = percentConvertY(y);
        this.width = percentConvertX(width);
        this.height = percentConvertY(height);
        
//        center();
        
        isOver = false;
        isActive = false;
        
        buttons.add(this);
    }
    
    public Button(String id, String txt, float x, float y, float width, float height)
    {
    	if (duplicateId(id))
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	this.id = id;
    	
        this.txt = txt;
        font = new Font("arial", 1, 15);
        fillColor = new Color(0, 0, 0, 0);
        
        this.x = percentConvertX(x);
        this.y = percentConvertY(y);
        this.width = percentConvertX(width);
        this.height = percentConvertY(height);
        
//        center();
        
        isOver = false;
        isActive = false;
        
        buttons.add(this);
    }
    
    public Button(String id, float x, float y, float width, float height)
    {
    	if (duplicateId(id))
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	this.id = id;
    	
        txt = "";
        font = new Font("arial", 1, 15);
        fillColor = new Color(0, 0, 0, 0);
        
        this.x = percentConvertX(x);
        this.y = percentConvertY(y);
        this.width = percentConvertX(width);
        this.height = percentConvertY(height);
        
//        center();

        isOver = false;
        isActive = false;
        
        buttons.add(this);
    }
    
    public Button(String id, String txt, Font font, Color fillColor, Color highlightColor)
    {
    	if (duplicateId(id))
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	this.id = id;
    	
        this.txt = txt;
        this.font = font;
        this.fillColor = fillColor;
        this.highlightColor = highlightColor;
        
        isOver = false;
        isActive = false;
        
        buttons.add(this);
    }
    
    public Button(String id, String txt)
    {
    	if (duplicateId(id))
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	this.id = id;
    	
        this.txt = txt;
        font = new Font("arial", 1, 15);
        fillColor = new Color(0, 0, 0, 0);
        
        isOver = false;
        isActive = false;
        
        buttons.add(this);
    }
    
    public Button(String id)
    {
    	if (duplicateId(id))
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	this.id = id;
    	
        txt = "";
        font = new Font("arial", 1, 15);
        fillColor = new Color(0, 0, 0, 0);
        
        isOver = false;
        isActive = false;
        
        buttons.add(this);
    }
    
    // GETTERS
    public String getText()
    {
    	return txt;
    }
    
    public Font getFont()
    {
    	return font;
    }
    
    public Color getFillColor()
    {
    	return fillColor;
    }
    
    public boolean isActive()
    {
    	return isActive;
    }
    
    // SETTERS
    public void setText(String txt)
    {
    	this.txt = txt;
    }
    
    public void setFont(Font font)
    {
    	this.font = font;
    }
    
    public void setFillColor(Color fillColor)
    {
    	this.fillColor = fillColor;
    }
    
    public void setOnClick(ClickAction obj) 
    {
    	actionObj = obj;
    }
    
    public void setActive(boolean isActive)
    {
    	this.isActive = isActive;
    }
    
    // BRAIN METHODS
    public void onClick()
    {
    	if (actionObj != null)
    	{
    		actionObj.action();
    	}
    }
    
    public static void click(int x, int y)
    {
    	Button current;
    	
    	for (int i = 0; i < buttons.size(); i ++)
    	{
    		current = buttons.get(i);
    		
    		if (current.isActive &&
    			x > current.getX() && x < current.getX() + current.getWidth() &&
    			y > current.getY() && y < current.getY() + current.getHeight()) 
    		{
    			current.onClick();
    		}
    	}
    }
    
    public static void mouseOver(int x, int y)
    {
    	Button current;
    	
    	for (int i = 0; i < buttons.size(); i ++)
    	{
    		current = buttons.get(i);
    		
    		if (current.isActive &&
    			x > current.getX() && x < current.getX() + current.getWidth() &&
    			y > current.getY() && y < current.getY() + current.getHeight()) 
    		{
    			current.isOver = true;
    		}
    		else
    		{
    			current.isOver = false;
    		}
    	}
    }
    
    @Override
    public void draw(Graphics2D g2D)
    {
    	Rectangle2D bounds = font.getStringBounds(txt, g2D.getFontRenderContext());
    	
    	g2D.setColor(isOver ? highlightColor : fillColor);
    	g2D.fillRect(x, y, width, height);
    	
    	g2D.setColor(Color.BLACK);
    	g2D.setFont(font);
    	g2D.drawString( txt, (int) (x + width/2 - bounds.getCenterX()), (int) (y + height/2 - bounds.getCenterY()) );
    }
    
    // TO STRING
    public String toString()
    {
    	return String.format("x:%d, y:%d, width:%d, height:%d", x, y, width, height);
    }
}