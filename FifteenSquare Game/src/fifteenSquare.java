import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

//Alexander Carson
//04/30/2014

//This is a GUI-Based implementation in Java of the classic puzzle known as the “fifteen-square” or the “fifteen puzzle”. 
//This is an old sliding-tile game, dating back to the late 1800’s. 
//The puzzle consists of a four-by-four grid of tiles held in a frame. 
//One of the tiles is missing, leaving fifteen tiles.

public class fifteenSquare extends JFrame 
{
	//Uses JFrame to create a window to run the game in
	//The entire game is in this class

	private static final long serialVersionUID = 5690714462348055964L;
	private static final int WIDTH = 60; //Tile sizes
	private static final int HEIGHT = 60; 
	private static final int MARGIN = 10; //Space between tiles/buttons
	private static final int WIDTH2 = (int)(4.0/3 * WIDTH); //Button sizes
	private static final int HEIGHT2 = WIDTH2/2;
	private static final int MARGIN2 = 25; //Vertical space between the tiles, and buttons
	private static final Font font = new Font(null, 0, 22); //Font on tiles
	private static int  moves = 0; //Move counter
	private static JPanel panel = new JPanel();
	private JButton[][] tiles = new JButton[4][4]; //Array for tiles
	private static JButton jbtSave = new JButton("Save"); //Buttons
	private static JButton jbtHint = new JButton("Hint"); 
	private static JButton jbtHelp = new JButton("Help");
	private static JButton jbtLoad = new JButton("Load");
	private static JButton jbtExit = new JButton("Exit");
	private static JLabel jlblMoves = new JLabel(" Moves:  "+(moves)); //Move counter
	Container[][] buttons = {{jbtSave,jbtLoad},{jbtHint,jbtExit},{jbtHelp, jlblMoves}}; //Array for buttons and move counter
	
	fifteenSquare()
	{
		//Constructor for game window
		//Creates GUI and shuffles the board
		
		for(int i=0; i<4; i++) //Creates and sets up tiles
			for(int j=0; j<4; j++)
			{
				tiles[i][j] = new JButton(""+(4*i+j+1));
				panel.add(tiles[i][j]);
				tiles[i][j].setBounds((WIDTH+MARGIN)*j+MARGIN , (HEIGHT+MARGIN)*i+MARGIN, WIDTH, HEIGHT);
				tiles[i][j].setFont(font);
				tiles[i][j].addActionListener(new TileListener());
				if(tiles[i][j].getText().equals("16")) //Sets blank tile to "0" and invisible
				{
					tiles[i][j].setText("0"); 
					tiles[i][j].setVisible(false);
				}
			}	
		
		for(int i=0; i<3; i++) //Sets up buttons and move counter
			for(int j=0; j<2; j++)
			{
				panel.add(buttons[i][j]);
				buttons[i][j].setBounds(MARGIN+(WIDTH2+MARGIN)*i, MARGIN2+(HEIGHT+MARGIN)*4+j*(HEIGHT2+MARGIN), WIDTH2, HEIGHT2);
				if((i==2&&j==1)) break; //Skip the following for move counter
				((JButton) buttons[i][j]).addActionListener(new ButtonListener());
			}
		panel.setLayout(null);
		add(panel);	
		shuffle();
	}

	class TileListener implements ActionListener 
	{
		//When tiles are clicked, move tiles
		//If moved, increase move counter
		//Checks if board is solved
		
		@Override
		public void actionPerformed(ActionEvent ev)
		{
			for(int i=0; i<4; i++)
				for(int j=0; j<4; j++)
					if(tiles[i][j] == ((JButton)ev.getSource()))
						if(moveTiles(4*i+j))
							jlblMoves.setText(" Moves:  "+(++moves));
			if(isSolved()) playAgain();
			
		}
	
	}
	
