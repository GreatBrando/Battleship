import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Battleship extends JFrame {

  // Constants
  public static final boolean DEBUG = true;

  public static final int BOARD_ROW = 20;
  public static final int BOARD_COL = 20;
  public static final int CELL_SIZE = 25;

  // Array representing the playing grid
  private Cell cell_grid[][] = new Cell[BOARD_ROW][BOARD_COL];

  public Battleship() {

    this.setTitle("Battleship");
    this.setSize(CELL_SIZE * BOARD_COL, CELL_SIZE * BOARD_ROW);
    this.getContentPane().setBackground(Color.BLACK);

    this.setLayout(new GridLayout(BOARD_ROW, BOARD_COL));

    // Create Cell Grid
    for (int r = 0; r < BOARD_ROW; r++) {
      for (int c = 0; c < BOARD_COL; c++) {
        cell_grid[r][c] = new Cell();
        cell_grid[r][c].addMouseListener(new Listener(r, c));
        this.add(cell_grid[r][c]);
      }
    }

    // Add Ships To Random Cells
    this.addShip(2);
    this.addShip(3);
    this.addShip(4);

    this.setVisible(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public enum Orientation { Horizontal, Vertical }

  public void addShip(int size) {

    // Select a random orientation for this ship.
    Orientation o = Orientation.values()[(int)(Math.random() * 2)];

    // Select random starting points for our 'a' and 'b' coordinates.
    int start_x = ((int)(Math.random() * (BOARD_COL)));
    int start_y = ((int)(Math.random() * (BOARD_ROW)));

    // Loop through the grid
    for (int x = start_x; x < BOARD_COL; x++) {
      for (int y = start_y; y < BOARD_ROW; y++) {
        
        // Create a flag for the inner loop to modify
        // in case we need skip over this cell.
        // eg. An Overlap or OOB
        boolean skip = false;
        
        // Check 'size' cells to determine if they are
        // occupied or if we go OOB.
        for (int c = 0; c < size; c++) {
          if (o.equals(Orientation.Vertical)) {
            if (((x + c) >= BOARD_COL) || cell_grid[x + c][y].isOccupied()) {
              skip = true;
              break;
            }
          } else {
            if (((y + c) >= BOARD_ROW) || cell_grid[x][y + c].isOccupied()) {
              skip = true;
              break;
            }
          }
        }

        // If we need to skip, move to the next cell
        if (skip)
          break;

        // Everything's good to go, let's occupy
        // the cells from earlier.
        for (int c = 0; c < size; c++) {
          if (o.equals(Orientation.Vertical)) {
            cell_grid[x + c][y].occupy();
          } else {
            cell_grid[x][y + c].occupy();
          }
        }

        // Our job here is done.
        return;
      }
    }
  }

  public static class Cell extends JPanel {

    public static final Color BG_NORMAL = Color.GRAY;
    public static final Color BG_HOVER = Color.LIGHT_GRAY;
    public static final Color BG_HIT = Color.GREEN;
    public static final Color BG_MISS = Color.RED;

    private boolean occupied = false;

    public Cell() {
      this.setOpaque(true);
      this.setBackground(BG_NORMAL);
      this.setPreferredSize(
          new Dimension(Battleship.CELL_SIZE, Battleship.CELL_SIZE));
      this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    // DEBUG - Show Which Cells Are Occupied
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (DEBUG && this.occupied)
        g.fillRect(0, 0, 5, 5);
    }

    public void occupy() { this.occupied = true; }
    public boolean isOccupied() { return this.occupied; }
  }

  // Mouse Movements and Calculations
  public class Listener implements MouseListener {

    private Cell cell;

    public Listener(int row, int col) { this.cell = cell_grid[row][col]; }

    public void mouseClicked(MouseEvent e) {
      this.cell.setBackground(cell.isOccupied() ? Cell.BG_HIT : Cell.BG_MISS);
    }

    public void mouseEntered(MouseEvent e) {
      if (this.cell.getBackground() != Cell.BG_HIT &&
          this.cell.getBackground() != Cell.BG_MISS) {
        this.cell.setBackground(Cell.BG_HOVER);
      }
    }

    public void mouseExited(MouseEvent e) {
      if (this.cell.getBackground() != Cell.BG_HIT &&
          this.cell.getBackground() != Cell.BG_MISS) {
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
