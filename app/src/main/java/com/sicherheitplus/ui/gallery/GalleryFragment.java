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
        for (int i = 0; i < 20; i++) {
            Button button = new Button(getContext());
            int day = (int) (Math.random() * 30 + 1);
            int month = (int) (Math.random() * 12 + 1);
            int year = (int) (Math.random() * 20 + 2000);
            int hour = (int) (Math.random() * 24);
            int minute = (int) (Math.random() * 60);
            int second = (int) (Math.random() * 60);

            button.setText(day + "/" + month + "/" + year + " - " + hour + ":" + minute + ":" + second);

            linearLayout.addView(button);
        }

    }
}
