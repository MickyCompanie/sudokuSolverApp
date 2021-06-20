package org.micky.sudokusolverapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SudokuBoard extends View {
    private final int boardColor;
    private final int cellFillColor;
    private final int cellsHighlightColor;

    private final int letterColor;
    private final int letterColorSolve;

    private final Paint boardColorPaint = new Paint();
    private final Paint cellFillColorPaint = new Paint();
    private final Paint cellsHighlightColorPaint = new Paint();

    private final Paint letterPaint = new Paint();
    private final Rect letterPaintBounds = new Rect();

    private final Solver solver = new Solver();

    public SudokuBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SudokuBoard, 0,0);
        try{
            boardColor = a.getInteger(R.styleable.SudokuBoard_boardColor, 0);
            cellFillColor = a.getInteger(R.styleable.SudokuBoard_cellFillColor, 0);
            cellsHighlightColor = a.getInteger(R.styleable.SudokuBoard_cellsHighlightColor, 0);
            letterColor = a.getInteger(R.styleable.SudokuBoard_letterColor, 0);
            letterColorSolve = a.getInteger(R.styleable.SudokuBoard_letterColorSolve, 0);
    }finally {
            a.recycle();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int dimension = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());
        int cellSize = dimension / 9;
        boolean isValid;

        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();

        if(action == MotionEvent.ACTION_DOWN){
            Solver.setSelectedRow((int) Math.ceil(y/cellSize));
            Solver.setSelectedColumn((int) Math.ceil(x/cellSize));
            isValid = true;
        }else{
            isValid = false;
        }

        return isValid;
        //return super.onTouchEvent(event);
    }

    private void drawNumbers(Canvas canvas){
        int dimension = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());
        int cellSize = dimension / 9;

        letterPaint.setTextSize(cellSize);

        for(int r = 0; r<9; r++) { // rows
            for (int c = 0; c < 9; c++) { // columns
                if (solver.getBoard()[r][c] != 0) {
                    String text = Integer.toString(solver.getBoard()[r][c]);
                    float width, height;

                    letterPaint.getTextBounds(text, 0, text.length(), letterPaintBounds);
                    width = letterPaint.measureText(text);
                    height = letterPaintBounds.height();

                    canvas.drawText(text, (c * cellSize) + ((cellSize - width) / 2),
                            (r * cellSize + cellSize) - ((cellSize - height) / 2),
                            letterPaint);
                }
            }
        }

        letterPaint.setColor(letterColorSolve);

            for (ArrayList<Object> letter : solver.getEmptyBoxIndex()){
                int r = (int) letter.get(0);
                int c = (int) letter.get(1);

                String text = Integer.toString(solver.getBoard()[r][c]);
                float width, height;

                letterPaint.getTextBounds(text, 0, text.length(), letterPaintBounds);
                width = letterPaint.measureText(text);
                height = letterPaintBounds.height();

                canvas.drawText(text, (c * cellSize) + ((cellSize - width) / 2),
                        (r * cellSize + cellSize) - ((cellSize - height) / 2),
                        letterPaint);
            }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int dimension = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());

        setMeasuredDimension(dimension, dimension);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(16);
        boardColorPaint.setColor(boardColor);
        boardColorPaint.setAntiAlias(true);

        cellFillColorPaint.setStyle(Paint.Style.FILL);
        cellFillColorPaint.setAntiAlias(true);
        cellFillColorPaint.setColor(cellFillColor);

        cellsHighlightColorPaint.setStyle(Paint.Style.FILL);
        cellsHighlightColorPaint.setAntiAlias(true);
        cellsHighlightColorPaint.setColor(cellsHighlightColor);

        letterPaint.setStyle(Paint.Style.FILL);
        letterPaint.setAntiAlias(true);
        letterPaint.setColor(letterColor);

        colorCell(canvas, solver.getSelectedRow(), solver.getSelectedColumn());
        canvas.drawRect(0,0, this.getWidth(), this.getHeight(), boardColorPaint);
        drawBoard(canvas);
        drawNumbers(canvas);
    }

    private void colorCell(Canvas canvas, int r, int c){
        int dimension = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());
        int cellSize = dimension / 9;

        if(solver.getSelectedColumn() != -1 && solver.getSelectedRow() != -1){
            // invalidate(); // Refresh

            canvas.drawRect((c-1) * cellSize, 0, c * cellSize,
                    cellSize * 9, cellFillColorPaint);

            canvas.drawRect(0, (r-1) * cellSize, cellSize * 9,
                    r * cellSize, cellFillColorPaint);

            canvas.drawRect((c-1) * cellSize, (r-1) * cellSize, c * cellSize,
                    r * cellSize, cellsHighlightColorPaint);

        }
        invalidate(); // Refresh
    }

    private void drawThickLine(){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(10);
        boardColorPaint.setColor(boardColor);
    }

    private void drawThinLine(){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(4);
        boardColorPaint.setColor(boardColor);
    }

    private void drawBoard(Canvas canvas){
        int dimension = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());
        int cellSize = dimension / 9;
        // columns
        for (int c = 0; c < 10; c++){
            if (c % 3 == 0){
                drawThickLine();
            }else{
                drawThinLine();
            }
            canvas.drawLine(cellSize * c, 0,
                    cellSize * c, this.getWidth(), boardColorPaint);
        }
        // rows
        for (int r = 0; r < 10; r++){
            if (r % 3 == 0){
                drawThickLine();
            }else{
                drawThinLine();
            }
            canvas.drawLine(0, cellSize * r,
                    this.getHeight(), cellSize * r, boardColorPaint);
        }
    }

    public Solver getSolver(){
        return this.solver;
    }
}
