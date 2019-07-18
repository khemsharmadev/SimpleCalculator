package com.khemsharmadev.simplecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    private Button changeColor;

    private boolean hasOperation = false;
    private boolean hasDot = false;
    private boolean hasNegative = false;
    private boolean canNegative = true;
    private boolean canOperate = false;

    private static final String STATE_HAS_OPERATION = "hasOperation";
    private static final String STATE_HAS_NEGATIVE = "hasNegative";
    private static final String STATE_HAS_DOT = "hasDot";
    private static final String STATE_CAN_NEGATIVE = "canNegative";
    private static final String STATE_CAN_OPERATE = "canOperate";
    private static final String STATE_RESULT = "result";

    Button num0,num1,num2,num3,num4,num5,num6,num7,num8,num9,divideBtn,multiplyBtn,addBtn,
            subtractBtn,dotBtn,equalsBtn,deteteBtn,bracketOpenBtn,bracketCloseBtn,xSquare,percentageBtn;

    TextView calculationInput,calculationOutput;

    RelativeLayout nightModeToggle;

    private final static int THEME_LIGHT = 1;
    private final static int THEME_DARK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTheme();

        setContentView(R.layout.activity_main);

        initUi();
    }

    View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {

            Button b = (Button) view;
            String idName = b.getResources().getResourceEntryName(b.getId());

            switch (idName) {
                case "dot_btn":
                    dotButton(b);
                    break;

                case "subtract_btn":
                    subtractButton(b);
                case "add_btn":
                case "multiply_btn":
                case "divide_btn":
                    operatorButton(b, idName);
                    break;

                case "delete_btn":
                    deleteButton(b);
                    break;

                case "bracket_open_btn":
                    appendToInput(b);

                    canOperate = false;
                    break;

                case "bracket_close_btn":
                    closeParButton(b);
                    break;

                case "equals_btn":
                    equalsButton(b);
                    break;

                case "percentage_btn":
                    //appendToInput(b);
                    if (!getInput().contains("%"))
                    {
                        hasOperation=true;
                        canOperate=true;
                        canNegative=false;
                        equalsButton(b);
                        operatorButton(b, idName);
                    }

                    break;

                case "x_square":
                    equalsButton(b);
                    calculationInput.setText(getInput()+"×"+getInput());
                    hasOperation=true;
                    canOperate=true;
                    performOperation();
                    break;

                default:
                    numbersButton(b);
            }
        }


    };


    private String getInput(){
        return calculationInput.getText().toString();
    }

    private int getInputLength(){
        return calculationInput.getText().length();
    }

    private void appendToInput(Button b){
        calculationInput.append(b.getText().toString());
        maintainTextSize();

    }

    private String getLastChar() throws NullPointerException{
        String input = calculationInput.getText().toString();
        return (input.length()>0)?input.substring(input.length()-1):null;
    }




    private void dotButton(Button b) throws NullPointerException {
        if (!hasDot && !getInput().isEmpty()) {
            if (getLastChar().equals("+") || getLastChar().equals("×") || getLastChar().equals("÷") || getLastChar().equals("-")) {
                calculationInput.append("0" + b.getText().toString());
            } else if (hasNegative) {
                ((Editable)calculationInput.getText()).insert(getInputLength() - 1, "0" + b.getText().toString());
            } else {
                appendToInput(b);
            }

            canOperate = false;
            hasDot = true;
        }
        if (getInput().isEmpty()) {
            calculationInput.append("0" + b.getText().toString());

            canOperate = false;
            hasDot = true;
        }
    }

    private void subtractButton(Button b) {
        if (getInput().isEmpty()) {

            calculationInput.append("-");

            hasNegative = true;
            canOperate = false;
        } else if (getLastChar().equals("+") || getLastChar().equals("÷") || getLastChar().equals("×")) {
            calculationInput.append("()");
            ((Editable)calculationInput.getText()).insert(getInputLength() - 1, "-");

            hasOperation = true;
            hasNegative = true;
            canOperate = false;
        } else if (getLastChar().equals("-") || getLastChar().equals(".") || (getLastChar().equals(")") && !canOperate)) {
            canOperate = false;
        } else {
            appendToInput(b);

            hasNegative = false;
            hasOperation = true;
            canOperate = false;
        }
    }

    private void operatorButton(Button b, String idName) {
        if (!getInput().isEmpty() && !idName.equals("subtractButton")) {
            if (canOperate) {
                appendToInput(b);

                hasOperation = true;
                canOperate = false;
            } else if (!canOperate && !getLastChar().equals(")") && !getLastChar().equals("0")) {
                if (calculationInput.getText().length() > 0) {
                    ((Editable)calculationInput.getText()).replace(getInputLength() - 1, getInputLength(), b.getText().toString());
                }
            }
        }

        if (hasDot) {
            hasDot = false;
        }

        if (!idName.equals("subtractButton")) {
            hasNegative = false;
        }

        if (!getInput().isEmpty()) {
            if (getLastChar().equals(")") && !idName.equals("subtractButton")) {
                if (getInputLength() > 3) {
                    ((Editable)calculationInput.getText()).replace(getInputLength() - 4, getInputLength(), b.getText().toString());
                } else {
                    calculationInput.setText("");
                }
            }
        }
    }

    private void deleteButton(Button b) {
        if (!getInput().isEmpty()) {
            if (getLastChar().equals(".")) {
                hasDot = false;
            }
            if (getLastChar().equals(")") || getLastChar().equals("(")) {
                canOperate = false;
            } else if (getLastChar().equals("+") && getLastChar().equals("-") && getLastChar().equals("×") && getLastChar().equals("÷")) {
                canOperate = true;
            }
            calculationInput.setText(getInput().substring(0, getInputLength() - 1));

            if (!getInput().isEmpty() && hasOperation) {
                if (!getLastChar().equals("+") && !getLastChar().equals("-") && !getLastChar().equals("×") && !getLastChar().equals("÷") && !getLastChar().equals(".")&& !getLastChar().equals("%")) {
                    performOperation();

                }else {
                    canOperate=false;
                }
            }

            if (getInput().isEmpty()) {
                calculationOutput.setText("");
                hasOperation = false;
            }
        }
    }

    private void equalsButton(Button b) {
        if (calculationOutput.getText().toString().equals("Bad Expression")) {
            calculationInput.setText("");
        } else if (!calculationOutput.getText().equals("")) {
            calculationInput.setText("");
            calculationInput.append(calculationOutput.getText().toString());
        }
        calculationOutput.setText("");
    }

    private void closeParButton(Button b) {
        appendToInput(b);
        if (hasOperation) {
            performOperation();
        }

        canOperate = true;
    }

    private void numbersButton(Button b) {
        if (!hasNegative) {
            appendToInput(b);
        } else if (hasNegative) {
            ((Editable) calculationInput.getText()).insert(getInputLength() - 1, b.getText().toString());
        }

        if (hasOperation) {
            performOperation();
        }

        canNegative = false;
        canOperate = true;
    }

    private void performOperation() {
        String expression = getInput().replace('×', '*');
        expression = expression.replace('÷', '/');
        expression = expression.replace("%","*0.01*");


        if (!getLastChar().equals("(")&&!getLastChar().equals(")")) {

            try{
                calculationOutput.setText(Double.toString(Evaluate.eval(expression)));
            }catch (RuntimeException e)
            {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_HAS_NEGATIVE, hasNegative);
        outState.putBoolean(STATE_HAS_DOT, hasDot);
        outState.putBoolean(STATE_HAS_OPERATION, hasOperation);
        outState.putBoolean(STATE_CAN_NEGATIVE, canNegative);
        outState.putBoolean(STATE_CAN_OPERATE, canOperate);
        outState.putString(STATE_RESULT, calculationOutput.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        hasNegative = savedInstanceState.getBoolean(STATE_HAS_NEGATIVE);
        hasDot = savedInstanceState.getBoolean(STATE_HAS_DOT);
        hasOperation = savedInstanceState.getBoolean(STATE_HAS_OPERATION);
        canNegative = savedInstanceState.getBoolean(STATE_CAN_NEGATIVE);
        canOperate = savedInstanceState.getBoolean(STATE_CAN_OPERATE);
        calculationOutput.setText(savedInstanceState.getString(STATE_RESULT));

    }



    View.OnLongClickListener longDelete = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {


            calculationInput.setText("");
            calculationOutput.setText("");
            hasOperation = false;
            canOperate=false;
            maintainTextSize();


            return true;
        }
    };



    private void initUi() {


        num0=findViewById(R.id.num_0);
        num1=findViewById(R.id.num_1);
        num2=findViewById(R.id.num_2);
        num3=findViewById(R.id.num_3);
        num4=findViewById(R.id.num_4);
        num5=findViewById(R.id.num_5);
        num6=findViewById(R.id.num_6);
        num7=findViewById(R.id.num_7);
        num8=findViewById(R.id.num_8);
        num9=findViewById(R.id.num_9);



        divideBtn=findViewById(R.id.divide_btn);
        multiplyBtn=findViewById(R.id.multiply_btn);
        addBtn=findViewById(R.id.add_btn);
        subtractBtn=findViewById(R.id.subtract_btn);
        dotBtn=findViewById(R.id.dot_btn);
        equalsBtn=findViewById(R.id.equals_btn);
        deteteBtn=findViewById(R.id.delete_btn);
        equalsBtn=findViewById(R.id.equals_btn);
        bracketOpenBtn=findViewById(R.id.bracket_open_btn);
        bracketCloseBtn=findViewById(R.id.bracket_close_btn);

        calculationInput=findViewById(R.id.calculation_input);
        calculationOutput=findViewById(R.id.calculation_output);

        percentageBtn=findViewById(R.id.percentage_btn);
        xSquare=findViewById(R.id.x_square);
        nightModeToggle=findViewById(R.id.night_mode_toggle);


        calculationInput.setText(calculationInput.getText(), TextView.BufferType.EDITABLE);

        num0.setOnClickListener(listener);
        num1.setOnClickListener(listener);
        num2.setOnClickListener(listener);
        num3.setOnClickListener(listener);
        num4.setOnClickListener(listener);
        num5.setOnClickListener(listener);
        num6.setOnClickListener(listener);
        num7.setOnClickListener(listener);
        num8.setOnClickListener(listener);
        num9.setOnClickListener(listener);

        addBtn.setOnClickListener(listener);
        subtractBtn.setOnClickListener(listener);
        multiplyBtn.setOnClickListener(listener);
        divideBtn.setOnClickListener(listener);
        deteteBtn.setOnClickListener(listener);

        equalsBtn.setOnClickListener(listener);
        bracketOpenBtn.setOnClickListener(listener);
        bracketCloseBtn.setOnClickListener(listener);

        percentageBtn.setOnClickListener(listener);
        xSquare.setOnClickListener(listener);

        deteteBtn.setOnLongClickListener(longDelete);

        /*calculationInput.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        calculationOutput.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
*/
        maintainTextSize();


        nightModeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (Utility.getTheme(MainActivity.this) == 2) {
                    Utility.setTheme(getApplicationContext(), 1);
                    recreateActivity();
                } else Utility.setTheme(getApplicationContext(), 2);
                recreateActivity();

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }








    }

    private void maintainTextSize(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            calculationInput.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            calculationOutput.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);

            calculationInput.setAutoSizeTextTypeUniformWithConfiguration(14,50,4,TypedValue.COMPLEX_UNIT_SP);
            calculationOutput.setAutoSizeTextTypeUniformWithConfiguration(12,34,4,TypedValue.COMPLEX_UNIT_SP);
        }
    }

    public void updateTheme() {
        if (Utility.getTheme(getApplicationContext()) == THEME_LIGHT) {
            setTheme(R.style.AppTheme_Light);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.color_2c3e50));
            }
        } else if (Utility.getTheme(getApplicationContext()) == THEME_DARK) {
            setTheme(R.style.AppTheme_Dark);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.color_202020));
           }
        }
    }

    public void recreateActivity() {



        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(0, 0);
        startActivity(intent); // start same activity
        finish(); // destroy older activity
        overridePendingTransition(0, 0); // this is important for seamless transition


    }

}
