package com.housekeeper.activity.tenant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.housekeeper.activity.BaseActivity;
import com.housekeeper.client.Constants;
import com.wufriends.housekeeper.keeper.R;

/**
 * Created by sth on 11/27/15.
 */
public class TenantDownloadLandlordActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_tenant_download_landlord);

        this.initView();
    }

    private void initView() {
        ((TextView) this.findViewById(R.id.titleTextView)).setText("房东，您好");
        this.findViewById(R.id.backBtn).setOnClickListener(this);

        this.findViewById(R.id.contactusLayout).setOnClickListener(this);
        this.findViewById(R.id.downloadLayout).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                this.finish();
                break;

            case R.id.contactusLayout: {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Constants.PHONE_SERVICE));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            break;

            case R.id.downloadLayout: {

            }
            break;
        }
    }
}
