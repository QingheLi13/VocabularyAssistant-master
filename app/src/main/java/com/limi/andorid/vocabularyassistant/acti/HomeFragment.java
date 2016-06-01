package com.limi.andorid.vocabularyassistant.acti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.limi.andorid.vocabularyassistant.R;
import com.limi.andorid.vocabularyassistant.app.AppController;
import com.limi.andorid.vocabularyassistant.helper.ColorArcProgressBar;
import com.special.ResideMenu.ResideMenu;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by limi on 16/4/20.
 */


public class HomeFragment extends Fragment {

    //    private String TAG = getActivity().getClass().getSimpleName();
    private View parentView;
    private ResideMenu resideMenu;
    private ColorArcProgressBar colorArcProgressBar;
    private TextView textView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.homefragment, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        MainActivity parentActivity = (MainActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

        parentView.findViewById(R.id.btn_open_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MissionActivity.class);
                startActivity(i);
            }
        });

        // add gesture operation's ignored views
        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.ignored_view);
        resideMenu.addIgnoredView(ignored_view);
        colorArcProgressBar = (ColorArcProgressBar) parentView.findViewById(R.id.progressbar);

        textView = (TextView) parentView.findViewById(R.id.todayMeaning);
        colorArcProgressBar.setCurrentValues(77);
        colorArcProgressBar.setDiameter(200);
        colorArcProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), RecitingActivity.class);
                startActivity(i);
            }
        });
        getSentenceOfToday();


    }

    @Override
    public void onStart() {
        super.onStart();
        colorArcProgressBar.setCurrentValues(77);
        colorArcProgressBar.setDiameter(200);
        getSentenceOfToday();
    }

    private void getSentenceOfToday() {
        String tag_string_req = "getSentence";

        String url = "http://open.iciba.com/dsapi";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    String eScentence = jObj.getString("content");
                    textView.setText(eScentence);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.i("Daily Sentence Error", error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(request, tag_string_req);

    }

}
