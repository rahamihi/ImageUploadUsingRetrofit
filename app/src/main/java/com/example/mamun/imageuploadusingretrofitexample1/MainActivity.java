package com.example.mamun.imageuploadusingretrofitexample1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.print.PrinterId;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private ImageView imageView;
    private Button choseBtn,uploadBtn;
    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editText);
        choseBtn = (Button) findViewById(R.id.chooseBtn);
        uploadBtn = (Button) findViewById(R.id.uploadBtn);

        choseBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);





    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.chooseBtn:
                selectImage();
                break;
            case R.id.uploadBtn:
                uploadImage();
                break;
        }
    }


    private void selectImage(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== IMG_REQUEST && resultCode==RESULT_OK && data!=null)
        {
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);
                choseBtn.setEnabled(false);
                uploadBtn.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String convertToString()
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte,Base64.DEFAULT);
    }

    private void uploadImage()
    {
        String image = convertToString();
        String imageName = editText.getText().toString();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<ImageClass> call = apiInterface.uploadImage(imageName,image);
        call.enqueue(new Callback<ImageClass>() {
            @Override
            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
                ImageClass imageClass = response.body();
                Toast.makeText(getApplicationContext(),"server response"+ imageClass.getResponse(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ImageClass> call, Throwable t) {

            }
        });

    }





}
