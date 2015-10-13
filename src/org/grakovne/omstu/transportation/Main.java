package org.grakovne.omstu.transportation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Transportation problem solver
 * OmSTU project
 * Author: GrakovNe
 * date: 08.10.2015
 */

public class Main {

    public static void main (String[] args){
        MainWindow mainWindow = new MainWindow();
        DebugLogger logger = new DebugLogger(mainWindow.consoleArea);
        List solution;

        mainWindow.solveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Solver solver = new Solver(mainWindow, logger);
                solver.solveTask(500);
            }
        });

        mainWindow.exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        mainWindow.clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.clearArea();
            }
        });

    }
}
