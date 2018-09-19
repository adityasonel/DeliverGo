package com.adityasonel.delivergo.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adityasonel.delivergo.R;
import com.adityasonel.delivergo.adapter.DeliveriesAdapter;
import com.adityasonel.delivergo.model.DeliveryModel;
import com.adityasonel.delivergo.ui.RecyclerViewItemDivider;
import com.adityasonel.delivergo.util.Config;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    private static final String TAG = MainActivity.class.getName();

    private ProgressBar progressBar;
    private ImageView errorImage;
    private TextView errorText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RelativeLayout parentLayout;

    private boolean isErrorOccured = false;

    private ArrayList<DeliveryModel> list = new ArrayList<>();
    private DeliveriesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        parentLayout = findViewById(R.id.parent_layout_activity_main);
        parentLayout.setPadding(0, getStatusBarHeight(), 0, 0);

        progressBar = findViewById(R.id.progress_bar);
        errorImage = findViewById(R.id.error_image);
        errorText = findViewById(R.id.error_text);
        recyclerView = findViewById(R.id.main_recycler_view);

        performNetworkCall();

        adapter = new DeliveriesAdapter(list, MainActivity.this, MainActivity.this);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isErrorOccured) {
                    performNetworkCall();
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
        list.clear();
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
                                DeliveryModel model = new DeliveryModel(array.optJSONObject(i));
                                list.add(model);
                                adapter.notifyItemInserted(i);
                                progressBar.setVisibility(View.GONE);
                                Log.i("xoxo", array.optJSONObject(i).optString("description") + "");
                            }
                        } catch (JSONException e) {
                            Log.i("xoxo", "json exception: " + e);
                        }
                        isErrorOccured = false;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                errorImage.setVisibility(View.VISIBLE);
                errorText.setVisibility(View.VISIBLE);
                if (error != null) {
                    Log.i("xoxo", "onErrorResponse: " + error);
                }
                isErrorOccured = true;
            }
        });

        Volley.newRequestQueue(MainActivity.this).add(request);
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
