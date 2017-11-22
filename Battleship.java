import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Battleship extends JFrame {

  // Constants
  public static final int BOARD_ROW = 7;
  public static final int BOARD_COL = 7;

  // Array representing the playing grid
  private Cell cell_grid[][] = new Cell[BOARD_ROW][BOARD_COL];

  private JLabel attempt_lbl;
  private int attempt_num = 0;

  private Ship ship_one = new Ship();
  private Ship ship_two = new Ship();
  private Ship ship_three = new Ship();

  public Battleship() {

    setTitle("Battleship");
    getContentPane().setBackground(Color.LIGHT_GRAY);

    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;

    JPanel cell_panel = new JPanel(new GridLayout(BOARD_ROW, BOARD_COL));
    cell_panel.setPreferredSize(new Dimension(500, 500));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.PAGE_START;
    add(cell_panel, gbc);

    attempt_lbl = new JLabel("Attempts: " + attempt_num);
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.PAGE_END;
    add(attempt_lbl, gbc);

    // Create Cell Grid
    for (int r = 0; r < BOARD_ROW; r++) {
      for (int c = 0; c < BOARD_COL; c++) {
        cell_grid[r][c] = new Cell();
        cell_grid[r][c].addMouseListener(new Listener(r, c));
        cell_panel.add(cell_grid[r][c]);
      }
    }

    // Add Ships To Random Cells
    ship_one.cells = addShip(1);
    ship_two.cells = addShip(2);
    ship_three.cells = addShip(3);

    setMinimumSize(new Dimension(500, 500));

    pack();
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public enum Orientation { Horizontal, Vertical }

  public Cell[] addShip(int size) {

    Cell[] result = new Cell[size];

    // Select a random orientation for ship.
    Orientation o = Orientation.values()[(int)(Math.random() * 2)];

    // Select random starting points for our 'a' and 'b' coordinates.
    int start_x = ((int)(Math.random() * (BOARD_COL - size)));
    int start_y = ((int)(Math.random() * (BOARD_ROW - size)));

    // Loop through the grid
    for (int x = start_x; x < BOARD_COL; x++) {
      for (int y = start_y; y < BOARD_ROW; y++) {

        // Create a flag for the inner loop to modify
        // in case we need skip over cell.
        // eg. An Overlap or OOB
        boolean skip = false;

        // Check 'size' cells to determine if they are
        // occupied or if we go OOB.
        for (int c = 0; c < size; c++) {
          if (o.equals(Orientation.Vertical)) {
            if (((x + c) >= BOARD_COL) || cell_grid[x + c][y].isOccupied) {
              skip = true;
              break;
            }
          } else {
            if (((y + c) >= BOARD_ROW) || cell_grid[x][y + c].isOccupied) {
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
            Cell cell = cell_grid[x + c][y];
            cell.isOccupied = true;
            result[c] = cell;
          } else {
            Cell cell = cell_grid[x][y + c];
            cell.isOccupied = true;
            result[c] = cell;
          }
        }

        // Our job here is done.
        return result;
      }
    }

    return result;
  }

  public static class Ship {
    public int times_hit;
    public Cell[] cells;
  }

  public static class Cell extends JPanel {

    public static final Color BG_NORMAL = Color.GRAY;
    public static final Color BG_HOVER = Color.LIGHT_GRAY;
    public static final Color BG_HIT = Color.WHITE;
    public static final Color BG_KILLED = Color.GREEN;
    public static final Color BG_MISS = Color.RED;

    public boolean isOccupied = false;
    public boolean isHit = false;
    public boolean isKilled = false;

    public Cell() {
      setOpaque(true);
      setBackground(BG_NORMAL);
      setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    // DEBUG - Show Which Cells Are Occupied
    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (isOccupied) {
        g.fillRect(0, 0, 10, 10);
      }
    }
  }

  // Mouse Movements and Calculations
  public class Listener implements MouseListener {

    private Cell cell;

    public Listener(int row, int col) { cell = cell_grid[row][col]; }

    public void mouseClicked(MouseEvent e) {

      if (cell.isHit || cell.isKilled) {
        return;
      }

      attempt_num++;
      attempt_lbl.setText("Attempts: " + attempt_num);

      cell.isHit = true;
      cell.setBackground(cell.isOccupied ? Cell.BG_HIT : Cell.BG_MISS);

      for (Cell c : ship_one.cells) {
        if (c == cell) {
          ship_one.times_hit++;
          break;
        }
      }

      if (ship_one.times_hit == 1)
        for (Cell c : ship_one.cells)
          c.setBackground(Cell.BG_KILLED);

      for (Cell c : ship_two.cells) {
        if (c == cell) {
          ship_two.times_hit++;
          break;
        }
      }

      if (ship_two.times_hit == 2)
        for (Cell c : ship_two.cells)
          c.setBackground(Cell.BG_KILLED);

      for (Cell c : ship_three.cells) {
        if (c == cell) {
          ship_three.times_hit++;
          break;
        }
      }

      if (ship_three.times_hit == 3)
        for (Cell c : ship_three.cells)
          c.setBackground(Cell.BG_KILLED);
    }

    public void mouseEntered(MouseEvent e) {

      if (cell.isHit) {
        return;
      }

      cell.setBackground(Cell.BG_HOVER);
    }

    public void mouseExited(MouseEvent e) {

      if (cell.isHit) {
        return;
      }

      cell.setBackground(Cell.BG_NORMAL);
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
  }

  public static void main(String[] args) { new Battleship(); }
}
