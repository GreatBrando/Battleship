import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Battleship extends JFrame {

  // Constants
  public static final boolean DEBUG = true;

  public static final int BOARD_ROW = 20;
  public static final int BOARD_COL = 20;

  // Array representing the playing grid
  private Cell cell_grid[][] = new Cell[BOARD_ROW][BOARD_COL];

  private JLabel score_label = null;

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

    score_label = new JLabel("Score: 0");
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.PAGE_END;
    add(score_label, gbc);

    // Create Cell Grid
    for (int r = 0; r < BOARD_ROW; r++) {
      for (int c = 0; c < BOARD_COL; c++) {
        cell_grid[r][c] = new Cell();
        cell_grid[r][c].addMouseListener(new Listener(r, c));
        cell_panel.add(cell_grid[r][c]);
      }
    }

    // Add Ships To Random Cells
    addShip(1);
    addShip(2);
    addShip(3);

    setMinimumSize(new Dimension(500, 500));

    pack();
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public enum Orientation { Horizontal, Vertical }

  public void addShip(int size) {

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
            if (((x + c) >= BOARD_COL) ||
                cell_grid[x + c][y].state.equals(Cell.State.Occupied)) {
              skip = true;
              break;
            }
          } else {
            if (((y + c) >= BOARD_ROW) ||
                cell_grid[x][y + c].state.equals(Cell.State.Occupied)) {
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
            cell_grid[x + c][y].setState(Cell.State.Occupied);
          } else {
            cell_grid[x][y + c].setState(Cell.State.Occupied);
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
    public static final Color BG_HIT = Color.WHITE;
    public static final Color BG_KILLED = Color.GREEN;
    public static final Color BG_MISS = Color.RED;

    public static enum State { Normal, Occupied, Hit, Killed, Missed }

    public State state = State.Normal;

    public Cell() {
      setOpaque(true);
      setBackground(BG_NORMAL);
      setBorder(BorderFactory.createLineBorder(Color.BLACK));
      setPreferredSize(new Dimension(25, 25));
    }

    public void setState(State s) {

      state = s;

      switch (s) {
      case Hit:
        setBackground(BG_HIT);
        break;

      case Killed:
        setBackground(BG_KILLED);
        break;

      case Missed:
        setBackground(BG_MISS);
        break;

      case Normal:
        setBackground(BG_NORMAL);
        break;

      case Occupied:
        setBackground(BG_NORMAL);
        break;
      }
    }

    // DEBUG - Show Which Cells Are Occupied
    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (state.equals(State.Occupied))
        g.fillRect(0, 0, 5, 5);
    }
  }

  // Mouse Movements and Calculations
  public class Listener implements MouseListener {

    private Cell cell;

    public Listener(int row, int col) { cell = cell_grid[row][col]; }

    public void mouseClicked(MouseEvent e) {
      if (cell.state.equals(Cell.State.Occupied)) {
        cell.setState(Cell.State.Hit);
      }
    }

    public void mouseEntered(MouseEvent e) {
      if (cell.state.equals(Cell.State.Hit) || cell.state.equals(Cell.State.Killed))
        return;

      cell.setBackground(Cell.BG_HOVER);
    }

    public void mouseExited(MouseEvent e) {
      if (cell.state.equals(Cell.State.Hit) || cell.state.equals(Cell.State.Killed))
        return;

      cell.setBackground(Cell.BG_NORMAL);
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
  }

  public static void main(String[] args) {
    JFrame.setDefaultLookAndFeelDecorated(true);
    new Battleship();
  }
}
