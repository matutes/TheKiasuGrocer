package com.raimikarim.thekiasushopper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText priceAEditText;
    private EditText priceBEditText;
    private EditText quantityAEditText;
    private EditText quantityBEditText;
    private TextView chatBox;
    private TextView choiceAYes;
    private TextView choiceBYes;
    private TextView choiceANo;
    private TextView choiceBNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatBox = findViewById(R.id.chat_box);
        priceAEditText = findViewById(R.id.choice_a_price);
        priceBEditText = findViewById(R.id.choice_b_price);
        quantityAEditText = findViewById(R.id.choice_a_quantity);
        quantityBEditText = findViewById(R.id.choice_b_quantity);
        choiceAYes = findViewById(R.id.choice_a_positive);
        choiceBYes = findViewById(R.id.choice_b_positive);
        choiceANo = findViewById(R.id.choice_a_negative);
        choiceBNo = findViewById(R.id.choice_b_negative);

        View.OnFocusChangeListener showHideKeyboard = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showKeyboard();
                else
                    hideKeyboard(v);
            }
        };
        priceAEditText.setOnFocusChangeListener(showHideKeyboard);
        priceBEditText.setOnFocusChangeListener(showHideKeyboard);
        quantityAEditText.setOnFocusChangeListener(showHideKeyboard);
        quantityBEditText.setOnFocusChangeListener(showHideKeyboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        chatBox.setText(Html.fromHtml((getText(R.string.auntie_intro)).toString()));
    }

    @SuppressWarnings("unused")
    public void reset(View v) {
        setViewsGone();
        priceAEditText.setText("");
        priceBEditText.setText("");
        quantityAEditText.setText("");
        quantityBEditText.setText("");
        chatBox.setText(Html.fromHtml((getText(R.string.auntie_intro)).toString()));
    }

    private void setViewsGone() {
        choiceAYes.setVisibility(View.GONE);
        choiceBYes.setVisibility(View.GONE);
        choiceANo.setVisibility(View.GONE);
        choiceBNo.setVisibility(View.GONE);
    }

    @SuppressWarnings("unused")
    public void compare(View v) {

        setViewsGone();

        String priceAString = priceAEditText.getText().toString();
        String priceBString = priceBEditText.getText().toString();
        String quantityAString = quantityAEditText.getText().toString();
        String quantityBString = quantityBEditText.getText().toString();

        Calculator calculator = new Calculator(priceAString, quantityAString, priceBString,
                quantityBString);

        if (!calculator.isAllDouble()) {
            chatBox.setText(Html.fromHtml(
                    (getText(R.string.auntie_no_input)).toString()));
            return;
        }
        calculator.parseStrings();

        if (!calculator.isAllValid()) {
            chatBox.setText(Html.fromHtml(
                    (getText(R.string.auntie_division_by_zero)).toString()));
            return;
        }
        calculator.calculateRates();

        String rateARounded = calculator.getRateARounded();
        String rateBRounded = calculator.getRateBRounded();

        if (calculator.isRateAHigher() == 1) {
            choiceBYes.setVisibility(View.VISIBLE);
            choiceANo.setVisibility(View.VISIBLE);
            chatBox.setText(Html.fromHtml("<b>Auntie: " +
                    "<font color=\"#379237\">Item B</font></b> more worth it." +
                    "<br>A: $" + rateARounded + "/unit. " +
                    "<br>B: $" + rateBRounded + "/unit."));

        } else if (calculator.isRateAHigher() == 0) {
            chatBox.setText(Html.fromHtml(
                    (getText(R.string.auntie_no_diff)).toString()));

        } else {
            choiceAYes.setVisibility(View.VISIBLE);
            choiceBNo.setVisibility(View.VISIBLE);
            chatBox.setText(Html.fromHtml("<b>Auntie: " +
                    "<font color=\"#379237\">Item A</font></b> more worth it." +
                    "<br>A: $" + rateARounded + "/unit. " +
                    "<br>B: $" + rateBRounded + "/unit."));
        }
    }

    private void showKeyboard() {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        final String appPackageName = getPackageName();

        int id = item.getItemId();

        switch (id) {
            case R.id.about: {
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.how_to_use: {
                Intent intent = new Intent(this, HowToUseActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_share: {
                String uri = "https://play.google.com/store/apps/details?id=" + appPackageName;
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, uri).setType("text/plain");
                startActivity(intent);
                break;
            }
            case R.id.nav_rate: {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.raimikarim.thekiasushopper"));
                startActivity(intent);
                break;
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
