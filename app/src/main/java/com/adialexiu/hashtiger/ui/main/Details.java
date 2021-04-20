package com.adialexiu.hashtiger.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adialexiu.hashtiger.DatabaseHelper;
import com.adialexiu.hashtiger.MainActivity;
import com.adialexiu.hashtiger.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Details#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Details extends Fragment {

    TextView name;
    Button logoutButton;
    GoogleSignInClient mGoogleSignInClient;
    SharedPreferences sharedPref;
    public static final String PREFERENCES_KEY = "preferences key";
    public static final String PREFERENCES_ID_KEY = "preferences id key";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Details() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Details.
     */
    // TODO: Rename and change types and number of parameters
    public static Details newInstance(String param1, String param2) {
        Details fragment = new Details();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        name = getView().findViewById(R.id.textView);
        logoutButton = getView().findViewById(R.id.button_sign_out);

        sharedPref = getActivity().getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        String username = sharedPref.getString(PREFERENCES_ID_KEY, "Nu merge nimica");

        name.setText(username);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_sign_out:
                        signOut();
                        break;
                }

            }
        });

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        makeSharedPreferences();
                        Toast.makeText(getActivity(), "Signed Out", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                });
    }

    private void makeSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}