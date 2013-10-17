package com.mycalculator;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CalcMain extends Activity {

	private Vibrator myVib;
	Toast myToast;
	Long lastToast = (long) 0; //This is needed to keep toasts from stacking up
	Double num1 = (double) 0;
	Double num2 = (double) 0;
	Integer len1 = 0; // Holds the length of num1 so that it can be extracted and parsed later
	Integer len2 = 0; // Holds the length of num2
	Double result = null;
	Double prevResult = (double) 0; 
	int operation = 13; // Holds the opcode
	/*
	 * '+' = 0
	 * '-' = 1
	 * '*' = 2
	 * '/' = 3
	 * 'square' = 4
	 * 'none' = 13 
	 */
	TextView inBox;
	TextView outBox; 
	Boolean equilFlag = false; // Checks if = has been pressed
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calc_main);
		myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Hides the notification bar
		myToast = Toast.makeText(getBaseContext(),"Maximum Characters Reached", Toast.LENGTH_SHORT);
		inBox = (TextView) findViewById(R.id.inputBox);
		outBox = (TextView) findViewById(R.id.outputBox);
		// Handles clear all function
		Button clr = (Button) findViewById(R.id.buttonClr);
		clr.setOnLongClickListener(new OnLongClickListener() { 
            @Override
            public boolean onLongClick(View v) {
            	myVib.vibrate(50);
            	TextView inBox = (TextView) findViewById(R.id.inputBox);
            	inBox.setText("");
            	outBox.setText("");
                return true;
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calc_main, menu);
		return true;
	}
	
	// This is the command called whenever a digit key is hit
	public void numPress(View v) {
		// provides haptic feedback
		myVib.vibrate(50);
		
		// Resets variables for next operation
		if(equilFlag == true) {
			// Emptied to conserve space and for flag purposes
			result = null;
			// Reset for next input
			num1 = (double) 0;
			num2 = (double) 0;
			inBox.setText("");
			outBox.setText("");
			equilFlag = false;
			len1 = 0;
			len2 = 0;
		}
		
		Button b = (Button) v;
		// Gets the input digit
		String input = b.getText().toString();
		String curText = inBox.getText().toString();
		// checks that screen isn't overloaded
		if(curText.length() < 20) {
			// Updates the stored numbers
			if(operation == (Integer) 13) len1++;
			else len2++;
			// Updates the screen
			input = curText + input;
			inBox.setText(input);
		}
		// Handles the case when the screen is full
		else {
			// Checks if previous toast is still up. This keeps them from queuing
			if (lastToast <= System.currentTimeMillis()/1000 - 2000) myToast.cancel();
			lastToast = System.currentTimeMillis()/1000;
			myToast.show();
		}
	}
	
	// This is the command called when the clear key is hit
		public void clrClick(View v) {
			// provides haptic feedback
			myVib.vibrate(50);
			if(result == null){
				// updates the input screen
				String curText = inBox.getText().toString();
				// Removes the last character. NOTE: Will need to be expanded when math functions are added
				String input = "";
				for(int i = 0; i < curText.length() - 1; i++) {
					input += curText.charAt(i);
				}
				inBox.setText(input);
				outBox.setText("");	
			}
			else {
				inBox.setText("");
				outBox.setText("");
			}
		}

		// This is called when the equals button is pressed. Definitely will be changed with 
		public void equalClick(View v) {
			
			// Extract num1 and num2
			String input = inBox.getText().toString();
			num1 = Double.parseDouble(input.substring(0, len1));
			if(operation != (Integer) 13) num2 = Double.parseDouble(input.substring(len1+1, input.length()));
			
			switch(operation) {
				case 13: result = num1;
					outBox.setText(Double.toString(result));
					prevResult = result;
					break;
				case 0: result = num1 + num2;
					outBox.setText(Double.toString(result));
					prevResult = result;
					break;
				case 1: result = num1 - num2;
					outBox.setText(Double.toString(result));
					prevResult = result;
					break;
				case 2: result = num1 * num2;
					outBox.setText(Double.toString(result));
					prevResult = result;
					break;
				case 3: 
					if(num2 != 0) {
						result = num1 / num2;
						outBox.setText(Double.toString(result));
						prevResult = result;
					}
					else Toast.makeText(getBaseContext(),"Cannot divide by 0", Toast.LENGTH_SHORT).show();
						break;
			}
			equilFlag = true;
			operation = 13;
		}
		
		// This is called when the equals button is pressed. Definitely will be changed with 
		public void opClick(View v) {
			// When one number is entered
			Button b = (Button) v; 
			// Gets the input digit
			String input = b.getContentDescription().toString();
			operation = Integer.parseInt(input);
			inBox.setText(inBox.getText().toString() + b.getText().toString());
			
		}
}
