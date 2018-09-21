package com.adityasonel.delivergo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adityasonel.delivergo.R;
import com.adityasonel.delivergo.adapter.DeliveriesAdapter;
import com.adityasonel.delivergo.db.RealmManager;
import com.adityasonel.delivergo.model.DeliveryItemPOJO;
import com.adityasonel.delivergo.model.DeliveryModel;
import com.adityasonel.delivergo.util.Config;
import com.adityasonel.delivergo.util.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private ProgressBar progressBar;
    private ImageView errorImage;
    private TextView errorText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RelativeLayout parentLayout;

    private boolean isErrorOccured = false;

    private List<DeliveryItemPOJO> list = new ArrayList<>();
    private DeliveriesAdapter adapter;

    private RealmManager realmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);

        realmManager = RealmManager.getInstance(this);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        parentLayout = findViewById(R.id.parent_layout_activity_main);
        parentLayout.setPadding(0, getStatusBarHeight(), 0, 0);

        progressBar = findViewById(R.id.progress_bar);
        errorImage = findViewById(R.id.error_image);
        errorText = findViewById(R.id.error_text);
        recyclerView = findViewById(R.id.main_recycler_view);

        if (!Utils.isNetworkAvailable(this)) {
            Toast.makeText(this,
                    "Loading cached data...", Toast.LENGTH_SHORT).show();
            if (realmManager.getRealm().isEmpty()) {
                progressBar.setVisibility(View.GONE);
                errorImage.setVisibility(View.VISIBLE);
                errorText.setVisibility(View.VISIBLE);
                errorText.setText(getString(R.string.sync_atleast_once));
            } else {
                loadCachedData();
            }
        } else {
            performNetworkCall();
        }


        adapter = new DeliveriesAdapter(list, MainActivity.this, MainActivity.this);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isNetworkAvailable(MainActivity.this)) {
                    if (isErrorOccured) {
                        performNetworkCall();
                    }
                } else {
                    loadCachedData();
                }
                swipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    private void performNetworkCall() {
        final StringRequest request = new StringRequest(
                Request.Method.GET,
                Config.getMockApiUrl("0", "20"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        errorImage.setVisibility(View.GONE);
                        errorText.setVisibility(View.GONE);
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                DeliveryItemPOJO model = new DeliveryItemPOJO(array.optJSONObject(i));
                                list.add(model);
                                adapter.notifyItemInserted(i);
                                progressBar.setVisibility(View.GONE);

                                if (realmManager.getRealm().isEmpty()) {
                                    final DeliveryItemPOJO itemPOJO = new DeliveryItemPOJO(array.optJSONObject(i));
                                    realmManager.setDeliveryItemData(itemPOJO);
                                }
                            }
                        } catch (JSONException e) {
                            Log.i(TAG, "json exception: " + e);
                        }
                        isErrorOccured = false;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                errorImage.setVisibility(View.VISIBLE);
                errorText.setVisibility(View.VISIBLE);
                isErrorOccured = true;
            }
        });

        Volley.newRequestQueue(MainActivity.this).add(request);
    }

    private void loadCachedData() {
        if (!realmManager.getRealm().isEmpty()) {
            progressBar.setVisibility(View.GONE);
            list = realmManager.getDeliveryItems();
        }
    }

    int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
