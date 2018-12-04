package jogodots;

import javax.swing.*;
import java.net.*;
import java.io.*;

public class Jogador extends Thread{

  private Socket conexao;
  private DataInputStream input;
  private DataOutputStream output;
  private FrameServidorDots control;
  boolean threadSuspended = true;
  private int numJogador;


  public Jogador(Socket s, FrameServidorDots d, int n) {

    conexao = s;

    try{

      input = new DataInputStream(conexao.getInputStream());
      output = new DataOutputStream(conexao.getOutputStream());

    }
    catch(IOException e){
      JOptionPane.showMessageDialog(null, "Ocorreu um erro durante a conexão!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
      System.exit(1);
    }

    control = d;
    numJogador = n;
  }

  public void jogadaAdversario(int linha, int coluna, char matriz){

    try{
      output.writeUTF("Jogada do Adversário");
      output.writeChar(matriz);
      output.writeInt(linha);
      output.writeInt(coluna);

    }
    catch(IOException e){
     JOptionPane.showMessageDialog(null, "Ocorreu um erro durante a conexão!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
    }

  }

  public void run(){

    boolean sair = false;

    try{
      control.display(numJogador);
      output.writeInt(numJogador);
      output.writeUTF("Jogador " + (numJogador + 1) + ", por favor espere");

      //espera outro jogador conectar
      if(numJogador == 0){

        output.writeUTF("Esperando por outro jogador");

        try{
          synchronized (this){
            while(threadSuspended) wait();
          }
        }
        catch (InterruptedException e){

        }
        output.writeUTF("Outro jogador conectado. Sua Vez");
      }

      //jogando
      while (!sair){
        char matriz = input.readChar();
        int linha = input.readInt();
        int coluna = input.readInt();

        if (matriz == 'F'){
          sair = true;
          control.finalizou();
        }

        else{

          if(control.movimentoValido(linha, coluna, numJogador, matriz)){
            output.writeUTF("Movimento Válido");
            output.writeBoolean(control.fechado(linha, coluna, matriz));
          }
          else
            output.writeUTF("Movimento Inválido");
        }

        if(control.gameOver()) sair = true;

      }

    }

    catch (IOException e){

    }

  }
  public void avisaGameOver(){
    try{
      output.writeUTF("Game Over");
      conexao.close();
    }
    catch (IOException e){
      e.printStackTrace();
      System.exit(1);
    }
  }

  public void avisaFinalizou(){
    try{
      output.writeUTF("Finalizou");
      conexao.close();
    }
    catch (IOException e){

    }
  }

  public int perguntaTamanho(){
    boolean flag = true;
    int tamanho = 0;

    try{
      output.writeUTF("Tamanho do tabuleiro");
    }

    catch(IOException e){
        e.printStackTrace();
    }

    while (flag){
      try{
        tamanho = input.readInt();
        flag = false;
      }
      catch(IOException e){

      }
    }

    return tamanho;
  }

  public void setTamanho(int tamanho){
     try{
        output.writeUTF("Pegar tamanho do tabuleiro");
        output.writeInt(tamanho);
     }
     catch(IOException e){

     }

  }

}