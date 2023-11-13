package com.sicherheitplus.ui.slideshow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.sicherheitplus.R;
import com.sicherheitplus.databinding.FragmentSlideshowBinding;

import java.util.HashMap;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private SharedPreferences sharedPreferences;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        final LinearLayout linearLayout = root.findViewById(R.id.contactHolder); // Replace with your LinearLayout's ID
        sharedPreferences = requireActivity().getSharedPreferences("Contacts", Context.MODE_PRIVATE);

        // Create and add buttons to the LinearLayout
        createAndAddButtons(linearLayout);

        return root;
    }

    private void createAndAddButtons(LinearLayout linearLayout) {
        HashMap<String, String> contacts = (HashMap<String, String>) sharedPreferences.getAll();
        for (String key : contacts.keySet()) {
            Button button = new Button(getContext());
            button.setText(String.format("%s - %s", key, contacts.get(key)));
            button.setOnClickListener(v -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(key);
                editor.apply();
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.nav_slideshow);

            });
            linearLayout.addView(button);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}