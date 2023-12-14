import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Image extends MenuItem
{
	private BufferedImage img;
	
	// CONSTRUCTORS
	public Image(String id, BufferedImage img, float x, float y, float width, float height)
    {
		if (duplicateId(id))
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	this.id = id;
		
        this.img = img;
        
        this.x = percentConvertX(x);
        this.y = percentConvertY(y);
        this.width = percentConvertX(width);
        this.height = percentConvertY(height);
        
        center();
    }
	
	public Image(String id, float x, float y, float width, float height)
    {
		if (duplicateId(id))
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	this.id = id;
		
        img = null;
        
        this.x = percentConvertX(x);
        this.y = percentConvertY(y);
        this.width = percentConvertX(width);
        this.height = percentConvertY(height);
        
        center();
    }
	
	public Image(String id, BufferedImage img)
    {
		if (duplicateId(id))
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	this.id = id;
		
        this.img = img;
    }
	
    public Image(String id)
    {
    	if (duplicateId(id))
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	this.id = id;
    	
        img = null;
    }
    
    // GETTERS
    public BufferedImage getImage()
    {
    	return img;
    }
    
    // SETTERS
    public void setImage(BufferedImage img)
    {
    	this.img = img;
    }
    
    // BRAIN METHODS
    @Override
    public void draw(Graphics2D g2D)
    {
    	g2D.drawImage(img, x, y, width, height, Main.game);
    }
}