import java.io.*;
import java.util.Random;
import java.util.Scanner;
/**
 * This class represents a Minesweeper game.
 *
 * @author Max Strauss <mas88806@uga.edu>
 */
public class Minesweeper {
    private int cols;
    private int rows;
    private String[][] grid;
    private int roundsPlayed;
    private String[][] gameGrid;
    BufferedReader read = null;
    /**
     * Constructs an object instance of the {@link Minesweeper} class using the
     * information provided in <code>seedFile</code>. Documentation about the 
     * format of seed files can be found in the project's <code>README.md</code>
     * file.
     *
     * @param seedFile the seed file used to construct the game
     * @throws FileNotFoundException 
     * @see            <a href="https://github.com/mepcotterell-cs1302/cs1302-minesweeper-alpha/blob/master/README.md#seed-files">README.md#seed-files</a>
     */
    public Minesweeper(File seedFile)  {
try
    {
	int mines = 0;
	read = new BufferedReader(new FileReader(seedFile));
	String string = "";
	if((string = read.readLine()) != null)
	    {
		String[] nums = string.split(" ");
		if(nums.length != 2)
		    {
			System.out.println("Cannot create game with FILENAME, because it is not formatted correctly.");
			System.exit(0);
		    }
		else
		    {
			int rows = Integer.parseInt(nums[0]);
			int cols = Integer.parseInt(nums[1]);
			grid = new String[rows][cols];
			for(int r = 0; r<rows; r++)
			    {
				for(int c = 0; c<cols; c++)
				    {
					grid[r][c] = " ";
				    }
			    }
			gameGrid = new String[rows][cols];
			for(int i = 0; i<rows; i++)
			    {
				for(int j = 0; j<cols; j++)
				    {
					gameGrid[i][j] = " ";
				    }
			    }
			if((rows<1 || rows>10) || (cols<1 || cols>10))
			    {
				System.out.println("ಠ_ಠ says, \"Cannot create a mine field with that many rows and/or columns!\"");
				System.exit(0);
			    }
		    }
	    }
	if((string = read.readLine()) != null)
	    {
		mines = Integer.parseInt(string.substring(0));
		
		for(int i = 0; i<mines+1; i++)
		    {
			if((string = read.readLine()) != null)
			    {
				String[] nums = string.split(" ");
				if(nums.length != 2)
				    {
					System.out.println("Cannot create game with" + seedFile.getName() + ", because it is not formatted correctly.");
					System.exit(0);
				    }
				int x = Integer.parseInt(nums[0]);
				int y = Integer.parseInt(nums[1]);
				grid[x][y] = "B";
			    }
		    }
	    }
    }
   catch(FileNotFoundException e)
    {
	System.out.println("Cannot create game with" + seedFile.getName() + ", because it is not formatted correctly.");
    }
  catch(IOException e)
    {
	System.out.println("Cannot create game with" + seedFile.getName() + ", because it is not formatted correctly.");
    }
   finally
    {
	try
	    {
		if(read != null)
		    read.close();
	    } catch (IOException e) {
	    System.out.println("Cannot create game with" + seedFile.getName() + ", because it is not formatted correctly.");
	}
    }
    } // Minesweeper
    /**
     * Constructs an object instance of the {@link Minesweeper} class using the
     * <code>rows</code> and <code>cols</code> values as the game grid's number
     * of rows and columns respectively. Additionally, One quarter (rounded up) 
     * of the squares in the grid will will be assigned mines, randomly.
     *
     * @param rows the number of rows in the game grid
     * @param cols the number of cols in the game grid
     */
    public Minesweeper(int rows, int cols) {
	this.rows = rows;
	this.cols = cols;
	grid = new String[rows][cols];
	int r = 0;
	int c = 0;
	for(r = 0; r<rows; r++)
	    {
		for(c = 0; c<cols; c++)
		    {
			grid[r][c] = " ";
		    }
	    }
	gameGrid = new String[rows][cols];
	for(int x = 0; x<rows; x++)
	    {
		for(int y = 0; y<cols; y++)
		    {
			gameGrid[x][y] = " ";
		    }
	    }
	if((rows<1 || rows>10) || (cols<1 || cols>10))
	    {
		System.out.println("ಠ_ಠ says, \"Cannot create a mine field with that many rows and/or columns!\"");
		System.exit(0);
	    }
    } // Minesweeper
        
