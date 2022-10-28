package DeliveryManagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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

import model.Delivery;
import ui.PickupDeliveryRecyclerViewAdapter;
import util.AdminApi;


public class PickUpDelivery extends Fragment {
    private RecyclerView recyclerView;
    private List<Delivery> pickupdelList;
    private PickupDeliveryRecyclerViewAdapter pickupDeliveryRecyclerViewAdapter;
    private ProgressBar pickupProgressl;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("ToPickUp");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.fragment_pick_up_delivery,container,false);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pickupdelList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.pickup_del_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getPickUpDeliveries();
    }

    private void getPickUpDeliveries() {
        super.onStart();

        collectionReference
                .whereEqualTo("userName", AdminApi.getInstance().getNameIn())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot delivery:queryDocumentSnapshots){
                                Delivery delivery1 = delivery.toObject(Delivery.class);
                                delivery1.setDel_id(delivery.getId());
                                pickupdelList.add(delivery1);
                            }

                            //Invoke recycler view
                            pickupDeliveryRecyclerViewAdapter = new PickupDeliveryRecyclerViewAdapter(PickUpDelivery.this,pickupdelList);
                            recyclerView.setAdapter(pickupDeliveryRecyclerViewAdapter);
                            pickupDeliveryRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}