package org.nothingugly.uglydeals;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FlashDealScanActivity extends AppCompatActivity {
    private static final String TAG = "ScanActvity";
    private SurfaceView cameraView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceHolder surfaceHolder;
    private String barcodeValue;


    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    //private  String deal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dealId retrived from the relevant SelectedDeal Acticity
        String dealId = null;

        //If no id is passed through, id = null
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                dealId = null;
            } else {
                dealId = (String) extras.get("dealId");
            }
        } else {
            dealId = (String) savedInstanceState.getSerializable("dealId");
        }

        //Setting deal global
        //deal = dealId;

        Log.d(TAG, "onCreate: *********" + dealId);

        setContentView(R.layout.activity_scan);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();


        cameraView = (SurfaceView) findViewById(R.id.cameraView);
        cameraView.setZOrderMediaOverlay(true);

        surfaceHolder = cameraView.getHolder();
        barcodeDetector = new BarcodeDetector
                .Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        if (!barcodeDetector.isOperational()) {
            Toast.makeText(this, "Barcode detector unavailable", Toast.LENGTH_LONG).show();
            this.finish();

        }

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(24)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1920, 1024)
                .build();
        Toast.makeText(FlashDealScanActivity.this, "Please scan the barcode", Toast.LENGTH_LONG).show();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ContextCompat.checkSelfPermission(FlashDealScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(cameraView.getHolder());
                    }

                } catch (IOException e) {
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
                //Checks the barcodes detected.
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0) {


                    String detectedBarcode = barcodes.valueAt(0).displayValue;
                    barcodeValue = detectedBarcode;

                    Log.d(TAG, "receiveDetections: *** " + detectedBarcode + ", " + finalDealId.trim());
                    if (barcodeValue.equals(finalDealId)) {

                        //Setting the resumes date to next day, Minus the hours and mins
                        Date date = new Date();
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DAY_OF_YEAR, +1);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);

                        Date resumeDate = cal.getTime();

                        Log.d(TAG, "receiveDetections: " + date.toString() + " " + resumeDate.toString());
                        // System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43


                        final String userId = mAuth.getUid();
                        final String dealId = finalDealId;

                        //Create two hash maps
                        final Map<String, Object> redeemedFlashDeal = new HashMap<>();
                        final Map<String, Object> unavailableFlashDeal = new HashMap<>();
                        final Map<String, Object> customerFlashDealHistory = new HashMap<>();
                        final Map<String, Object> scannedCustomer = new HashMap<>();

                        //Customers scanned
                        scannedCustomer.put("customer", userId);

                        //Hashmap for redeemed deals collection
                        redeemedFlashDeal.put("deal", finalDealId);
                        redeemedFlashDeal.put("user", userId);
                        redeemedFlashDeal.put("timestamp ", date);

                        //Hashmap for unavailable deal for the user
                        unavailableFlashDeal.put("unavailableDeal", finalDealId);
                        unavailableFlashDeal.put("timestamp", date);
                        unavailableFlashDeal.put("dealResumeDate", resumeDate);

                        customerFlashDealHistory.put("deal", finalDealId);
                        customerFlashDealHistory.put("timestamp", date);

                        //adding to redeemed deals
                        mFirestore.collection("redeemedFlashDeals").add(redeemedFlashDeal).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {

                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {

                                    Log.d(TAG, "onComplete: redeemedFlashDeals record added");

                                    // Adding to unavailable deals of user
                                    mFirestore.collection("customers").document(userId).collection("unavailableFlashDeals").add(unavailableFlashDeal).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "onComplete: unavailable FlashDeal added");

                                                mFirestore.collection("customers").document(userId).collection("customerFlashDealHistory").add(customerFlashDealHistory).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG, "onComplete: FlashDealHistoryDocument added");

                                                            mFirestore.collection("flashDeals").document(dealId).collection("redeemedUsers").add(scannedCustomer).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Log.d(TAG, "onComplete: redeemedUser record added");
                                                                    } else {
                                                                        Log.d(TAG, "onComplete: redeemedUser document could not be added");
                                                                    }

                                                                }
                                                            });
                                                        } else {
                                                            Log.d(TAG, "onComplete: FlashDealHistoryDocument could not be added");
                                                        }
                                                    }
                                                });
                                            } else {
                                                Log.d(TAG, "onComplete: Unavailable deal could not be added");
                                            }

                                        }
                                    });
                                } else {

                                    Log.d(TAG, "onComplete: Redeemed deal document could not be added");
                                }

                            }
                        });

                        //If flashDeal scanned the user will be rewarded 10 points.
                        mFirestore.collection("customers").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                Long userPoints = (Long) documentSnapshot.get("points");
                                Long newPoints = userPoints + 10;

                                documentSnapshot.getReference().update("points", newPoints).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "onComplete: New points have been awarded and updated.");
                                        } else {
                                            Log.d(TAG, "onComplete: New points have NOT been added and updated");
                                        }
                                    }
                                });


                            }
                        });

                        //Open the new intent
                        Intent intent = new Intent(FlashDealScanActivity.this, FlashDealRedeemSuccessActivity.class);
                        intent.putExtra("dealId", barcodeValue);
                        startActivity(intent);
                        finish();

                    } else {
                        System.out.println("Invalid");
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
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sendToLogin();
        }
    }


    //Send to LoginActivity
    private void sendToLogin() {
        Intent loginintent = new Intent(this, LogInActivity.class);
        startActivity(loginintent);
        finish();
    }

}
