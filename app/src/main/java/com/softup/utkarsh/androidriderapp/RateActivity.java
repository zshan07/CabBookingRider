package com.softup.utkarsh.androidriderapp;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.softup.utkarsh.androidriderapp.Common.Common;
import com.softup.utkarsh.androidriderapp.Model.Rate;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RateActivity extends AppCompatActivity {

    Button btnSubmit;
    MaterialRatingBar ratingBar;
    MaterialEditText edtComment;

    FirebaseDatabase database;
    DatabaseReference rateDetailRef;
    DatabaseReference driverInformationRef;

    double ratingStars = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        database = FirebaseDatabase.getInstance();
        rateDetailRef = database.getReference(Common.rate_detail_tb1);
        driverInformationRef = database.getReference(Common.user_driver_tb1);

        //Init View
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        ratingBar = (MaterialRatingBar)findViewById(R.id.ratingBar);
        edtComment = (MaterialEditText)findViewById(R.id.edtComment);

        //Event
        ratingBar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                ratingStars = rating;
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRateDetails(Common.driverId);
            }
        });

    }

    private void submitRateDetails(final String driverId) {
        final AlertDialog alertDialog = new SpotsDialog(this);
        alertDialog.show();

        Rate rate = new Rate();
        rate.setRates(String.valueOf(ratingStars));
        rate.setComments(edtComment.getText().toString());

        //Update new value to Firebase
        rateDetailRef.child(driverId)
                .push() //Gen unique key
                .setValue(rate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //If upload succeed on Firebase , just calculate average and update to Driver Information
                        rateDetailRef.child(driverId)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        double averageStars = 0.0;
                                        int count = 0;
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                                        {
                                            Rate rate = postSnapshot.getValue(Rate.class);
                                            averageStars+=Double.parseDouble(rate.getRates());
                                            count++;
                                        }
                                        double finalAverage = averageStars/count;
                                        DecimalFormat df = new DecimalFormat("#.#");
                                        String valueUpdate = df.format(finalAverage);

                                        //Create object update
                                        Map<String,Object> driverUpdateRate = new HashMap<>();
                                        driverUpdateRate.put("rates",valueUpdate);

                                        driverInformationRef.child(Common.driverId)
                                                .updateChildren(driverUpdateRate)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                          alertDialog.dismiss();
                                                        Toast.makeText(RateActivity.this, "Thank you for submit", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        alertDialog.dismiss();
                                                        Toast.makeText(RateActivity.this, "Rate updated but can't write to Driver Information", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        alertDialog.dismiss();
                        Toast.makeText(RateActivity.this, "Rate failed !", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
