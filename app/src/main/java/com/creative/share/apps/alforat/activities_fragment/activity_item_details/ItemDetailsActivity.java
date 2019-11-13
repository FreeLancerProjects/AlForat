package com.creative.share.apps.alforat.activities_fragment.activity_item_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.adapters.PricesAdapter;
import com.creative.share.apps.alforat.databinding.ActivityItemDetailsBinding;
import com.creative.share.apps.alforat.interfaces.Listeners;
import com.creative.share.apps.alforat.language.LanguageHelper;
import com.creative.share.apps.alforat.models.ItemCartModel;
import com.creative.share.apps.alforat.models.ProductDataModel;
import com.creative.share.apps.alforat.singleton.CartSingleTon;

import java.util.Locale;

import io.paperdb.Paper;

public class ItemDetailsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityItemDetailsBinding binding;
    private String lang;
    private ProductDataModel.ProductModel productModel = null;
    private PricesAdapter adapter;
    private int count = 1;
    private int bonus = 0;
    private boolean isGeneralOffer = false;
    private double offer = 0.0;
    private String general_offer_type = null;
    private CartSingleTon cartSingleTon;
    private ProductDataModel.Price price = null;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_details);
        getDataFromIntent();
        initView();

    }
    private void getDataFromIntent()
    {

        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("data"))
        {
            productModel = (ProductDataModel.ProductModel) intent.getSerializableExtra("data");
        }
    }
    private void initView()
    {
        cartSingleTon = CartSingleTon.newInstance();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setProductModel(productModel);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PricesAdapter(productModel.getPrices(),this);
        binding.recView.setAdapter(adapter);
        if (productModel.getProduct_offer() ==null)
        {
            bonus = 0;
            binding.tvBonus.setText(String.valueOf(bonus));

        }

        binding.imageIncrease.setOnClickListener(view -> {
            count++;
            binding.edtCount.setText(String.valueOf(count));
            calculateData();
        });

        binding.imageDecrease.setOnClickListener(view -> {
            if (count>1)
            {
                count--;
                binding.edtCount.setText(String.valueOf(count));
                calculateData();

            }

        });

        binding.edtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty())
                {
                    binding.edtCount.setText("1");
                    count = 1;
                }else
                    {
                        count = Integer.parseInt(editable.toString());
                    }

                calculateData();
            }
        });
        binding.flAddItem.setOnClickListener(view -> {

            if (price!=null)
            {
                ItemCartModel.Items items = new ItemCartModel.Items(productModel.getId_item(),productModel.getItem_name(),productModel.getItem_image(),count,bonus,price.getPrice_value(),price.getTax_value());
                items.setPrice_id(price.getPrice_id());

                items.setCurrent_amount(productModel.getCurrent());

                if (isGeneralOffer)
                {
                    items.setLimit(0);
                    items.setOffer_value(0);
                }else
                    {

                        if (productModel.getProduct_offer()!=null)
                        {
                            items.setLimit(Integer.parseInt(productModel.getProduct_offer().getOffer_limit()));
                            items.setOffer_value(Integer.parseInt(productModel.getProduct_offer().getOffer_value()));

                        }else
                            {
                                items.setLimit(0);
                                items.setOffer_value(0);

                            }
                    }

                cartSingleTon.addItem(items);
                Intent intent = getIntent();
                intent.putExtra("isGeneralOffer",isGeneralOffer);
                intent.putExtra("offer",offer);
                intent.putExtra("general_offer_type",general_offer_type);


                setResult(RESULT_OK,intent);
                finish();

            }else
                {
                    Toast.makeText(this,getString(R.string.ch_price), Toast.LENGTH_SHORT).show();
                }
        });

    }
    private void calculateData()
    {

        if (productModel.getGeneralaOffer()==null)
        {
            isGeneralOffer = false;
            general_offer_type = null;
            if (productModel.getProduct_offer()!=null)
            {
                if (productModel.getProduct_offer().getOffer_type().equals("8"))
                {


                    if (count<Integer.parseInt(productModel.getProduct_offer().getOffer_limit()))
                    {
                        bonus=0;
                        binding.tvBonus.setText(String.valueOf(bonus));

                    }else
                    {
                        if (count%Integer.parseInt(productModel.getProduct_offer().getOffer_limit())==0)
                        {

                            if (Integer.parseInt(productModel.getProduct_offer().getOffer_limit())!=0)
                            {
                                int bonus_value = (count/Integer.parseInt(productModel.getProduct_offer().getOffer_limit()))*Integer.parseInt(productModel.getProduct_offer().getOffer_value());

                                if ((bonus_value+count)<productModel.getCurrent())
                                {
                                    bonus = bonus_value;
                                    binding.tvBonus.setText(String.valueOf(bonus));

                                }else
                                {
                                    Toast.makeText(this,getString(R.string.inv_amount), Toast.LENGTH_SHORT).show();
                                }
                            }else
                                {
                                    bonus=0;
                                    binding.tvBonus.setText(String.valueOf(bonus));

                                }




                        }else
                        {
                            int bonus_value = (count/Integer.parseInt(productModel.getProduct_offer().getOffer_limit()))*Integer.parseInt(productModel.getProduct_offer().getOffer_value());
                            bonus = bonus_value;
                            binding.tvBonus.setText(String.valueOf(bonus));


                        }
                    }

                }else if (productModel.getProduct_offer().getOffer_type().equals("0"))
                {

                }
            }
        }else
            {
                general_offer_type = productModel.getGeneralaOffer().getOffer_type();
                isGeneralOffer = true;
                bonus=0;

                if (productModel.getGeneralaOffer().getOffer_type().equals("0"))
                {
                    offer = Double.parseDouble(productModel.getGeneralaOffer().getOffer_value());
                }
            }


    }

    @Override
    public void back() {
        finish();
    }

    public void setItemData(ProductDataModel.Price price) {
        this.price = price;


    }
}
