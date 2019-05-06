package org.nothingugly.uglydeals;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity {

    private static final String TAG = "ScanActvity";
   private SurfaceView cameraView;
   private BarcodeDetector barcodeDetector;
   private CameraSource cameraSource;
   private SurfaceHolder surfaceHolder;
   private String barcodeValue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dealId retrived from the relevant SelectedDeal Acticity
        String dealId = null;

        //If no id is passed through, id = null
        if(savedInstanceState ==  null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                dealId = null;
            } else {
                dealId = (String) extras.get("dealId");
            }
        }else{
            dealId = (String) savedInstanceState.getSerializable("dealId");
        }

        setContentView(R.layout.activity_scan);


        cameraView = (SurfaceView ) findViewById(R.id.cameraView);
        cameraView.setZOrderMediaOverlay(true);

        surfaceHolder = cameraView.getHolder();
        barcodeDetector = new BarcodeDetector
                            .Builder(this)
                            .setBarcodeFormats(Barcode.QR_CODE)
                            .build();

        if(!barcodeDetector.isOperational()){
            Toast.makeText(this, "Barcode detector unavailable",Toast.LENGTH_LONG).show();
            this.finish();

        }

        cameraSource = new CameraSource.Builder(this,barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(24)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1920,1024)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try{
                    if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){}
                    cameraSource.start(cameraView.getHolder());

                } catch (IOException e){
                    e.printStackTrace();
                }
            }



            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        final String finalDealId = dealId;

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0){

                    String detectedBarcode = barcodes.valueAt(0).displayValue;
                    barcodeValue = detectedBarcode;

                    if (barcodeValue.equals(finalDealId)){
                        Intent intent = new Intent(ScanActivity.this, RedeemSuccessActivity.class);
                        intent.putExtra("dealId", barcodeValue);
                        startActivity(intent);
                        finish();
                    } else {

                    }


                }

            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraSource != null) {
            cameraSource.release();
            cameraSource = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
