import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Battleship extends JFrame {

	//Panels for Game
	JPanel mainframe;

	//Attempts user makes
	int attempts = 0;
	
	//Constants
	public static final int BOARD_W = 7;
	public static final int BOARD_H = 7;
	public static final int CELL_WH = 64;

	//Which cell the user attempts to attack
	Cell target;
	
	//Array representing the playing grid
	Cell cell_grid[][] = new Cell[BOARD_W][BOARD_H];
	
	public Battleship() {
		
		this.setTitle("Battleship");
		this.setSize(CELL_WH*BOARD_W,CELL_WH*BOARD_H);
		this.getContentPane().setBackground(Color.BLACK);

		this.setLayout(new GridLayout(BOARD_W,BOARD_H));
		
		// Assigns Panels to GameFrame
		for (int a = 0; a < BOARD_W; a++)
        {
            for (int b = 0; b < BOARD_H; b++)
            {   
				cell_grid[a][b] = new Cell();
                cell_grid[a][b].addMouseListener(new Listener(a,b));
           		this.add(cell_grid[a][b]);
        	}
        } 
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//Creates Specialized Cells For Battleship
	public static class Cell extends JPanel
	{
		public static final Color BG_NORMAL = Color.GRAY;
		public static final Color BG_HOVER  = Color.LIGHT_GRAY;
		public static final Color BG_TARGET = Color.CYAN;
		public static final Color BG_HIT    = Color.GREEN;
		public static final Color BG_MISS   = Color.RED;

		public Cell()
		{
			this.setOpaque(true); 
			this.setBackground(BG_NORMAL);
			this.setPreferredSize(new Dimension (Battleship.CELL_WH, Battleship.CELL_WH));
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
	}
	
	//Mouse Movements and Calculations
	public class Listener implements MouseListener 
	{
		private int row; // stores the value of which row the panel that was pressed is in
		private int col; // stores the value of which column the panel that was pressed is in
		
		public Listener(int row, int col)
        {
        	this.row = row;
        	this.col = col;
        }
		
		public void mouseClicked(MouseEvent e) {
			switch (e.getButton())
			{
				// Left Mouse Button - Select Target
				case MouseEvent.BUTTON1:
					cell_grid[row][col].setBackground(Cell.BG_TARGET);
					break;

				// Right Mouse Button - Remove Target
				case MouseEvent.BUTTON3:
					cell_grid[row][col].setBackground(Cell.BG_NORMAL);
					break;
			}

			// System.out.println(String.format("Clicked panel: [%d,%d]", row, col));
		}

		public void mouseEntered(MouseEvent e) {
			if (cell_grid[row][col].getBackground() != Cell.BG_TARGET)
				cell_grid[row][col].setBackground(Cell.BG_HOVER);

			// System.out.println(String.format("Mouse entered panel: [%d,%d]", row, col));
		}

		public void mouseExited(MouseEvent e) {
			if (cell_grid[row][col].getBackground() != Cell.BG_TARGET)
				cell_grid[row][col].setBackground(Cell.BG_NORMAL);
		
			// System.out.println(String.format("Mouse left panel: [%d,%d]", row, col));
		}

		public void mousePressed(MouseEvent e) {}

		public void mouseReleased(MouseEvent e) {}
	}

	public static void main(String[] args){
		JFrame.setDefaultLookAndFeelDecorated(true);
		new Battleship();
	}
}
