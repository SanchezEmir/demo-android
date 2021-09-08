package com.sanchezemir.appcobranza.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.sanchezemir.appcobranza.R;
import com.sanchezemir.appcobranza.providers.ImageProvider;
import com.sanchezemir.appcobranza.utils.FileUtil;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddClientesActivity extends AppCompatActivity {

    CircleImageView mImagenCLiente;
    private final int GALERY_REQUEST_CODE = 1;
    File mImageFile;
    Button mButtonPost;
    ImageProvider mImageProvider;
    TextInputEditText mTextInputNombre;
    TextInputEditText mTextInputdni;
    TextInputEditText mTextInputTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clientes);

        mImageProvider = new ImageProvider();

        mTextInputNombre = findViewById(R.id.txtNombre);
        mTextInputdni = findViewById(R.id.txtDni);
        mTextInputTelefono= findViewById(R.id.txtTelefono);


        mImagenCLiente = findViewById(R.id.addFotoCliente);
        mImagenCLiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalery();
            }
        });

        mButtonPost = findViewById(R.id.btnAddCliente);
        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveImage();
                clickPost();
            }
        });


    }

    private void clickPost(){
        String nombre = mTextInputNombre.getText().toString();
        String dni = mTextInputdni.getText().toString();
        String telefono = mTextInputTelefono.getText().toString();

        if(!nombre.isEmpty() && !dni.isEmpty() && !telefono.isEmpty()){
            if (mImageFile != null){
                saveImage();
            }else {
                Toast.makeText(this, "Debes seleccionar una imagen", Toast.LENGTH_SHORT).show();
            }
            
        }else{
            Toast.makeText(this, "Completa los campos para publicar", Toast.LENGTH_SHORT).show();
            
        }


    }

    private void saveImage(){
        mImageProvider.save(AddClientesActivity.this, mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url= uri.toString();
                        }
                    });


                }else{
                    Toast.makeText(AddClientesActivity.this, "Ocurrio un error al almacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void openGalery(){
        Intent galeryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galeryIntent.setType("image/*");
        startActivityForResult(galeryIntent, GALERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALERY_REQUEST_CODE && resultCode == RESULT_OK){
            try {
                //trandormar la url en archivo
                mImageFile = FileUtil.from(this,data.getData());
                //termina de seleccionar la imagen de galeria
                mImagenCLiente.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            }catch (Exception e){
                Log.d("ERRORFOTO", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " +e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}