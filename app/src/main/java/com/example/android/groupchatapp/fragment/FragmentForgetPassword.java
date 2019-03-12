package com.example.android.groupchatapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.android.groupchatapp.model.ModelForgetPassword;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentForgetPassword extends Fragment {

    AppCompatActivity activity;
    EditText otp,new_pwd,cnf_pwd;
    TextView change_pwd;
    int otpNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forget_password_fragment, container, false);
        activity = (AppCompatActivity) getActivity();
        otp=(EditText)view.findViewById(R.id.otp_forget_password);
        new_pwd=(EditText) view.findViewById(R.id.new_pwd);
        cnf_pwd=(EditText) view.findViewById(R.id.confirm_pwd);
        change_pwd=(TextView) view.findViewById(R.id.change_pwd);
        change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPwd();
            }
        });
        return view;
    }

    public void forgetPwd(){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Changing...");
        progressDialog.show();
        String otp1=otp.getText().toString().trim();
        String newPwd=new_pwd.getText().toString().trim();
        String cnfPwd=cnf_pwd.getText().toString().trim();
        try {
            otpNo= Integer.parseInt(otp1);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        ModelForgetPassword modelForgetPassword=new ModelForgetPassword(otpNo,newPwd,cnfPwd);
        ApiInterface apiInterface=ApiClient.ApiClient().create(ApiInterface.class);
        Call<ModelForgetPassword> call =apiInterface.forgetPwd(ForgetEmail.getUserId(),modelForgetPassword);
        call.enqueue(new Callback<ModelForgetPassword>() {
            @Override
            public void onResponse(Call<ModelForgetPassword> call, Response<ModelForgetPassword> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    Toast.makeText(activity,"Password Changed",Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(activity,LoginActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(activity,"Password Not Changed",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelForgetPassword> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(activity,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