    //Getter
    public String[][] getGrid()
    {
	return this.grid;
    }
        
    //Getter for the actual game grid
    public String[][] getgameGrid()
    {
	return this.gameGrid;
    }
        
    //Setter
    public void setGrid(String[][] array)
    {
	this.grid = array;
    }
        
    //Randomly sets the mines in the grid
    public void setMines()
    {
	int numMines = ((this.rows*this.cols)/10)+1;
	Random rand = new Random();
	int i = 0;
	while(i<numMines)
	    {
		int r = rand.nextInt(rows);
		int c = rand.nextInt(cols);
		if(grid[r][c].equals("B"))
		    {
			continue;
		    }
		else
		    {
			grid[r][c] = "B";
			i++;
		    }
	    }
    }
    //Prints the grid that the player sees while playing the game.
    public void printGrid(String[][] grid)
    {
	System.out.println();
	System.out.println("Rounds completed: " + roundsPlayed);
	System.out.println();
	for(int i = 0; i < rows; i++) 
	    {
		System.out.print(" "+i+"|");
		for(int x = 0; x < cols; x++)
		    {
			System.out.print(grid[i][x]+"|");
		    }
		System.out.println();
	    }
	System.out.print("   ");
	for(int a = 0; a < cols; a++)
	    {
		System.out.print(a + " ");
	    }
    }
        
    //Allows the game to determine if a certain space is actually a real space while checking for bombs.
    public boolean isInbounds(int rows, int cols)
    {
	if((rows-1)<0 || rows+1>this.rows)
	    {
		return false;
	    }
	else if((cols-1)<0 || cols+1>this.rows)
	    {
		return false;
	    }
	else
	    {
		return true;
	    }
    }
        
    //Calculates how many mines surround the given spot and sets the given spot on the grid to the number of mines.
    public void numberadjMines(int rows, int cols)
    {
	int numMines = 0;
	if(grid[rows][cols]=="B")
	    {
		return;
	    }
	if(isInbounds(rows+1,cols+1)==true && grid[rows+1][cols+1]=="B")
	    {
		numMines += 1;
	    }
	if(isInbounds(rows+1,cols-1)==true && grid[rows+1][cols-1]=="B")
	    {
		numMines += 1;
	    }
	if(isInbounds(rows-1,cols+1)==true && grid[rows-1][cols+1]=="B")
	    {
		numMines += 1;
	    }
	if(isInbounds(rows-1,cols-1)==true && grid[rows-1][cols-1]=="B")
	    {
		numMines += 1;
	    }
	if(isInbounds(rows+1,cols)==true && grid[rows+1][cols]=="B")
	    {
		numMines += 1;
	    }
	if(isInbounds(rows-1,cols)==true && grid[rows-1][cols]=="B")
	    {
		numMines += 1;
	    }
	if(isInbounds(rows,cols+1)==true && grid[rows][cols+1]=="B")
	    {
		numMines += 1;
	    }
	if(isInbounds(rows,cols-1)==true && grid[rows][cols-1]=="B")
	    {
		numMines += 1;
	    }
	grid[rows][cols] = ""+numMines;
    }
        
    //Determines if the player has won.
    public boolean hasWon()
    {
	int goodFlag = 0;
	if((rows*cols)>roundsPlayed)
	    {
		return false;
	    }
	else
	    {
		for(int i = 0; i < rows; i++)
		    {
			for(int y = 0; y < cols; y++)
			    {
				if(gameGrid[i][y].equals(" "))
				    {
					return false;
				    }
				else if(gameGrid[i][y].equals("?"))
				    {
					return false;
				    }
				if(gameGrid[i][y].equals("F") && grid[i][y].equals("B"))
				    {
					goodFlag++;
				    }
			    }
		    }
		if(goodFlag == ((rows*cols)/10))
		    {
			return true;
		    }
		else
		    {
			return false;
		    }
	    }
    }
        
