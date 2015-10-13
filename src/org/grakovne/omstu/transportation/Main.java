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
        Window mainWindow = new Window();
        mainWindow.setVisible(true);
        mainWindow.setResizable(false);
        DebugLogger logger = new DebugLogger(mainWindow.consoleArea);


        mainWindow.solveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MathCore core = new MathCore(mainWindow.getShopNeeds(), mainWindow.getStorageStock(), mainWindow.getCostTable());
                if (core.makeClosed()){
                    logger.writeLine("Транспортная задача закрыта изначально");
                }
                else {
                    logger.writeLine("Транспортная задача закрыта в процессе решения");
                }
                logger.separate();

                logger.writeLine("Построим начальную карту перевозок:");
                logger.writeDoubleArrayInt(core.createBasicRoutes());
                logger.writeLine("Текущая стоимость перевозок: " + core.getCurrentCost());
                logger.separate();

                if (core.checkDegeneracy()){
                    logger.writeLine("Задача не вырождена");
                }
                else{
                    logger.writeLine("Присутствующая в задаче вырожденность исправлена");
                }

                logger.separate();
                logger.writeLine("Рассчитаем потенциалы:");
                core.calcPotintials();
                logger.writeLine("Для поставшиков:  (" + core.getStockPotentials() + ")");
                logger.writeLine("Для заказчиков:     (" + core.getOrdersPotentials() + ")");
                logger.separate();

                core.calcDeltas();

                logger.write("Отыщем минимальное Δ: ");
                logger.writeSingleArrayInt(core.getMinimalDeltaCoords());
                logger.separate();

                logger.writeLine("Построим замкнутый цикл:");
                List<int[]> cycle = core.getCycle(core.getMinimalDeltaCoords());
                logger.writePath(cycle);
                logger.separate();

                logger.writeLine("Перераспределим перевозки:");
                core.redistribute();
                logger.writeDoubleArrayInt(core.getCurrentRoutes());
                logger.writeLine("Текущая стоимость перевозок: " + core.getCurrentCost());
                logger.separate();


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
