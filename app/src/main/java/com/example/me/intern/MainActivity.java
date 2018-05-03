package com.example.me.intern;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText number;
    private EditText Pass;
    private DatabaseReference ref;
    private int i=0,count=0;

    Handler handler;
    Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        handler = new Handler();
        r = new Runnable() {

            @Override
            public void run() {
                Intent in=new Intent(getApplicationContext(),MainActivity.class);
                startActivityForResult(in, 1);
            }
        };
        startHandler();

        number=(EditText) findViewById(R.id.mob_num);
        Pass=(EditText) findViewById(R.id.input_password);

        TextView t=(TextView)findViewById(R.id.link_signup);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),signup.class);
                startActivityForResult(in, 0);
            }
        });

        final Button login = (Button)findViewById(R.id.btn_login);
        login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(!validate() ){
                        if(count>=3){
                            new CountDownTimer(300000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    login.setEnabled(false);
                                }

                                public void onFinish() {
                                    login.setEnabled(true);
                                    count=0;
                                }
                            }.start();
                        }
                    }else{
                        count++;
                    }i=0;
            }
        });

    }

    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        stopHandler();
        startHandler();
    }
    public void stopHandler() {
        handler.removeCallbacks(r);
    }
    public void startHandler() {
        handler.postDelayed(r, 600000);
    }

    private boolean validate() {
        final String phone=number.getText().toString();
        final String auth=Pass.getText().toString();
        ref= FirebaseDatabase.getInstance().getReference();
        ref.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Userinfo contact = snapshot.getValue(Userinfo.class);
                    String p=contact.password;
                    if(p.equals(auth)){
                        Intent in1=new Intent(getApplicationContext(),RSSReader.class);
                        Log.d("contact:: ", contact.number);
                        startActivityForResult(in1, 10);
                    }
                    else{
                        Toast.makeText(getBaseContext(), "Wrong Password", Toast.LENGTH_LONG).show();
                        i++;
                    }
                }

                else {
                    Toast.makeText(getBaseContext(), "No such user registered", Toast.LENGTH_LONG).show();
                    i++;
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        if(i!=0){
            return false;
        }return true;
    }


}
