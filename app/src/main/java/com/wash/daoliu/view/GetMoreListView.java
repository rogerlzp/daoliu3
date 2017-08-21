package com.wash.daoliu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wash.daoliu.R;
import com.wash.daoliu.imageloader.ImageLoaderProxy;

/**
 * Created by rogerlzp on 15/12/10.
 */
public class GetMoreListView extends ListView {


    public interface OnGetMoreListner {
        public void onGetMore();
    }


    private static final String TAG = GetMoreListView.class.getSimpleName();

    private LayoutInflater inflater;

    // 加载更多试图
    private View footView;

    // 加载更多文字
    private TextView tvFootTitle;

    // 加载更多进度框
    private ProgressBar pbFootRefreshing;

    // 是否已经添加了footer
    private boolean addFooterFlag;

    // 是否还有数据标志, 刚开始第一次时候总是试图去加载,因此default 为true
    private boolean hasMoreDataFlag = true;

    // scroll 时,到达最后一个item的次数, 只有第一次能触发自动刷新
    private int reachLastPositionCount = 0;

    private OnGetMoreListner getMoreListner;

    // 正在获取中
    private boolean isGettingMore = false;

    public GetMoreListView(Context context) {
        this(context, null);
    }

    public GetMoreListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GetMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    public void init(Context context, AttributeSet attrs) {

        inflater = LayoutInflater.from(context);

        // 底部
        footView = inflater.inflate(R.layout.get_more_list_view_foot, this, false);
        tvFootTitle = (TextView) footView.findViewById(R.id.tv_foot_title);
        pbFootRefreshing = (ProgressBar) footView.findViewById(R.id.pb_foot_refreshing);

        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                doOnScrollStateChanged(view, scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                doOnScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
    }

    // 加载更多
    private void getMore(){
        if(getMoreListner != null){
            isGettingMore = true;
            pbFootRefreshing.setVisibility(View.VISIBLE);
            tvFootTitle.setText("正在加载...");
            getMoreListner.onGetMore();
        }
    }

    // TODO: 优化
    private boolean checkCanAutoGetMore(){
        if(footView == null){
            return false;
        }
        if(getMoreListner == null){
            return false;
        }
        if(isGettingMore){
            return false;
        }
        if (!hasMoreDataFlag){
            return false;
        }
        if(getAdapter() == null){
            return false;
        }
        if(!canScroll(1) && !canScroll(-1)) {
            return false;
        }
        if(getLastVisiblePosition()!= getAdapter().getCount()-1){
            return false;
        }
        if(reachLastPositionCount != 1){
            return false;
        }
        return true;
    }

private  boolean canScroll(int direction){
    final int childCount = getChildCount();
    if(childCount == 0){
        return false;
    }
    final int firstPostion = getFirstVisiblePosition();
    final int listPaddingTop = getListPaddingTop();
    final int listPaddingBottom = getPaddingBottom();
    final int itemCount = getAdapter().getCount();
    if(direction > 0 ){
        final int lastBottom = getChildAt(childCount-1).getBottom();
        final int lastPostion = firstPostion + childCount;
        return  lastPostion < itemCount || lastBottom> getHeight()-listPaddingBottom;
    } else {
        final int firstTop = getChildAt(0).getTop();
        return firstPostion> 0 || firstTop < listPaddingTop;
    }
}

    public void setOnGerMoreListener(OnGetMoreListner getMoreListner){
        this.getMoreListner = getMoreListner;
        if (!addFooterFlag) {
            addFooterFlag = true;
            this.addFooterView(footView);
        }
    }

    public void getMoreComplete(){
        isGettingMore = false;
        pbFootRefreshing.setVisibility(View.GONE);
        tvFootTitle.setText("加载更多");
    }

    public void setNoMore(){
        hasMoreDataFlag = false;
        if(footView!=null){
            footView.setVisibility(View.GONE);
        }
    }


    public void setHasMore(){
        hasMoreDataFlag = true;
        if(footView != null){
            footView.setVisibility(View.VISIBLE);
        }
    }

    public void doOnScrollStateChanged(AbsListView view, int scrollState){
        switch (scrollState){
            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                break;
            case OnScrollListener.SCROLL_STATE_FLING:
                //滑动时候停止加载页面
                ImageLoaderProxy.getInstance().pause(getContext());
                break;
            case OnScrollListener.SCROLL_STATE_IDLE:
                ImageLoaderProxy.getInstance().resume(getContext());
                break;
            default:
                break;
        }
    }

    public void doOnScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        if (getAdapter() == null){
            return;
        }
        if(getLastVisiblePosition() == getAdapter().getCount() -1 ){
            reachLastPositionCount++;
        } else {
            reachLastPositionCount =0;
        }
        if(checkCanAutoGetMore()){
            getMore();
        }
    }

}

