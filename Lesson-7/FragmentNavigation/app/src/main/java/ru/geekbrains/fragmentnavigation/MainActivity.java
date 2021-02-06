package ru.geekbrains.fragmentnavigation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG_MAIN = "main";
    private final static String TAG_FAVORITE = "favorite";
    private final static String TAG_SETTINGS = "settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        initButtonMain();
        initButtonFavorite();
        initButtonSettings();
        initButtonBack();
    }

    private void initButtonBack() {
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
    }

    private void initButtonSettings() {
        Button buttonSettings = findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(new SettingsFragment(), TAG_SETTINGS);
            }
        });
    }

    private void initButtonFavorite() {
        Button buttonFavorite = findViewById(R.id.buttonFavorite);
        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(new FavoriteFragment(), TAG_FAVORITE);
            }
        });
    }

    private void initButtonMain() {
        Button buttonMain = findViewById(R.id.buttonMain);
        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(new MainFragment(), TAG_MAIN);
            }
        });
    }

    private Fragment getVisibleFragment(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        int countFragments = fragments.size();
        for (int i = countFragments - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment.isVisible())
                return fragment;
        }
        return null;
    }

    private void addFragment(Fragment fragment, String name) {

        //Получить менеджер фрагментов
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Открыть транзакцию
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Добавить фрагмент
        if (Settings.isAddFragment) {
            // Перед использованием метода add, если фрагмент не удалить,
            // то новый фрагмент наслоится на старый
            Fragment currentFragment = getVisibleFragment(fragmentManager);
            if (currentFragment != null) {
                fragmentTransaction.remove(currentFragment);
            }
            fragmentTransaction.add(R.id.fragment_container, fragment, name);
        } else {
            fragmentTransaction.replace(R.id.fragment_container, fragment, name);
        }

        // Добавить транзакцию в бэкстек
        if (Settings.isBackStack) {
            fragmentTransaction.addToBackStack(null);
        }

        // Закрыть транзакцию
        fragmentTransaction.commit();
    }

    public boolean jumpFragment(String name) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(name);
        if (fragment == null) {
            return false;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, name);

        // Добавить транзакцию в бэкстек
        if (Settings.isBackStack) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
        return true;
    }
}