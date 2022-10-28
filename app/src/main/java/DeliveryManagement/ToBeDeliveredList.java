package DeliveryManagement;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parithyaga.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ToBeDeliveredList extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    PendingDelivery pendingDelivery = new PendingDelivery();
    PickUpDelivery pickUpDelivery = new PickUpDelivery();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_to_be_delivered_list);

        bottomNavigationView = findViewById(R.id.del_bottom_sheet);
        getSupportFragmentManager().beginTransaction().replace(R.id.delframe,pendingDelivery).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.pen_del:
                        getSupportFragmentManager().beginTransaction().replace(R.id.delframe,pendingDelivery).commit();
                        return true;
                    case R.id.pick_del:
                        getSupportFragmentManager().beginTransaction().replace(R.id.delframe,pickUpDelivery).commit();
                        return true;
                }
                return false;
            }
        });

    }
}