package jogodots;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FrameGame extends JFrame {

  private int rows, columns;
  private Controller controller;

  private JPanel jpContent;
  private BorderLayout borderLayout1 = new BorderLayout();
  private JPanel jpSouth = new JPanel();
  private JPanel jpMessages = new JPanel();
  private GridLayout gridLayout1 = new GridLayout();
  private JLabel jlMessages = new JLabel();
  private JPanel jpButtons = new JPanel();
  private JButton jbExit = new JButton();
  private JButton jbHelp = new JButton();
  private JPanel jpBoard = new JPanel(){
    public void paint(Graphics g){
      super.paint(g);
      drawBoard(g);
    }
  };

  //Construct the frame
  public FrameGame(int rows, int columns, Controller controller) {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      this.rows = rows;
      this.columns = columns;
      this.controller = controller;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  //Component initialization
  private void jbInit() throws Exception  {

    jpContent = (JPanel) this.getContentPane();
    jpContent.setLayout(borderLayout1);
    this.setSize(new Dimension(400, 400));

    // Move the window to the center
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = this.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    this.setVisible(true);


    this.setTitle("Jogo Dots!");
    jpSouth.setLayout(gridLayout1);
    gridLayout1.setRows(2);
    jlMessages.setText("");
    jbExit.setText("Exit");
    jbExit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbExit_actionPerformed(e);
      }
    });
    jbHelp.setText("Help");
    jpContent.add(jpSouth, BorderLayout.SOUTH);
    jpSouth.add(jpMessages, null);
    jpMessages.add(jlMessages, null);
    jpSouth.add(jpButtons, null);
    jpButtons.add(jbExit, null);
    jpButtons.add(jbHelp, null);
    jpContent.add(jpBoard, BorderLayout.CENTER);

    jpBoard.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        jpBoard_mouseClicked(e);
      }
    });

    jpBoard.repaint();
    show();

  }
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }

  // Mouse events
  private void jpBoard_mouseClicked(MouseEvent e) {

   if (controller.isMyTurn()){

    Dot dot = getDot(e.getX(), e.getY());

    Dimension dimension = jpBoard.getSize();
    double height = dimension.getHeight()/(rows +2);
    double width = dimension.getWidth()/(columns +2);

    // Test if the click is inside the board
    if ((e.getX() < width) || (e.getX() > (dimension.getWidth()-width)) ||
        (e.getY() < height) || (e.getY() > (dimension.getHeight()-height))){}

    else{

      // Getting the difference more important
      int deltaX = e.getX()-dot.getX();
      int deltaY = e.getY()-dot.getY();
      int boardLine = e.getY()/((int)height);
      int boardColumn = e.getX()/((int)width);

      // Negative deltaX means the line should be drawn with the left, upper or bottom dot
      if (deltaX < 0){

        // Negative deltaY means the line should be drawn with the left or upper dot
        if (deltaY < 0){

          if ((-deltaX) > (-deltaY)) {
            // Left horizontal line
            controller.markHorizontalDash(boardLine, boardColumn - 1);
            controller.setOutput(boardLine, boardColumn - 1, 'H');
          }
          else{
            // Upper vertical line
            controller.markVerticalDash(boardLine - 1, boardColumn);
            controller.setOutput(boardLine - 1, boardColumn, 'V');
          }
        }
        // Positive deltaY means the line should be drawn with the left or bottom dot
        else {
          if ((-deltaX) > deltaY) {
            // Left horizontal line
            controller.markHorizontalDash(boardLine - 1, boardColumn - 1);
            controller.setOutput(boardLine - 1, boardColumn - 1, 'H');
          }
          else {
            // Bottom vertical line
            controller.markVerticalDash(boardLine - 1, boardColumn);
            controller.setOutput(boardLine - 1, boardColumn, 'V');
          }
        }
      }

      // Positive deltaX means the line should be drawn with the right, upper or bottom dot
      else{

        // Negative deltaY means the line should be drawn with the right or upper dot
        if (deltaY < 0){
          if (deltaX > (-deltaY)){
            // Right horizontal line
            controller.markHorizontalDash(boardLine, boardColumn - 1);
            controller.setOutput(boardLine, boardColumn - 1, 'H');
          }
          else {
            // Upper vertical line
            controller.markVerticalDash(boardLine - 1, boardColumn - 1);
            controller.setOutput(boardLine - 1, boardColumn - 1, 'V');

          }
        }
        // Positive deltaY means the line should be drawn with the right or bottom dot
        else {
          if (deltaX > deltaY) {
            // Right horizontal line
            controller.markHorizontalDash(boardLine - 1, boardColumn - 1);
            controller.setOutput(boardLine - 1, boardColumn - 1, 'H');
          }
          else {
            // Bottom vertical line
            controller.markVerticalDash(boardLine - 1, boardColumn-1);
            controller.setOutput(boardLine - 1, boardColumn - 1, 'V');
          }
        }
      }

      jpBoard.repaint();
    }
   }
  }

  private void drawBoard(Graphics graphics){

    Dimension dimension = jpBoard.getSize();
    double height = dimension.getHeight()/(rows +2);
    double width = dimension.getWidth()/(columns +2);
    int verticalPadding = (int)height/5;
    int horizontalPadding = (int)width/5;


    // Draw the dots
    for (double i = width; i <= ((columns +1)*width) + (width/10); i += width)
      for (double j = height; j <= ((rows +1)*height) + (height/10); j += height)
        graphics.fillOval((int)i, (int)j, 5, 5);

    // Draw the horizontal dashes
    for (double i = width; i <= columns *width; i += width)
      for (double j = height; j <= (rows +1)*height; j += height)
        if (controller.isHorizontalDash(((int)j/((int)height))-1, ((int)i/((int)width))-1))
          graphics.drawLine((int)i, (int)(j+2), (int)(i+width), (int)(j+2));


    // Draw the vertical dashes
    for (double i = width; i <= (columns +1)*width; i += width)
      for (double j = height; j <= rows *height; j += height)
        if (controller.isVerticalDash(((int)j/((int)height))-1, ((int)i/((int)width))-1))
          graphics.drawLine((int)i+2, (int)j, (int)(i+2), (int)(j+height));


    // Draw the markers
    for (double i = width; i <= columns *width; i += width)
      for (double j = height; j <= rows *height; j += height)
        if (controller.getMarkers(((int)j/((int)height))-1, ((int)i/((int)width))-1) == 1){
          graphics.setColor(Color.red);
          graphics.drawOval((int)(i + horizontalPadding), (int)(j + verticalPadding), ((int)width) - (horizontalPadding + horizontalPadding/3), ((int)height) - (verticalPadding + verticalPadding/3));
        }
        else if (controller.getMarkers(((int)j/((int)height))-1, ((int)i/((int)width))-1) == 2){
          graphics.setColor(Color.blue);
          graphics.drawOval((int)(i + horizontalPadding), (int)(j + verticalPadding), ((int)width) - (horizontalPadding + horizontalPadding/3), ((int)height) - (verticalPadding + verticalPadding/3));
        }
  }

  private Dot getDot(int x, int y){

    Dimension dimension = jpBoard.getSize();
    double height = dimension.getHeight()/(rows +2);
    double width = dimension.getWidth()/(columns +2);
    // remainders of division
    double rx, ry;
    // quotient of division
    int dx, dy;
    Dot dot = new Dot();

    rx = ((double) x) % width;
    ry = ((double) y) % height;

    dx = (int)((double) x / width);
    dy = (int)((double)y / height);

    if ((rx >= width / 2.0) && (dx <= rows)) dot.setX((dx+1)*((int)width));
    else  dot.setX(dx*((int)width));

    if ((ry >= height / 2.0) && (dy <= columns)) dot.setY((dy+1)*((int)height));
    else dot.setY(dy*((int)height));

    return dot;
  }

  void jbExit_actionPerformed(ActionEvent e) {
       controller.setOutput(-1,-1,'F');
  }

  public void updateLabel(String s){
    jlMessages.setText(s);
  }

  public void updateScreen(){
    jpBoard.repaint();
  }
  public void setBoardSize(int boardSize){
    rows = columns = boardSize;
    updateScreen();
  }
}