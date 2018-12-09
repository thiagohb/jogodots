package jogodots;

public class Board {

  private boolean horizontalLines [][];
  private boolean verticalLines [][];
  private int cells [][];
  private int rows;
  private int columns;

  public Board(int rows, int columns){

    horizontalLines = new boolean [rows + 1][columns];
    verticalLines = new boolean [rows][columns + 1];
    cells = new int [rows][columns];

    this.rows = rows;
    this.columns = columns;

    //Initialize matrixes
    for(int i=0; i <= rows; i++)
       for(int j=0; j < columns; j++)
         horizontalLines[i][j] = false;

    for(int i=0; i < rows; i++)
       for(int j=0; j <= columns; j++)
         verticalLines[i][j] = false;

    for(int i=0; i < rows; i++)
       for(int j=0; j < columns; j++)
         cells[i][j] = 0;

  }

  public void initBoard(){


    for(int i=0; i <= rows; i++)
       for(int j=0; j < columns; j++)
         horizontalLines[i][j] = false;

    for(int i=0; i < rows; i++)
       for(int j=0; j <= columns; j++)
         verticalLines[i][j] = false;

    for(int i=0; i < rows; i++)
       for(int j=0; j < columns; j++)
         cells[i][j] = 0;

  }

  public void setHorizontalDash(int row, int column){
    horizontalLines [row][column] = true;
  }

  public void setVerticalDash(int row, int column){
    verticalLines [row][column] = true;
  }

  public void setCellPlayer(int row, int column, int player){
    cells [row][column] = player;
  }

  public boolean getHorizontalDash(int row, int column){
    return horizontalLines [row][column];
  }

  public boolean getVerticalDash(int row, int column){
    return verticalLines [row][column];
  }

  public int getCellPlayer(int row, int column){
    return cells [row][column];
  }

  //Return if the markers matrix is completed
  public boolean isBoardCompleted(){

     for(int i=0; i < rows; i++)
       for(int j=0; j < columns; j++)
         if (cells[i][j] == 0) return false;

    return true;
  }

  //Return how many points the player has.
  public int getNumberOfPoints(int player){

     int points = 0;

     for(int i=0; i < rows; i++)
       for(int j=0; j < columns; j++)
          if (cells[i][j] == player) points++;

     return points;
  }

  public boolean markDash(int row, int column, int player, char dash){

    // Closed squares at upper left boarder
    if ( ((row == 0) && (dash == 'H')) || ((column == 0) && (dash == 'V'))){

      if (dash == 'H') {
        if ((horizontalLines [row + 1][column]) && (verticalLines [row][column]) && (verticalLines [row][column + 1])){
          cells[row][column] = player+1;
          return true;
        }
      }

      if (dash == 'V') {
        if ((verticalLines [row][column + 1]) && (horizontalLines [row][column]) && (horizontalLines [row + 1][column])){
         cells[row][column] = player+1;
         return true;
        }
      }
    }

    // Closed squares at botton right boarder
    else if (((row == rows) && (dash == 'H')) || ((column == columns) && (dash == 'V'))){

      if (dash == 'H') {
       if ((horizontalLines [row - 1][column]) && (verticalLines [row - 1][column]) && (verticalLines [row - 1][column + 1])){
         cells[row - 1][column] = player+1;
         return true;
       }
      }

      if (dash == 'V') {
       if ((verticalLines [row][column - 1]) && (horizontalLines [row][column - 1]) && (horizontalLines [row + 1][column - 1])){
         cells[row][column - 1] = player+1;
         return true;
       }
      }

    }

    // Closed inner boarder squares
    else{

      if (dash == 'H'){
      // Closed upper and botton squares
       if ((horizontalLines [row - 1][column]) && (horizontalLines [row + 1][column]) &&
           (verticalLines [row - 1][column]) && (verticalLines [row - 1][column + 1]) &&
           (verticalLines [row][column]) && (verticalLines [row][column + 1])){
         cells[row - 1][column] = player+1;
         cells[row][column] = player+1;
         return true;
       }
       // Closed only upper square
       else if ((horizontalLines [row - 1][column]) && (verticalLines [row - 1][column]) && (verticalLines [row - 1][column + 1])){
               cells[row - 1][column] = player+1;
               return true;
            }
            // Closed only booton square
            else if ((horizontalLines [row + 1][column]) && (verticalLines [row][column]) && (verticalLines [row][column + 1])){
                    cells[row][column] = player+1;
                    return true;
                 }

      }

      if (dash == 'V') {
        // Closed left and right squares
        if ((verticalLines [row][column - 1]) && (verticalLines [row][column + 1])&&
            (horizontalLines [row][column - 1]) && (horizontalLines [row + 1][column - 1]) &&
            (horizontalLines [row][column]) && (horizontalLines [row + 1][column])){
           cells[row][column - 1] = player+1;
           cells[row][column] = player+1;
           return true;
        }
        // Closed only left square
        else if ((verticalLines [row][column - 1]) && (horizontalLines [row][column - 1]) && (horizontalLines [row + 1][column - 1])){
                 cells[row][column - 1] = player+1;
                 return true;
             }
            // Closed only right square
             else if((verticalLines [row][column + 1]) && (horizontalLines [row][column]) && (horizontalLines [row + 1][column])){
                      cells[row][column] = player+1;
                      return true;
                  }

        }
     }
     return false;
  }

