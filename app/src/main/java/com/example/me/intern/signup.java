package com.example.me.intern;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

public class signup extends MainActivity {
    public String s1;
    private EditText name;
    private EditText age;
    private EditText email;
    private EditText mob_num;
    private EditText Pass;
    private Button b;
    private ImageView image;

    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        email = (EditText) findViewById(R.id.email);
        mob_num = (EditText) findViewById(R.id.mob_num);
        Pass = (EditText) findViewById(R.id.password);
        b = findViewById(R.id.register);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sign_up();


            }
        });

        ref=FirebaseDatabase.getInstance().getReference();

        Spinner s = (Spinner) findViewById(R.id.spin);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s1 = parent.getSelectedItem().toString();
                Log.i("Str", s1);
                //Toast.makeText(MainActivity.this, parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        image = (ImageView)findViewById(R.id.photo);
        Button capture_Button = (Button)findViewById(R.id.capture);
        capture_Button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);

            }
        });

        TextView t=(TextView)findViewById(R.id.link_signup);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),MainActivity.class);
                startActivityForResult(in, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            if (requestCode == 0) {
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                image.setImageBitmap(bitmap);
            }
        }

    }



    private void sign_up() {
        final String name1=name.getText().toString();
        final String year=age.getText().toString();
        final String mail=email.getText().toString();
        final String phone=mob_num.getText().toString();
        final String auth=Pass.getText().toString();
//        Log.i("gender", s1);
//        Log.i("name", name1);
        if(year.length()==0){
                Toast.makeText(getApplicationContext(), "Invalid Age",
                        Toast.LENGTH_SHORT).show();
        }
        else if(!isEmailValid(mail)){
            Toast.makeText(getApplicationContext(), "Invalid Email ID",
                    Toast.LENGTH_SHORT).show();
        }
        else if(mob_num.length()!=10){
            Toast.makeText(getApplicationContext(), "Enter correct mobile number",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            ref.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(getApplicationContext(), "Phone number already registered",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Userinfo add_user = new Userinfo(name1, s1, year, mail, phone, auth);

                        ref.child(add_user.number).setValue(add_user);
                        Toast.makeText(getBaseContext(), "Account created", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}