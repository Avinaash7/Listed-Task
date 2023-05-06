package com.openin.listed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopLinkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopLinkFragment extends Fragment {

    private VerticalLinksAdapter mAdapter;
    private final List<VerticalLinksModel> linkslist = new ArrayList<>();
    LinearLayoutManager mLayoutManager2;

    RecyclerView verticalrv;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TopLinkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TopLinkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TopLinkFragment newInstance(String param1, String param2) {
        TopLinkFragment fragment = new TopLinkFragment();
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
        View v = inflater.inflate(R.layout.fragment_top_link, container, false);

        verticalrv = v.findViewById(R.id.vertical_rv);
        mAdapter = new VerticalLinksAdapter(linkslist);
         mLayoutManager2 = new LinearLayoutManager(requireContext());
        mLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);


        requestWithSomeHttpHeaders();

        return v;
    }


    public void requestWithSomeHttpHeaders() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = "https://api.inopenapp.com/api/v1/dashboardNew";
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject array = new JSONObject(response);
                            JSONObject data = array.getJSONObject("data");
                            JSONArray top_links = data.getJSONArray("top_links");

                            for (int i = 0; i < top_links.length(); ++i) {
                                JSONObject rec = top_links.getJSONObject(i);
                                String linkurl = rec.getString("web_link");
                                String title = rec.getString("title");
                                String clicks = rec.getString("total_clicks");
                                String date = rec.getString("created_at");

                                VerticalLinksModel link = new VerticalLinksModel(title,linkurl,date,clicks);
                                linkslist.add(link);
                            }

                            verticalrv.setLayoutManager(mLayoutManager2);
                            verticalrv.setItemAnimator(new DefaultItemAnimator());
                            verticalrv.setAdapter(mAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI");
                return params;
            }
        };
        queue.add(getRequest);

    }
}