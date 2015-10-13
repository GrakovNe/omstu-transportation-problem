package org.grakovne.omstu.transportation;

import java.util.*;

/**
 * Transportaton problem sovler
 * @author GrakovNe
 * @version 1.00-SNAPSHOT
 */
public class MathCore {
    private List<Integer> orders = new ArrayList<>();
    private List<Integer> stock = new ArrayList<>();
    private List<Integer> ordersPotentials = new ArrayList<>();
    private List<Integer> stockPotentials = new ArrayList<>();
    private List<List> cost = new ArrayList<>();
    private int[][] routes;
    private int[][] deltas;
    private int currentCost = 0;

    MathCore(List orders, List stock, List cost){
        if (orders != null){
            this.orders = orders;
        }
        if (cost != null) {
            this.cost = cost;
        }
        if (stock != null) {
            this.stock = stock;
        }
    }

    /**
     * Will make our task conditions math-closed
     * Will finalize all Lists to arrays
     * @return true, if we have closed problem; false, if problem was closed in method
     */
    public boolean makeClosed(){
        if ((cost == null) || (stock == null)){
            throw new RuntimeException("empty stock or cost table element");
        }

        int orderSumm = 0, stockSumm = 0;

        for (int element: orders){
            orderSumm += element;
        }

        for (int element: stock){
            stockSumm += element;
        }

        if (stockSumm == orderSumm){
            return true; // we have closed problem
        }

        if (stockSumm > orderSumm){
            orders.add(stockSumm - orderSumm);
            for (int i = 0; i < cost.size(); i++){
                cost.get(i).add(0);
            }
        }

        if (orderSumm > stockSumm){
            stock.add(orderSumm - stockSumm);
            List <Integer> fictionCost = new ArrayList<>();
            for (int i = 0; i < cost.get(0).size(); i++){
                fictionCost.add(0);
            }
            cost.add(fictionCost);
        }

        return false; // problem was closed just now
    }

    /**
     * will create basis routing table by NW-method
     * @return int[][] routing table
     */
    public int[][] createBasicRoutes(){
        routes = new int[stock.size()][orders.size()];

        List<Integer> orders = new ArrayList<>();
        for (int element: this.orders){
            orders.add(element);
        }
        List<Integer> stock = new ArrayList<>(this.stock.size());
        for (int element: this.stock){
            stock.add(element);
        }

        for (int i = 0; i < stock.size(); i++){
           for (int j = 0; j < orders.size(); j++){
               if (stock.get(i) >= orders.get(j)){
                   routes[i][j] = orders.get(j);
                   stock.set(i, stock.get(i)-routes[i][j]);
                   orders.set(j, 0);
               }

               else if (stock.get(i) > 0) {
                   routes[i][j] = stock.get(i);
                   stock.set(i, 0);
                   orders.set(j, orders.get(j) - routes[i][j]);
               }
           }
        }

        return this.routes;
    }


    /**
     * will calculate current summary cost for thr transportation
     * Also will write this value to class-member variable
     * @return Current cost value (int)
     */
    public int getCurrentCost(){
        int summaryCost = 0;
        for(int i = 0; i < stock.size(); i++){
            for (int j = 0; j < orders.size(); j++){
                summaryCost += routes[i][j] * Integer.parseInt(cost.get(i).get(j).toString());
            }
        }
        this.currentCost = summaryCost;
        return summaryCost;
    }

    /**
     * Will check problem for degeneracy and fix it if need
     * @return true, if probles wasn't degeneracy and false, if it's
     */
    public boolean checkDegeneracy(){
        int basicSellsNum = 0;
        for (int i = 0; i < stock.size(); i++){
            for (int j = 0; j < orders.size(); j++){
                if (routes[i][j] != 0){
                    basicSellsNum++;
                }
            }
        }

        for (int i = 0; i < stock.size(); i++){
            for (int j = 0; j < orders.size(); j++){
                if (routes[i][j] == 0){
                    routes[i][j] = -1;
                }
            }
        }

        if (basicSellsNum >= (stock.size() + orders.size() - 1)){
            return true;
        }

        while (basicSellsNum < stock.size() + orders.size() - 1) {
            int[] randomCell = getRandomCell();
            while (routes[randomCell[0]][randomCell[1]] != -1) {
                randomCell = getRandomCell();
            }

            if (getCycle(randomCell).size() == 0) {
                routes[randomCell[0]][randomCell[1]] = 0;
                basicSellsNum++;
            }
        }
        return false;

    }

    /**
     * getter for read-only access to routes array
     * @return int[][] of routes
     */
    public int[][] getCurrentRoutes(){
        return this.routes;
    }

