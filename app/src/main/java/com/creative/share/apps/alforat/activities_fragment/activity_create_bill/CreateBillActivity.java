package com.creative.share.apps.alforat.activities_fragment.activity_create_bill;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_client.ClientActivity;
import com.creative.share.apps.alforat.adapters.CartAdapter;
import com.creative.share.apps.alforat.adapters.PriceAdapter;
import com.creative.share.apps.alforat.adapters.SpinnerProductAdapter;
import com.creative.share.apps.alforat.databinding.ActivityCreateBillBinding;
import com.creative.share.apps.alforat.interfaces.Listeners;
import com.creative.share.apps.alforat.language.LanguageHelper;
import com.creative.share.apps.alforat.models.ClientsDataModel;
import com.creative.share.apps.alforat.models.CreateBillModel;
import com.creative.share.apps.alforat.models.ProductDataModel;
import com.creative.share.apps.alforat.models.UserModel;
import com.creative.share.apps.alforat.preferences.Preferences;
import com.creative.share.apps.alforat.remote.Api;
import com.creative.share.apps.alforat.share.Common;
import com.creative.share.apps.alforat.tags.Tags;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateBillActivity extends AppCompatActivity implements Listeners.BackListener, DatePickerDialog.OnDateSetListener {
    private ActivityCreateBillBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private ClientsDataModel.ClientModel clientModel = null;
    private DatePickerDialog datePickerDialog;
    private long bill_date = 0, dueDate = 0;
    private int selected_date = 0;
    private SpinnerProductAdapter spinnerProductAdapter;
    private PriceAdapter priceAdapter;
    private List<ProductDataModel.Price> priceList;
    private List<ProductDataModel.ProductModel> productModelList;

    private String item_id = "";
    private String price_id = "";
    private String discount_id = "";
    private String center_cost_id = "";
    private int current_amount = 0;
    private double price = 0;
    private int amount = 0;
    private double tax = 0;
    private double discount = 0;
    private int bonus = 0;
    private CreateBillModel createBillModel;
    private CreateBillModel.Item item,final_item;
    private CartAdapter adapter;
    private List<CreateBillModel.Item> itemList;
    private LinearLayoutManager manager;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_bill);

        initView();
    }

    private void initView() {
        itemList = new ArrayList<>();
        item = new CreateBillModel.Item();

        createBillModel = new CreateBillModel();
        productModelList = new ArrayList<>();
        productModelList.add(new ProductDataModel.ProductModel(getString(R.string.choose), ""));
        priceList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);


        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);

        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        /*adapter = new CartAdapter(itemList,this);
        binding.recView.setAdapter(adapter);*/


        spinnerProductAdapter = new SpinnerProductAdapter(this, productModelList);
        binding.spinnerProducts.setAdapter(spinnerProductAdapter);

        priceAdapter = new PriceAdapter(this, priceList);
        binding.spinnerPrice.setAdapter(priceAdapter);


        binding.spinnerProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    priceList.clear();
                    priceAdapter.notifyDataSetChanged();
                    item_id = "";
                    discount_id = "";
                    price_id = "";
                    price = 0;
                    discount = 0;
                    bonus = 0;
                    tax = 0;
                    current_amount = 0;

                    binding.tvTotal.setText("0.00");
                    binding.edtTax.setText("");
                    binding.edtAmount.setText("");
                    binding.edtBouns.setText("");

                    item.setItem_id("");
                    item.setItem_name("");



                } else {

                   // current_amount = Integer.parseInt(productModelList.get(i).getCurrent());
                    item_id = productModelList.get(i).getId_item();
                    item.setItem_id(item_id);
                    item.setItem_name(productModelList.get(i).getItem_name());

                    priceList.addAll(productModelList.get(i).getPrices());
                    priceAdapter.notifyDataSetChanged();



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    price_id = "";
                    price = 0;
                } else {

                }
                if (!item_id.isEmpty()) {
                   // calculateTotal(price, discount, bonus, amount, tax);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerDiscount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    discount_id = "";
                    discount = 0;
                    item.setDiscount_id(discount_id);
                    item.setDiscount_title("");

                } else {

                }
                if (!item_id.isEmpty()) {
                    calculateTotal(price, discount, bonus, amount, tax);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > 0) {
                    amount = Integer.parseInt(editable.toString().trim());
                } else {
                    amount = 0;
                }

                if (!item_id.isEmpty()) {
                    calculateTotal(price, discount, bonus, amount, tax);

                }


            }
        });
        binding.edtBouns.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (editable.toString().length() > 0) {
                    bonus = Integer.parseInt(editable.toString().trim());
                } else {
                    bonus = 0;
                }

                if (!item_id.isEmpty()) {
                    Log.e("4", "4");
                    calculateTotal(price, discount, bonus, amount, tax);

                }


            }
        });
        binding.edtTax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (editable.toString().length() > 0) {
                    tax = Double.parseDouble(editable.toString().trim());
                } else {
                    tax = 0.0;
                }

                if (!item_id.isEmpty()) {
                    calculateTotal(price, discount, bonus, amount, tax);

                }


            }
        });

        binding.llClient.setOnClickListener(view -> {
            Intent intent = new Intent(this, ClientActivity.class);
            intent.putExtra("client", true);
            startActivityForResult(intent, 100);
        });

        binding.llBillDate.setOnClickListener(view -> {
            selected_date = 1;
            datePickerDialog.show(getFragmentManager(), "");

        });

        binding.llDueDate.setOnClickListener(view -> {
            selected_date = 2;
            datePickerDialog.show(getFragmentManager(), "");

        });

        binding.flAddItem.setOnClickListener(view -> {
            String bill = binding.edtBillStatement.getText().toString().trim();

            if (clientModel != null &&
                    bill_date != 0 &&
                    !center_cost_id.isEmpty() &&
                    dueDate != 0 &&
                    !bill.isEmpty() &&
                    !item_id.isEmpty() &&
                    !price_id.isEmpty() &&
                    tax!=0&&
                    amount>0&&
                    !discount_id.isEmpty()


            ) {

                binding.tvClient.setError(null);
                binding.tvBillDate.setError(null);
                binding.tvDueDate.setError(null);
                binding.edtBillStatement.setError(null);
                binding.edtAmount.setError(null);
                binding.edtTax.setError(null);

                createBillModel.setBill(bill);
                createBillModel.setClient_id(clientModel.getId_client());
                createBillModel.setClient_name(clientModel.getClient_name());
                createBillModel.setBill_date(bill_date);
                createBillModel.setDue_date(dueDate);



                createBillModel.addItem(item);
                final_item = new CreateBillModel.Item(item.getItem_id(),item.getItem_name(),item.getUnit(),item.getBonus(),item.getTax(),item.getTotal_tax_value(),item.getDiscount_id(),item.getDiscount(),item.getDiscount_title(),item.getTotal_discount_value(),item.getTotal_after_calc(),item.getAmount(),item.getPrice(),item.getNet_total());

                itemList.add(final_item);
                adapter.notifyDataSetChanged();
                binding.AllTotal.setText(String.format(Locale.ENGLISH,"%.2f",createBillModel.getTotal()));
                binding.tvAllTotalDiscount.setText(String.format(Locale.ENGLISH,"%.2f",createBillModel.getTotal_discount_value()));
                binding.AllTotalTax.setText(String.format(Locale.ENGLISH,"%.2f",createBillModel.getTotal_tax_value()));
                binding.tvNetTotalPrice.setText(String.format(Locale.ENGLISH,"%.2f",createBillModel.getNet_total()));

                if (!binding.expandLayout.isExpanded())
                {
                    binding.expandLayout.setExpanded(true,true);

                }

            } else {
                if (clientModel == null) {
                    binding.tvClient.setError(getString(R.string.field_req));
                } else {
                    binding.tvClient.setError(null);

                }



                if (bill_date == 0) {
                    binding.tvBillDate.setError(getString(R.string.field_req));
                } else {
                    binding.tvBillDate.setError(null);

                }

                if (center_cost_id.isEmpty()) {
                    Toast.makeText(CreateBillActivity.this, R.string.ch_center, Toast.LENGTH_SHORT).show();
                }

                if (dueDate == 0) {
                    binding.tvDueDate.setError(getString(R.string.field_req));
                } else {
                    binding.tvDueDate.setError(null);

                }


                if (bill.isEmpty()) {
                    binding.edtBillStatement.setError(getString(R.string.field_req));
                } else {
                    binding.edtBillStatement.setError(null);

                }


                if (item_id.isEmpty()) {
                    Toast.makeText(CreateBillActivity.this, R.string.ch_item, Toast.LENGTH_SHORT).show();
                }

                if (price_id.isEmpty()) {
                    Toast.makeText(CreateBillActivity.this, R.string.ch_price, Toast.LENGTH_SHORT).show();
                }


                if (amount==0) {
                    binding.edtAmount.setError(getString(R.string.field_req));
                }else
                    {
                        binding.edtAmount.setError(null);

                    }

                if (tax==0) {
                    binding.edtTax.setError(getString(R.string.field_req));
                }else
                {
                    binding.edtTax.setError(null);

                }

                if (discount_id.isEmpty()) {
                    Toast.makeText(this, R.string.ch_disc, Toast.LENGTH_SHORT).show();
                }

            }
        });

        setDate();
        createDateDialog();
        getProducts();

    }

    private void calculateTotal(double price, double discount, int bonus, int amount, double tax) {
        if (!item_id.isEmpty()) {


            if ((amount + bonus) > current_amount) {
                if (amount > 0) {
                    Toast.makeText(this, getString(R.string.amount_not_aval), Toast.LENGTH_SHORT).show();

                }

            } else {

                if (amount > 0) {
                    double amount_price = amount * price;
                    double discount_value;
                    double price_after_discount;
                    double price_after_tax;
                    double tax_value;


                    if (discount > 0 && tax > 0) {
                        discount_value = amount_price * (discount / 100.0);
                        price_after_discount = Math.abs(amount_price - discount_value);
                        tax_value = price_after_discount * (tax / 100.0);
                        price_after_tax = price_after_discount + tax_value;



                        binding.tvTotal.setText(String.format(Locale.ENGLISH, "%.2f", price_after_tax));


                        item.setAmount(amount);
                        item.setPrice(price);
                        item.setBonus(bonus);
                        item.setDiscount(discount/100.0);
                        item.setTax(tax/100.0);
                        item.setTotal_discount_value(price_after_discount);
                        item.setTotal_tax_value(tax_value);
                        item.setNet_total(amount_price);
                        item.setTotal_after_calc(price_after_tax);


                    }


                }

            }
        } else {
            Toast.makeText(this, R.string.ch_item, Toast.LENGTH_SHORT).show();
        }
    }

    private void setDate() {
        Calendar calendar = Calendar.getInstance();
        bill_date = calendar.getTimeInMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String date = dateFormat.format(new Date(bill_date));
        binding.tvBillDate.setText(date);

    }

    private void createDateDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setOkColor(ContextCompat.getColor(this, R.color.colorPrimary));
        datePickerDialog.setCancelColor(ContextCompat.getColor(this, R.color.gray5));
        datePickerDialog.setOkText(getString(R.string.select));
        datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(calendar);


    }

    private void getProducts() {
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {
            Api.getService(Tags.base_url)
                    .getStoreProducts(userModel.getToken())
                    .enqueue(new Callback<ProductDataModel>() {
                        @Override
                        public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                updateSpinnersUI(response.body());
                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(CreateBillActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(CreateBillActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductDataModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(CreateBillActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(CreateBillActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }


    private void updateSpinnersUI(ProductDataModel productDataModel) {
        productModelList.addAll(productDataModel.getData());
        spinnerProductAdapter.notifyDataSetChanged();


    }


    @Override
    public void back() {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("client")) {
                clientModel = (ClientsDataModel.ClientModel) data.getSerializableExtra("client");
                binding.tvClient.setText(clientModel.getClient_name());

                createBillModel.setClient_id(clientModel.getId_client());
                createBillModel.setClient_name(clientModel.getClient_name());

            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String date = dateFormat.format(new Date(calendar.getTimeInMillis()));


        if (selected_date == 1) {
            bill_date = calendar.getTimeInMillis();

            binding.tvBillDate.setText(date);
            createBillModel.setBill_date(bill_date);

        } else if (selected_date == 2) {
            dueDate = calendar.getTimeInMillis();
            binding.tvDueDate.setText(date);
            createBillModel.setBill_date(dueDate);


        }
    }
}
