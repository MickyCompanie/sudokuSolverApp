package org.micky.sudokusolverapp;

public class Solver {
    private static int selectedRow;
    private static int selectedColumn;

    Solver(){
        selectedRow = -1;
        selectedColumn = -1;
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
