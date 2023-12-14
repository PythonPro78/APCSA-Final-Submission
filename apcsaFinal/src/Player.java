public class Player
{
    public int y, x;
    public int width, height;
    
    private float speed;
    
    private float beforePeak;
    private float beforeGround;
    private float maxHeight;
    
    private float jumpForce;
    private float upGravity;
    private float downGravity;
    
    public boolean right;

    private int prevX, prevY;
    
    private float yVelocity;
    
    private boolean onGround = false;

    // Constructors
    public Player(int x, int y, float speed, float maxHeight, float beforePeak, float beforeGround)
    {
        this.y = y;
        this.x = x;

        prevX = x;
        prevY = y;
        
        width = (int) (18 * Main.PIXEL_SIZE);
        height = (int) (18 * Main.PIXEL_SIZE);

        right = true;
        yVelocity = 0;

        this.speed = speed;
        
        this.maxHeight = maxHeight * Main.PIXEL_SIZE;
        this.beforePeak = beforePeak * Main.FPS;
        this.beforeGround = beforeGround * Main.FPS;
        
        calcJumpPhysics();
    }
    
    public Player(int x, int y, float maxHeight, float beforePeak, float beforeGround)
    {
        this.y = y;
        this.x = x;

        prevX = x;
        prevY = y;
        
        width = (int) (18 * Main.PIXEL_SIZE);
        height = (int) (18 * Main.PIXEL_SIZE);

        right = true;
        yVelocity = 0;

        speed = 2.2f;
        
        this.maxHeight = maxHeight * Main.PIXEL_SIZE;
        this.beforePeak = beforePeak * Main.FPS;
        this.beforeGround = beforeGround * Main.FPS;
        
        calcJumpPhysics();
    }
    
    public Player(int x, int y, float speed)
    {
        this.y = y;
        this.x = x;

        prevX = x;
        prevY = y;
        
        width = (int) (18 * Main.PIXEL_SIZE);
        height = (int) (18 * Main.PIXEL_SIZE);

        right = true;
        yVelocity = 0;

        this.speed = speed;
        
        maxHeight = 30 * Main.PIXEL_SIZE;
        beforePeak = 1f * Main.FPS;
        beforeGround = 0.5f * Main.FPS;
        
        calcJumpPhysics();
    }

    public Player(int x, int y)
    {
        this.y = y;
        this.x = x;

        prevX = x;
        prevY = y;
        
        width = (int) (18 * Main.PIXEL_SIZE);
        height = (int) (18 * Main.PIXEL_SIZE);

        right = true;
        yVelocity = 0;
        
        speed = 2.2f;
        
        maxHeight = 30 * Main.PIXEL_SIZE;
        beforePeak = 1f * Main.FPS;
        beforeGround = 0.5f * Main.FPS;
        
        calcJumpPhysics();
    }

    public Player()
    {
        y = 0;
        x = 0;

        prevX = 0;
        prevY = 0;
        
        width = (int) (18 * Main.PIXEL_SIZE);
        height = (int) (18 * Main.PIXEL_SIZE);

        right = true;
        yVelocity = 0;
        
        speed = 2.2f;
        
        maxHeight = 30 * Main.PIXEL_SIZE;
        beforePeak = 1f * Main.FPS;
        beforeGround = 0.5f * Main.FPS;
        
        calcJumpPhysics();
    }

    // GETTERS
    public float getSpeed()
    {
        return speed;
    }
    
    public float getBeforePeak()
    {
    	return beforePeak;
    }
    
    public float getBeforeGround()
    {
    	return beforeGround;
    }
    
    public float getMaxHeight()
    {
    	return maxHeight;
    }
    
    public float getJumpForce()
    {
    	return jumpForce;
    }
    
    public float getUpGravity()
    {
    	return upGravity;
    }
    
    public float getDownGravity()
    {
    	return downGravity;
    }
    
    public boolean isOnGround()
    {
    	return onGround;
    }

    // SETTERS
    public void setSpeed(float speed)
    {
        this.speed = speed;
    }
    
    public void setBeforePeak(float beforePeak)
    {
        this.beforePeak = beforePeak * Main.FPS;
        calcJumpPhysics();
    }
    
    public void setBeforeGround(float beforeGround)
    {
        this.beforeGround = beforeGround * Main.FPS;
        calcJumpPhysics();
    }
    
    public void setMaxHeight(float maxHeight)
    {
    	this.maxHeight = maxHeight * Main.PIXEL_SIZE;
        calcJumpPhysics();
    }
    
    public void cancelVelocity()
    {
    	yVelocity = 0;
    }

    // OTHER STUFF
    public void movePlayer(int dir)
    {
        right = dir > 0;

        x += speed * dir * Main.PIXEL_SIZE;

        Main.game.applyLookAhead(x - prevX);
    }
    
    public void jump()
    {
    	yVelocity = (int) jumpForce;
    }
    
    public void applyGravity()
    {
    	y -= yVelocity;
    	
    	if (yVelocity > 0)
    	{
    		yVelocity -= upGravity;
    	}
    	else
    	{
    		yVelocity -= downGravity;
    	}
    }
    
    public void start()
    {
        onGround = false;
    }

    public void finish()
    {
        prevX = x;
        prevY = y;
    }

    public boolean collide(int wallX, int wallY, int wallWidth, int wallHeight)
    {
        int[] entitySides = {x, x + width, y, y + height};
        int[] wallSides = {wallX, wallX + wallWidth, wallY, wallY + wallHeight};

        if (entitySides[0] < wallSides[1] && entitySides[1] > wallSides[0] &&
                entitySides[2] < wallSides[3] && entitySides[3] > wallSides[2])
        {
            int deltaX = x - prevX;
            int deltaY = y - prevY;

            int xBarrier;
            int yBarrier;

            int entitySideX;
            int entitySideY;

            // If we are moving right
            if (deltaX > 0)
            {
                // Get x of the left side of the wall
                xBarrier = wallSides[0];

                // Get the right side of the entity
                entitySideX = entitySides[1];
            }
            // If we are moving left
            else
            {
                // Get x of the right side of the wall
                xBarrier = wallSides[1];

                // Get the left side of the entity
                entitySideX = entitySides[0];
            }

            // If we are moving down
            if (deltaY > 0)
            {
                // Get y of the top side of the wall
                yBarrier = wallSides[2];

                // Get the bottom side of the entity
                entitySideY = entitySides[3];
            }
            // If we are moving up
            else
            {
                // Get y of the bottom side of the wall
                yBarrier = wallSides[3];

                // Get the bottom side of the entity
                entitySideY = entitySides[2];
            }

            float xDis = (entitySideX - xBarrier) * 1.1f;
            float yDis = (entitySideY - yBarrier) * 1.1f;

            // If we are not moving vertically, it is a horizontal issue;
            if (deltaY == 0)
            {
                x -= (int) xDis;
            }
            // If we are not moving horizontally, it is a vertical issue;
            else if (deltaX == 0)
            {
                y -= (int) yDis;
                
                if (deltaY > 0)
                {
                	onGround = true;
                }
                
                yVelocity = 0;
            }
            // If we hit the horizontal side first, so move horizontally
            else if (xDis/deltaX < yDis/deltaY)
            {
                x -= (int) xDis;
            }
            // Otherwise, we hit the vertical side first, so move vertically
            else
            {
                y -= (int) yDis;
                
                if (deltaY > 0)
                {
                	onGround = true;
                }
                
                yVelocity = 0;
            }

            return true;
        }

        return false;
    }
    
    public boolean collideDown(int wallX, int wallY, int wallWidth, int wallHeight)
    {
    	if (y - prevY <= 0 || prevY + height > wallY)
    	{
    		return false;
    	}
    	
        int[] entitySides = {x, x + width, y, y + height};
        int[] wallSides = {wallX, wallX + wallWidth, wallY, wallY + wallHeight};

        if (entitySides[0] < wallSides[1] && entitySides[1] > wallSides[0] &&
                entitySides[2] < wallSides[3] && entitySides[3] > wallSides[2])
        {
            int deltaX = x - prevX;
            int deltaY = y - prevY;

            int xBarrier;
            int yBarrier;

            int entitySideX;
            int entitySideY;

            // If we are moving right
            if (deltaX > 0)
            {
                // Get x of the left side of the wall
                xBarrier = wallSides[0];

                // Get the right side of the entity
                entitySideX = entitySides[1];
            }
            // If we are moving left
            else
            {
                // Get x of the right side of the wall
                xBarrier = wallSides[1];

                // Get the left side of the entity
                entitySideX = entitySides[0];
            }

            // We are moving down
            // Get y of the top side of the wall
            yBarrier = wallSides[2];

            // Get the bottom side of the entity
            entitySideY = entitySides[3];

            float xDis = (entitySideX - xBarrier) * 1.1f;
            float yDis = (entitySideY - yBarrier) * 1.1f;

            // If we are not moving horizontally, it is a vertical issue
            if (deltaX == 0)
            {
                y -= (int) yDis;
                
                onGround = true;
                yVelocity = 0;
            }
            // If we hit the vertical side first, it is a vertical issue
            else if (xDis/deltaX > yDis/deltaY)
            {
                y -= (int) yDis;
                
                onGround = true;
                yVelocity = 0;
            }
            // Otherwise, we did not hit down
            else
            {
            	return false;
            }

            return true;
        }

        return false;
    }
    
    private void calcJumpPhysics()
    {
    	jumpForce = beforePeak*2 - 1;
    	jumpForce *= maxHeight;
    	jumpForce /= beforePeak*beforePeak;
    	
    	upGravity = jumpForce;
    	upGravity /= beforePeak - 0.5f;
    	
    	downGravity = beforeGround*2 - 1;
    	downGravity *= maxHeight;
    	downGravity /= beforeGround*beforeGround;
    	downGravity /= beforeGround - 0.5;
    }
}