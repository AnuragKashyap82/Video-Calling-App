package kashyap.anurag.videomeeting.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.videomeeting.Utilities.Constants;
import kashyap.anurag.videomeeting.Utilities.PreferenceManager;
import kashyap.anurag.videomeeting.databinding.ActivitySignUpBinding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private String firstName, lastName, email, password, cPassword;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());

        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finishAffinity();
            }
        });
        binding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
        firstName = binding.inputName.getText().toString().trim();
        lastName = binding.inputLastName.getText().toString().trim();
        email = binding.inputEmail.getText().toString().trim();
        password = binding.inputPassword.getText().toString().trim();
        cPassword = binding.inputCPassword.getText().toString().trim();

        if (firstName.isEmpty()){
            Toast.makeText(SignUpActivity.this, "bhadwasala.....", Toast.LENGTH_SHORT).show();
        }else if (lastName.isEmpty()){
            Toast.makeText(SignUpActivity.this, "bakrichod.....", Toast.LENGTH_SHORT).show();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(SignUpActivity.this, "bsdk.....", Toast.LENGTH_SHORT).show();
        }else if (password.length() < 8){
            Toast.makeText(SignUpActivity.this, "lassan.....", Toast.LENGTH_SHORT).show();
        }else if (!cPassword.equals(password)){
            Toast.makeText(SignUpActivity.this, "loda.....", Toast.LENGTH_SHORT).show();
        }else {
            createUserWithEmailAndPassword(email, password, firstName, lastName, password, cPassword);
        }
    }

    private void createUserWithEmailAndPassword(String email, String password, String firstName, String lastName, String s, String cPassword) {
        binding.buttonSignUp.setVisibility(View.GONE);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(Constants.KEY_FIRST_NAME, this.firstName);
        hashMap.put(Constants.KEY_LAST_NAME, this.lastName);
        hashMap.put(Constants.KEY_EMAIL, email);
        hashMap.put(Constants.KEY_PASSWORD, password);

        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(hashMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                        preferenceManager.putString(Constants.KEY_FIRST_NAME, firstName);
                        preferenceManager.putString(Constants.KEY_LAST_NAME, lastName);
                        preferenceManager.putString(Constants.KEY_EMAIL, email);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.buttonSignUp.setVisibility(View.VISIBLE);
                        Toast.makeText(SignUpActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}