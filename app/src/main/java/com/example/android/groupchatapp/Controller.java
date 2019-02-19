package com.example.android.groupchatapp;

import android.app.Application;

import com.example.android.groupchatapp.model.NumberListModel;
import com.example.android.groupchatapp.model.ModelLogin;
import com.example.android.groupchatapp.model.ModelProfile;
import com.example.android.groupchatapp.model.ModelSignUp;
import com.example.android.groupchatapp.model.ModelToken;

public class Controller extends Application {
    /*
    * This is the controller class, it will control the models and views*/

    private ModelSignUp modelSignUp =new ModelSignUp();
    private ModelLogin modelLogin=new ModelLogin() ;
    private ModelProfile modelProfile=new ModelProfile() ;
    private ModelToken modelToken=new ModelToken();
    private NumberListModel numberListModel=new NumberListModel();


     public void setModelSignUp(ModelSignUp modelSignUp){
         this.modelSignUp=modelSignUp;
     }

     public void setModelLogin(ModelLogin modelLogin){
         this.modelLogin=modelLogin;
     }

    public void setModelProfile(ModelProfile modelProfile){
        this.modelProfile=modelProfile;
    }

    public void setModelToken(ModelToken modelToken){
         this.modelToken=modelToken;
    }

    public ModelSignUp getModelSignUp(){
         return modelSignUp;
    }

    public ModelLogin getModelLogin(){
         return modelLogin;
    }

    public ModelProfile getModelProfile(){
         return modelProfile;
    }

    public ModelToken getModelToken(){
         return modelToken;
    }

    public void setNumberListModel(NumberListModel numberListModel){
        this.numberListModel=numberListModel;
    }

    public NumberListModel getNumberListModel(){
        return numberListModel;
    }


}