    //A game command.  Allows the player to reveal the grid spot at coordinate (a,b).
    public void reveal(int a, int b)
    {
	if(!(grid[a][b].equals("B")))
	    {
		String s = " ";
		s = grid[a][b];
		gameGrid[a][b] = s;
		roundsPlayed++;
		hasWon();
	    }
	else
	    {
		System.out.println();
		System.out.println(" Oh no... You revealed a mine!");
		System.out.println("  __ _  __ _ _ __ ___   ___    _____   _____ _ __ ");
		System.out.println(" / _` |/ _` | '_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ '__|");
		System.out.println("| (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |   ");
		System.out.println(" \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|   ");
		System.out.println(" |___/                                          ");
		System.out.println();
		System.exit(0);
	    }
    }
        
    //A game command.  Allows the player to mark the grid spot at (a,b) as a 'flag' if they think that spot contains a bomb.
    public void mark(int a, int b)
    {
	gameGrid[a][b] = "F";
	roundsPlayed++;
	hasWon();
    }
        
    //A game command.  Allows the player to mark a spot at (a,b) as potentially containing a mine.
    public void guess(int a, int b)
    {
	gameGrid[a][b] = "?";
	roundsPlayed++;
	hasWon();
    }
        
    //A game command.  Allows the player to see the possible commands because they have terrible memory and can't remember simple instructions.
    public void help()
    {
	System.out.println("Commands Available...");
	System.out.println("- Reveal: r/reveal row col");
	System.out.println("-   Mark: m/mark   row col");
	System.out.println("-  Guess: g/guess  row col");
	System.out.println("-   Help: h/help");
	System.out.println("-   Quit: q/quit");
	roundsPlayed++;
    }
        
    //A game command. Quits the game and displays a message addressing the issue.
    public void quit()
    {
	System.out.println();
	System.out.println("ლ(ಠ_ಠლ)");
	System.out.println("Y U NO PLAY MORE?");
	System.out.println("Bye!");
	System.exit(0);
    }

