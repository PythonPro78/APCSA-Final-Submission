public class Camera
{
    public int x, y;

    private int maxLookAhead;
    private int speed;
    private int deadZone;

    private int currentLookAhead;
    private int deadZonePos;
    
    private int minX, maxX, minY, maxY;
    private boolean boundCamera;

    // Constructors
    public Camera(int speed, int lookAhead, int deadZone, int minX, int maxX, int minY, int maxY)
    {
        x = 0;
        y = 0;

        this.maxLookAhead = lookAhead;
        this.speed = speed;
        
        this.deadZone = deadZone;

        currentLookAhead = 0;
        deadZonePos = 0;
        
        boundCamera = true;
        
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }
    
    public Camera(int minX, int maxX, int minY, int maxY)
    {
    	x = 0;
        y = 0;

        maxLookAhead = 0;
        speed = 1;
        
        deadZone = 0;

        currentLookAhead = 0;
        deadZonePos = 0;
        
        boundCamera = true;
        
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }
    
    public Camera(int speed, int lookAhead, int deadZone)
    {
        x = 0;
        y = 0;

        this.maxLookAhead = lookAhead;
        this.speed = speed;
        
        this.deadZone = deadZone;

        currentLookAhead = 0;
        deadZonePos = 0;
        
        boundCamera = false;
    }
    
    public Camera(int speed, int lookAhead)
    {
        x = 0;
        y = 0;

        this.maxLookAhead = lookAhead;
        this.speed = speed;
        
        deadZone = 0;

        currentLookAhead = 0;
        deadZonePos = 0;
        
        boundCamera = false;
    }

    public Camera()
    {
        x = 0;
        y = 0;

        maxLookAhead = 0;
        speed = 1;
        
        deadZone = 0;

        currentLookAhead = 0;
        deadZonePos = 0;
        
        boundCamera = false;
    }

    // GETTERS
    public int getMaxLookAhead()
    {
        return maxLookAhead;
    }

    public int getSpeed()
    {
        return speed;
    }

    public int getDeadZone()
    {
        return deadZone;
    }
    
    public int getMinX()
    {
    	return minX;
    }
    
    public int getMaxX()
    {
    	return maxX;
    }
    
    public int getMinY()
    {
    	return minY;
    }
    
    public int getMaxY()
    {
    	return maxY;
    }
    
    // SETTERS
    public void setMaxLookAhead(int lookAhead)
    {
        this.maxLookAhead = lookAhead;
    }

    public void setSpeed(int speed)
    {
        this.speed = speed;
    }

    public void setDeadZone(int deadZone)
    {
        this.deadZone = deadZone;
    }

    public void alignLookAhead()
    {
        currentLookAhead = 0;
    }
    
    public void setBound(int minX, int maxX, int minY, int maxY)
    {
    	this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        
        boundCamera = true;
    }
    
    public void setBound(boolean boundCamera)
    {
    	this.boundCamera = boundCamera;
    }

    // EVERYTHING ELSE (idk why it's all caps)
    public void moveCenter(int subjectX, int subjectY, int subjectWidth, int subjectHeight)
    {
        x = subjectX - Main.SCREEN_SIZE.width/2 + subjectWidth/2;
        y = Main.SCREEN_SIZE.height/2 - subjectHeight/2;;
        
        if (boundCamera)
        {
        	bound();
        }
    }
    
    public void moveLookAhead(int subjectX, int subjectY, int subjectWidth, int subjectHeight, boolean onGround)
    {
        x = (subjectX - Main.SCREEN_SIZE.width/2 + subjectWidth/2) + currentLookAhead;
        
        if (onGround)
        {
        	y = Main.SCREEN_SIZE.height/2 - subjectHeight/2;
        }
        
        if (boundCamera)
        {
        	bound();
        }
    }

    public void applyLookAhead(int deltaX, boolean giveMyBroAMinute)
    {
        if (giveMyBroAMinute)
        {
            deadZonePos += deltaX;

            if (deadZonePos > deadZone)
            {
                deadZonePos = deadZone;
            }
            else if (deadZonePos < -deadZone)
            {
                deadZonePos = -deadZone;
            }

            // If we are in the dead zone, just chill
            if ((deltaX > 0 && deadZonePos < deadZone) || (deltaX < 0 && deadZonePos > -deadZone))
            {
                return;
            }
        }

        currentLookAhead += deltaX * speed;

        if (currentLookAhead > maxLookAhead)
        {
            currentLookAhead = maxLookAhead;
        }
        else if (currentLookAhead < -maxLookAhead)
        {
            currentLookAhead = -maxLookAhead;
        }
    }

    public void moveAdv(int subjectX, int subjectY, int subjectWidth, int subjectHeight, boolean right)
    {
        x += Main.SCREEN_SIZE.width/2 - subjectWidth/2;

        subjectX += right ? maxLookAhead : -maxLookAhead;

        if (x < subjectX && right)
        {
            x += speed;

            if (x > subjectX)
            {
                x = subjectX;
            }
        }
        else if (x > subjectX && !right)
        {
            x -= speed;

            if (x < subjectX)
            {
                x = subjectX;
            }
        }

        x -= Main.SCREEN_SIZE.width/2 - subjectWidth/2;

        y = Main.SCREEN_SIZE.height/2 - subjectHeight/2;
        
        if (boundCamera)
        {
        	bound();
        }
    }
    
    public void bound()
    {
    	if (x < minX)
    	{
    		x = minX;
    	}
    	else if (x > maxX - Main.SCREEN_SIZE.width)
    	{
    		x = maxX - Main.SCREEN_SIZE.width;
    	}
    	
    	if (y < minY)
    	{
    		y = minY;
    	}
    	else if (y > maxY - Main.SCREEN_SIZE.height)
    	{
    		y = maxY - Main.SCREEN_SIZE.height;
    	}
    	
    }
}