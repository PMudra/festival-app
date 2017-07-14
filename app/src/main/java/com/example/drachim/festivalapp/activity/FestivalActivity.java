package com.example.drachim.festivalapp.activity;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.drachim.festivalapp.FestivalActivityPager;
import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.data.DatabaseUtil;
import com.example.drachim.festivalapp.data.Festival;
import com.example.drachim.festivalapp.data.FestivalImageLoader;
import com.example.drachim.festivalapp.data.LocalStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FestivalActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ValueEventListener {

    public static final String EXTRA_FESTIVAL_ID = "FESTIVAL_ID";

    private boolean isFestivalAccentColor;
    private int festivalAccentColor;

    private ViewPager viewPager;
    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private TabLayout tabLayout;
    private Festival festival;
    private Menu menu;

    public Festival getFestival() {
        return festival;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        setContentView(R.layout.activity_festival);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2 = (FloatingActionButton) findViewById(R.id.fab1);

        Bundle bundle = getIntent().getExtras();

        final FirebaseDatabase database = DatabaseUtil.getDatabase();
        final int festivalId = bundle.getInt(FestivalActivity.EXTRA_FESTIVAL_ID);
        DatabaseReference festivalRef = database.getReference("festivals").child(String.valueOf(festivalId));
        festivalRef.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        loadFestival(dataSnapshot.getValue(Festival.class));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void loadFestival(final Festival festival) {
        this.festival = festival;

        toggleFavorite(false);

        FestivalImageLoader.loadTitleImage(festival.getId(), new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                initColors(resource);
                initGui(resource);
            }
        });
    }

    private void initGui(Bitmap bitmap) {
        FestivalActivityPager adapter = new FestivalActivityPager(getFragmentManager(), this);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);
        if (tabLayout.getSelectedTabPosition() == 2) {
            showFab();
        }

        ImageView titleImage = (ImageView) findViewById(R.id.festivalCover);
        titleImage.setImageBitmap(bitmap);
    }

    private void initColors(Bitmap bitmap) {
        Palette palette = Palette.from(bitmap).generate();
        Palette.Swatch psVibrant = palette.getVibrantSwatch();

        isFestivalAccentColor = psVibrant != null;

        if (isFestivalAccentColor) {
            festivalAccentColor = psVibrant.getRgb();
            int festivalTextColor = psVibrant.getTitleTextColor();
            int festivalTextColorSelected = psVibrant.getBodyTextColor();

            tabLayout.setBackgroundColor(festivalAccentColor);
            tabLayout.setTabTextColors(festivalTextColor, festivalTextColorSelected);
            tabLayout.setSelectedTabIndicatorColor(festivalTextColorSelected);

            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setContentScrimColor(festivalAccentColor);
            collapsingToolbarLayout.setBackgroundColor(festivalAccentColor);
            collapsingToolbarLayout.setStatusBarScrimColor(festivalAccentColor);

            fab.setBackgroundTintList(ColorStateList.valueOf(festivalAccentColor));
            fab1.setBackgroundTintList(ColorStateList.valueOf(festivalAccentColor));
            fab2.setBackgroundTintList(ColorStateList.valueOf(festivalAccentColor));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fab.setImageTintList(ColorStateList.valueOf(festivalTextColorSelected));
                fab1.setImageTintList(ColorStateList.valueOf(festivalTextColorSelected));
                fab2.setImageTintList(ColorStateList.valueOf(festivalTextColorSelected));
            } else {
                ColorStateList cslTabSelectedTextColor = ColorStateList.valueOf(festivalTextColorSelected);
                DrawableCompat.setTintList(DrawableCompat.wrap(fab.getDrawable()), cslTabSelectedTextColor);
                DrawableCompat.setTintList(DrawableCompat.wrap(fab1.getDrawable()), cslTabSelectedTextColor);
                DrawableCompat.setTintList(DrawableCompat.wrap(fab2.getDrawable()), cslTabSelectedTextColor);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_festival, menu);
        this.menu = menu;
        toggleFavorite(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle_favorite:
                toggleFavorite(true);
                return true;
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 2) {
            showFab();
        }
    }

    private void showFab() {
        fab.setRotation(0);
        fab.show();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if (tab.getPosition() == 2) {
            fab.hide();
            fab1.hide();
            fab2.hide();
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    private void toggleFavorite(final boolean switchStatus) {
        if (festival == null || menu == null) {
            return;
        }
        final boolean isFavorite = LocalStorage.isFavorite(this, festival.getId());
        final boolean newState = isFavorite ^ switchStatus;
        final int icon = newState ? R.drawable.ic_action_favorite_white : R.drawable.ic_action_favorite_border_white;
        menu.findItem(R.id.action_toggle_favorite).setIcon(icon);
        LocalStorage.setFavorite(this, festival.getId(), newState);
    }

    public boolean hasFestivalAccentColor() {
        return isFestivalAccentColor;
    }

    public int getFestivalAccentColor() {
        return festivalAccentColor;
    }

}