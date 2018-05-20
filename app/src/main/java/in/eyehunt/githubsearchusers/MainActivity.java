package in.eyehunt.githubsearchusers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    RequestQueue queue;
    List<Users> listOfusers;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listOfusers = new ArrayList<Users>();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        editText = (EditText) findViewById(R.id.searchAutoComplete);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editText.getText().toString();
                if (s.length() >= 3) {
                    searchUsers(s);
                } else {
                    listOfusers.clear();
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void searchUsers(String user) {

        String searchUser = user.replaceAll("\\s+","%20");
        listOfusers.clear();
        if (queue != null) {
            queue.cancelAll(this);
            queue.stop();
        }

        String url = "https://api.github.com/search/users?q=" + searchUser + "+sort:followers";
        queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response: ", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArrayItems = jsonObject.getJSONArray("items");
                    for (int i = 0; i < jsonArrayItems.length(); i++) {

                        JSONObject jsonObj = jsonArrayItems.getJSONObject(i);
                        Users users = new Users();
                        users.setId(jsonObj.getInt("id"));
                        users.setLogin(jsonObj.getString("login"));
                        listOfusers.add(users);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter = new Myadapter(listOfusers);
                mRecyclerView.setAdapter(mAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }

}
