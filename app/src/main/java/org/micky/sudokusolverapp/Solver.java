package org.micky.sudokusolverapp;

import java.util.ArrayList;

public class Solver {

    int[][] board;
    ArrayList<ArrayList<Object>> emptyBoxIndex;

    static int selectedRow;
    static int selectedColumn;

    Solver(){
        selectedRow = -1;
        selectedColumn = -1;

        board = new int[9][9];

        for(int r = 0; r  < 9; r++){ // rows
            for(int c = 0; c < 9; c++){ // columns
                board[r][c] = 0;
            }
        }

        emptyBoxIndex = new ArrayList<>();
    }

    private void getEmptyBoxIndexes(){

        for(int r = 0; r<9; r++){ // rows
            for(int c = 0; c<9; c++){ // columns
                if(this.board[r][c] == 0){
                    this.emptyBoxIndex.add(new ArrayList<>());
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size()-1).add(r);
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size()-1).add(c);
                }
            }
        }
    }



    public void setNumberPos(int num){

        if (this.selectedRow != -1 && this.selectedColumn != -1){
            if (this.board[this.selectedRow-1][this.selectedColumn-1] == num){
                this.board[this.selectedRow-1][this.selectedColumn-1] = 0;
            }
            else{
                this.board[this.selectedRow-1][this.selectedColumn-1] = num;
            }
        }
    }

    public ArrayList<ArrayList<Object>> getEmptyBoxIndex(){
        return this.emptyBoxIndex;
    }

    public int[][] getBoard(){
        return this.board;
    }

    public static int getSelectedRow() {
        return selectedRow;
    }

    public static void setSelectedRow(int selectedRow) {
        Solver.selectedRow = selectedRow;
    }

    public static int getSelectedColumn() {
        return selectedColumn;
    }

    public static void setSelectedColumn(int selectedColumn) {
        Solver.selectedColumn = selectedColumn;
    }
}
