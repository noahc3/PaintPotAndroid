package com.sdsetup.noahc3.paintpot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Debug;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    DoodleCanvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canvas = new DoodleCanvas(this); //create a new canvas
        canvas.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 8)); //set layout paramaters to the canvas to it stretches correctly and stuff

        ((LinearLayout)findViewById(R.id.LayoutMain)).addView(canvas); //add the canvas to the main horizontal linear layout
    }

    public void btnRed_Click(View v) { //when the red button is pressed
        canvas.setColor(2); //tell the canvas to set the paint color to red
    }

    public void btnGreen_Click(View v) { //when the green button is pressed
        canvas.setColor(1); //tell the canvas to set the paint color to green
    }

    public void btnBlue_Click(View v) { //when the blue button is pressed
        canvas.setColor(0); //tell the canvas to set the paint color to blue
    }

    public void btnPicture_Click(View v) { //called when the take picture button is pressed
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //create an intent to take an image
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) { //make sure there is an app that can take a picture
            startActivityForResult(takePictureIntent, 1); //send a request to take a picture
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //when an activity is done (a picture was taken)
        if (requestCode == 1 && resultCode == RESULT_OK) { //if the result is for the camera intent and the image capture was successful:
            Bundle extras = data.getExtras(); //get the returned data
            Bitmap imageBitmap = (Bitmap) extras.get("data"); //get the image bitmap

            Matrix matrix = new Matrix(); //create a new matrix (used to rotate the image correctly)
            matrix.postRotate(270); //rotate the matrix by 270 degrees
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, imageBitmap.getHeight(), imageBitmap.getWidth(), true); //scale the bitmap correctly to what it should be after rotation
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true); //rotate the bitmap using the matrix

            canvas.setBackground(new BitmapDrawable(rotatedBitmap)); //set the background of the canvas to the bitmap
        }
    }

    public void btnClear_Click(View v) { //when the wipe button is pressed
        canvas.clear(); //call clear on the canvas
    }

    public void btnSizeSmaller_Click(View v) { //when the (-) button is pressed
        canvas.setBrushSize(canvas.getBrushSize() - 1); //decrease the canvas brush size by 1
        ((TextView)findViewById(R.id.txtSize)).setText("Size: " + (int) canvas.getBrushSize()); //update the size text to match the new size
    }

    public void btnSizeBigger_Click(View v) { //when the (+) button is pressed
        canvas.setBrushSize(canvas.getBrushSize() + 1); //increase the canvas brush size by 1
        ((TextView)findViewById(R.id.txtSize)).setText("Size: " + (int) canvas.getBrushSize()); //update the size text to match the new size
    }

}
