package com.example.whatsub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class JoinActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    // 로그 찍을 때 사용하는 TAG 변수
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    EditText userid_et, passwd_et;
    Button join_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_WhatSub);
        setContentView(R.layout.activity_join);



        //Firebase 데이터베이스 인스턴스 초기화
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        // 컴포넌트 초기화
        userid_et = findViewById(R.id.userid_et);
        passwd_et = findViewById(R.id.passwd_et);
        join_button = findViewById(R.id.join_button);

        // 버튼 이벤트 추가
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 함수 호출
                //joinUser(userid_et.getText().toString(), passwd_et.getText().toString());
                String strEmail = userid_et.getText().toString();
                String strPwd = passwd_et.getText().toString();

                //Firebase Auth 진행
                mAuth.createUserWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(JoinActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebasaeUser = mAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebasaeUser.getUid());
                            account.setEmailId(firebasaeUser.getEmail());
                            account.setPassword(strPwd);

                            mDatabase.child("UserAccount").child(firebasaeUser.getUid()).setValue(account);
                            Toast.makeText(JoinActivity.this, "회원가입에 성공했습니다", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else{
                            Toast.makeText(JoinActivity.this, "회원가입에 실패했습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

}




