package com.sdsetup.noahc3.paintpot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DoodleCanvas extends View {

    //currently selected paint and path
    private Paint mPaint;
    private Path mPath;

    //lists of paints and paths
    //treat these lists as linked pairs. when drawn, the path at index 0 of paths will be drawn with the paint at index 0 of paints.
    //an alternative would be a LinkedHashMap but i didnt know that existed sooo ¯\_(ツ)_/¯
    private ArrayList<Paint> paints = new ArrayList<>();
    private ArrayList<Path> paths = new ArrayList<>();

    public DoodleCanvas(Context context) {
        super(context);
        mPaint = newPaint(Color.BLUE); //set a default paint
    }


    //when the canvas view render is marked outdated and needs to be redrawn:
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paths.size() > 0 && paints.size() > 0) { //only run if there are paths to draw (java doesn't like for loops with no thing to iterate)
            for (int i = 0; i  < paths.size(); i++) { //for each path in paths (and also paints, they should have the same # of items)
                canvas.drawPath(paths.get(i), paints.get(i)); //draw the path at index i of paths on the canvas using the paint at index i of paints.
            }
        }

    }

    //creates a new paint object using specified color
    private Paint newPaint(int color) {
        float size; //init size
        if (mPaint != null) size = mPaint.getStrokeWidth(); //if mPaint exists, reuse it's size
        else size = 10.0f; //otherwise default to 10 (used when the program first runs and there is no existing paint object)
        Paint paint = new Paint(); //create the new paint
        paint.setStyle(Paint.Style.STROKE); //set the style to stroke the path (as opposed to treating it as an outline and filling it)
        paint.setStrokeJoin(Paint.Join.ROUND); //Sharp points will be rounded off
        paint.setStrokeCap(Paint.Cap.ROUND); //Ends will be rounded off
        paint.setStrokeWidth(size); //Set the width to the size determined earlier
        paint.setColor(color); //Set the color to the passed in color
        return paint; //return the generated paint object
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) { //Called when some event involving the touch screen happens:

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN: //If a finger was just touched to the screen this frame:
                mPath = new Path(); // Generate a new path
                paints.add(mPaint); //Add the currently chosen paint to the paints list
                paths.add(mPath); //Add the current path to the paths list
                mPath.moveTo(event.getX(), event.getY()); //Start the path at the touched coordinate
                break;

            case MotionEvent.ACTION_MOVE: //If the touch point moves:
                mPath.lineTo(event.getX(), event.getY()); //Trace from the previous point to the new touched point
                invalidate(); //Invalidate the view so it redraws the updated path
                break;

            case MotionEvent.ACTION_UP: //When touch input stops:
                mPaint = newPaint(mPaint.getColor()); //Create a new paint using existing properties so the old path doesn't get changed when the user changes paint properties
                break;
        }

        return true;
    }

    public void setColor(int paint) { //Called by the activity to set the color based on the color button pressed. Just uses ids
        switch (paint) {
            case 0:
                mPaint = newPaint(Color.BLUE); //if 0, set the paint to blue
                break;
            case 1:
                mPaint = newPaint(Color.GREEN); //green if 1
                break;
            case 2:
                mPaint = newPaint(Color.RED); //red if 2
                break;
        }
    }

    public void clear() { //Clears all of the previously drawn paints/paths
        paths.clear(); //Clear all paths
        paints.clear(); //Clear all paints
        invalidate(); //Invalidate the view so it reflects the fact that the paths are gone
    }

    public float getBrushSize() {
        return mPaint.getStrokeWidth(); //returns the current brush size, used by the activity
    }

    public void setBrushSize(float size) {
        mPaint.setStrokeWidth(size); //sets the brush size, used by the activity
    }
}
