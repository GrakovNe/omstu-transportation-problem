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

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        DebugLogger logger = new DebugLogger(mainWindow.consoleArea);

        mainWindow.solveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Solver solver = new Solver(mainWindow, logger);
                List resultData = new ArrayList();
                for (int i = 0; i < 5; i++) {
                    logger.clearArea();
                    resultData = solver.solveTask(64);
                    if ((boolean)resultData.get(2)){
                        break;
                    }
                }
                ResultWindow resultWindow = new ResultWindow(resultData);
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
                mainWindow.clearTables();
            }
        });

    }
}
