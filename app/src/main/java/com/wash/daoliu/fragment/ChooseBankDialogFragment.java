package com.wash.daoliu.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wash.daoliu.R;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.BankItem;
import com.wash.daoliu.net.LTNHttpClient;
import com.wash.daoliu.utility.LTNConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rogerlzp on 15/11/23.
 */
public class ChooseBankDialogFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    //    BANK_ICBC("ICBC", "中国工商银行"),
//    BANK_ABC("ABC", "中国农业银行"),
//    BANK_CCB("CCB", "中国建设银行"),
//    BANK_BOC("BOC", "中国银行"),
//    BANK_CEB("CEB", "光大银行"),
//    BANK_CIB("CIB", "兴业银行"),
//    BANK_CMBC("CMBC", "中国民生银行"),
//    BANK_HXB("HXB", "华夏银行"),
//    BANK_PSBC("PSBC", "邮储银行"),
//    BANK_SPDB("SPDB", "浦东发展银行"),
//    BANK_COMM("COMM", "交通银行"),
//    BANK_GDB("GDB", "广发银行"),
//    BANK_CITIC("CITIC", "中信银行"),
//    BANK_CMB("CMB", "招商银行"),
//    BANK_SPAB("SPAB", "平安银行");
    private OnChooseBank onChooseBank = null;
    //    private String[] bankCodes = new String[]{"ICBC-中国工商银行", "ABC-中国农业银行", "CCB-中国建设银行", "BOC-中国银行", "CEB-光大银行", "CIB-兴业银行", "CMBC-中国民生银行", "HXB-华夏银行",
//            "PSBC-邮储银行", "SPDB-浦东发展银行", "COMM-交通银行", "GDB-广发银行", "CITIC-中信银行", "CMB-招商银行", "SPAB-平安银行"};
    private ListView listView = null;
    private ArrayList<BankItem> bankItems = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnChooseBank) {
            onChooseBank = (OnChooseBank) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(LoginDialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_bank_dialog, null);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBankList();
    }

    private void getBankList() {
        final String bankJson = LTNApplication.getInstance().getBankList();
        if (!TextUtils.isEmpty(bankJson)) {
            Gson gson = new Gson();
            bankItems = gson.fromJson(bankJson, new TypeToken<ArrayList<BankItem>>() {
            }.getType());
            listView.setAdapter(new BankAdapter(bankItems));
        }
        RequestParams mReqParams = new RequestParams();
        mReqParams.add(LTNConstants.CLIENT_TYPE_PARAM, LTNConstants.CLIENT_TYPE_MOBILE);
        //TODO: 添加错误或者失败的跳转
        LTNHttpClient.getLTNHttpClient().get(LTNConstants.ACCESS_URL.BANK_LIST, mReqParams,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        try {
                            String resultCode = jsonObject.getString(LTNConstants.RESULT_CODE);

                            if (resultCode.equals(LTNConstants.MSG_SUCCESS)) {
                                // 将用户信息存储到
                                Gson gson = new Gson();
                                JSONObject accountInfoObj = (JSONObject) jsonObject.get(LTNConstants.DATA);
                                String bankListJson = accountInfoObj.optJSONArray(LTNConstants.LIST).toString();
                                if (!bankListJson.equals(bankJson)) {
                                    bankItems = gson.fromJson(accountInfoObj.optJSONArray(LTNConstants.LIST).toString(), new TypeToken<ArrayList<BankItem>>() {
                                    }.getType());
                                    listView.setAdapter(new BankAdapter(bankItems));
                                    LTNApplication.getInstance().setBankList(bankListJson);
                                }
                            }
                        } catch (JSONException je) {
                        }
                    }
                });
    }

    /**
     * 初始化view
     */
    public void initView(View view) {
        listView = (ListView) view.findViewById(R.id.bank_list);
//        listView.setAdapter(new BankAdapter());
        listView.setOnItemClickListener(this);
        view.findViewById(R.id.bg).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bg:
                dismissAllowingStateLoss();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onChooseBank != null) {
            onChooseBank.chooseBank(bankItems.get(position));
        }
        dismissAllowingStateLoss();
    }

    public class BankAdapter extends BaseAdapter {
        ArrayList<BankItem> bankItems;

        public BankAdapter(ArrayList<BankItem> bankItems) {
            this.bankItems = bankItems;
        }

        @Override
        public int getCount() {
            return bankItems == null ? 0 : bankItems.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) View.inflate(getActivity(), R.layout.bank_list_item, null);
            view.setText(bankItems.get(position).bankName);
            return view;
        }
    }

    public interface OnChooseBank {
        void chooseBank(BankItem bankItem);
    }


}

