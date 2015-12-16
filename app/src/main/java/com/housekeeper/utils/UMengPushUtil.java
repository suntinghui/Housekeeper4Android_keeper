package com.housekeeper.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

import com.housekeeper.client.Constants;
import com.umeng.message.PushAgent;

public class UMengPushUtil {

    public class AddAliasTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private String alias;

        public AddAliasTask(Context context, String aliasString) {
            this.alias = aliasString + "_" + Constants.ROLE;
            this.context = context;
        }

        protected Boolean doInBackground(Void... params) {
            try {
                PushAgent.getInstance(this.context).enable();

                String oldId = ActivityUtil.getSharedPreferences().getString(Constants.UMengPUSHId, null);

                // TODO
                if (!PushAgent.getInstance(this.context).isRegistered()) {
                    Log.e("PUSH", "*******************************************推送还未注册");
                    return false;
                }

                if (oldId != null) {
                    PushAgent.getInstance(this.context).removeAlias(oldId, "house");
                }

                Editor editor = ActivityUtil.getSharedPreferences().edit();
                editor.putString(Constants.UMengPUSHId, alias);
                editor.commit();

                boolean flag = PushAgent.getInstance(this.context).addAlias(alias, "house");
                if (flag) {
                    Log.e("PUSH", "*******************************************推送ID添加成功");
                } else {
                    Log.e("PUSH", "*******************************************推送ID添加失败");
                }
                return flag;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

    }

    public class RemoveAliasTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;

        public RemoveAliasTask(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(Void... params) {
            try {

                String oldId = ActivityUtil.getSharedPreferences().getString(Constants.UMengPUSHId, null);
                if (oldId == null)
                    return false;

                if (!PushAgent.getInstance(this.context).isRegistered()) {
                    Log.e("PUSH", "－－－－－－－－－－－－－－－－－－－－－－－－－推送还未注册");
                    return false;
                }

                boolean flag = PushAgent.getInstance(this.context).removeAlias(oldId, "house");

                if (flag) {
                    Log.e("PUSH", "*******************************************推送ID删除成功");
                } else {
                    Log.e("PUSH", "*******************************************推送ID删除失败");
                }
                return flag;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

    }

}
