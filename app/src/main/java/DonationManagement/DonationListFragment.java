package DonationManagement;

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
import ui.UserDonationsRecyclerAdpater;


public class DonationListFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Donations> alldonationslist;
    private UserDonationsRecyclerAdpater userDonationsRecyclerAdpater;
    private ProgressBar donationlistprogress;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Donation");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donation_list,container,false);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alldonationslist = new ArrayList<>();

        donationlistprogress = view.findViewById(R.id.donation_frag_progress);
        recyclerView = view.findViewById(R.id.recyclerview_donations);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getUserDonations();
    }

    private void getUserDonations() {
        super.onStart();
        donationlistprogress.setVisibility(View.VISIBLE);

            collectionReference.whereNotEqualTo("donationAmount","Rs.0.0")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot userdonations: queryDocumentSnapshots){
                                Donations user_donations = userdonations.toObject(Donations.class);
                                user_donations.setDonationNumber(userdonations.getId());
                                alldonationslist.add(user_donations);
                            }
                            userDonationsRecyclerAdpater = new UserDonationsRecyclerAdpater(DonationListFragment.this,alldonationslist);
                            recyclerView.setAdapter(userDonationsRecyclerAdpater);
                            userDonationsRecyclerAdpater.notifyDataSetChanged();
                            donationlistprogress.setVisibility(View.INVISIBLE);
                            
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        donationlistprogress.setVisibility(View.INVISIBLE);
                        Log.d("donationsList", "onFailure: "+e.toString());
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}