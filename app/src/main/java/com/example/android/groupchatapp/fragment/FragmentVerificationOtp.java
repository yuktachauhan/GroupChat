package com.example.android.groupchatapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.activity.LoginActivity;
import com.example.android.groupchatapp.activity.SignUpActivity;
import com.example.android.groupchatapp.model.ModelOtp;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentVerificationOtp extends Fragment {

    AppCompatActivity activity;
    EditText otp_text;
    Button otp_send;
    TextView resend_otp;
    int otpNo;
    SharedPreferences sharedPreferences;
    int userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.otp_fragment_verification, container, false);
        activity = (AppCompatActivity) getActivity();
        sharedPreferences=activity.getSharedPreferences("signup",0);
        userId=sharedPreferences.getInt("usersignupid",0);
        otp_text=(EditText)view.findViewById(R.id.otp);
        otp_send=(Button) view.findViewById(R.id.otp_send);
        resend_otp=(TextView)view.findViewById(R.id.otp_resend);
        otp_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp();
            }
        });
        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend();
            }
        });
        return view;
    }

    public void sendOtp(){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("sending...");
        progressDialog.show();
        String otp =otp_text.getText().toString().trim();
        try {
             otpNo= Integer.parseInt(otp);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        ModelOtp modelOtp=new ModelOtp(otpNo);
        ApiInterface apiInterface=ApiClient.ApiClient().create(ApiInterface.class);
        Call<ModelOtp> call = apiInterface.otpSend(userId,modelOtp);
        call.enqueue(new Callback<ModelOtp>() {
            @Override
            public void onResponse(Call<ModelOtp> call, Response<ModelOtp> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    Toast.makeText(activity,"Verified",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(activity,LoginActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(activity,"Not Verified",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelOtp> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(activity,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void resend(){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("sending...");
        progressDialog.show();

        ApiInterface apiInterface= ApiClient.ApiClient().create(ApiInterface.class);
        Call<ResponseBody> call =apiInterface.resendOtp(userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    Toast.makeText(activity,"otp resend",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(activity," otp not resend",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(activity,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    }
