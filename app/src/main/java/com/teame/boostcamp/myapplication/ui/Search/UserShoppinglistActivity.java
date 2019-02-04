package com.teame.boostcamp.myapplication.ui.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityUsersShoppinglistBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;

public class UserShoppinglistActivity extends BaseMVPActivity<ActivityUsersShoppinglistBinding, UserShoppinglistContract.Presenter> {

    @Override
    protected UserShoppinglistContract.Presenter getPresenter() {
        return null;
    }

    @Override
    public void setPresenter(UserShoppinglistContract.Presenter presenter) {
        super.setPresenter(presenter);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_users_shoppinglist;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_users_shoppinglist,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.tb_back_import:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.tbBackImport);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }
}
