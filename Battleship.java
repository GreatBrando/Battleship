import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Battleship extends JFrame {

	// Constants
	public static final int BOARD_ROW = 10;
	public static final int BOARD_COL = 10;
	public static final int CELL_SIZE = 50;

	// Array representing the playing grid
	private Cell cell_grid[][] = new Cell[ BOARD_ROW ][ BOARD_COL ];
	
	public Battleship() {

		this.setTitle( "Battleship" );
		this.setSize( CELL_SIZE * BOARD_COL, CELL_SIZE * BOARD_ROW );
		this.getContentPane().setBackground( Color.BLACK );

		this.setLayout( new GridLayout( BOARD_ROW,BOARD_COL ) );
		
		// Create Cell Grid
		for ( int r = 0; r < BOARD_ROW; r++ )
        {
            for ( int c = 0; c < BOARD_COL; c++ )
            {   
				cell_grid[r][c] = new Cell();
                cell_grid[r][c].addMouseListener( new Listener( r, c ) );
           		this.add( cell_grid[r][c] );
        	}
        }
		
		// Add Ships To Random Cells
		this.addShip( 1 );
		this.addShip( 2 );
		this.addShip( 3 );

		this.setVisible( true );
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void addShip( int size ) {

		boolean dir = ( ( int )( Math.random() * 2 ) ) != 0; // 0 = Horizontal, 1 = Vertical
		int start_a = ( ( int )( Math.random() * ( dir ? BOARD_COL : BOARD_ROW ) - 1 ) );
		int start_b = ( ( int )( Math.random() * ( dir ? BOARD_ROW : BOARD_COL ) - 1 ) );

		for ( int a = start_a; a < ( dir ? BOARD_COL : BOARD_ROW ); a++ )	{
			for (int b = start_b; b < ( dir ? BOARD_ROW : BOARD_COL ); b++ ) {

				boolean overlap = false;

				for ( int c = 0; c < size; c++ ) {

					boolean condition = ( dir ) ? ( cell_grid[a][b+c].isOccupied() )
												: ( cell_grid[a+c][b].isOccupied() );

					if ( condition ) {
						overlap = true;
						break;
					}
				}

				if ( overlap ) continue;

				for ( int c = 0; c < size; c++ ) {
					
					Cell to_occupy = ( dir ) ? ( cell_grid[a][b+c] )
											 : ( cell_grid[a+c][b] );

					to_occupy.occupy();
				}

				return;
			}
		}
	}

	public static class Cell extends JPanel {

		public static final Color BG_NORMAL = Color.GRAY;
		public static final Color BG_HOVER  = Color.LIGHT_GRAY;
		public static final Color BG_HIT    = Color.GREEN;
		public static final Color BG_MISS   = Color.RED;
		
		private boolean occupied = false;

		public Cell() {
			this.setOpaque(true); 
			this.setBackground(BG_NORMAL);
			this.setPreferredSize(new Dimension (Battleship.CELL_SIZE, Battleship.CELL_SIZE));
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
		
		// Visual Debugging
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawString( this.occupied ? "O" : "X", 10, 20);
		}

		public void occupy() { this.occupied = true; }
		public boolean isOccupied() { return this.occupied;	}
	}
	
	// Mouse Movements and Calculations
	public class Listener implements MouseListener {

		private Cell cell;

		public Listener(int row, int col) {
        	this.cell = cell_grid[row][col];
        }
		
		public void mouseClicked(MouseEvent e) {
			this.cell.setBackground(cell.isOccupied() ? Cell.BG_HIT : Cell.BG_MISS);
		}

		public void mouseEntered(MouseEvent e) {
			if (this.cell.getBackground() != Cell.BG_HIT
			&&  this.cell.getBackground() != Cell.BG_MISS) {
				this.cell.setBackground(Cell.BG_HOVER);
			}
		}

		public void mouseExited(MouseEvent e) {
			if (this.cell.getBackground() != Cell.BG_HIT
			&&  this.cell.getBackground() != Cell.BG_MISS) {
				this.cell.setBackground(Cell.BG_NORMAL);
			}
		}

		public void mousePressed(MouseEvent e) {}

		public void mouseReleased(MouseEvent e) {}
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		new Battleship();
	}
}
