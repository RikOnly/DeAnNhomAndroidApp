package com.example.deannhom.fragment.Home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.deannhom.databinding.FragmentHomeBinding;
import com.example.deannhom.utils.DictionaryApiRequest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Event handler
        binding.btnSearch.setOnClickListener(v -> {
            String word = Objects.requireNonNull(binding.inputWord.getText()).toString();
            if (word.isEmpty()) {
                binding.inputWord.setError("Word can't not be empty!");
                return;
            }

            Activity activity = this.getActivity();
            assert activity != null;

            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            DictionaryApiRequest dictionaryApiRequest = new DictionaryApiRequest();
            dictionaryApiRequest.fetchDefinitionFromApi(word, definition -> {
                if (definition != null) {
                    binding.textDefinition.setText(definition);
                } else {
                    Toast.makeText(this.getContext(), "Word not found!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}