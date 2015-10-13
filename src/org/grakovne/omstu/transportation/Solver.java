package org.grakovne.omstu.transportation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GrakovNe on 13.10.2015.
 */
public class Solver {
    MainWindow mainWindow;
    DebugLogger logger;


    Solver(MainWindow mainWindow, DebugLogger logger) {
        if (mainWindow != null) {
            this.mainWindow = mainWindow;
        }

        if (logger != null) {
            this.logger = logger;
        }
    }

    public List solveTask(int maxIterations) {
        List result = new ArrayList();
        MathCore core = new MathCore(mainWindow.getShopNeeds(), mainWindow.getStorageStock(), mainWindow.getCostTable());

        /*Make our task closed*/
        if (core.makeClosed()) {
            logger.writeLine("Транспортная задача закрыта изначально");
        } else {
            logger.writeLine("Транспортная задача закрыта в процессе решения");
        }
        logger.separate();

        /*create first routing map*/
        logger.writeLine("Построим начальную карту перевозок:");
        logger.writeDoubleArrayInt(core.createBasicRoutes());

        /*calculate current cost*/
        logger.writeLine("Текущая стоимость перевозок: " + core.getCurrentCost());
        logger.separate();


        /*check for degeneracy and fix it*/
        if (core.checkDegeneracy()) {
            logger.writeLine("Задача не вырождена");
        } else {
            logger.writeLine("Присутствующая в задаче вырожденность исправлена");
        }
        logger.separate();

        /*start iterations. maxIterations is need for avoid looping*/
        logger.writeLine("Начинаем итерационный процесс:");
        for (int i = 0; i < 512; i++) {
            /*write some decorations*/
            logger.separate();
            logger.writeLine("####################");
            logger.writeLine(("              Итерация #" + (i + 1)));
            logger.writeLine("####################");
            logger.separate();

            /*calculate potentials for orders and storages*/
            logger.writeLine("Рассчитаем потенциалы:");
            core.calcPotintials();
            logger.writeLine("Для поставшиков:  (" + core.getStockPotentials() + ")");
            logger.writeLine("Для заказчиков:     (" + core.getOrdersPotentials() + ")");
            logger.separate();

            /*calculate deltas for every cell*/
            core.calcDeltas();

            /*find minimal delta*/
            logger.write("Отыщем минимальное Δ: ");
            logger.writeLine(String.valueOf(core.getMinimalDelta()));
            logger.separate();

            /*check the iterative process to perfection*/
            if (core.getMinimalDelta() >= 0) {
                logger.writeLine("####################");
                logger.separate();
                logger.writeLine("Все значения Δ неотрицательны\nОптимизация завершена!");
                logger.writeLine("Текущая стоимость: " + core.getCurrentCost());
                result.add(core.getCurrentCost());
                result.add(core.getCurrentRoutes());
                result.add(true);
                return result;
            }

            /*make a cyclical route on basic cells*/
            logger.writeLine("Построим замкнутый цикл:");
            List<int[]> cycle = core.getCycle(core.getMinimalDeltaCoords());
            logger.writePath(cycle);
            logger.separate();

            /*redistribute routing*/
            logger.write("Перераспределим перевозки и просчитаем стоимость: ");
            core.redistribute();

            /*calculate current cost*/
            logger.writeLine("" + core.getCurrentCost());
        }

        /*return our data as-is. Maybe something is wrong?*/
        result.add(core.getCurrentCost());
        result.add(core.getCurrentRoutes());
        result.add(false);
        return result;
    }

}
