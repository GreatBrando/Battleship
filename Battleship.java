import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Battleship extends JFrame{

	//Panels for Game
	 JPanel mainframe;
	 
	 //Attempts user makes
	 int attempts = 0;
	 
	 //Array representing the playing grid
	 JPanel playgrid[][] = new JPanel [7][7];
	 
	//Array used for storing locations of ships
	 int gameboard[][] = new int[7][7];
	
	 int x;  
	 int y;
	
	 Cell  bombTarget;
	
	public Battleship(){
		setTitle("Battleship");
		setSize(800,600);
		setLayout(new GridLayout(7,7));
		
		
		//Assigns Panels to GameFrame
		for (int a = 0; a < 7; a++)
        {
            for (int b = 0; b < 7; b++)
            {                
                playgrid[a][b] = new Cell();
                playgrid[a][b].addMouseListener(new Listener(a,b));
                add(playgrid[a][b]);
            }
        } 
		
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//Creates Specialized Cells For Battleship
	public class Cell extends JPanel {

	        public Cell()
	        {
	            setOpaque (true); // without this, the cell is transparent and
	                              // the background color will not show up

	            setBackground (Color.WHITE);

	            
	            setPreferredSize (new Dimension (200, 200));

	            // if your prefer a 3D look, put a border around each cell
	            setBorder (BorderFactory.createLineBorder(Color.BLACK));
	        }
	    }
	
	//Mouse Movements and Calculations
	public class Listener implements MouseListener 
	{
		int a; // stores the value of which row the panel that was pressed is in
		int b; // stores the value of which column the panel that was pressed is in
		
	
		public Listener(int row, int column)
        {
            a = row;
            b = column;
        }
		
		public void mouseClicked(MouseEvent e) {
			System.out.println("Mouse Clicked");
			bombTarget = (Cell)playgrid[a][b].getComponentAt(e.getX(),e.getY());
			bombTarget.setBackground(Color.RED);
			
		}

		public void mouseEntered(MouseEvent e) {
			System.out.println("mouse entered the panel");
			
		}

		public void mouseExited(MouseEvent e) {
			System.out.println("The mouse exited the panel.");
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}



public static void main(String[] args){
	new Battleship();
  }
}