	class ButtonListener implements ActionListener 
	{
		//When buttons are clicked, performs respective action
		
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			switch(((JButton)arg0.getSource()).getText())
			{
				case "Save":{save(); break;}
				case "Hint":{hint(); break;}
				case "Help":{help(); break;}
				case "Load":{load(); break;}
				case "Exit":{System.exit(0); break;} //Ends game
			}
		}
	
	}

	public static void main(String[] args)
	{
		//Constructs the window and starts game
		fifteenSquare window = new fifteenSquare();
		window.setTitle("FifteenSquare Game");
		window.setSize(WIDTH*4+MARGIN*6, MARGIN2+(HEIGHT+MARGIN)*4+3*(HEIGHT2+MARGIN));
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		jbtHint.setEnabled(false); //Disables Hint
	}
	
	void shuffle()
	{
		//Shuffles the board
		//Uses random numbers 0-15 to choose moves randomly
		
		for(int i=0; i<1000; i++) moveTiles((int)((Math.random()*(100))%16));
	}
	
	int findBlank()
	{
		//Returns an integer 0-15 representing the position of the blank tile
		
		int blank=-1;
		for(int i=0; i<4; i++)
			for(int j=0; j<4; j++)
				if(tiles[i][j].getText().equals("0"))
					blank = 4*i+j;
		return blank;
	}
	
	int row(int i) {return i/4;}
	
	int col(int i) {return i%4;}
	
	void swap(JButton a, JButton b)
	{
		//Switches the number labels between two buttons
		//Checks that the "0" tile remains invisible
		
		String c = a.getText();
		a.setText(b.getText());
		b.setText(c);
		for(int i=0; i<4; i++)
			for(int j=0; j<4; j++)
			{
				if(tiles[i][j].getText().equals("0"))
					tiles[i][j].setVisible(false);
				else tiles[i][j].setVisible(true);
			}
	}	

	boolean moveTiles(int clicked)
	{
		//Moves clicked tile towards blank tile
		//Boolean returns if a move was made
		
		if (row(clicked) == row(findBlank())) //Move horizontal
		{
			if(col(clicked) < col(findBlank())) //Move forward
			{
				int j = col(findBlank()) - col(clicked);
				for(int i=0; i<j; i++)
					swap(tiles[row(findBlank())][col(findBlank())-1],tiles[row(findBlank())][col(findBlank())]);
				return true;
			}
			if(col(clicked) > col(findBlank())) //Move backwards
			{	
				int j =col(clicked) - col(findBlank());
				for(int i=0; i< j; i++)
					swap(tiles[row(findBlank())][col(findBlank())+1],tiles[row(findBlank())][col(findBlank())]);
				return true;
			}	
		}	
		if (col(clicked) == col(findBlank())) //Move vertical
		{
			if(row(clicked) < row(findBlank())) //Move up
			{
				int j = row(findBlank()) - row(clicked);
				for(int i=0; i<j; i++)
					swap(tiles[row(findBlank())-1][col(findBlank())],tiles[row(findBlank())][col(findBlank())]);
				return true;
			}
			if(row(clicked) > row(findBlank())) //Move down
			{	
				int j = row(clicked) - row(findBlank());
				for(int i=0; i< j; i++)
					swap(tiles[row(findBlank())+1][col(findBlank())],tiles[row(findBlank())][col(findBlank())]);
				return true;
			}
		}
		return false;
	}

	boolean isSolved()
	{
		//Checks for each tile if it is in the correct position
		//Count is the number of correct tiles
		//When count is 15, all tiles are correct
		//This means the board is solved
		
		int count = 0;
		for(int i=0; i<4; i++)
			for(int j=0; j<4; j++)
				if(tiles[i][j].getText().equals(""+(4*i+j+1)))
					count++;
		if(count == 15) return true;
		else	return false;
	}

	void playAgain()
	{
		//Asks to play again
		//Re-shuffles and resets move counter or ends the game
		
		int again = JOptionPane.showOptionDialog(null, new String("Play Again?"), new String("You Win!"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (again == JOptionPane.YES_OPTION)
		{
			jlblMoves.setText(" Moves:  "+(moves = 0));
			shuffle();
		}	
		else System.exit(0);;
	}	

	void save()
	{
		//Opens a file dialog to choose where to save the game data
		//Writes each tile's string, and the move counter to the selected file.
		
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("DAT data", "dat"); //Sets .dat extension
		chooser.setFileFilter(filter);
		chooser.setSelectedFile(new File("fifteenSquare.dat")); //Sets default filename
		int confirm = -1;
		do
		{
			if(chooser.showSaveDialog(null) == 1) //User has cancelled
				return;
			if (chooser.getSelectedFile().exists()) //Overwrite warning
				confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to overwrite existing file?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		}
		while(confirm == 1); //Repeat to select new file if user chooses no
		String path = chooser.getSelectedFile().getAbsolutePath();
		try
		{
			if (!new File(path).exists())  //Creates file if it doesn't exist.
				new File(path).createNewFile();
			FileOutputStream out = new FileOutputStream(path);
			for(int i=0; i<4; i++)
				for(int j=0; j<4; j++)
					out.write(Integer.parseInt(tiles[i][j].getText())); //Writes tiles
			out.write(moves); //Writes move count
			out.close();
		}
		catch (IOException e)
		{
			System.out.print("Error");
			
		}
	}
	
	void load()
	{
		//Opens a file to choose the file to load from
		//Loads the tile's strings into the "tiles" array, and the move count into "moves"
		
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("DAT data", "dat"); //Sets .dat extension
		chooser.setFileFilter(filter);
		chooser.setSelectedFile(new File("fifteenSquare.dat")); //Sets default filename
		do
		{
			if(chooser.showOpenDialog(null) == 1) //User has cancelled
				return;
			if (!chooser.getSelectedFile().exists()) //File not found Error
				JOptionPane.showMessageDialog(null, "Please select another", "File Not Found", JOptionPane.ERROR_MESSAGE);
		}	
		while (!chooser.getSelectedFile().exists()); //Repeats if file not found
		String path = chooser.getSelectedFile().getAbsolutePath();
		try
		{
			FileInputStream in = new FileInputStream(path);
			for(int i=0; i<4; i++)
				for(int j=0; j<4; j++)
				{	
					tiles[i][j].setText(""+in.read()); //Loads strings
					if(tiles[i][j].getText().equals("0")) //Sets blank tile invisible
						tiles[i][j].setVisible(false);
					else tiles[i][j].setVisible(true);
				}
			jlblMoves.setText(" Moves:  "+in.read()); //Loads move counter and displays count at 0
			in.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.print("File Not Found");
		}
		catch (IOException e)
		{
			System.out.print("Error");
		}
		
	}
	
	void help()
	{
		//Opens a window to display instructions
		
		JFrame helpF = new JFrame("Help");
		helpF.setSize(400,230);
		helpF.setResizable(false);
		helpF.setLocationRelativeTo(null);
		JPanel helpP = new JPanel();
		helpF.add(helpP);
		ImageIcon solved = new ImageIcon("image/solved.jpg");
		helpP.add(new JLabel(solved));
		
		JTextArea helpT = new JTextArea
		(
			"The FifteenSquare Game consists of 15 tiles numbered from 1 to 15 that are placed in a 4×4 box leaving one position out of the 16 empty."
			+ " The goal is to reposition the tiles from the randomized starting arrangement by sliding them one at a time into the configuration shown above. "
			+ "Clicking on any tile will slide the tile towards the empty space if it is possible."
		);
		helpT.setSize(200, 400);
		helpT.setBackground(null);
		helpT.setEditable(false);
		helpT.setBorder(null);
		helpT.setLineWrap(true);
		helpT.setWrapStyleWord(true);
		helpT.setFocusable(false);
		helpP.add(helpT);
		helpF.setVisible(true);
	}
	
	void hint()
	{
		//“Hint” is reserved for a potential future assignment.
	}
}
