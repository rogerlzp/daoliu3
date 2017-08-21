package com.wash.daoliu.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wash.daoliu.R;

//import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
//import com.baidu.autoupdatesdk.UICheckUpdateCallback;


public class UpdateManager {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 保存解析的XML信息 */
    HashMap<String, String> mHashMap;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private Activity mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private AlertDialog mDownloadDialog;
    private String downloadUrl;
    private TextView cancel, install,tv_progress,download_title;
    private View divide = null;
    private boolean isForce = false;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    tv_progress.setText(progress+"%");
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    mProgress.setProgress(100);
                    tv_progress.setText("100%");
                    download_title.setText(R.string.downloaded);
                    divide.setVisibility(View.VISIBLE);
                    install.setVisibility(View.VISIBLE);
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public UpdateManager(Activity context, String url, boolean isForce) {
        this.mContext = context;
        downloadUrl = url;
        this.isForce = isForce;
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate(String vs_explain, String downloadUrl) {
        // 显示提示对话框
        showNoticeDialog(vs_explain, downloadUrl);
    }


//	public void checkUpdate(UICheckUpdateCallback uiCheckUpdateCallback )
//	{
//		// 显示提示对话框
//		BDAutoUpdateSDK.uiUpdateAction(mContext, uiCheckUpdateCallback);
//	}

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog(String vs_explain, final String downloadUrl) {
        if (TextUtils.isEmpty(downloadUrl)) {
            return;
        }
        this.downloadUrl = downloadUrl;
        ViewUtils.showWarnDialogWithTitle(mContext, mContext.getString(R.string.update_title), vs_explain, mContext.getString(R.string.update_btn), mContext.getString(R.string.cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDownloadDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isForce) {
                    ActivityUtils.finishAll();
                }
            }
        }, !isForce);
        // 构造对话框
//        Builder builder = new Builder(mContext);
//        builder.setTitle("更新提醒");
//        builder.setMessage(vs_explain);
//        // 更新
//        builder.setPositiveButton("去更新", new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
////				dialog.dismiss();
//                // 显示下载对话框
////				showDownloadDialog();
////				Intent intent = new Intent(Intent.ACTION_VIEW);
////				intent.setData(Uri.parse(downloadUrl));
////				mContext.startActivity(intent);
////				if (isForce) {
////					ActivityUtils.finishAll();
////				}
//                if (!android.text.TextUtils.isEmpty(downloadUrl)) {
//                    downloadApk();
//                }
//            }
//        });
//
//        builder.setNegativeButton("取消", new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                if (isForce) {
//                    ActivityUtils.finishAll();
//                }
//            }
//        });
//        Dialog noticeDialog = builder.create();
//        noticeDialog.setCancelable(!isForce);
//        noticeDialog.show();
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        // 构造软件下载对话框
        mDownloadDialog = new AlertDialog.Builder(mContext).create();
        mDownloadDialog.setCancelable(false);
        mDownloadDialog.show();
        Window window = mDownloadDialog.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.softupdate_progress);
        // 给下载对话框增加进度条
        mProgress = (ProgressBar) window.findViewById(R.id.update_progress);
        divide = window.findViewById(R.id.divide);
        tv_progress = (TextView) window.findViewById(R.id.tv_progress);
        install = (TextView) window.findViewById(R.id.install);
        download_title = (TextView) window.findViewById(R.id.download_title);
        install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installApk();
            }
        });
        cancel = (TextView) window.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cancelUpdate = true;
                if (isForce) {
                    ActivityUtils.finishAll();
                } else {
                    mDownloadDialog.cancel();
                    // 设置取消状态
                }
            }
        });
        // 现在文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
//                if (FileHelper.ExistSDCard()) {
                // 获得存储卡的路径
//                    String sdpath = Environment.getExternalStorageDirectory() + "/";
//                    mSavePath = sdpath + "LTN";
                mSavePath = FileHelper.getFileDirectory(mContext, "update").getPath();
                URL url = new URL(downloadUrl);
                // 创建连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                // 获取文件大小
                int length = conn.getContentLength();
                // 创建输入流
                InputStream is = conn.getInputStream();

                File file = new File(mSavePath);
                // 判断文件目录是否存在
                if (!file.exists()) {
                    file.mkdir();
                }
                File apkFile = new File(mSavePath, "lingtouniao.apk");
                if (apkFile.exists()) {
                    apkFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(apkFile);
                int count = 0;
                // 缓存
                byte buf[] = new byte[1024];
                // 写入到文件中
                do {
                    int numread = is.read(buf);
                    count += numread;
                    // 计算进度条位置
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWNLOAD);
                    if (numread <= 0) {
                        // 下载完成
                        if (!cancelUpdate) {
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                        }
                        break;
                    }
                    // 写入文件
                    fos.write(buf, 0, numread);
                } while (!cancelUpdate);// 点击取消就停止下载.
                fos.close();
                is.close();
//                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            //			mDownloadDiaLog.dismiss();
        }
    }

    ;

    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkFile = new File(mSavePath, "lingtouniao.apk");
        if (!apkFile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkFile.getPath()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }


}
