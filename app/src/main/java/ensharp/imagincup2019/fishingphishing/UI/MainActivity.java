package ensharp.imagincup2019.fishingphishing.UI;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import ensharp.imagincup2019.fishingphishing.UI.Fragments.LogFragment;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.NumbersFragment;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.RecentsFragment;
import ensharp.imagincup2019.fishingphishing.R;

public class MainActivity extends AppCompatActivity {

    private ImageButton[] bottomButtons;
    private Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragments = new Fragment[] {
                new RecentsFragment(), new NumbersFragment(), new LogFragment()
        };

        bottomButtons = new ImageButton[] {
                findViewById(R.id.recentsButton),
                findViewById(R.id.numbersButton),
                findViewById(R.id.logButton)
        };

        for (int i = 0; i < fragments.length; i++) {
            final Fragment fragment = fragments[i];
            final int position = i;
            bottomButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, fragment)
                            .commit();
                    setBottomButtons(position);
                }
            });
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragments[0])
                .commit();
    }

    public void setBottomButtons(int position) {
        switch (position) {
            case 0:
                bottomButtons[0].setImageResource(R.drawable.icon_recents_clicked);
                bottomButtons[1].setImageResource(R.drawable.icon_numbers_normal);
                bottomButtons[2].setImageResource(R.drawable.icon_analytics_normal);
                break;
            case 1:
                bottomButtons[0].setImageResource(R.drawable.icon_recents_normal);
                bottomButtons[1].setImageResource(R.drawable.icon_numbers_clicked);
                bottomButtons[2].setImageResource(R.drawable.icon_analytics_normal);
                break;
            case 2:
                bottomButtons[0].setImageResource(R.drawable.icon_recents_normal);
                bottomButtons[1].setImageResource(R.drawable.icon_numbers_normal);
                bottomButtons[2].setImageResource(R.drawable.icon_analytics_clicked);
                break;

        }
    }
}
