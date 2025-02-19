package com.example.qrcodescanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {

    private TextView txtResult;
    private Button btnScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtResult = findViewById(R.id.txtResult);
        btnScan = findViewById(R.id.btnScan);

        txtResult.setVisibility(View.INVISIBLE);

        btnScan.setOnClickListener(v -> startQRCodeScanner());



    }


    private void startQRCodeScanner() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan a QR Code");
        options.setCameraId(0);  // Use rear camera
        options.setOrientationLocked(true);
        options.setBeepEnabled(true);
        options.setBarcodeImageEnabled(true);

        options.setCaptureActivity(CustomScannerActivity.class);
        
        barcodeLauncher.launch(options);
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    String scannedData = result.getContents();
                    txtResult.setText("Scanned: " + scannedData);

                    // Perform actions based on scanned content
                    handleScannedData(scannedData);
                }
            }
    );
    private void handleScannedData(String data) {
        if (data.startsWith("http://") || data.startsWith("https://")) {
            // Open in browser
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
            startActivity(browserIntent);
        } else if (data.startsWith("mailto:")) {
            // Open email app
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse(data));
            startActivity(emailIntent);
        } else if (data.startsWith("tel:")) {
            // Open dialer
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse(data));
            startActivity(dialIntent);
        } else {
            // Display the scanned text
            Toast.makeText(this, "Scanned: " + data, Toast.LENGTH_LONG).show();
        }

    }

}