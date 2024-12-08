package ca.gbc.g60_project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    private TextView tvDetails;
    private Button btnBackDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvDetails = findViewById(R.id.tv_details);
        btnBackDetails = findViewById(R.id.btn_back_details);

        String details = getIntent().getStringExtra("details");

        tvDetails.setText(details);

        btnBackDetails.setOnClickListener(v -> finish());
    }
}
