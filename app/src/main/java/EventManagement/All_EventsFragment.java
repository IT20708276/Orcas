package EventManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parithyaga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import model.CharityEvent;
import ui.AllCharityRecyclerViewAdapter;
import util.UserApi;


public class All_EventsFragment extends Fragment {
    private FloatingActionButton sponsorRedbtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String currentUserID;
    private String currentUserName;
    private int increment;
    private CollectionReference collectionReference = db.collection("Event");
    private TextView noallEvent;
    private TextView allparticipant_text;
    private List<CharityEvent> allcharityeventlist;
    private RecyclerView allrecyclerView;
    private AllCharityRecyclerViewAdapter allCharityRecyclerViewAdapter;
    private Button join_btn;
    private ProgressBar alleventporgress;

    private CharityEvent charityEvent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all__events, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if(UserApi.getInstance() != null){
            currentUserID = UserApi.getInstance().getUserID();
            currentUserName = UserApi.getInstance().getUserName();

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allcharityeventlist = new ArrayList<>();
        allrecyclerView = view.findViewById(R.id.all_events_recycler);
        sponsorRedbtn = view.findViewById(R.id.go_to_sponsors);
        alleventporgress = view.findViewById(R.id.all_event_progress);

        allrecyclerView.setHasFixedSize(true);
        allrecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getEveryEvent();


        Animation slidein = AnimationUtils.loadAnimation(getContext(),R.anim.slide_in_top);
        sponsorRedbtn.startAnimation(slidein);


        sponsorRedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),SponsorRecords.class));

            }
        });

    }

    private void getEveryEvent() {
        super.onStart();
        alleventporgress.setVisibility(View.VISIBLE);
        collectionReference
                .whereNotEqualTo("userId",currentUserID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot allevents: queryDocumentSnapshots){
                                CharityEvent charityEvent = allevents.toObject(CharityEvent.class);
                                charityEvent.setEventId(allevents.getId());
                                allcharityeventlist.add(charityEvent);
                            }

                            //invoking all recyclerview
                            allCharityRecyclerViewAdapter= new AllCharityRecyclerViewAdapter(All_EventsFragment.this,allcharityeventlist);
                            allrecyclerView.setAdapter(allCharityRecyclerViewAdapter);
                            allCharityRecyclerViewAdapter.notifyDataSetChanged();
                            alleventporgress.setVisibility(View.INVISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        alleventporgress.setVisibility(View.INVISIBLE);
                        Log.d("all", "onFailure: "+e.toString());
                    }
                });
    }
    }
