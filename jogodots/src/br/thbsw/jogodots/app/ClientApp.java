package br.thbsw.jogodots.app;

import br.thbsw.jogodots.controller.Controller;

import javax.swing.*;

public class ClientApp {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Controller();
    }
}
