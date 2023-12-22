package com.example.appbookticketmovie.ui.user;

import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.appbookticketmovie.MainActivity;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.databinding.FragmentHomeBinding;
import com.example.appbookticketmovie.databinding.FragmentUserBinding;
import com.example.appbookticketmovie.ui.home.HomeViewModel;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private UserViewModel userViewModel;
    private VideoView backgroundView;

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        backgroundView = binding.videoView2;
//        int rawId = getResources().getIdentifier("iryna_happy holidays_moment",  "raw", getContext().getPackageName());

        Uri uri = Uri.parse("android.resource://"+ requireContext().getPackageName() +"/" +R.raw.background);
        backgroundView.setVideoURI(uri);

        MediaController mediaController = new MediaController(requireContext());

        // sets the anchor view
        // anchor view for the videoView
        mediaController.setAnchorView(backgroundView);

        // sets the media player to the videoView
        mediaController.setMediaPlayer(backgroundView);

        // sets the media controller to the videoView
        backgroundView.setMediaController(mediaController);
        backgroundView.start();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // TODO: Use the ViewModel
    }

}