package org.grakovne.omstu.transportation;

import javafx.scene.layout.Pane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;

/**
 * @author GrakovNe
 * @version 1.00
 *          creates main application window
 *          all JComponents are available from package
 *          please, make this window non-resizable
 */

public class MainWindow extends JFrame {
    JButton solveBtn, clearBtn, exitBtn;
    JTextArea consoleArea;
    private JSeparator vericalSeparator;
    private JLabel shopNumHint, storageNumHint, consoleAreaHint, storageStockHint, shopNeedsHint, costHiht;
    private JSlider storageNumBar, shopNumBar;
    private JTable shopNeeds, storageStock, mainTable;
    private JScrollPane shopNeedsWrapper, storageStockWparrer, mainTableWrapper, consoleAreaScroller;

    public MainWindow() {
        super("TRANSPORTATION PROBLEM SOLVER 1.00");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("can't apply native interface");
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(300, 100, 605, 600);
        setLayout(null);

        solveBtn = new JButton("Решить");
        solveBtn.setBounds(400, 470, 190, 25);
        add(solveBtn);

        clearBtn = new JButton("Очистить");
        clearBtn.setBounds(400, 500, 190, 25);
        add(clearBtn);

        exitBtn = new JButton("Выход");
        exitBtn.setBounds(400, 530, 190, 25);
        add(exitBtn);

        consoleArea = new JTextArea("");
        consoleArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
        consoleArea.setEditable(false);
        consoleArea.setWrapStyleWord(true);
        consoleArea.setLineWrap(true);
        consoleArea.setBackground(Color.white);

        consoleAreaScroller = new JScrollPane(consoleArea);
        consoleAreaScroller.setBounds(400, 30, 190, 430);
        consoleAreaScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(consoleAreaScroller);


        storageNumHint = new JLabel("Количество складов:");
        storageNumHint.setFont(new Font("Arial", Font.ITALIC, 14));
        storageNumHint.setBounds(20, 500, 200, 30);
        add(storageNumHint);

        shopNumHint = new JLabel("Количество магазинов:");
        shopNumHint.setFont(new Font("Arial", Font.ITALIC, 14));
        shopNumHint.setBounds(20, 525, 200, 30);
        add(shopNumHint);

        consoleAreaHint = new JLabel("Консоль отладки:");
        consoleAreaHint.setFont(new Font("Arial", Font.ITALIC, 14));
        consoleAreaHint.setBounds(400, 10, 190, 15);
        consoleAreaHint.setHorizontalAlignment(JLabel.CENTER);
        add(consoleAreaHint);

        shopNeedsHint = new JLabel("НЕОБХОДИМО ДОСТАВИТЬ");
        shopNeedsHint.setFont(new Font("Tahoma", Font.PLAIN, 20));
        shopNeedsHint.setBounds(10, 20, 365, 50);
        shopNeedsHint.setHorizontalAlignment(JLabel.CENTER);
        add(shopNeedsHint);

        storageStockHint = new JLabel("ТОВАРА НА СКЛАДАХ");
        storageStockHint.setFont(new Font("Tahoma", Font.PLAIN, 20));
        storageStockHint.setBounds(10, 130, 365, 50);
        storageStockHint.setHorizontalAlignment(JLabel.CENTER);
        add(storageStockHint);

        costHiht = new JLabel("ПО СТОИМОСТИ");
        costHiht.setFont(new Font("Tahoma", Font.PLAIN, 20));
        costHiht.setBounds(10, 240, 365, 50);
        costHiht.setHorizontalAlignment(JLabel.CENTER);
        add(costHiht);

        vericalSeparator = new JSeparator(JToolBar.Separator.VERTICAL);
        vericalSeparator.setBounds(390, 0, 10, 600);
        add(vericalSeparator);

        storageNumBar = new JSlider(JSlider.HORIZONTAL, 10, 1);
        storageNumBar.setBounds(180, 506, 200, 20);
        storageNumBar.setValue(1);

        storageNumBar.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                remove(storageStockWparrer);
                remove(mainTableWrapper);
                updateStorageStockTable();
                updateMainTable();
                clearTables();
            }
        });
        add(storageNumBar);

        shopNumBar = new JSlider(JSlider.HORIZONTAL, 10, 1);
        shopNumBar.setBounds(180, 531, 200, 20);
        shopNumBar.setValue(5);

        shopNumBar.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                remove(shopNeedsWrapper);
                remove(mainTableWrapper);
                updateShopNeedsTable();
                updateMainTable();
                clearTables();
            }
        });
        add(shopNumBar);

        updateShopNeedsTable();
        updateStorageStockTable();
        updateMainTable();

        setVisible(true);
        setResizable(false);


        /*just debug examples data. MUST be deleted before commit to production*/
        storageNumBar.setValue(3);
        shopNumBar.setValue(4);

        mainTable.setValueAt(2, 0, 1);
        mainTable.setValueAt(3, 0, 2);
        mainTable.setValueAt(2, 0, 3);
        mainTable.setValueAt(4, 0, 4);
        mainTable.setValueAt(3, 1, 1);
        mainTable.setValueAt(2, 1, 2);
        mainTable.setValueAt(5, 1, 3);
        mainTable.setValueAt(1, 1, 4);
        mainTable.setValueAt(4, 2, 1);
        mainTable.setValueAt(3, 2, 2);
        mainTable.setValueAt(2, 2, 3);
        mainTable.setValueAt(6, 2, 4);

        shopNeeds.setValueAt(20, 0, 0);
        shopNeeds.setValueAt(30, 0, 1);
        shopNeeds.setValueAt(30, 0, 2);
        shopNeeds.setValueAt(10, 0, 3);

        storageStock.setValueAt(30, 0, 0);
        storageStock.setValueAt(40, 0, 1);
        storageStock.setValueAt(20, 0, 2);
    }

    /**
     * Routine method, which updates size of table with shops orders
     */
    private void updateShopNeedsTable() {
        Vector<String> rowNames = new Vector<>();
        Vector rowData = new Vector();

        for (int i = 0; i < shopNumBar.getValue(); i++) {
            rowNames.add("B" + (i + 1));
            rowData.add("0");
        }

        Vector rowDataWrapper = new Vector();
        rowDataWrapper.add(rowData);

        shopNeeds = new JTable(rowDataWrapper, rowNames);
        shopNeeds.setRowHeight(0, 30);
        shopNeeds.setRowSelectionAllowed(false);
        shopNeedsWrapper = new JScrollPane(shopNeeds, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        shopNeedsWrapper.setBounds(6, 70, 376, 50);

        shopNeedsWrapper.setBorder(BorderFactory.createEmptyBorder());

        add(shopNeedsWrapper);

    }

    /**
     * Routine method, which updates size of table with storage stocks
     */
    private void updateStorageStockTable() {
        Vector<String> rowNames = new Vector<>();
        Vector rowData = new Vector();

        for (int i = 0; i < storageNumBar.getValue(); i++) {
            rowNames.add("A" + (i + 1));
            rowData.add("0");
        }

        Vector rowDataWrapper = new Vector();
        rowDataWrapper.add(rowData);

        storageStock = new JTable(rowDataWrapper, rowNames);
        storageStock.setRowHeight(0, 30);
        storageStock.setRowSelectionAllowed(false);
        storageStockWparrer = new JScrollPane(storageStock, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        storageStockWparrer.setBounds(6, 180, 376, 50);

        storageStockWparrer.setBorder(BorderFactory.createEmptyBorder());
        add(storageStockWparrer);
    }

    /**
     * Routine method, which updates size of table with cost for every route
     */
    private void updateMainTable() {
        mainTable = new JTable(storageNumBar.getValue(), shopNumBar.getValue() + 1);
        mainTable.setRowSelectionAllowed(false);
        mainTable.getColumnModel().getColumn(0).setHeaderValue((""));

        for (int i = 1; i < shopNumBar.getValue() + 1; i++) {
            mainTable.getColumnModel().getColumn(i).setHeaderValue("Магазин №" + i);
        }

        for (int i = 0; i < storageNumBar.getValue(); i++) {
            mainTable.setValueAt("Склад №" + (i + 1), i, 0);
        }

        mainTableWrapper = new JScrollPane(mainTable);
        mainTableWrapper.setBounds(6, 290, 376, 200);
        mainTableWrapper.setBorder(BorderFactory.createEmptyBorder());

        add(mainTableWrapper);
    }

    /**
     * Will take info about shops orders from JTable
     *
     * @return List with needs of every shops
     */
    public List getShopNeeds() {
        List<Integer> shopsList = new ArrayList();
        for (int i = 0; i < shopNumBar.getValue(); i++) {
            try {
                shopsList.add(Integer.parseInt(shopNeeds.getValueAt(0, i).toString()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Не все параметры магазинов указаны", "TRANSPORTATION PROBLEM SOLVER", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException("Not all options are listed for orders");
            }
        }
        return shopsList;
    }

    /**
     * Will take info about storages stocks from JTable
     *
     * @return List with stock iof every storages
     */
    public List getStorageStock() {
        List<Integer> storageList = new ArrayList();
        for (int i = 0; i < storageNumBar.getValue(); i++) {
            try {
                storageList.add(Integer.parseInt(storageStock.getValueAt(0, i).toString()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Не все параметры складов указаны", "TRANSPORTATION PROBLEM SOLVER", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException("Not all options are listed for storages");
            }
        }
        return storageList;
    }

    /**
     * Will take info about routes cost from JTable
     *
     * @return List with list with cost for route from current storage to current shop.
     * First list has contain list with cost for route to every shop. Index of first list has contain number of storage
     */
    public List getCostTable() {
        List storageCostList = new ArrayList();
        for (int i = 0; i < storageNumBar.getValue(); i++) {
            List shopCost = new ArrayList();
            for (int j = 1; j < shopNumBar.getValue() + 1; j++) {
                try {
                    Integer.parseInt(mainTable.getValueAt(i, j).toString());
                }catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Не все цены перевозок указаны", "TRANSPORTATION PROBLEM SOLVER", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException("Not all options are listed for costs");
                }

                    shopCost.add(mainTable.getValueAt(i, j));

            }
            storageCostList.add(shopCost);
        }

        return storageCostList;
    }

    /**
     * Will set 0 as value for all working cell for all working tables
     */
    public void clearTables() {
        for (int i = 0; i < mainTable.getRowCount(); i++) {
            for (int j = 1; j < mainTable.getColumnCount(); j++) {
                mainTable.setValueAt(0, i, j);
            }
        }

        for (int i = 0; i < shopNeeds.getRowCount(); i++) {
            for (int j = 1; j < shopNeeds.getColumnCount(); j++) {
                shopNeeds.setValueAt(0, i, j);
            }
        }

        for (int i = 0; i < storageStock.getRowCount(); i++) {
            for (int j = 1; j < storageStock.getColumnCount(); j++) {
                storageStock.setValueAt(0, i, j);
            }
        }

    }
}