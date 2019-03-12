package com.example.android.groupchatapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.activity.LoginActivity;
import com.example.android.groupchatapp.model.ModelForgetPassword;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetEmail extends Fragment {
    EditText email;
    Button send_email;
    AppCompatActivity activity;
    private static int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_email, container, false);
        activity = (AppCompatActivity) getActivity();
        email=(EditText) view.findViewById(R.id.forget_email);
        send_email=(Button) view.findViewById(R.id.email_send);
        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        return view;
    }

    public void sendEmail(){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Sending...");
        progressDialog.show();
        String emailId=email.getText().toString().trim();
        ModelForgetPassword modelForgetPassword=new ModelForgetPassword(emailId);
        ApiInterface apiInterface=ApiClient.ApiClient().create(ApiInterface.class);
        Call<ModelForgetPassword> call =apiInterface.forgetEmail(modelForgetPassword);
        call.enqueue(new Callback<ModelForgetPassword>() {
            @Override
            public void onResponse(Call<ModelForgetPassword> call, Response<ModelForgetPassword> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    Toast.makeText(activity, "successful", Toast.LENGTH_SHORT).show();
                    id = response.body().getUser_id();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container2, new FragmentForgetPassword())
                            .commit();
                }else{
                    Toast.makeText(activity, "not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelForgetPassword> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static int getUserId(){
        return id;
    }
}
