package com.raimikarim.thekiasushopper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText priceAEditText, priceBEditText, quantityAEditText, quantityBEditText;
    String priceAString, priceBString, quantityAString, quantityBString;
    String rateAString, rateBString, rateARounded, rateBRounded;
    TextView chatbox, choiceAYes, choiceBYes, choiceANo, choiceBNo;
    InputMethodManager imm;
    double priceA, priceB, rateA, rateB;
    double quantityA, quantityB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(getApplicationContext(), getString(R.string.admobs_app_id));
        // Load ad
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ////////////////////////////////////////////////////////////////////////////////////////

        chatbox = (TextView) findViewById(R.id.chat_box);
        priceAEditText = (EditText) findViewById(R.id.choice_a_price);
        priceBEditText = (EditText) findViewById(R.id.choice_b_price);
        quantityAEditText = (EditText) findViewById(R.id.choice_a_quantity);
        quantityBEditText = (EditText) findViewById(R.id.choice_b_quantity);

        choiceAYes = (TextView) findViewById(R.id.choice_a_positive);
        choiceBYes = (TextView) findViewById(R.id.choice_b_positive);
        choiceANo = (TextView) findViewById(R.id.choice_a_negative);
        choiceBNo = (TextView) findViewById(R.id.choice_b_negative);

        chatbox.setText(Html.fromHtml((getText(R.string.auntie_intro)).toString()));

        Toast.makeText(getApplicationContext(), R.string.auntie_message, Toast.LENGTH_SHORT).show();

        Button button = (Button) findViewById(R.id.button);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                priceAString = priceAEditText.getText().toString();
                priceBString = priceBEditText.getText().toString();
                quantityAString = quantityAEditText.getText().toString();
                quantityBString = quantityBEditText.getText().toString();

                // force hide keyboard
                imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                if (!isDouble(priceAString) ||
                        !isDouble(priceBString) ||
                        !isDouble(quantityAString) ||
                        !isDouble(quantityBString)) {

                    chatbox.setText(Html.fromHtml((getText(R.string.auntie_no_input)).toString()));

                } else {

                    choiceAYes.setVisibility(View.GONE);
                    choiceBYes.setVisibility(View.GONE);
                    choiceANo.setVisibility(View.GONE);
                    choiceBNo.setVisibility(View.GONE);

                    priceA = Double.parseDouble(priceAString);
                    priceB = Double.parseDouble(priceBString);
                    quantityA = Double.parseDouble(quantityAString);
                    quantityB = Double.parseDouble(quantityBString);

                    if (quantityA == 0 || quantityB == 0) {
                        chatbox.setText(Html.fromHtml((getText(R.string.auntie_division_by_zero)).toString()));
                        return;
                    }

                    rateA = priceA / quantityA;
                    rateB = priceB / quantityB;

                    rateAString = Double.toString(rateA);
                    rateBString = Double.toString(rateB);

                    if (rateAString.length() - (rateAString.indexOf(".") + 1) > 5) {
                        rateARounded = String.format("%.5f", rateA);
                    } else {
                        rateARounded = rateAString;
                    }
                    if (rateBString.length() - (rateBString.indexOf(".") + 1) > 5) {
                        rateBRounded = String.format("%.5f", rateB);
                    } else {
                        rateBRounded = rateBString;
                    }

                    if (rateA > rateB) {
                        choiceBYes.setVisibility(View.VISIBLE);
                        choiceANo.setVisibility(View.VISIBLE);
                        chatbox.setText(Html.fromHtml("<b>Auntie: " +
                                "<font color=\"#379237\">Item B</font></b> lor." +
                                "<br>A: $" + rateARounded + "/unit. " +
                                "<br>B: $" + rateBRounded + "/unit."));
                    } else if (rateA == rateB) {
                        chatbox.setText(Html.fromHtml((getText(R.string.auntie_no_diff)).toString()));
                    } else {
                        choiceAYes.setVisibility(View.VISIBLE);
                        choiceBNo.setVisibility(View.VISIBLE);
                        chatbox.setText(Html.fromHtml("<b>Auntie: " +
                                "<font color=\"#379237\">Item A</font></b> lor." +
                                "<br>A: $" + rateARounded + "/unit. " +
                                "<br>B: $" + rateBRounded + "/unit."));
                    }
                }
                Toast.makeText(getApplicationContext(), R.string.toast_auntie_reply, Toast.LENGTH_SHORT).show();
            }
        });

        Button resetButton = (Button) findViewById(R.id.reset);
        assert resetButton != null;
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceAEditText.setText("");
                priceBEditText.setText("");
                quantityAEditText.setText("");
                quantityBEditText.setText("");
                choiceAYes.setVisibility(View.GONE);
                choiceBYes.setVisibility(View.GONE);
                choiceANo.setVisibility(View.GONE);
                choiceBNo.setVisibility(View.GONE);
                chatbox.setText(Html.fromHtml((getText(R.string.auntie_intro)).toString()));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
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
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,
                        "https://play.google.com/store/apps/details?id=com.raimikarim.thekiasushopper");
                intent.setType("text/plain");
                startActivity(intent);
                break;
            }
            case R.id.nav_send: {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.raimikarim.thekiasushopper"));
                startActivity(intent);
                break;
            }
            case R.id.grocery_list:
                Toast.makeText(MainActivity.this, "Feature currently unavailable",
                        Toast.LENGTH_SHORT).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
