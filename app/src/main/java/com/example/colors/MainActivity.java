//package com.example.colors;
//
//import android.os.Bundle;
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class MainActivity extends AppCompatActivity {
//
//    TextView title;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//
//        initTextViews();
//    }
//
//    private void initTextViews(){
//        title = findViewById(R.id.title);
//    }
//}
package com.example.colors;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.security.SecureRandom;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // UI элементы
    TextView title;

    TextInputEditText etLength;
    TextInputEditText etResult;

    CheckBox cbUpper;
    CheckBox cbLower;
    CheckBox cbNumbers;
    CheckBox cbSymbols;

    Button selectAll;
    Button button5;
    Button clearHistory;

    MaterialButton btnGenerate;

    TextView password1, password2, password3, password4;
    TextView password1Len, password2Len, password3Len, password4Len;

    // История паролей
    ArrayList<String> history = new ArrayList<>();

    // Наборы символов
    final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    final String NUMBERS = "0123456789";
    final String SYMBOLS = "!@#$%^&*()_-+=<>?/{}[]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initViews();
        initButtons();
    }

    private void initViews() {

        title = findViewById(R.id.title);

        etLength = findViewById(R.id.etLength);
        etResult = findViewById(R.id.etResult);

        cbUpper = findViewById(R.id.cbUpper);
        cbLower = findViewById(R.id.cbLower);
        cbNumbers = findViewById(R.id.cbNumbers);
        cbSymbols = findViewById(R.id.cbSymbols);

        selectAll = findViewById(R.id.selectAll);
        button5 = findViewById(R.id.button5);

        clearHistory = findViewById(R.id.clearHistory);

        btnGenerate = findViewById(R.id.btnGenerate);

        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        password3 = findViewById(R.id.password3);
        password4 = findViewById(R.id.password4);

        password1Len = findViewById(R.id.password1Len);
        password2Len = findViewById(R.id.password2Len);
        password3Len = findViewById(R.id.password3Len);
        password4Len = findViewById(R.id.password4Len);
    }

    private void initButtons() {

        // Генерация пароля
        btnGenerate.setOnClickListener(v -> generatePassword());

        // Выбрать всё
        selectAll.setOnClickListener(v -> {
            cbUpper.setChecked(true);
            cbLower.setChecked(true);
            cbNumbers.setChecked(true);
            cbSymbols.setChecked(true);
        });

        // Инвертировать
        button5.setOnClickListener(v -> {
            cbUpper.setChecked(!cbUpper.isChecked());
            cbLower.setChecked(!cbLower.isChecked());
            cbNumbers.setChecked(!cbNumbers.isChecked());
            cbSymbols.setChecked(!cbSymbols.isChecked());
        });

        // Очистить историю
        clearHistory.setOnClickListener(v -> {
            history.clear();
            updateHistory();

            Toast.makeText(this,
                    "История очищена",
                    Toast.LENGTH_SHORT).show();
        });

        // Копирование результата
        etResult.setOnClickListener(v -> copyPassword());
    }

    // Генерация пароля
    private void generatePassword() {

        String lenText = etLength.getText().toString();

        if (lenText.isEmpty()) {
            Toast.makeText(this,
                    "Введите длину",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int length = Integer.parseInt(lenText);

        String chars = "";

        if (cbUpper.isChecked()) {
            chars += UPPER;
        }

        if (cbLower.isChecked()) {
            chars += LOWER;
        }

        if (cbNumbers.isChecked()) {
            chars += NUMBERS;
        }

        if (cbSymbols.isChecked()) {
            chars += SYMBOLS;
        }

        if (chars.isEmpty()) {
            Toast.makeText(this,
                    "Выберите хотя бы один тип символов",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {

            int index = random.nextInt(chars.length());

            password.append(chars.charAt(index));
        }

        String result = password.toString();

        etResult.setText(result);

        addToHistory(result);
    }

    // Добавление в историю
    private void addToHistory(String password) {

        history.add(0, password);

        // Максимум 4 записи
        if (history.size() > 4) {
            history.remove(4);
        }

        updateHistory();
    }

    // Обновление таблицы истории
    private void updateHistory() {

        TextView[] passwords = {
                password1,
                password2,
                password3,
                password4
        };

        TextView[] lengths = {
                password1Len,
                password2Len,
                password3Len,
                password4Len
        };

        for (int i = 0; i < 4; i++) {

            if (i < history.size()) {

                String pass = history.get(i);

                passwords[i].setText(pass);

                lengths[i].setText(
                        String.valueOf(pass.length())
                );

            } else {

                passwords[i].setText("—");
                lengths[i].setText("0");
            }
        }
    }

    // Копирование в буфер
    private void copyPassword() {

        String password = etResult.getText().toString();

        if (password.isEmpty()) {

            Toast.makeText(this,
                    "Пароль отсутствует",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        ClipboardManager clipboard =
                (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip =
                ClipData.newPlainText("password", password);

        clipboard.setPrimaryClip(clip);

        Toast.makeText(this,
                "Пароль скопирован",
                Toast.LENGTH_SHORT).show();
    }
}