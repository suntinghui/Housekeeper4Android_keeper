package com.wufriends.housekeeper.keeper.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.housekeeper.client.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
		api.handleIntent(getIntent(), this);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		// TODO

		// Intent intent = new Intent(this, ShareActivity.class);

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			// 分享成功
			 Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
			// intent.putExtra("Result", BaseResp.ErrCode.ERR_OK);
			break;

		case BaseResp.ErrCode.ERR_USER_CANCEL:
			// 分享取消
			Toast.makeText(this, "分享取消", Toast.LENGTH_SHORT).show();
			// intent.putExtra("Result", BaseResp.ErrCode.ERR_USER_CANCEL);
			break;

		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			// 分享拒绝
			Toast.makeText(this, "分享拒绝", Toast.LENGTH_SHORT).show();
			// intent.putExtra("Result", BaseResp.ErrCode.ERR_AUTH_DENIED);
			break;
		}
		// this.startActivity(intent);

		this.finish();
	}

}