    /**
     * Starts the game and execute the game loop.
     */
    public void run() {
	Scanner input = new Scanner(System.in);
	System.out.println("        _");
	System.out.println("  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __");
	System.out.println(" /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|");
	System.out.println("/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |");
	System.out.println("\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|");
	System.out.println("                                    ALPHA |_| EDITION ");
	setMines();
	for(int i = 0; i<rows; i++)
	    {
		for(int j = 0; j<cols; j++)
		    {
			numberadjMines(i,j);
		    }
	    }
	  do
	      {
		  printGrid(gameGrid);
		  System.out.println();
		  String s = input.nextLine() + "       ";
		  if(s.substring(0,1).equals("h"))
		      {
			  help();
		      }
		  else if(s.substring(0,1).equals("q"))
		      {
			  quit();
		      }
		  else if(s.substring(0,4).equals("quit"))
		      {
			  quit();
		      }
		  else if(s.substring(0,4).equals("help"))
		      {
			  help();
		      }
		  else if(s.substring(0,1).equals("r"))
		      {
			  int a = s.indexOf(" ");
			  s = s.substring(a).trim();
			  int i = s.indexOf(" ");
			  int x = Integer.parseInt(s.substring(0,i));
			  a = s.indexOf(" ");
			  s = s.substring(a).trim();
			  int y = Integer.parseInt(s.substring(0));
			  if(x > rows || y > cols)
			      {
				  System.out.println("ಠ_ಠ says, \"Command not recognized!\"");
				  roundsPlayed++;
			      }
			  else
			      {
				  reveal(x,y);
			      }
		      }
		  else if(s.substring(0,1).equals("m")||s.substring(0,4).equals("mark"))
		      {
			  s = s.trim();
			  s = s + " ";
			  int a = s.indexOf(" ");
			  s = s.substring(a).trim();
			  int i = s.indexOf(" ");
			  int x = Integer.parseInt(s.substring(0,i));
			  a = s.indexOf(" ");
			  s = s.substring(a).trim();
			  int y = Integer.parseInt(s.substring(0));
			  if(x > rows || y > cols)
			      {
				  System.out.println("ಠ_ಠ says, \"Command not recognized!\"");
				  roundsPlayed++;
			      }
			  else
			      {
				  mark(x,y);
			      }
		      }
		  else if(s.substring(0,1).equals("g")||s.substring(0,5).equals("guess"))
		      {
			  s = s.trim();
			  s = s + " ";
			  int a = s.indexOf(" ");
			  s = s.substring(a).trim();
			  int i = s.indexOf(" ");
			  int x = Integer.parseInt(s.substring(0,i));
			  a = s.indexOf(" ");
			  s = s.substring(a).trim();
			  int y = Integer.parseInt(s.substring(0));
			  if(x > rows || y > cols)
			      {
				  System.out.println("ಠ_ಠ says, \"Command not recognized!\"");
				  roundsPlayed++;
			      }
			  else
			      {
				  guess(x,y);
			      }
		      }
		  else if(s.substring(0,6).equals("reveal"))
		      {
			  s = s.trim();
			  s = s + " ";
			  int a = s.indexOf(" ");
			  s = s.substring(a).trim();
			  int i = s.indexOf(" ");
			  int x = Integer.parseInt(s.substring(0,i));
			  a = s.indexOf(" ");
			  s = s.substring(a).trim();
			  int y = Integer.parseInt(s.substring(0));
			  if(x > rows || y > cols)
			      {
				  System.out.println("ಠ_ಠ says, \"Command not recognized!\"");
				  roundsPlayed++;
			      }
			  else
			      {
				  reveal(x,y);
			      }
		      }
		  else
		      {
			  System.out.println("ಠ_ಠ says, \"Command not recognized!\"");
			  roundsPlayed++;
		      }
	      }while(hasWon() == false);
	  if(hasWon()== true)
	      {
		  System.out.println(" ░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░ \"So Doge\"");
		  System.out.println(" ░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░");
		  System.out.println("  ░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░ \"Such Score\"");
		  System.out.println("  ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░");
		  System.out.println("  ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░ \"Much Minesweeping\"");
		  System.out.println("  ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░");
		  System.out.println("  ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░ \"Wow\"");
		  System.out.println("  ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░");
		  System.out.println("  ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌░");
		  System.out.println("  ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░");
		  System.out.println("  ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░");
		  System.out.println("  ▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒▌");
		  System.out.println("  ▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░");
		  System.out.println("  ░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░");
		  System.out.println("  ░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░");
		  System.out.println("  ░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░");
		  System.out.println("  ░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░ CONGRATULATIONS!");
		  System.out.println("  ░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░ YOU HAVE WON!");
		  System.out.println("  ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░ SCORE: " + ((rows * cols) - ((rows*cols)/10) - roundsPlayed));
	      }
	  

    } // run


    /**
     * The entry point into the program. This main method does implement some
     * logic for handling command line arguments. If two integers are provided
     * as arguments, then a Minesweeper game is created and started with a 
     * grid size corresponding to the integers provided and with 10% (rounded
     * up) of the squares containing mines, placed randomly. If a single word 
     * string is provided as an argument then it is treated as a seed file and 
     * a Minesweeper game is created and started using the information contained
     * in the seed file. If none of the above applies, then a usage statement
     * is displayed and the program exits gracefully. 
     *
     * @param args the shell arguments provided to the program
     */
    public static void main(String[] args) {

	/*
	    The following switch statement has been designed in such a way that if
	      errors occur within the first two cases, the default case still gets
	        executed. This was accomplished by special placement of the break
		  statements.
	*/

	Minesweeper game = null;

	switch (args.length) {

	    // random game
	case 2: 

	    int rows, cols;

	    // try to parse the arguments and create a game
	    try {
		rows = Integer.parseInt(args[0]);
		cols = Integer.parseInt(args[1]);
		game = new Minesweeper(rows, cols);
		break;
	    } catch (NumberFormatException nfe) { 
		// line intentionally left blank
	    } // try

	    // seed file game
	case 1: 

	    String filename = args[0];
	    File file = new File(filename);

	    if (file.isFile()) {
		game = new Minesweeper(file);
		break;
	    } // if
	        
	    // display usage statement
	default:

	    System.out.println("Usage: java Minesweeper [FILE]");
	    System.out.println("Usage: java Minesweeper [ROWS] [COLS]");
	    System.exit(0);

	} // switch

	// if all is good, then run the game
	game.run();

    } // main


} // Minesweeper