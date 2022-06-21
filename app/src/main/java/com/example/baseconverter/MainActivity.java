package com.example.baseconverter;

import static java.lang.Math.pow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    Button button;
    Spinner spinnerDestination, spinnerSource;
    int countPoint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.editText);
        textView=findViewById(R.id.textView);
        button=findViewById(R.id.button);
        spinnerSource=findViewById(R.id.spinnerSource);
        spinnerDestination=findViewById(R.id.spinnerDestination);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(R.string.result);
                clicking(view);
            }
        });
    }

    public void clicking(View view) {
        String str = editText.getText().toString();
        str = str.toUpperCase(Locale.ROOT);
        if (str.equals("")) {
            Toast.makeText(MainActivity.this, "Enter a number please", Toast.LENGTH_SHORT).show();
        } else if (isCorrect(str)) {
            if (countPoint == 0) {

            }
            int base = 2;
            if (spinnerSource.getSelectedItem().toString().equals("Decimal")) {
                switch (spinnerDestination.getSelectedItem().toString()) {
                    case "Decimal":
                        base = 10;
                        break;
                    case "Binary":
                        base = 2;
                        break;
                    case "Octal":
                        base = 8;
                        break;
                    case "Hexadecimal":
                        base = 16;
                        break;
                }
                str = toAnyBase(str, base);
            } else if (spinnerSource.getSelectedItem().toString().equals("Binary")) {
                switch (spinnerDestination.getSelectedItem().toString()) {
                    case "Decimal":
                        str = toDecimal(str, 2);
                        break;
                    case "Binary":
                        break;
                    case "Octal":
                        str = toDecimal(str, 2);
                        str = toAnyBase(str, 8);
                        break;
                    case "Hexadecimal":
                        str = toDecimal(str, 2);
                        str = toAnyBase(str, 16);
                        break;
                }
            } else if (spinnerSource.getSelectedItem().toString().equals("Octal")) {
                switch (spinnerDestination.getSelectedItem().toString()) {
                    case "Decimal":
                        str = toDecimal(str, 8);
                        break;
                    case "Binary":
                        str = toDecimal(str, 8);
                        str = toAnyBase(str, 2);
                        break;
                    case "Octal":
                        break;
                    case "Hexadecimal":
                        str = toDecimal(str, 8);
                        str = toAnyBase(str, 16);
                        break;
                }
            } else if (spinnerSource.getSelectedItem().toString().equals("Hexadecimal")) {
                switch (spinnerDestination.getSelectedItem().toString()) {
                    case "Decimal":
                        str = toDecimal(str, 16);
                        break;
                    case "Binary":
                        str = toDecimal(str, 16);
                        str = toAnyBase(str, 2);
                        break;
                    case "Octal":
                        str = toDecimal(str, 16);
                        str = toAnyBase(str, 8);
                        break;
                    case "Hexadecimal":
                        break;
                }
            }
            String text = "Result: " + str;
            textView.setText(text);
        }
    }

    public boolean isCorrect(String str) {
        // for all
        Pattern pattern = Pattern.compile("[^A-Z0-9.]");
        Matcher match = pattern.matcher(str);
        if (match.find()) {
            Toast.makeText(MainActivity.this, "Enter number from 0 to 9 and A to F", Toast.LENGTH_SHORT).show();
            return false;
        }
        // count point
        countPoint = 0;
        pattern = Pattern.compile("[.]");
        match = pattern.matcher(str);
        while (match.find()) {
            countPoint++;
        }
        if (countPoint > 1) {
            Toast.makeText(this, "Enter a valid number", Toast.LENGTH_SHORT).show();
            return false;
        }
        // for hexadecimal
        if (spinnerSource.getSelectedItem().toString().equals("Hexadecimal")) {
            pattern = Pattern.compile("[^A-F0-9.]");
            match = pattern.matcher(str);
            if (match.find()) {
                Toast.makeText(this, "Hexadecimal does not support G to Z alphabets", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        // for decimal, octal, binary
        if (!spinnerSource.getSelectedItem().toString().equals("Hexadecimal")) {
            pattern = Pattern.compile("[^0-9.]");
            match = pattern.matcher(str);
            if (match.find()) {
                Toast.makeText(this, spinnerSource.getSelectedItem().toString() + " does not support alphabets", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        // for octal
        if (spinnerSource.getSelectedItem().toString().equals("Octal")) {
            pattern = Pattern.compile("[^0-7.]");
            match = pattern.matcher(str);
            if (match.find()) {
                Toast.makeText(this, "Octal does not have 8 and 9", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        // for binary
        if (spinnerSource.getSelectedItem().toString().equals("Binary")) {
            pattern = Pattern.compile("[^01.]");
            match = pattern.matcher(str);
            if (match.find()) {
                Toast.makeText(this, "Binary has 0 and 1 only", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public String toAnyBase(String str, int base) {
        int num = Integer.parseInt(str);
        int r;
        StringBuilder stringBuilder = new StringBuilder();
        while (num != 0) {
            r = num % base;
            if (r < 10) {
                stringBuilder.append(r);
            } else {
                char[] alphabet = new char[]{'A', 'B', 'C', 'D', 'E', 'F'};
                r -= 10;
                System.out.println(alphabet[r]);
                stringBuilder.append(alphabet[r]);
            }
            num /= base;
        }
        stringBuilder.reverse();
        return stringBuilder.toString();
    }

    public String toDecimal(String str, int base) {
        int s = 0, r = 0, p = 0;
        for (int i = str.length()-1; i >= 0; --i) {
            char c = str.charAt(i);
            if (c >= 'A' && c <= 'F') {
                switch (c) {
                    case 'A':
                        r = 10;
                        break;
                    case 'B':
                        r = 11;
                        break;
                    case 'C':
                        r = 12;
                        break;
                    case 'D':
                        r = 13;
                        break;
                    case 'E':
                        r = 14;
                        break;
                    case 'F':
                        r = 15;
                        break;
                }
            } else {
                r = Integer.parseInt(String.valueOf(c));
            }
            s += r * pow(base, p);
            p++;
        }
        return Integer.toString(s);
    }
}