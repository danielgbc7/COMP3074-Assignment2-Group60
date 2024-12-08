package ca.gbc.g60_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etBillAmount, etCustomTip, etSplitCount;
    private RadioGroup rgTipPercentage;
    private TextView tvTipAmount, tvTotalAmount, tvSharePerPerson;
    private Button btnReset, btnCalculate, btnViewHistory;
    private double tipPercentage = 10.0;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);

        etBillAmount = findViewById(R.id.et_bill_amount);
        etCustomTip = findViewById(R.id.et_custom_tip);
        etSplitCount = findViewById(R.id.et_split_count);
        rgTipPercentage = findViewById(R.id.rg_tip_percentage);
        tvTipAmount = findViewById(R.id.tv_tip_amount);
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        tvSharePerPerson = findViewById(R.id.tv_share_per_person);
        btnReset = findViewById(R.id.btn_reset);
        btnCalculate = findViewById(R.id.btn_calculate);
        btnViewHistory = findViewById(R.id.btn_view_history);

        rgTipPercentage.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_10) {
                tipPercentage = 10.0;
                etCustomTip.setVisibility(View.GONE);
            } else if (checkedId == R.id.rb_15) {
                tipPercentage = 15.0;
                etCustomTip.setVisibility(View.GONE);
            } else if (checkedId == R.id.rb_20) {
                tipPercentage = 20.0;
                etCustomTip.setVisibility(View.GONE);
            } else if (checkedId == R.id.rb_custom) {
                etCustomTip.setVisibility(View.VISIBLE);
            }
        });

        btnReset.setOnClickListener(v -> {
            etBillAmount.setText("");
            etCustomTip.setText("");
            etSplitCount.setText("");
            tvTipAmount.setText("Tip Amount: $0.00");
            tvTotalAmount.setText("Total Amount: $0.00");
            tvSharePerPerson.setText("Share per Person: $0.00");
            rgTipPercentage.check(R.id.rb_10); // Reset to default
        });

        btnCalculate.setOnClickListener(v -> calculateTipAndTotal());

        btnViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    private void calculateTipAndTotal() {
        if (TextUtils.isEmpty(etBillAmount.getText())) {
            etBillAmount.setError("Please enter the bill amount");
            return;
        }

        double billAmount = Double.parseDouble(etBillAmount.getText().toString());

        if (etCustomTip.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(etCustomTip.getText())) {
            tipPercentage = Double.parseDouble(etCustomTip.getText().toString());
        }

        double tipAmount = billAmount * (tipPercentage / 100);
        double totalAmount = billAmount + tipAmount;

        int splitCount = TextUtils.isEmpty(etSplitCount.getText()) ? 1 : Integer.parseInt(etSplitCount.getText().toString());
        double sharePerPerson = totalAmount / splitCount;

        DecimalFormat df = new DecimalFormat("#.##");
        tvTipAmount.setText("Tip Amount: $" + df.format(tipAmount));
        tvTotalAmount.setText("Total Amount: $" + df.format(totalAmount));
        tvSharePerPerson.setText("Share per Person: $" + df.format(sharePerPerson));

        String[] entry = {String.valueOf(tipPercentage), String.valueOf(billAmount), String.valueOf(tipAmount),
                String.valueOf(totalAmount), String.valueOf(sharePerPerson)};
        dbHelper.addHistoryEntry(entry);
    }
}

