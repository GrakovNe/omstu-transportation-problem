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
                logger.separate();
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

                logger.writeLine("Рассчитаем Δ: ");
                logger.separate();
                logger.writeDoubleArrayInt(core.calcDeltas());

                logger.write("Отыщем минимальное Δ: ");
                int[] coords = core.getMinimalDeltaCoords();
                logger.writeSingleArrayInt(core.getMinimalDeltaCoords());
                logger.separate();

                core.getCycle(coords);







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
