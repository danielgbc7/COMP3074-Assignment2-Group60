package ca.gbc.g60_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout historyContainer;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyContainer = findViewById(R.id.history_container);
        dbHelper = new DatabaseHelper(this);

        displayHistoryEntries();

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }

    private void displayHistoryEntries() {
        historyContainer.removeAllViews();
        List<String[]> historyEntries = dbHelper.getAllHistoryEntries();

        for (String[] entry : historyEntries) {
            String id = entry[0];
            String billAmount = entry[2];
            String details = "Bill: $" + billAmount + "\nTip: $" + entry[3] +
                    "\nTotal: $" + entry[4] + "\nSplit: $" + entry[5] + " per person";

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView tvBillAmount = new TextView(this);
            tvBillAmount.setText("Bill Amount: $" + billAmount);
            tvBillAmount.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            ));

            Button btnDetails = new Button(this);
            btnDetails.setText("Details");
            btnDetails.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            btnDetails.setTextSize(12);
            btnDetails.setPadding(10, 10, 10, 10);

            Button btnDelete = new Button(this);
            btnDelete.setText("Delete");
            btnDelete.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            btnDelete.setTextSize(12);
            btnDelete.setPadding(10, 10, 10, 10);

            btnDetails.setOnClickListener(v -> {
                Intent intent = new Intent(HistoryActivity.this, DetailsActivity.class);
                intent.putExtra("details", details);
                startActivity(intent);
            });

            btnDelete.setOnClickListener(v -> {
                dbHelper.deleteHistoryEntry(id);
                displayHistoryEntries();
            });

            row.addView(tvBillAmount);
            row.addView(btnDetails);
            row.addView(btnDelete);

            historyContainer.addView(row);
        }
    }
}

