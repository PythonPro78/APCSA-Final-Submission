import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Label extends MenuItem
{
    private String txt;
    private Font font;

    private Color fillColor;

    // CONSTRUCTORS
    public Label(String id, String txt, float x, float y, float width, float height,  Font font, Color fillColor)
    {
    	if (duplicateId(id))
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	this.id = id;
    	
        this.txt = txt;
        this.font = font;
        this.fillColor = fillColor;
        
        this.x = percentConvertX(x);
        this.y = percentConvertY(y);
        this.width = percentConvertX(width);
        this.height = percentConvertY(height);
        
//        center();
    }
    
    public Label(String id, String txt, float x, float y, float width, float height)
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
    }
    
    public Label(String id, float x, float y, float width, float height)
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
    }
    
    public Label(String id, String txt, Font font, Color fillColor)
    {
    	if (duplicateId(id))
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	this.id = id;
    	
        this.txt = txt;
        this.font = font;
        this.fillColor = fillColor;
    }
    
    public Label(String id, String txt)
    {
    	if (duplicateId(id))
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	this.id = id;
    	
        this.txt = txt;
        font = new Font("arial", 1, 15);
        fillColor = new Color(0, 0, 0, 0);
    }
    
    public Label(String id)
    {
    	if (duplicateId(id))
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	this.id = id;
    	
        txt = "";
        font = new Font("arial", 1, 15);
        fillColor = new Color(0, 0, 0, 0);
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
    
    // BRAIN METHODS
    @Override
    public void draw(Graphics2D g2D)
    {
    	Rectangle2D bounds = font.getStringBounds(txt, g2D.getFontRenderContext());
    	
    	g2D.setColor(fillColor);
    	g2D.fillRect(x, y, width, height);
    	
    	g2D.setColor(Color.BLACK);
    	g2D.setFont(font);
    	g2D.drawString( txt, (int) (x + width/2 - bounds.getCenterX()), (int) (y + height/2 - bounds.getCenterY()) );
    }
}