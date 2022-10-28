package EventManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import ui.MyEventsRecyclerViewAdapter;
import util.UserApi;


public class MyEventsFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Event");
    private TextView noEvent;
    private TextView participant_text;
    private List<CharityEvent> charityeventlist;
    private RecyclerView recyclerView;
    private MyEventsRecyclerViewAdapter myEventsRecyclerViewAdapter;
    private FloatingActionButton add_events_btn;
    private ProgressBar myeventslistprogress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        charityeventlist = new ArrayList<>();

        noEvent = view.findViewById(R.id.no_event_text);
        add_events_btn = view.findViewById(R.id.add_event_float);
        myeventslistprogress = view.findViewById(R.id.choose_card_progress);
        recyclerView = view.findViewById(R.id.my_event_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getUserEvents();

        Animation slidein = AnimationUtils.loadAnimation(getContext(),R.anim.slide_in_bottom);
        add_events_btn.startAnimation(slidein);

        add_events_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getContext(),InsertEvent.class));

            }
        });
    }

    private void getUserEvents() {
        super.onStart();
        myeventslistprogress.setVisibility(View.VISIBLE);
        Log.d("userEvent", "getUserEvents: "+ UserApi.getInstance().getUserName());
        collectionReference.whereEqualTo("userName", UserApi.getInstance().getUserName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot chairty : queryDocumentSnapshots) {
                                CharityEvent charityEvent = chairty.toObject(CharityEvent.class);
                                charityEvent.setEventId(chairty.getId());
                                charityeventlist.add(charityEvent);
                                myeventslistprogress.setVisibility(View.INVISIBLE);
                            }

                            //Invoke recycler view
                            myEventsRecyclerViewAdapter = new MyEventsRecyclerViewAdapter(MyEventsFragment.this, charityeventlist);
                            recyclerView.setAdapter(myEventsRecyclerViewAdapter);
                            myEventsRecyclerViewAdapter.notifyDataSetChanged();

                        } else {
                            noEvent.setVisibility(View.VISIBLE);
                            myeventslistprogress.setVisibility(View.INVISIBLE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myeventslistprogress.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}