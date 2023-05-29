package com.example.helloworldapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.helloworldapplication.entities.Request;
import com.example.helloworldapplication.entities.RequestStatus;
import com.example.helloworldapplication.entities.Status;
import com.example.helloworldapplication.entities.User;
import com.example.helloworldapplication.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Profile extends AppCompatActivity {

    TextInputEditText editTextFirstName, editTextLastName, editTextDepartment,
            editTextGrade, editTextPhoneNumber, editTextDuration, editTextDistance;
    Button buttonSave;
    TextInputLayout textInputLayoutDuration, textInputLayoutDistance;
    TextView textViewDuration, textViewDistance;
    ImageView profileImageView;
    Spinner spinner;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userRef = db.collection("user");

    CollectionReference requestRef = db.collection("request");

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;



    public void onStart() {
        super.onStart();

        System.out.println("onStart worked");

    }

    @Override
    public void onBackPressed() {

        Intent intent = getIntent();
        String fromActivity = intent.getStringExtra("FROM_ACTIVITY");

        if (fromActivity != null && fromActivity.equals("STUDENTS")) {
            Intent studentActivity = new Intent(this, Students.class);
            startActivity(studentActivity);
        } else {
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
        }

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume worked");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String userId;

        mAuth = FirebaseAuth.getInstance();
        editTextFirstName = findViewById(R.id.firstName);
        editTextLastName = findViewById(R.id.lastName);
        editTextDepartment = findViewById(R.id.department);
        editTextGrade = findViewById(R.id.grade);
        editTextPhoneNumber = findViewById(R.id.phone);
        buttonSave = findViewById(R.id.btn_save);
        profileImageView = findViewById(R.id.profile_photo);
        spinner = findViewById(R.id.status);
        editTextDistance = findViewById(R.id.distance);
        editTextDuration = findViewById(R.id.duration);
        textInputLayoutDistance = findViewById(R.id.distanceLayout);
        textInputLayoutDuration = findViewById(R.id.durationLayout);
        textViewDistance = findViewById(R.id.distanceLabel);
        textViewDuration = findViewById(R.id.durationLabel);

        Status[] options = Status.values();

        ArrayAdapter<Status> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")){
            userId = intent.getStringExtra("userId");

            // editTextlerin enable edilmesi

            editTextFirstName.setEnabled(false);
            editTextLastName.setEnabled(false);
            editTextDepartment.setEnabled(false);
            editTextGrade.setEnabled(false);
            editTextPhoneNumber.setEnabled(false);
            editTextDistance.setEnabled(false);
            editTextDuration.setEnabled(false);
            spinner.setEnabled(false);

            buttonSave.setText("Eşleşme Talebi Gönder");

            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Request request = new Request();
                    request.setId(Utils.generateRandomId(6));
                    request.setSenderId(mAuth.getCurrentUser().getUid());
                    request.setReceiverId(userId);
                    request.setStatus(RequestStatus.NOTRESPONSEYET);
                    request.setViewed(false);

                    db.collection("request").document(request.getId()).set(request)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Profile.this, "Eşleşme Talebi Gönderildi.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Profile.this, "Eşleşme Talebi Gönderilmedi.", Toast.LENGTH_SHORT).show();

                                }
                            });



                }
            });

        } else {
            userId = mAuth.getCurrentUser().getUid();
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Status selectedOption = (Status) spinner.getSelectedItem();
                    if(selectedOption == Status.HOME) {
                        System.out.println("HOME");
                        textInputLayoutDistance.setVisibility(View.VISIBLE);
                        textInputLayoutDuration.setVisibility(View.VISIBLE);
                        textViewDistance.setVisibility(View.VISIBLE);
                        textViewDuration.setVisibility(View.VISIBLE);
                        editTextDistance.setHint(R.string.home_distance);
                        editTextDuration.setHint(R.string.home_duration);
                    } else if(selectedOption == Status.FRIEND) {
                        System.out.println("FRIEND");
                        textInputLayoutDistance.setVisibility(View.VISIBLE);
                        textInputLayoutDuration.setVisibility(View.VISIBLE);
                        textViewDistance.setVisibility(View.VISIBLE);
                        textViewDuration.setVisibility(View.VISIBLE);
                        editTextDistance.setHint(R.string.friend_distance);
                        editTextDuration.setHint(R.string.friend_duration);
                    } else if (selectedOption == Status.NONE) {
                        textViewDistance.setVisibility(View.GONE);
                        textViewDuration.setVisibility(View.GONE);
                        textInputLayoutDistance.setVisibility(View.GONE);
                        textInputLayoutDuration.setVisibility(View.GONE);
                    } else {
                        System.out.println("Dropdown Seçiniz'de");
                        textViewDistance.setVisibility(View.GONE);
                        textViewDuration.setVisibility(View.GONE);
                        textInputLayoutDistance.setVisibility(View.GONE);
                        textInputLayoutDuration.setVisibility(View.GONE);
                    }
                };

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String userEmail = mAuth.getCurrentUser().getEmail();
                    Query query = userRef.whereEqualTo("email", userEmail);

                    query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            User user = documentSnapshot.toObject(User.class);


                            user.setFirstName(editTextFirstName.getText().toString());
                            user.setLastName(editTextLastName.getText().toString());
                            user.setDepartment(editTextDepartment.getText().toString());
                            user.setGrade(editTextGrade.getText().toString());
                            user.setPhoneNumber(editTextPhoneNumber.getText().toString());
                            user.setStatus((Status) spinner.getSelectedItem());
                            user.setDistance(editTextDistance.getText().toString());
                            user.setDuration(editTextDuration.getText().toString());
                            documentSnapshot.getReference().set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(Profile.this, "Kullanıcı Profili Kaydedildi.", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(Profile.this, "Kullanıcı Profili Oluşturulurken Hata oldu.", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(Profile.this, "Kullanıcı Bulunamadı.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                    });
                }
            });

            profileImageView.setOnClickListener(v -> {
                showImageSelectionMenu();
            });

        }

        Query query = userRef.whereEqualTo("id", userId);

        query.get().addOnSuccessListener( queryDocumentSnapshots -> {
            if(!queryDocumentSnapshots.isEmpty()){
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                User user = documentSnapshot.toObject(User.class);

                String firstName = user.getFirstName();
                String lastName = user.getLastName();
                String department = user.getDepartment();
                String grade = user.getGrade();
                String phoneNumber = user.getPhoneNumber();
                String profilePhotoUrl = user.getProfilePhotoUrl();
                Status status = user.getStatus();

                if( status != null) {
                    int selectedIndex = Arrays.asList(options).indexOf(status);
                    spinner.setSelection(selectedIndex);
                    setLookforInputs(status);
                }
                if( firstName != null) editTextFirstName.setText(firstName);
                if( lastName != null) editTextLastName.setText(lastName);
                if( department != null) editTextDepartment.setText(department);
                if( grade != null) editTextGrade.setText(grade);
                if( phoneNumber != null) editTextPhoneNumber.setText(phoneNumber);
                if( profilePhotoUrl != null) Glide.with(Profile.this).load(profilePhotoUrl).into(profileImageView);

            }
        }  ).addOnFailureListener( e -> {});




    }

    private void setLookforInputs(Status status){

        editTextDistance = findViewById(R.id.distance);
        editTextDuration = findViewById(R.id.duration);
        textInputLayoutDistance = findViewById(R.id.distanceLayout);
        textInputLayoutDuration = findViewById(R.id.durationLayout);

        System.out.println("new Status is: " + status.toString() );
        switch (status){
            case HOME:
                System.out.println("HOME");
                textInputLayoutDistance.setVisibility(View.VISIBLE);
                textInputLayoutDuration.setVisibility(View.VISIBLE);
                editTextDistance.setHint("Kampüse İstenen Uzaklık");
                editTextDuration.setHint("Evde Kalınacak Süre");
            case FRIEND:
                System.out.println("FRIEND");
                textInputLayoutDistance.setVisibility(View.VISIBLE);
                textInputLayoutDuration.setVisibility(View.VISIBLE);
                editTextDistance.setHint("Kampüse Olan Ev Uzaklığı");
                editTextDuration.setHint("Evi Paylaşacağı Süre");
            case NONE:
                textInputLayoutDistance.setVisibility(View.GONE);
                textInputLayoutDuration.setVisibility(View.GONE);
            case NULL:
                textInputLayoutDistance.setVisibility(View.GONE);
                textInputLayoutDuration.setVisibility(View.GONE);

        }
    }

    private void showImageSelectionMenu() {
        PopupMenu popupMenu = new PopupMenu(this, profileImageView);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_camera:
                    requestCameraPermission();
                    return true;
                case R.id.menu_gallery:
                    openGallery();
                    return true;
                default:
                    return false;
            }
        });

        popupMenu.show();
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(Profile.this, "Kamera İzni Verilmedi.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputStream inputStream = null;
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            inputStream = new ByteArrayInputStream(baos.toByteArray());

        } else if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                inputStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        String userId = mAuth.getCurrentUser().getUid();

        StorageReference profilePhotoRef = storageRef.child("profile_photos/"+userId+".jpg");


        profilePhotoRef.putStream(inputStream)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            profilePhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    String downloadUrl = downloadUri.toString();
                                    System.out.println(downloadUrl);
                                    Query query = userRef.whereEqualTo("id", userId);

                                    query.get().addOnSuccessListener(
                                            queryDocumentSnapshots -> {
                                                if( !(queryDocumentSnapshots.isEmpty())){
                                                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                                    User user = documentSnapshot.toObject(User.class);
                                                    user.setProfilePhotoUrl(downloadUrl);

                                                    documentSnapshot.getReference().set(user).addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(Profile.this, "Fotoğraf kaydedildi.", Toast.LENGTH_SHORT).show();
                                                        Glide.with(Profile.this)
                                                                .load(downloadUrl)
                                                                .into(profileImageView);

                                                    }).addOnFailureListener(e -> {
                                                        Toast.makeText(Profile.this, "Url kaydetme esnasında hata oluştu.", Toast.LENGTH_SHORT).show();
                                                    });
                                                }
                                            }
                                    );
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
    }

    private void openCamera() {
        Toast.makeText(Profile.this, "Kamera Açılıyor...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void openGallery() {
        Toast.makeText(Profile.this, "Galeri Açılıyor...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

}