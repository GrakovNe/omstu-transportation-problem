package org.grakovne.omstu.transportation;

import javax.swing.*;
import java.awt.*;
import java.util.List;


/**
 * Created by GrakovNe on 13.10.2015.
 */
public class ResultWindow extends JFrame {
    JLabel resultFinishedHint, currentCost;
    JTable routingTable;
    JScrollPane routingTableWrapper;
    JSeparator bottomSeparator;

    int resultCost;
    int[][] resultRoutes;
    boolean isResultCorrect;

    ResultWindow(List data) {
        super("TRANSPORTATION PROBLEM SOLVER 1.00 - RESULTS");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("can't apply native interface");
        }
        setLayout(null);
        setBounds(350, 150, 500, 500);

        setResizable(false);
        setVisible(true);

        bottomSeparator = new JSeparator(JSeparator.HORIZONTAL);
        bottomSeparator.setBounds(0, 400, 500, 10);
        add(bottomSeparator);


        try {
            resultCost = (int) data.get(0);
            resultRoutes = (int[][]) data.get(1);
            isResultCorrect = (boolean) data.get(2);
        } catch (Exception ex) {
            System.err.println("incorrect data for result table");
        }

        currentCost = new JLabel("ИТОГОВАЯ МИНИМАЛЬНАЯ СТОИМОСТЬ: " + resultCost);
        currentCost.setBounds(20, 400, 500, 40);
        currentCost.setFont(new Font("Tahoma", Font.PLAIN, 20));
        add(currentCost);

        routingTable = new JTable(resultRoutes.length, resultRoutes[0].length + 1);
        routingTable.setRowSelectionAllowed(false);
        routingTable.getColumnModel().getColumn(0).setHeaderValue((""));

        for (int i = 1; i < resultRoutes[0].length + 1; i++) {
            routingTable.getColumnModel().getColumn(i).setHeaderValue("Магазин №" + i);
        }

        for (int i = 0; i < resultRoutes.length; i++) {
            routingTable.setValueAt("Склад №" + (i + 1), i, 0);
        }

        for (int i = 0; i < resultRoutes.length; i++) {
            for (int j = 0; j < resultRoutes[0].length; j++) {
                if (resultRoutes[i][j] > 0) {
                    routingTable.setValueAt(resultRoutes[i][j], i, j + 1);
                }

            }
        }

        resultFinishedHint = new JLabel("");
        resultFinishedHint.setBounds(20, 435, 500, 30);
        resultFinishedHint.setFont(new Font("Tahoma", Font.PLAIN, 20));

        if (isResultCorrect) {
            resultFinishedHint.setText("РАСЧЕТ УСПЕШНО ЗАВЕРШЕН");
            resultFinishedHint.setForeground(new Color(0, 128, 0));
        } else {
            resultFinishedHint.setText("РАСЧЕТ ЗАВЕРШЕН С ОШИБКАМИ");
            resultFinishedHint.setForeground(new Color(128, 0, 0));
        }
        add(resultFinishedHint);


        routingTableWrapper = new JScrollPane(routingTable);
        routingTableWrapper.setBounds(10, 10, 475, 380);
        add(routingTableWrapper);


    }
}