    /**
     * Will calculate potentials for orders and stock when cell is part of basis
     */
    public void calcPotintials(){
        List<Integer> ordersPotentials = new ArrayList<>();
        List<Integer> stockPotentials = new ArrayList<>();

        stockPotentials.add(0);

        for (int i = 0; i < stock.size(); i++){
            for (int j = 0; j < orders.size(); j++){
                if (routes[i][j] == -1){
                    continue;
                }

                if (stockPotentials.size() > i){
                    ordersPotentials.add(Integer.parseInt(cost.get(i).get(j).toString()) - stockPotentials.get(i));
                    continue;
                }

                if (ordersPotentials.size() > j){
                    stockPotentials.add(Integer.parseInt(cost.get(i).get(j).toString()) - ordersPotentials.get(j));
                }

            }
        }

        this.ordersPotentials = ordersPotentials;
        this.stockPotentials = stockPotentials;
    }

    /**
     * getter for orderPotentials
     * @return String with orders potentials
     */
    public String getOrdersPotentials(){
        return ordersPotentials.toString().substring(1, ordersPotentials.toString().length() - 1);
    }

    /**
     * getter for stockPotentials
     * @return String with stocks potentials
     */
    public String getStockPotentials(){
        return stockPotentials.toString().substring(1, stockPotentials.toString().length() - 1);
    }

    /**
     * Will calculate deltas for every route cell. For basic cell it's mut be zero always
     * @return int[][] deltas array of int
     */
    public int[][] calcDeltas(){
        deltas = new int[stock.size()][orders.size()];
        for (int i  = 0; i < stock.size(); i++){
            for (int j = 0; j < orders.size(); j++){
                deltas[i][j] = Integer.parseInt(cost.get(i).get(j).toString()) - stockPotentials.get(i) - ordersPotentials.get(j);
            }
        }
        return this.deltas;
    }


    /**
     * Will find minimal delta in delta table
     * @return int[2], coords of minimal deltas
     */
    public int[] getMinimalDeltaCoords(){
        int[] currentMinimalDeltaCoords = new int[2];
        int currentMinimalDelta = Integer.MAX_VALUE;
        for (int i = 0; i < stock.size(); i++){
            for (int j  = 0; j < orders.size(); j++){
                if (deltas[i][j] < currentMinimalDelta){
                    currentMinimalDelta = deltas[i][j];
                    currentMinimalDeltaCoords[0] = i;
                    currentMinimalDeltaCoords[1] = j;
                }
            }
        }
        return currentMinimalDeltaCoords;
    }




    /**
     * Will get random (really) cell in routes area
     * @return int[2] with coords
     */
    private int[] getRandomCell(){
        int[] randomCoords = new int[2];
        Random random = new Random();
        randomCoords[0] = random.nextInt(stock.size());
        randomCoords[1] = random.nextInt(orders.size());
        return randomCoords;
    }


    /**
     * IT'S template! MUST BE WRITTEN AGAING!
     * Return List with coordinates of array's cells when it can be
     * List.size() == 0 when cycle is't possible
     * @param coords of cell
     * @return List with coords of cell
     */

    List<int[]> cyclePath = new ArrayList<>();
    public List<int[]> getCycle(int[] startCell){
        cyclePath.clear();
        cyclePath.add(startCell);

        //TODO: make recursive algorithm for building route


        lookHorizotaly(startCell);

        if (cyclePath.size() == 1){
            cyclePath.clear();
        }

        System.out.println("finished");
        for (int[] element: cyclePath){
            System.out.println(element[0] + " " + element[1]);
        }
        System.out.print(cyclePath.size());
        return cyclePath;
    }

    private boolean lookHorizotaly(int[] currentCoords){
        //System.out.println(cyclePath.get(0)[0] + " " + cyclePath.get(0)[1]);

        for (int i = 0; i < orders.size(); i++){
            if (routes[cyclePath.get(0)[0]][i] > 0){
                if ((currentCoords[0] == cyclePath.get(0)[0]) && (currentCoords[0] == cyclePath.get(0)[1]) && (cyclePath.size() > 2)){
                    cyclePath.add(currentCoords);
                    return true; // full complete circuit
                }

                //System.out.println(currentCoords[0] + " " + i);
                if (i == currentCoords[1]){
                    continue;
                }

                int[] newCurrentCoords = {currentCoords[0], i};
                if (lookVerticaly(newCurrentCoords)){
                    System.out.println(newCurrentCoords[0] + " " + newCurrentCoords[1]);
                    cyclePath.add(newCurrentCoords);
                    return true;
                }
            }
        }

        return false;
    }

    private boolean lookVerticaly(int[] currentCoords){
        /*for (int j  = 0; j < stock.size(); j++){
           if (currentCoords[0] == cyclePath.get(0)[0])&&(){

            }
        }*/

        //System.out.println(currentCoords[0] + " " + currentCoords[1]);
        return true;
    }




}
