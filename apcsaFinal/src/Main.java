import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Main extends JPanel implements KeyListener, MouseListener, Runnable
{
	private static final long serialVersionUID = 1146877795903425749L;

	public static Main game;

    public final static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public final static int CELLS_ON_SCREEN = 20;
    public final static int CELL_SIZE = SCREEN_SIZE.width/CELLS_ON_SCREEN;
    public final static float PIXEL_SIZE = CELL_SIZE/16f;
    public final static int FPS = 30;
    public final static int TIME_BETWEEN_FRAMES = 1000/FPS;

    private final static String CHAR_TO_INT = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789&$";
    private final static String IMAGE_PATH = "src/SwampTileset.png";
    private final static String MENU_DATA_PATH = "src/MenusUI.json";

    private final static JFrame frame = new JFrame();
    private static ArrayList<BufferedImage> tileSets = new ArrayList<BufferedImage>();
    private boolean[] keys = new boolean[256];
    private BitSet level;
    private int height, width;
    private int mouseX, mouseY;
    
    private boolean inMenu = true;
    private String activeMenu = "startMenu";
    private boolean win = false;

    private Player player = new Player(50, 50, 2.5f, 35, 0.5f, 0.4f);
    private Camera camera = new Camera(1, 100, 100);
    
    private static HashMap<String, Menu> menus = new HashMap<String, Menu>();
    
    Thread thread;

    public static void main(String[] args)
    {
    	// Store the tile sets in memory
        try {tileSets.add(  ImageIO.read(new File(IMAGE_PATH))  );}
        catch (IOException e) {System.out.println("You dun goofed the path name");}
        
        // Load the menu data (button placement and whatnot)
        try {loadMenuData();}
        catch (Exception e) {System.out.println("Something broke");}
        
        setButtonClickAction("startMenu", "startButton", new ClickAction()
        {
			@Override
			public void action()
			{
				Main.game.setActiveMenu("levelMenu");
			}
        });
        
        setButtonClickAction("startMenu", "quitGameButton", new ClickAction()
        {
			@Override
			public void action()
			{
				// Close the game
				Main.frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
        });
        
        setButtonClickAction("levelMenu", "backButton", new ClickAction()
        {
			@Override
			public void action()
			{
				Main.game.setActiveMenu("startMenu");
			}
        });
        
        setButtonClickAction("levelMenu", "levelButton1", new ClickAction()
        {
			@Override
			public void action()
			{
				Main.game.deactiveMenu();
				
				Main.game.inMenu = false;
				Main.game.resetLevel();
				
				try {Main.game.loadLevel(0);}
		        catch (IOException e) {System.out.println("crap");}
			}
        });
        
        setButtonClickAction("levelMenu", "levelButton2", new ClickAction()
        {
			@Override
			public void action()
			{
				Main.game.deactiveMenu();
				
				Main.game.inMenu = false;
				Main.game.resetLevel();
				
				try {Main.game.loadLevel(1);}
		        catch (IOException e) {System.out.println("crap");}
			}
        });
        
        setButtonClickAction("levelMenu", "levelButton3", new ClickAction()
        {
			@Override
			public void action()
			{
				Main.game.deactiveMenu();
				
				Main.game.inMenu = false;
				Main.game.resetLevel();
				
				try {Main.game.loadLevel(2);}
		        catch (IOException e) {System.out.println("crap");}
			}
        });
        
        setButtonClickAction("levelMenu", "levelButton4", new ClickAction()
        {
			@Override
			public void action()
			{
				Main.game.deactiveMenu();
				
				Main.game.inMenu = false;
				Main.game.resetLevel();
				
				try {Main.game.loadLevel(3);}
		        catch (IOException e) {System.out.println("crap");}
			}
        });
        
        setButtonClickAction("levelMenu", "levelButton5", new ClickAction()
        {
			@Override
			public void action()
			{
				Main.game.deactiveMenu();
				
				Main.game.inMenu = false;
				Main.game.resetLevel();
				
				try {Main.game.loadLevel(4);}
		        catch (IOException e) {System.out.println("crap");}
			}
        });
        
        setButtonClickAction("pauseMenu", "resumePauseButton", new ClickAction()
        {
			@Override
			public void action()
			{
				Main.game.inMenu = false;
			}
        });
        
        setButtonClickAction("pauseMenu", "restartPauseButton", new ClickAction()
        {
			@Override
			public void action()
			{
				Main.game.resetLevel();
				Main.game.inMenu = false;
			}
        });
        
        setButtonClickAction("pauseMenu", "quitPauseButton", new ClickAction()
        {
			@Override
			public void action()
			{
				Main.game.setActiveMenu("levelMenu"); 
				Main.game.inMenu = true;
			}
        });
        
        setButtonClickAction("defeatMenu", "restartDefeatButton", new ClickAction()
        {
			@Override
			public void action()
			{
				Main.game.resetLevel();
				Main.game.inMenu = false;
			}
        });
        
        setButtonClickAction("defeatMenu", "quitDefeatButton", new ClickAction()
        {
			@Override
			public void action()
			{
				Main.game.setActiveMenu("levelMenu"); 
				Main.game.inMenu = true;
			}
        });

        game = new Main();
    }

    public Main()
    {
    	// Configure the frame
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(SCREEN_SIZE);
        frame.setLocation(0, 0);
        
        // Add the JPanel
        frame.add(this);
        
        frame.addMouseListener(this);
        frame.addKeyListener(this);

        camera.x = player.x;
        camera.y = player.y;
        
        // Create and start a thread
        thread = new Thread(this);
        thread.start();
    }
    
    private static void setButtonClickAction(String menuId, String buttonId, ClickAction action)
    {
    	((Button) menus.get(menuId).getItem(buttonId)).setOnClick(action);
    }
    
    public static void loadMenuData() throws FileNotFoundException, IOException, ParseException
    {
    	// Store the menu data in memory
    	JSONParser parser = new JSONParser();
    	JSONArray menuData = (JSONArray) parser.parse(new FileReader(MENU_DATA_PATH));
    	
    	Menu currentMenu;
    	JSONObject currentObj;
    	JSONArray items;
    	
    	JSONObject currentItem;
    	
    	// Iterate over each menu
    	for (int i = 0; i < menuData.size(); i ++)
    	{
    		// Create a new menu and get the menu specific data from the json file
    		currentMenu = new Menu();
    		currentObj = (JSONObject) menuData.get(i);
    		items = (JSONArray) currentObj.get("Items");
    		
    		// Add the menu to the menus list with its id
    		menus.put((String) currentObj.get("Id"), currentMenu);
    		
    		
    		// Iterate over each item in the menu
    		for (int j = 0; j < items.size(); j ++)
    		{
    			currentItem = (JSONObject) items.get(j);
    			
    			// Get the Id, X, Y, Width, Height
    			String id = (String) currentItem.get("Id");
    			float x = (float) (double) currentItem.get("X");
    			float y = (float) (double) currentItem.get("Y");
    			float width = (float) (double) currentItem.get("Width");
    			float height = (float) (double) currentItem.get("Height");
    			
    			// If it's a button
    			if ( ((String)currentItem.get("Type")).equals("btn") )
    			{
    				// Get the Text, TextSize, Font, Color, Highlight
    				String txt = (String) currentItem.get("Text");
    				
    				int size = (int) (long) currentItem.get("TextSize");
    				String style = (String) currentItem.get("Font");
    				
    				Font font = new Font(style, 1, size);
    				
    				Color color = getRGB((String) currentItem.get("Color"));
    				Color highlight = getRGB((String) currentItem.get("Highlight"));
    				
    				currentMenu.addItem(new Button(id, txt, x, y, width, height, font, color, highlight));
    			}
    			// If it's a label
    			else if (currentItem.get("Type").equals("lbl"))
    			{
    				// Get the Text, TextSize, Font, Color
    				String txt = (String) currentItem.get("Text");
    				
    				int size = (int) (long) currentItem.get("TextSize");
    				String style = (String) currentItem.get("Font");
    				
    				Font font = new Font(style, 1, size);
    				
    				Color color = getRGB((String) currentItem.get("Color"));
    				
    				currentMenu.addItem(new Label(id, txt, x, y, width, height, font, color));
    			}
    			// Otherwise, it is a image
    			else
    			{
    				// Get the Path
    				String path = (String) currentItem.get("Path");
    				
    				BufferedImage img = ImageIO.read(new File(path));
    				
    				currentMenu.addItem(new Image(id, img, x, y, width, height));
    			}
    			
    		} // END OF ITEMS LOOP
    	} // END OF MENUS LOOP
    	
    }
    
    public static Color getRGB(String str)
    {
    	// Each number is separated by a space
    	int r = Integer.parseInt(str.substring(0, 3)); // First 3 chars
    	int g = Integer.parseInt(str.substring(4, 7)); // Second 3 chars
    	int b = Integer.parseInt(str.substring(8, 11)); // Third 3 chars
    	
    	return new Color(r, g, b);
    }
    
    private void resetLevel()
    {
    	win = false;
    	
    	player.x = 50;
    	player.y = 50;
    	
    	player.cancelVelocity();
    }
    
    public void deactiveMenu()
    {
    	menus.get(activeMenu).deactivate();
    }
    
    public void setActiveMenu(String id)
    {
    	menus.get(activeMenu).deactivate();
    	
    	activeMenu = id;
    }

    public void loadLevel(int levelIndex) throws FileNotFoundException, IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader("src/Levels.txt"));
        
        // Go to the line with the level data
        for (int i = 0; i < levelIndex; i ++)
        {
        	reader.readLine();
        }
        
        String levelData = reader.readLine();

        height = charToInt(levelData.substring(0, 1));
        width = charToInt(levelData.substring(1, 3));

        int total = height * width;
        level = new BitSet(total * 2);

        int index = 0;
        int txtIndex = 3;
        // Loop through all of the level cells
        while (index < total * 2)
        {
        	// Add the current section of cells to memory
            setBitSect(level, index, charToInt(levelData.charAt(txtIndex) + ""));

            index += 6;
            txtIndex ++;
        }
        
        // Delete any extra data that got into the bitset
        if (index - total*2 > 0)
        {
            index = total*2;

            level.set(index, index + 6, false);
        }

        reader.close();
        
        camera.setBound(0, width * CELL_SIZE + player.width/2, 0, height * CELL_SIZE + player.height/2);
    }

    public int charToInt(String str)
    {
        int num = 0;
        int place = 1;

        // Convert each character of a string into an int using the key in CHAR_TO_INT
        for (int i = 0; i < str.length(); i ++)
        {
            num += CHAR_TO_INT.indexOf(str.charAt(i)) * place;
            place *= 64;
        }

        return num;
    }

    public static void setBitSect(BitSet set, int start, int data)
    {
        set.set(start, (data & 1) != 0);
        set.set(start + 1, (data & 2) != 0);
        set.set(start + 2, (data & 4) != 0);
        set.set(start + 3, (data & 8) != 0);
        set.set(start + 4, (data & 16) != 0);
        set.set(start + 5, (data & 32) != 0);
    }

    public void drawCell(Graphics2D g2D, int x, int y, BufferedImage tileSet, int tile)
    {
        int tileX = tile % 3;
        int tileY = tile / 3;

        // Get the cell from the tile set
        BufferedImage img = tileSet.getSubimage(tileX * 16, tileY * 16, 16, 16);

        g2D.drawImage(img, x, y, CELL_SIZE, CELL_SIZE, this);
    }

    public byte getLevelCell(int col, int row)
    {
    	// If we are out of bounds
    	if (col < 0 || row < 0 || col*height + row >= level.length())
    	{
    		return -1;
    	}
    	
        byte answer;
        
        // Each cell takes up two bytes, so multiply the row and col by two
        row *= 2;
        col *= 2;
        
        answer = (byte) (level.get(col*height + row) ? 2:0);
        answer += (byte) (level.get(col*height + row + 1) ? 1:0);

        return answer;
    }

    public int valToCord(int val)
    {
    	// Returns what cell a coordinate is in
        return val/CELL_SIZE;
    }

    public void applyLookAhead(int deltaX)
    {
        camera.applyLookAhead(deltaX, true);
    }
    
    public void playerCollision()
    {
    	// Get what cell the player is in
    	int playerCol = valToCord(player.x);
    	int playerRow = valToCord(player.y);
    	
    	byte prevCell;
    	byte currCell;
    	// Loop through all of the columns the player is in
    	for (int col = playerCol; col < playerCol + player.width/CELL_SIZE + 2; col ++)
        {
    		prevCell = 0;
    		
            // Loop through all of the cells in the column that the player is in
            for (int row = playerRow; row < playerRow + player.height/CELL_SIZE + 2; row ++)
            {
            	currCell = getLevelCell(col, row);
            	
            	// If it is wall
            	if (currCell == 3)
            	{
            		player.collide(col*CELL_SIZE, row*CELL_SIZE, CELL_SIZE, CELL_SIZE);
            	}
            	// If it is a semisolid and the one above isn't a semisolid
            	else if (currCell == 2 && prevCell != 2)
            	{
            		player.collideDown(col*CELL_SIZE, row*CELL_SIZE, CELL_SIZE, CELL_SIZE);
            	}
            	
            	prevCell = currCell;
            } // END OF ROW LOOP
        } // END OF COLUMN LOOP
    }
    
    public void updateMousePos()
    {
    	mouseX = (int) (MouseInfo.getPointerInfo().getLocation().getX()) - frame.getX();
    	mouseY = (int) (MouseInfo.getPointerInfo().getLocation().getY()) - frame.getY() - 30;
    	
    	Button.mouseOver(mouseX, mouseY);
    }
    
    public void paint(Graphics g)
    {
        Graphics2D g2D = (Graphics2D) g;
        
        // Draw the background
        g2D.setColor(Color.WHITE);
        g2D.fillRect(0, 0, SCREEN_SIZE.width, SCREEN_SIZE.height);
        
        if (inMenu)
        {
        	menus.get(activeMenu).draw(g2D);
        }
        else
        {
        	drawGame(g2D);
        }
    }
    
    public void drawGame(Graphics2D g2D)
    {
    	// Get what cell the camera's x coordinate is in
        int camX = valToCord(camera.x);

        byte prevCell;
        byte currCell;
        ArrayList<Integer> inFrontsX = new ArrayList<Integer>();
        ArrayList<Integer> inFrontsY = new ArrayList<Integer>();
        // Loop through each column that the player can see
        for (int col = camX; col < CELLS_ON_SCREEN + camX + 1; col ++)
        {
            prevCell = 3;

            // Loop through each row in the column
            for (int row = 0; row < height; row ++)
            {
                currCell = getLevelCell(col, row);

                switch (currCell)
                {
                    // If the current cell is a wall
                    case 3:
                    	// If there is a wall above this wall, draw the underneath variation
                        if (prevCell == 3)
                        {
                            drawCell(g2D, col * CELL_SIZE - camera.x, row * CELL_SIZE - camera.y, tileSets.get(0), 3);
                        }
                        // Otherwise, draw the top variation
                        else
                        {
                            drawCell(g2D, col * CELL_SIZE - camera.x, row * CELL_SIZE - camera.y, tileSets.get(0), 0);
                        }

                        break;
                    // If the current cell is a semisolid
                    case 2:
                    	// If there is a wall or semisolid above this one, draw the underneath variation
                        if (prevCell == 3 || prevCell == 2)
                        {
                            drawCell(g2D, col * CELL_SIZE - camera.x, row * CELL_SIZE - camera.y, tileSets.get(0), 4);
                        }
                        // Otherwise, draw the top variation
                        else
                        {
                            drawCell(g2D, col * CELL_SIZE - camera.x, row * CELL_SIZE - camera.y, tileSets.get(0), 1);
                        }

                        break;
                    // If the current cell is an infront decoration
                    case 1:
                    	// Store the coordinates in memory so we can draw them later
                        inFrontsX.add(col * CELL_SIZE - camera.x);
                        inFrontsY.add(row * CELL_SIZE - camera.y);

                        break;
                }

                prevCell = currCell;
            } // END OF ROW LOOP
        } // END OF COLUMN LOOP

        // Draw the player
        g2D.setColor(Color.BLACK);
        g2D.fillRect(player.x - camera.x, player.y - camera.y, player.width, player.height);

        // Draw the infront cells
        int currX;
        for (int i = 0; i < inFrontsX.size(); i ++)
        {
            currX = inFrontsX.get(i);

            // If there is another infront cell in the same column
            if ((i != 0 && inFrontsX.get(i - 1) == currX) ||
                    (i != inFrontsX.size() - 1 && inFrontsX.get(i + 1) == currX))
            {
            	// Draw the underneath variation
                drawCell(g2D, inFrontsX.get(i), inFrontsY.get(i), tileSets.get(0), 5);
            }
            else
            {
            	// Draw the on top variation
                drawCell(g2D, inFrontsX.get(i), inFrontsY.get(i), tileSets.get(0), 2);
            }

        }
        
        if (win)
        {
        	g2D.setFont(new Font("Arial", 1, SCREEN_SIZE.height/5));
        	g2D.drawString("YOU WON!!!!!!!!!", 0, SCREEN_SIZE.height/2);
        }
    }
    
    public void runGame()
    {
    	
    	// If the level does not exist, we should probably wait a hot minute before we start doing calculations and stuff
    	if (level == null)
    	{
    		return;
    	}
    	
    	
    	
    	if (keys[37]) // Left arrow key
        {
            player.movePlayer(-1);
        }
        if (keys[39]) // Right arrow key
        {
            player.movePlayer(1);
        }
        
        if (keys[38] && player.isOnGround()) // Up arrow key
        {
        	player.jump();
        }
        
        if (keys[27]) // Escape (esc) key
        {
        	activeMenu = "pauseMenu";
        	inMenu = true;
        }
        
        // Signal to the player that we began calculations
        player.start();
        
        player.applyGravity();
        playerCollision();

        camera.moveLookAhead(player.x, player.y, player.width, player.height, player.isOnGround());
        
     // Signal to the player that we finished calculations
        player.finish();
        
        if (player.x > (width - 2) * CELL_SIZE)
        {
        	win = true;
        }
        else if (player.y > height * CELL_SIZE * 2)
        {
        	activeMenu = "defeatMenu";
        	inMenu = true;
        }
    }
    
    @Override
	public void mouseClicked(MouseEvent e)
	{
    	Button.click(e.getX(), e.getY() - 30);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		
	}

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() < 256)
        {
            keys[e.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() < 256)
        {
            keys[e.getKeyCode()] = false;
        }
    }

    @Override
    public void run()
    {
        while (true)
        {
            repaint();
            
            updateMousePos();
            
            if (!inMenu)
            {
            	runGame();
            }

            try {Thread.sleep(TIME_BETWEEN_FRAMES);}
            catch (InterruptedException e) {System.out.println("crap");}
        }
    }
}