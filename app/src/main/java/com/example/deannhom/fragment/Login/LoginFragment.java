package com.example.deannhom.fragment.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.deannhom.R;
import com.example.deannhom.databinding.FragmentLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    private FragmentLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize Google Login
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this.requireActivity(), googleSignInOptions);

        // Event handlers
        binding.btnSignIn.setOnClickListener(Login());
        binding.btnSignUp.setOnClickListener(view -> NavHostFragment.findNavController(this).navigate(R.id.action_navigation_login_to_navigation_register));

        /*binding.btnGoogleLogin.setOnClickListener(GoogleLogin());*/

        return root;
    }

    /*private View.OnClickListener GoogleLogin() {
        return view -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();

            startActivityForResult(signInIntent, 1000);
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//
//                firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task1 -> {
//                    if (task.isSuccessful()) {
//
//
//                    } else {
//                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.i(TAG, account.getId());

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

                Toast.makeText(this.getContext(), "Login Successfully", Toast.LENGTH_LONG).show();

            } catch (ApiException e) {
                e.printStackTrace();
                Log.i(TAG, "Error: " + e.getStatus());
                Toast.makeText(this.getContext(), "Login failed!" + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }*/

    View.OnClickListener Login() {
        return view -> {
            // Get user input;
            String email = Objects.requireNonNull(binding.inputEmail.getText()).toString();
            String password = Objects.requireNonNull(binding.inputPassword.getText()).toString();

            if (email.isEmpty()) {
                binding.inputEmail.setError("Email can not be blank!");
                return;
            }

            if (password.isEmpty()) {
                binding.inputPassword.setError("Password can not be blank!");
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this.requireActivity(), task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this.getContext(), "Email or password is incorrect!", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            });
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }

        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            return;
        }

        NavHostFragment.findNavController(this).navigate(R.id.action_navigation_login_to_navigation_account);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}