package com.itparkbynipun.floodspredictor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class volunteerActivity extends AppCompatActivity {

    private DatabaseReference mdatabase;
    private EditText nametxt;
    private FirebaseAuth mAuth;
    private Button addVolunterBtn;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        mdatabase = FirebaseDatabase.getInstance().getReference().child("volunteers");
        addVolunterBtn = (Button)(findViewById(R.id.volunteerBtn));
        nametxt = (EditText)(findViewById(R.id.nametxt));
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid().toString();

        addVolunterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdatabase.child(userid).child("name").setValue(nametxt.getText().toString());
                mdatabase.child(userid).child("status").setValue("true");
                Toast.makeText(volunteerActivity.this, "Registered for Volunteer", Toast.LENGTH_SHORT).show();
                Intent volunteer = new Intent(volunteerActivity.this,MainActivity.class);
                startActivity(volunteer);
            }
        });
    }
}
