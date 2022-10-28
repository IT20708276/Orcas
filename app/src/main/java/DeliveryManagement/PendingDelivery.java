package DeliveryManagement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parithyaga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import model.Donations;
import ui.PendingDelRecyclerViewAdapter;


public class PendingDelivery extends Fragment {
    private RecyclerView recyclerView;
    private List<Donations> pendingdonlist;
    private PendingDelRecyclerViewAdapter pendingDelRecyclerViewAdapter;
    private ProgressBar pendingdelprogress;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Donation");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_delivery,container,false);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pendingdonlist = new ArrayList<>();
        pendingdelprogress = view.findViewById(R.id.pickup_del_progress);
        recyclerView = view.findViewById(R.id.pickup_del_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getPendingDeliveries();
    }

    private void getPendingDeliveries() {
        super.onStart();
        pendingdelprogress.setVisibility(View.VISIBLE);

        collectionReference.whereEqualTo("donationAmount","Rs.0.0")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot pendel: queryDocumentSnapshots){
                                Donations pendDelivery = pendel.toObject(Donations.class);
                                pendDelivery.setDonationNumber(pendel.getId());
                                pendingdonlist.add(pendDelivery);
                                pendingdelprogress.setVisibility(View.INVISIBLE);
                            }
                            pendingDelRecyclerViewAdapter = new PendingDelRecyclerViewAdapter(PendingDelivery.this,pendingdonlist);
                            recyclerView.setAdapter(pendingDelRecyclerViewAdapter);
                            pendingDelRecyclerViewAdapter.notifyDataSetChanged();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pendingdelprogress.setVisibility(View.INVISIBLE);
                        Log.d("pendinglist", "onFailure: "+e.toString());
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


}