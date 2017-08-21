package com.wash.daoliu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.activities.CurrentAccountActivity;
import com.wash.daoliu.activities.ProductDetailActivity;
import com.wash.daoliu.activities.ProductTYBDetailActivity;
import com.wash.daoliu.application.LTNApplication;
import com.wash.daoliu.model.Product;
import com.wash.daoliu.utility.Constant;
import com.wash.daoliu.utility.LTNConstants;
import com.wash.daoliu.utility.TextUtils;
import com.wash.daoliu.utility.Utils;
import com.wash.daoliu.view.ProductTagView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by rogerlzp on 15/12/16.
 */
public class ProductAdapter extends BaseAdapter {

    private static final String TAG = ProductAdapter.class.getSimpleName();
    public ArrayList<Product> mProducts = new ArrayList<Product>();
    public Context ctx;

//    public ProductDetailListener productDetailListener;

    // 当前时间
    SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ProductAdapter(Context context) {
        this.ctx = context;
        // 当前时间
    }

    public void setProducts(ArrayList<Product> _mProducts) {
        mProducts = _mProducts;
        notifyDataSetChanged();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = (ViewGroup) LayoutInflater.from(ctx).inflate(
                    R.layout.product_item, null);
            holder = new ViewHolder();
            holder.remaing_layout = (LinearLayout) convertView.findViewById(R.id.remaing_layout);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tag_view = (ProductTagView) convertView.findViewById(R.id.tag_view);
            holder.tag_view_1 = (ProductTagView) convertView.findViewById(R.id.tag_view_1);
            holder.img_show = (ImageView) convertView.findViewById(R.id.img_show);
            holder.tv_amountIncome = (TextView) convertView.findViewById(R.id.tv_amountIncome);
            holder.tv_deadline = (TextView) convertView.findViewById(R.id.tv_deadline);
            holder.tv_remaingAmount_value = (TextView) convertView.findViewById(R.id.tv_remaingAmount_value);
            holder.buyType = (TextView) convertView.findViewById(R.id.buyType);
            holder.buy = (TextView) convertView.findViewById(R.id.buy);
            // holder.tv_amountIncome_percent = (TextView) convertView.findViewById(R.id.tv_amountIncome_percent);
            holder.tv_amountIncome2 = (TextView) convertView.findViewById(R.id.tv_amountIncome2);
            holder.tv_deadlin_day = (TextView) convertView.findViewById(R.id.tv_deadlin_day);
            holder.tv_investDate2 = (TextView) convertView.findViewById(R.id.tv_investDate2);
            holder.tv_remaingAmount = (TextView) convertView.findViewById(R.id.tv_remaingAmount);
            holder.divide_tag = convertView.findViewById(R.id.divide_tag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product productData = mProducts.get(position);
        ProductDetailListener productDetailListener = new ProductDetailListener(ctx, productData);
        convertView.setOnClickListener(productDetailListener);
        holder.buy.setOnClickListener(productDetailListener);
        holder.tv_name.setText(productData.getProductName());
        String annualIncomText = productData.getAnnualIncomeText();
        holder.tv_deadlin_day.setText(productData.getDeadlineUnit());
        holder.tv_amountIncome.setText(getNormalTextStyle(annualIncomText), TextView.BufferType.SPANNABLE); //不同的内容设置不同的大小
        //  holder.tv_amountIncome.setText("" + productData.getAnnualIncomeText());
        holder.tv_deadline.setText("" + productData.getProductDeadline());
        holder.tv_remaingAmount_value.setText(TextUtils.formatDoubleValueWithUnit(productData.getProductRemainAmount()));
        holder.divide_tag.setVisibility(android.text.TextUtils.isEmpty(productData.getProductTag()) ? View.INVISIBLE : View.VISIBLE);
        holder.tag_view_1.setVisibility(View.GONE);
        if (productData.getProductType().equals(LTNConstants.PRODUCT.TYPE_TYB)) { //体验标
            holder.buyType.setText(LTNConstants.PRO_BUY_TYPE_TYB);
            holder.buy.setText(LTNConstants.PRO_BTN_TY);
            holder.img_show.setImageResource(R.drawable.icon_label_ty);
            holder.img_show.setVisibility(View.VISIBLE);
            holder.tv_name.setTextColor(ContextCompat.getColor(ctx, R.color.text_color1));
            setNormal(holder);
            holder.remaing_layout.setVisibility(View.INVISIBLE);

            holder.tag_view.setListTag(productData.getProductTag(), productData.getProductType());

        } else if (productData.getProductType().equals(LTNConstants.PRODUCT.TYPE_SXT)) {
            // 活期标,跳转到随心投

            holder.tv_name.setTextColor(ContextCompat.getColor(ctx, R.color.text_color1));
            holder.buyType.setText(productData.getStaInvestAmount() + LTNConstants.PRO_BUY_TYPE_SB);
            //TODO: 根据产品类型来区分
            holder.buy.setText(LTNConstants.PRO_BTN_BUY);
            setNormal(holder);
            holder.tag_view.setListTag(productData.getProductTag(), productData.getProductType());
            holder.img_show.setImageResource(R.drawable.ic_suixintou_tag);
            holder.img_show.setVisibility(View.VISIBLE);
        } else if (productData.getProductType().equals(LTNConstants.PRODUCT.TYPE_XSB)) { //TODO: 优化新手标逻辑,待确定后
            // 新手标, 添加
            holder.tag_view_1.setVisibility(View.VISIBLE);
            holder.remaing_layout.setVisibility(View.VISIBLE);
            holder.tag_view_1.setTag1(ctx.getString(R.string.product_tag1),ctx.getString(R.string.product_tag1));
            if (productData.getProductStatus().equals(LTNConstants.PRODUCT.STATUS_JS) || //结标
                    productData.getProductStatus().equals(LTNConstants.PRODUCT.STATUS_HKZ) || //还款中
                    productData.getProductStatus().equals(LTNConstants.PRODUCT.STATUS_YHK)    // 已还款
                    ) {//满标
                holder.img_show.setImageResource(R.drawable.watermark_over);
                holder.img_show.setVisibility(View.VISIBLE);
                holder.buyType.setText(productData.getStaInvestAmount() + LTNConstants.PRO_BUY_TYPE_SB);
                //TODO: 根据产品类型来区分
                holder.buy.setText(LTNConstants.PRO_STOP_BTN_BUY);
                setGray1(holder);


                holder.tag_view.setListDarkTag(productData.getProductTag(), productData.getProductType());
                holder.tv_amountIncome.setText(getGrayTextStyle(annualIncomText), TextView.BufferType.SPANNABLE); //不同的内容设置不同的大小
            } else { // 散标
                // 名字
                holder.tv_name.setTextColor(ContextCompat.getColor(ctx, R.color.text_color1));
                holder.buyType.setText(productData.getStaInvestAmount() + LTNConstants.PRO_BUY_TYPE_SB);
                //TODO: 根据产品类型来区分
                holder.buy.setText(LTNConstants.PRO_BTN_BUY);
                setNormal(holder);
                holder.tag_view.setListTag(productData.getProductTag(), productData.getProductType());
                holder.img_show.setVisibility(View.GONE);
            }
        } else {
            holder.remaing_layout.setVisibility(View.VISIBLE);
            if (productData.getProductStatus().equals(LTNConstants.PRODUCT.STATUS_JS) || //结标
                    productData.getProductStatus().equals(LTNConstants.PRODUCT.STATUS_HKZ) || //还款中
                    productData.getProductStatus().equals(LTNConstants.PRODUCT.STATUS_YHK)    // 已还款
                    ) {//满标
                holder.img_show.setImageResource(R.drawable.watermark_over);
                holder.img_show.setVisibility(View.VISIBLE);
                holder.buyType.setText(productData.getStaInvestAmount() + LTNConstants.PRO_BUY_TYPE_SB);
                //TODO: 根据产品类型来区分
                holder.buy.setText(LTNConstants.PRO_STOP_BTN_BUY);
                setGray1(holder);


                holder.tag_view.setListDarkTag(productData.getProductTag(), productData.getProductType());
                holder.tv_amountIncome.setText(getGrayTextStyle(annualIncomText), TextView.BufferType.SPANNABLE); //不同的内容设置不同的大小
            } else { // 散标
                // 名字
                holder.tv_name.setTextColor(ContextCompat.getColor(ctx, R.color.text_color1));
                holder.buyType.setText(productData.getStaInvestAmount() + LTNConstants.PRO_BUY_TYPE_SB);
                //TODO: 根据产品类型来区分
                holder.buy.setText(LTNConstants.PRO_BTN_BUY);
                setNormal(holder);
                holder.tag_view.setListTag(productData.getProductTag(), productData.getProductType());
                holder.img_show.setVisibility(View.GONE);
            }

        }

        // 首先判断是否是体验标


        return convertView;
    }

    private SpannableString getNormalTextStyle(String annualIncomText) {

        SpannableString styledText = new SpannableString(annualIncomText);
        String[] textArray = annualIncomText.split("-");
        if (textArray.length > 1) { // 采用SpannableString格式
            //    textArray[0]
            styledText.setSpan(new TextAppearanceSpan(ctx, R.style.text_style3), 0, textArray[0].length() - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(ctx, R.style.text_style4), textArray[0].length() - 1, textArray[0].length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(ctx, R.style.text_style3), textArray[0].length(), textArray[0].length() + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(ctx, R.style.text_style3), textArray[0].length() + 1, annualIncomText.length() - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(ctx, R.style.text_style4), annualIncomText.length() - 1, annualIncomText.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            // 判断最后是否有%,没有的话,加上一个
            if (!annualIncomText.endsWith("%")) {
                annualIncomText = annualIncomText + "%";
                styledText = new SpannableString(annualIncomText);
            }
            styledText.setSpan(new TextAppearanceSpan(ctx, R.style.text_style3), 0, annualIncomText.length() - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            styledText.setSpan(new TextAppearanceSpan(ctx, R.style.text_style4), annualIncomText.length() - 1, annualIncomText.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return styledText;
    }

    private SpannableString getGrayTextStyle(String annualIncomText) {

        SpannableString styledText = new SpannableString(annualIncomText);
        String[] textArray = annualIncomText.split("-");
        if (textArray.length > 1) { // 采用SpannableString格式
            //    textArray[0]
            styledText.setSpan(new TextAppearanceSpan(ctx, R.style.text_style5), 0, textArray[0].length() - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(ctx, R.style.text_style6), textArray[0].length() - 1, textArray[0].length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(ctx, R.style.text_style5), textArray[0].length(), textArray[0].length() + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(ctx, R.style.text_style5), textArray[0].length() + 1, annualIncomText.length() - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(ctx, R.style.text_style6), annualIncomText.length() - 1, annualIncomText.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            // 判断最后是否有%,没有的话,加上一个
            if (!annualIncomText.endsWith("%")) {
                annualIncomText = annualIncomText + "%";
                styledText = new SpannableString(annualIncomText);
            }
            styledText.setSpan(new TextAppearanceSpan(ctx, R.style.text_style5), 0, annualIncomText.length() - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            styledText.setSpan(new TextAppearanceSpan(ctx, R.style.text_style6), annualIncomText.length() - 1, annualIncomText.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return styledText;
    }

    // 产品颜色设为灰色
    public void setGray1(ViewHolder holder) {
        holder.tv_name.setTextColor(ContextCompat.getColor(ctx, R.color.label_grey1));
//        holder.tv_amountIncome.setTextColor(ContextCompat.getColor(ctx, R.color.label_grey1));
        holder.tv_deadline.setTextColor(ContextCompat.getColor(ctx, R.color.label_grey1));
        holder.tv_remaingAmount_value.setTextColor(ContextCompat.getColor(ctx, R.color.label_grey1));
        holder.buyType.setTextColor(ContextCompat.getColor(ctx, R.color.label_grey1));
        holder.buyType.setBackgroundColor(ContextCompat.getColor(ctx, R.color.white));
        holder.buy.setTextColor(ContextCompat.getColor(ctx, R.color.label_grey1));
        holder.buy.setBackgroundResource(R.color.white);

        holder.tv_amountIncome2.setTextColor(ContextCompat.getColor(ctx, R.color.label_grey1));
        holder.tv_deadlin_day.setTextColor(ContextCompat.getColor(ctx, R.color.label_grey1));
        holder.tv_investDate2.setTextColor(ContextCompat.getColor(ctx, R.color.label_grey1));
        holder.tv_remaingAmount.setTextColor(ContextCompat.getColor(ctx, R.color.label_grey1));
    }

    // 产品颜色设为正常
    public void setNormal(ViewHolder holder) {
        // 名字
        holder.tv_name.setTextColor(ContextCompat.getColor(ctx, R.color.text_color1));

        //
        holder.buyType.setTextColor(ContextCompat.getColor(ctx, R.color.text_color1));
        holder.buyType.setBackgroundColor(ContextCompat.getColor(ctx, R.color.white));
        holder.buy.setTextColor(ContextCompat.getColor(ctx, R.color.white));

        // 年化收益率
        holder.tv_amountIncome.setTextColor(ContextCompat.getColor(ctx, R.color.main_text_s_color));
        //  holder.tv_amountIncome_percent.setTextColor(ContextCompat.getColor(ctx,R.color.main_text_s_color));
        holder.tv_amountIncome2.setTextColor(ContextCompat.getColor(ctx, R.color.text_color3));

        // 投资期限
        holder.tv_investDate2.setTextColor(ContextCompat.getColor(ctx, R.color.text_color3));

        // 剩余金额
        holder.tv_remaingAmount_value.setTextColor(ContextCompat.getColor(ctx, R.color.text_color3));
        holder.tv_remaingAmount.setTextColor(ContextCompat.getColor(ctx, R.color.text_color3));

        // 天
        holder.tv_deadline.setTextColor(ContextCompat.getColor(ctx, R.color.text_color1));
        holder.tv_deadlin_day.setTextColor(ContextCompat.getColor(ctx, R.color.text_color1));

        holder.buy.setBackgroundResource(R.drawable.button_radius);


    }


    private class ViewHolder {
        TextView tv_name;
        ProductTagView tag_view;
        ProductTagView tag_view_1;
        ImageView img_show;
        TextView tv_amountIncome;
        TextView tv_deadline;
        TextView tv_remaingAmount_value;
        TextView buyType;
        LinearLayout remaing_layout;

        //  TextView tv_amountIncome_percent;
        TextView tv_amountIncome2;
        TextView tv_deadlin_day;
        TextView tv_investDate2;
        TextView tv_remaingAmount;
        View divide_tag;
        TextView buy;
        ViewGroup progress;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mProducts == null ? 0 : mProducts.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    //
    class ProductDetailListener implements View.OnClickListener {
        Product product;
        public Context ctx;

        public ProductDetailListener(Context _ctx, Product product) {
            ctx = _ctx;
            this.product = product;
        }


        /**
         * 体验标跳转到体验标
         * 散标跳转到散标
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (product.getProductType().equals(LTNConstants.PRODUCT.TYPE_TYB)) {
                Intent intent = new Intent(ctx, ProductTYBDetailActivity.class);
                Bundle bundle = new Bundle();
                // 通过字段来传值,不通过product对象来传递
                bundle.putString(LTNConstants.PRO_Id, product.getProductId()); // 产品ID

                bundle.putFloat(LTNConstants.PRO_PrAmount, product.getProductRemainAmount()); // 产品余额
                bundle.putInt(LTNConstants.PRO_Deadline, product.getProductDeadline());      // 截止时间
                bundle.putString(LTNConstants.PRO_RepaymentType, product.getRepaymentType());  // 还款方式
                bundle.putString(LTNConstants.PRO_StaRateDate, product.getStaRateDate());   //起息日期
                bundle.putFloat(LTNConstants.PRO_AnnualIncome, product.getAnnualIncome());  //产品收益率
                bundle.putString(LTNConstants.PRO_Tags, product.getProductTag());   //产品标签
                bundle.putFloat(LTNConstants.PRO_PtAmount, product.getProductTotalAmount()); // 产品总额
                bundle.putString(LTNConstants.PRO_Type, product.getProductType());  // 产品类型

                bundle.putInt(LTNConstants.PRO_StaInvestAmount, product.getStaInvestAmount()); // 起投金额

                bundle.putString(LTNConstants.PRO_RateCalculateType, product.getRateCalculateType()); // 计息方式
                bundle.putString(LTNConstants.PRO_AnnualIncomeText, product.getAnnualIncomeText()); // 产品收益率
                bundle.putInt(LTNConstants.PRO_StaInvestAmount, product.getStaInvestAmount()); // 起投金额
                bundle.putString(LTNConstants.PRO_Name, product.getProductName()); // 产品名称
                bundle.putString(LTNConstants.PRO_Title, product.getProductTitle()); // 产品名称
                bundle.putString(LTNConstants.PRO_DEADLINEUNIT, product.getDeadlineUnit()); // 投资期限单位

                // 产品status
                bundle.putString(LTNConstants.PRO_Status, product.getProductStatus()); // 产品状态

                intent.putExtras(bundle);
                ctx.startActivity(intent);
            } else if (product.getProductType().equals(LTNConstants.PRODUCT.TYPE_SXT)) { // 跳转到随心投
                if(android.text.TextUtils.isEmpty(LTNApplication.getInstance().getSessionKey())){
                    Bundle b = new Bundle();
                    b.putString(Constant.LoginParams.LOGIN_TYPE, Constant.LoginParams.FROM_PRODUCT_LIST_SXT);
                    Utils.loginJump(ctx,b);
                }else{
                Intent intent = new Intent(ctx, CurrentAccountActivity.class);
                ctx.startActivity(intent);
                }
            } else {
                Intent intent = new Intent(ctx, ProductDetailActivity.class);
                Bundle bundle = new Bundle();

                bundle.putFloat(LTNConstants.PRO_AnnualIncome, product.getAnnualIncome());
                bundle.putString(LTNConstants.PRO_Id, product.getProductId()); // 产品ID
                bundle.putFloat(LTNConstants.PRO_PrAmount, product.getProductRemainAmount()); // 产品余额
                bundle.putInt(LTNConstants.PRO_Deadline, product.getProductDeadline());      // 截止时间
                bundle.putString(LTNConstants.PRO_RepaymentType, product.getRepaymentType());  // 还款方式
                bundle.putString(LTNConstants.PRO_StaRateDate, product.getStaRateDate());   //起息日期
                bundle.putString(LTNConstants.PRO_Tags, product.getProductTag());   //产品标签

                bundle.putFloat(LTNConstants.PRO_PtAmount, product.getProductTotalAmount()); // 产品总额
                bundle.putString(LTNConstants.PRO_DetailUrl, product.getDetailUrl()); // 产品详情
                bundle.putString(LTNConstants.PRO_RaiseEndDate, product.getRaiseEndDate()); // 产品截止时间
                bundle.putString(LTNConstants.PRO_Name, product.getProductName());  // 产品名称
                bundle.putString(LTNConstants.PRO_Type, product.getProductType());  // 产品类型
                bundle.putString(LTNConstants.PRO_RateCalculateType, product.getRateCalculateType()); // 计息方式
                bundle.putString(LTNConstants.PRO_Name, product.getProductName()); // 产品名称
                bundle.putString(LTNConstants.PRO_DEADLINEUNIT, product.getDeadlineUnit()); // 投资期限单位

// 产品title

                bundle.putString(LTNConstants.PRO_Title, product.getProductTitle()); // 产品名称
                bundle.putString(LTNConstants.PRO_AnnualIncomeText, product.getAnnualIncomeText()); // 产品收益率
                bundle.putInt(LTNConstants.PRO_StaInvestAmount, product.getStaInvestAmount()); // 起投金额

                bundle.putString(LTNConstants.PRO_Status, product.getProductStatus()); // 产品状态

                intent.putExtras(bundle);
                ctx.startActivity(intent);
            }
        }
    }

}

