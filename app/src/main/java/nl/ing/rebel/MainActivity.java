package nl.ing.rebel;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

import lombok.val;
import nl.ing.rebel.screens.PlaceholderFragment;
import nl.ing.rebel.screens.TransitionOverviewFragment;
import nl.ing.rebel.transitions.AccountTransitions;
import nl.ing.rebel.transitions.TransactionTransitions;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private FrameLayout mContainer;
    private FragmentManager fragmentManager;
    private int selectedTab;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedTab", selectedTab);

        // store current fragment.... to restore it later
    }

    private class MenuSelectedListener implements TabLayout.OnTabSelectedListener {
        private final Map<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();

        public MenuSelectedListener() {
            val f = new TransitionOverviewFragment();
            f.setTransitions(new AccountTransitions());
            val x = new TransitionOverviewFragment();
            x.setTransitions(new TransactionTransitions());
            fragments.put(0, f);
            fragments.put(1, x);
            fragments.put(2, PlaceholderFragment.newInstance(2));
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            val m = getSupportFragmentManager();
            val uniqueFragment = fragments.get(tab.getPosition());
            m.beginTransaction().replace(R.id.container, uniqueFragment).commit();
            selectedTab = tab.getPosition();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) { }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the ViewPager with the sections adapter.
        mContainer = (FrameLayout) findViewById(R.id.container);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Accounts"), 0);
        tabLayout.addTab(tabLayout.newTab().setText("Transactions"), 1);
        tabLayout.addTab(tabLayout.newTab().setText("Info"), 2);

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            selectedTab = savedInstanceState.getInt("selectedTab");
            tabLayout.getTabAt(selectedTab).select();
            System.out.println("Select tab: " + selectedTab);
            tabLayout.addOnTabSelectedListener(new MenuSelectedListener());
            return;
        }
        tabLayout.addOnTabSelectedListener(new MenuSelectedListener());


        // Initial code
        val fragmentTransaction = fragmentManager.beginTransaction();
        val f = new TransitionOverviewFragment();
        f.setTransitions(new AccountTransitions());
        fragmentTransaction.add(R.id.container, f);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}