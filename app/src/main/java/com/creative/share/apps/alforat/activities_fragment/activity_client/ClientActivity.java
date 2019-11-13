package com.creative.share.apps.alforat.activities_fragment.activity_client;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_add_client.AddClientActivity;
import com.creative.share.apps.alforat.adapters.ClientsAdapter;
import com.creative.share.apps.alforat.databinding.ActivityClientBinding;
import com.creative.share.apps.alforat.interfaces.Listeners;
import com.creative.share.apps.alforat.language.LanguageHelper;
import com.creative.share.apps.alforat.models.ClientsDataModel;
import com.creative.share.apps.alforat.models.UserModel;
import com.creative.share.apps.alforat.preferences.Preferences;
import com.creative.share.apps.alforat.remote.Api;
import com.creative.share.apps.alforat.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityClientBinding binding;
    private String lang;
    private LinearLayoutManager manager;
    private List<ClientsDataModel.ClientModel> clientModelList;
    private ClientsAdapter adapter;
    private boolean isLoading = false;
    private int current_page = 1;
    private Preferences preferences;
    private UserModel userModel;
    private String query = "";
    private boolean isWaitingData = false;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client);
        getDataFromIntent();
        initView();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("client")) {
            isWaitingData = true;
        }
    }


    private void initView() {
        clientModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        binding.imageAddClient.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddClientActivity.class);
            startActivityForResult(intent, 100);
        });
        adapter = new ClientsAdapter(clientModelList, this);
        binding.recView.setAdapter(adapter);
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int total_items = adapter.getItemCount();
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();

                    if (lastVisibleItem > 5 && lastVisibleItem == (total_items - 2) && !isLoading) {
                        isLoading = true;
                        int page = current_page + 1;
                        clientModelList.add(null);
                        adapter.notifyDataSetChanged();
                        if (query.isEmpty()) {
                            loadMore(page);

                        } else {
                            loadMoreSearchByName(page);

                        }
                    }
                }
            }
        });

        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String query = binding.edSearch.getText().toString().trim();
                if (query.length() > 0) {
                    binding.imageDelete.setVisibility(View.VISIBLE);
                } else {
                    binding.imageDelete.setVisibility(View.GONE);
                    ClientActivity.this.query = "";
                    getClients();

                }
            }
        });
        binding.edSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (EditorInfo.IME_ACTION_SEARCH == i) {
                query = binding.edSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    getClientsByName(query);

                }
            }
            return false;
        });
        binding.imageDelete.setOnClickListener(view -> {
            query = "";
            binding.imageDelete.setVisibility(View.GONE);
            binding.edSearch.setText("");
            getClients();
        });
        getClients();


    }

    private void getClients() {
        Api.getService(Tags.base_url)
                .getClients(userModel.getToken(), 1, 15)
                .enqueue(new Callback<ClientsDataModel>() {
                    @Override
                    public void onResponse(Call<ClientsDataModel> call, Response<ClientsDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getClientModelList() != null) {
                            clientModelList.clear();
                            clientModelList.addAll(response.body().getClientModelList());
                            adapter.notifyDataSetChanged();

                            if (clientModelList.size() > 0) {
                                binding.llNoClient.setVisibility(View.GONE);
                            } else {
                                binding.llNoClient.setVisibility(View.VISIBLE);

                            }
                        } else {

                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(ClientActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ClientActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ClientsDataModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ClientActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ClientActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void loadMore(int page) {
        Api.getService(Tags.base_url)
                .getClients(userModel.getToken(), page, 15)
                .enqueue(new Callback<ClientsDataModel>() {
                    @Override
                    public void onResponse(Call<ClientsDataModel> call, Response<ClientsDataModel> response) {
                        isLoading = false;
                        clientModelList.remove(clientModelList.size() - 1);
                        adapter.notifyDataSetChanged();
                        if (response.isSuccessful() && response.body() != null && response.body().getClientModelList() != null) {

                            current_page = response.body().getMeta().getCurrent_page();
                            clientModelList.addAll(response.body().getClientModelList());
                            adapter.notifyDataSetChanged();
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(ClientActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ClientActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ClientsDataModel> call, Throwable t) {
                        try {
                            clientModelList.remove(clientModelList.size() - 1);
                            isLoading = false;
                            adapter.notifyDataSetChanged();

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ClientActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ClientActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void getClientsByName(String query) {
        Api.getService(Tags.base_url)
                .searchClientByeName(userModel.getToken(), query, 1, 15)
                .enqueue(new Callback<ClientsDataModel>() {
                    @Override
                    public void onResponse(Call<ClientsDataModel> call, Response<ClientsDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getClientModelList() != null) {
                            clientModelList.clear();
                            clientModelList.addAll(response.body().getClientModelList());
                            adapter.notifyDataSetChanged();

                            if (clientModelList.size() > 0) {
                                binding.llNoClient.setVisibility(View.GONE);
                            } else {
                                binding.llNoClient.setVisibility(View.VISIBLE);

                            }
                        } else {

                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(ClientActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ClientActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ClientsDataModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ClientActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ClientActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void loadMoreSearchByName(int page) {
        Api.getService(Tags.base_url)
                .searchClientByeName(userModel.getToken(), query, page, 15)
                .enqueue(new Callback<ClientsDataModel>() {
                    @Override
                    public void onResponse(Call<ClientsDataModel> call, Response<ClientsDataModel> response) {
                        isLoading = false;
                        clientModelList.remove(clientModelList.size() - 1);
                        adapter.notifyDataSetChanged();
                        if (response.isSuccessful() && response.body() != null && response.body().getClientModelList() != null) {

                            current_page = response.body().getMeta().getCurrent_page();
                            clientModelList.addAll(response.body().getClientModelList());
                            adapter.notifyDataSetChanged();
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(ClientActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ClientActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ClientsDataModel> call, Throwable t) {
                        try {
                            clientModelList.remove(clientModelList.size() - 1);
                            isLoading = false;
                            adapter.notifyDataSetChanged();

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ClientActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ClientActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            getClients();
        }
    }

    @Override
    public void back() {
        finish();
    }

    public void setItemData(ClientsDataModel.ClientModel clientModel) {

        if (isWaitingData) {
            Intent intent = getIntent();
            if (intent != null) {
                intent.putExtra("client", clientModel);
                setResult(RESULT_OK, intent);
            }
            finish();

        } else {

        }
    }
}

