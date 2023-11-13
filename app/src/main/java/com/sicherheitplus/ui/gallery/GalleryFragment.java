package com.sicherheitplus.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sicherheitplus.R;
import com.sicherheitplus.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final LinearLayout linearLayout = root.findViewById(R.id.historyHolder);

        // Create and add buttons to the LinearLayout
        createAndAddButtons(linearLayout);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void createAndAddButtons(LinearLayout linearLayout) {
        // Create and add buttons here
        Button button1 = new Button(requireContext());
        button1.setText("Button 1");
        button1.setTextColor(getResources().getColor(R.color.black));

        Button button2 = new Button(requireContext());
        button2.setText("Button 2");
        button2.setTextColor(getResources().getColor(R.color.black));

        // Add buttons to the LinearLayout
        linearLayout.addView(button1);
        linearLayout.addView(button2);
    }
}
