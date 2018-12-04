package jogodots;

public class Tabuleiro {

  private boolean lHorizontais [][];
  private boolean lVerticais [][];
  private int marcadores [][];
  private int linhas;
  private int colunas;

  public Tabuleiro(int linhas, int colunas){

    lHorizontais = new boolean [linhas + 1][colunas];
    lVerticais = new boolean [linhas][colunas + 1];
    marcadores = new int [linhas][colunas];

    this.linhas = linhas;
    this.colunas = colunas;

    //Inicializando matrizes
    for(int i=0; i <= linhas; i++)
       for(int j=0; j < colunas; j++)
         lHorizontais[i][j] = false;

    for(int i=0; i < linhas; i++)
       for(int j=0; j <= colunas; j++)
         lVerticais[i][j] = false;

    for(int i=0; i < linhas; i++)
       for(int j=0; j < colunas; j++)
         marcadores[i][j] = 0;

  }

  public void limpaTabuleiro(){


    for(int i=0; i <= linhas; i++)
       for(int j=0; j < colunas; j++)
         lHorizontais[i][j] = false;

    for(int i=0; i < linhas; i++)
       for(int j=0; j <= colunas; j++)
         lVerticais[i][j] = false;

    for(int i=0; i < linhas; i++)
       for(int j=0; j < colunas; j++)
         marcadores[i][j] = 0;

  }

  public void setHorizontais(int linha, int coluna){
    lHorizontais [linha][coluna] = true;
  }

  public void setVerticais(int linha, int coluna){
    lVerticais [linha][coluna] = true;
  }

  public void setMarcadores(int linha, int coluna, int jogador){
    marcadores [linha][coluna] = jogador;
  }

  public boolean getHorizontais(int linha, int coluna){
    return lHorizontais [linha][coluna];
  }

  public boolean getVerticais(int linha, int coluna){
    return lVerticais [linha][coluna];
  }

  public int getMarcadores(int linha, int coluna){
    return marcadores [linha][coluna];
  }

  //Retorna se a matriz marcadores está completa
  public boolean completo(){

     for(int i=0; i < linhas; i++)
       for(int j=0; j < colunas; j++)
         if (marcadores[i][j] == 0) return false;

    return true;
  }

  //Retorna quantos pontos tem o jogador
  public int getPontos(int jogador){

     int pontos = 0;

     for(int i=0; i < linhas; i++)
       for(int j=0; j < colunas; j++)
          if (marcadores[i][j] == jogador) pontos++;

     return pontos;
  }

  public boolean fechouQuadrado(int l, int c, int jogador, char matriz){

    //fechou quadrados das bordas da esquerda e acima
    if ( ((l == 0) && (matriz == 'H')) || ((c == 0) && (matriz == 'V'))){

      if (matriz == 'H') {
        if ((lHorizontais [l + 1][c]) && (lVerticais [l][c]) && (lVerticais [l][c + 1])){
          marcadores[l][c] = jogador+1;
          return true;
        }
      }

      if (matriz == 'V') {
        if ((lVerticais [l][c + 1]) && (lHorizontais [l][c]) && (lHorizontais [l + 1][c])){
         marcadores[l][c] = jogador+1;
         return true;
        }
      }
    }

    //fechou quadrados das bordas da direita e abaixo
    else if (((l == linhas) && (matriz == 'H')) || ((c == colunas) && (matriz == 'V'))){

      if (matriz == 'H') {
       if ((lHorizontais [l - 1][c]) && (lVerticais [l - 1][c]) && (lVerticais [l - 1][c + 1])){
         marcadores[l - 1][c] = jogador+1;
         return true;
       }
      }

      if (matriz == 'V') {
       if ((lVerticais [l][c - 1]) && (lHorizontais [l][c - 1]) && (lHorizontais [l + 1][c - 1])){
         marcadores[l][c - 1] = jogador+1;
         return true;
       }
      }

    }

    //fechou quadrados do meio
    else{

      if (matriz == 'H'){
      //fechou os quadrados acima e abaixo
       if ((lHorizontais [l - 1][c]) && (lHorizontais [l + 1][c]) &&
           (lVerticais [l - 1][c]) && (lVerticais [l - 1][c + 1]) &&
           (lVerticais [l][c]) && (lVerticais [l][c + 1])){
         marcadores[l - 1][c] = jogador+1;
         marcadores[l][c] = jogador+1;
         return true;
       }
       //fechou somente o quadrado acima
       else if ((lHorizontais [l - 1][c]) && (lVerticais [l - 1][c]) && (lVerticais [l - 1][c + 1])){
               marcadores[l - 1][c] = jogador+1;
               return true;
            }
            //fechou somente o quadrado abaixo
            else if ((lHorizontais [l + 1][c]) && (lVerticais [l][c]) && (lVerticais [l][c + 1])){
                    marcadores[l][c] = jogador+1;
                    return true;
                 }

      }

      if (matriz == 'V') {
        //fechou os quadrados direito e esquerdo
        if ((lVerticais [l][c - 1]) && (lVerticais [l][c + 1])&&
            (lHorizontais [l][c - 1]) && (lHorizontais [l + 1][c - 1]) &&
            (lHorizontais [l][c]) && (lHorizontais [l + 1][c])){
           marcadores[l][c - 1] = jogador+1;
           marcadores[l][c] = jogador+1;
           return true;
        }
        //fechou o quadrado esquerdo
        else if ((lVerticais [l][c - 1]) && (lHorizontais [l][c - 1]) && (lHorizontais [l + 1][c - 1])){
                 marcadores[l][c - 1] = jogador+1;
                 return true;
             }
            //fechou o quadrado direito
             else if((lVerticais [l][c + 1]) && (lHorizontais [l][c]) && (lHorizontais [l + 1][c])){
                      marcadores[l][c] = jogador+1;
                      return true;
                  }

        }
     }
     return false;
  }

