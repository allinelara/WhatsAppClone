package com.example.allininha.whatsapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.allininha.whatsapp.fragment.ContatosFragment;
import com.example.allininha.whatsapp.fragment.ConversasFragment;

/**
 * Created by allininha on 08/12/17.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    private  String[] titulosAbas={"CONVERSAS", "CONTATOS"};
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new ConversasFragment();
                break;
            case 1:
                fragment = new ContatosFragment();
                break;
            default:
                fragment = new ConversasFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return titulosAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titulosAbas[position];
    }
}