   // Return if closed one square
   public boolean isClosed(int row, int column, char dash){

    // Closed squares at upper left boarder
    if ( ((row == 0) && (dash == 'H')) || ((column == 0) && (dash == 'V'))){

      if (dash == 'H') {
        if ((horizontalLines [row + 1][column]) && (verticalLines [row][column]) && (verticalLines [row][column + 1]))
           return true;
      }

      if (dash == 'V') {
        if ((verticalLines [row][column + 1]) && (horizontalLines [row][column]) && (horizontalLines [row + 1][column]))
           return true;
      }
    }

    // Closed squares at botton right boarder
    else if (((row == rows) && (dash == 'H')) || ((column == columns) && (dash == 'V'))){

      if (dash == 'H') {
       if ((horizontalLines [row - 1][column]) && (verticalLines [row - 1][column]) && (verticalLines [row - 1][column + 1]))
         return true;
      }

      if (dash == 'V') {
       if ((verticalLines [row][column - 1]) && (horizontalLines [row][column - 1]) && (horizontalLines [row + 1][column - 1]))
          return true;
      }

    }

    // Closed inner boarder squares
    else{

      if (dash == 'H'){
       // Closed upper and botton squares
       if ((horizontalLines [row - 1][column]) && (horizontalLines [row + 1][column]) &&
           (verticalLines [row - 1][column]) && (verticalLines [row - 1][column + 1]) &&
           (verticalLines [row][column]) && (verticalLines [row][column + 1]))
         return true;

       // Closed only upper square
       else if ((horizontalLines [row - 1][column]) && (verticalLines [row - 1][column]) && (verticalLines [row - 1][column + 1]))
               return true;

            // Closed only botton square
            else if ((horizontalLines [row + 1][column]) && (verticalLines [row][column]) && (verticalLines [row][column + 1]))
                    return true;


      }

      if (dash == 'V') {
        // Closed left and right squares
        if ((verticalLines [row][column - 1]) && (verticalLines [row][column + 1])&&
            (horizontalLines [row][column - 1]) && (horizontalLines [row + 1][column - 1]) &&
            (horizontalLines [row][column]) && (horizontalLines [row + 1][column]))
           return true;

        // Closed only left square
        else if ((verticalLines [row][column - 1]) && (horizontalLines [row][column - 1]) && (horizontalLines [row + 1][column - 1]))
                 return true;

            // Closed only right square
             else if((verticalLines [row][column + 1]) && (horizontalLines [row][column]) && (horizontalLines [row + 1][column]))
                      return true;

        }
     }
     return false;
  }

}