   //Retorna se fechou um quadrado
   public boolean fechado(int l, int c, char matriz){

    //fechou quadrados das bordas da esquerda e acima
    if ( ((l == 0) && (matriz == 'H')) || ((c == 0) && (matriz == 'V'))){

      if (matriz == 'H') {
        if ((lHorizontais [l + 1][c]) && (lVerticais [l][c]) && (lVerticais [l][c + 1]))
           return true;
      }

      if (matriz == 'V') {
        if ((lVerticais [l][c + 1]) && (lHorizontais [l][c]) && (lHorizontais [l + 1][c]))
           return true;
      }
    }

    //fechou quadrados das bordas da direita e abaixo
    else if (((l == linhas) && (matriz == 'H')) || ((c == colunas) && (matriz == 'V'))){

      if (matriz == 'H') {
       if ((lHorizontais [l - 1][c]) && (lVerticais [l - 1][c]) && (lVerticais [l - 1][c + 1]))
         return true;
      }

      if (matriz == 'V') {
       if ((lVerticais [l][c - 1]) && (lHorizontais [l][c - 1]) && (lHorizontais [l + 1][c - 1]))
          return true;
      }

    }

    //fechou quadrados do meio
    else{

      if (matriz == 'H'){
      //fechou os quadrados acima e abaixo
       if ((lHorizontais [l - 1][c]) && (lHorizontais [l + 1][c]) &&
           (lVerticais [l - 1][c]) && (lVerticais [l - 1][c + 1]) &&
           (lVerticais [l][c]) && (lVerticais [l][c + 1]))
         return true;

       //fechou somente o quadrado acima
       else if ((lHorizontais [l - 1][c]) && (lVerticais [l - 1][c]) && (lVerticais [l - 1][c + 1]))
               return true;

            //fechou somente o quadrado abaixo
            else if ((lHorizontais [l + 1][c]) && (lVerticais [l][c]) && (lVerticais [l][c + 1]))
                    return true;


      }

      if (matriz == 'V') {
        //fechou os quadrados direito e esquerdo
        if ((lVerticais [l][c - 1]) && (lVerticais [l][c + 1])&&
            (lHorizontais [l][c - 1]) && (lHorizontais [l + 1][c - 1]) &&
            (lHorizontais [l][c]) && (lHorizontais [l + 1][c]))
           return true;

        //fechou o quadrado esquerdo
        else if ((lVerticais [l][c - 1]) && (lHorizontais [l][c - 1]) && (lHorizontais [l + 1][c - 1]))
                 return true;

            //fechou o quadrado direito
             else if((lVerticais [l][c + 1]) && (lHorizontais [l][c]) && (lHorizontais [l + 1][c]))
                      return true;

        }
     }
     return false;
  }

}