package com.uc.webview.export;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.os.Build$VERSION;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View$OnKeyListener;
import android.view.View$OnLongClickListener;
import android.view.View$OnTouchListener;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.webkit.ValueCallback;
import android.widget.FrameLayout$LayoutParams;
import android.widget.FrameLayout;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.extension.CommonExtension;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.extension.UCExtension;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.b;
import com.uc.webview.export.internal.interfaces.IWebView;
import com.uc.webview.export.internal.interfaces.IWebViewOverride;
import com.uc.webview.export.internal.interfaces.InvokeObject;
import com.uc.webview.export.internal.utility.ReflectionUtil;
import java.lang.reflect.Field;
import java.util.Map;

@Api public class WebView extends FrameLayout implements IWebViewOverride {
    public static final int CORE_TYPE_ANDROID = 2;
    public static final int CORE_TYPE_U3 = 1;
    public static final int CORE_TYPE_U4 = 3;
    public static final int CREATE_FLAG_FORCE_USING_SYSTEM = 2;
    public static final int CREATE_FLAG_QUICK = 1;
    public static final int DEFAULT_CORE_TYPE = 1;
    public static final String SCHEME_GEO = "geo:0,0?q=";
    public static final String SCHEME_MAILTO = "mailto:";
    public static final String SCHEME_TEL = "tel:";
    private WebSettings a;
    private b b;
    private CommonExtension c;
    private UCExtension d;
    private boolean e;
    private WebView$a f;
    private int g;
    private WebView$WebViewCountting h;
    private boolean i;
    protected IWebView mWebView;
    protected static int[] sInstanceCount;

    static {
        WebView.sInstanceCount = new int[]{0, 0, 0, 0};
    }

    public WebView(Context arg7) {
        this(arg7, null, 16842885, false, false);
    }

    private WebView(Context arg10, AttributeSet arg11, int arg12, boolean arg13, boolean arg14) {
        int v0_1;
        int v8 = 10061;
        int v7 = 3;
        super(arg10, arg11, arg12);
        this.a = null;
        this.b = null;
        this.e = true;
        this.h = new WebView$WebViewCountting();
        this.i = false;
        if(arg10 == null) {
            try {
                throw new IllegalArgumentException("Invalid context argument");
            }
            catch(Throwable v0) {
                goto label_20;
            }
        }

        if(arg14) {
            v0_1 = 10061;
            try {
                SDKFactory.invoke(v0_1, new Object[]{Boolean.valueOf(true)});
                SDKFactory.invoke(10062, new Object[]{Boolean.valueOf(true)});
                SDKFactory.invoke(10063, new Object[]{Boolean.valueOf(true)});
            label_61:
                this.f = new WebView$a(arg10);
                int[] v1 = new int[1];
                this.mWebView = SDKFactory.invoke(10012, new Object[]{this.f, arg11, this, Boolean.valueOf(arg13), Boolean.valueOf(arg14), v1});
                this.mWebView.setOverrideObject(((IWebViewOverride)this));
                this.g = v1[0];
                ++WebView.sInstanceCount[this.g];
                this.b = SDKFactory.invoke(10014, new Object[]{Integer.valueOf(this.g), this.f.getApplicationContext()});
                this.a = this.mWebView.getSettingsInner();
                if(arg11 == null) {
                    this.addView(this.mWebView.getView(), new FrameLayout$LayoutParams(-1, -1));
                }
                else {
                    goto label_172;
                }

                goto label_120;
            }
            catch(Throwable v0) {
                goto label_20;
            }
        }

        goto label_61;
    label_172:
        v0_1 = 10040;
        try {
            if(SDKFactory.invoke(v0_1, new Object[0]).booleanValue()) {
                this.addView(this.mWebView.getView());
            }
            else {
                this.addView(this.mWebView.getView(), this.generateLayoutParams(arg11));
            }

        label_120:
            this.c = new CommonExtension(this.mWebView);
            this.d = SDKFactory.invoke(10013, new Object[]{arg10, this.mWebView, Integer.valueOf(this.g)});
            if(this.d != null) {
                this.d.setPrivateBrowsing(false);
            }

            if(!SDKFactory.g) {
                this.setWillNotDraw(false);
            }

            if(this.g == v7 && ((this.mWebView instanceof InvokeObject))) {
                this.mWebView.invoke(9, null);
            }
        }
        catch(Throwable v0) {
        label_20:
            if(arg14) {
                SDKFactory.invoke(v8, new Object[]{Boolean.valueOf(false)});
                SDKFactory.invoke(10062, new Object[]{Boolean.valueOf(false)});
                SDKFactory.invoke(10063, new Object[]{Boolean.valueOf(false)});
            }

            throw v0;
        }

        if(arg14) {
            SDKFactory.invoke(v8, new Object[]{Boolean.valueOf(false)});
            SDKFactory.invoke(10062, new Object[]{Boolean.valueOf(false)});
            SDKFactory.invoke(10063, new Object[]{Boolean.valueOf(false)});
        }
    }

    public WebView(Context arg7, int arg8) {
        boolean v5 = true;
        AttributeSet v2 = null;
        int v3 = 16842885;
        boolean v4 = (arg8 & 2) == 2 ? true : false;
        if((arg8 & 1) != 1) {
            v5 = false;
        }

        this(arg7, v2, v3, v4, v5);
    }

    public WebView(Context arg7, AttributeSet arg8) {
        this(arg7, arg8, 16842885, false, false);
    }

    public WebView(Context arg7, AttributeSet arg8, int arg9) {
        this(arg7, arg8, arg9, false, false);
    }

    @Deprecated public WebView(Context arg7, AttributeSet arg8, int arg9, boolean arg10) {
        this(arg7, arg8, arg9, false, false);
    }

    public WebView(Context arg7, AttributeSet arg8, boolean arg9) {
        this(arg7, arg8, 16842885, arg9, false);
    }

    public WebView(Context arg7, AttributeSet arg8, boolean arg9, int arg10) {
        this(arg7, arg8, arg10, arg9, false);
    }

    public WebView(Context arg7, boolean arg8) {
        this(arg7, null, 16842885, arg8, false);
    }

    private void a() {
        if(this.mWebView == null) {
            throw new IllegalStateException("WebView had destroyed,forbid it\'s interfaces to be called.");
        }
    }

    public void addJavascriptInterface(Object arg2, String arg3) {
        this.a();
        this.mWebView.addJavascriptInterface(arg2, arg3);
    }

    public static void asyncNew(Class arg2, Class[] arg3, Object[] arg4, ValueCallback arg5) {
        new Thread(new d(arg2, arg3, arg4, arg5)).start();
    }

    public boolean canGoBack() {
        this.a();
        return this.mWebView.canGoBack();
    }

    public boolean canGoBackOrForward(int arg2) {
        this.a();
        return this.mWebView.canGoBackOrForward(arg2);
    }

    public boolean canGoForward() {
        this.a();
        return this.mWebView.canGoForward();
    }

    @Deprecated public boolean canZoomIn() {
        boolean v0_1;
        this.a();
        try {
            v0_1 = this.mWebView.canZoomIn();
        }
        catch(NoSuchMethodError v0) {
            v0_1 = false;
        }

        return v0_1;
    }

    @Deprecated public boolean canZoomOut() {
        boolean v0_1;
        this.a();
        try {
            v0_1 = this.mWebView.canZoomOut();
        }
        catch(NoSuchMethodError v0) {
            v0_1 = false;
        }

        return v0_1;
    }

    @Deprecated public Picture capturePicture() {
        this.a();
        return this.mWebView.capturePicture();
    }

    public void clearCache(boolean arg2) {
        this.a();
        this.mWebView.clearCache(arg2);
    }

    public void clearFormData() {
        this.a();
        this.mWebView.clearFormData();
    }

    public void clearHistory() {
        this.a();
        this.mWebView.clearHistory();
    }

    public void clearMatches() {
        this.a();
        this.mWebView.clearMatches();
    }

    public void clearSslPreferences() {
        this.a();
        this.mWebView.clearSslPreferences();
    }

    public final void computeScroll() {
        super.computeScroll();
    }

    public WebBackForwardList copyBackForwardList() {
        this.a();
        return this.mWebView.copyBackForwardListInner();
    }

    public void coreComputeScroll() {
        if(this.mWebView != null) {
            this.mWebView.superComputeScroll();
        }
    }

    public void coreDestroy() {
        if(this.mWebView != null) {
            this.mWebView.superDestroy();
        }
    }

    public boolean coreDispatchTouchEvent(MotionEvent arg2) {
        boolean v0 = this.mWebView != null ? this.mWebView.superDispatchTouchEvent(arg2) : false;
        return v0;
    }

    public void coreDraw(Canvas arg2) {
        if(this.mWebView != null) {
            this.mWebView.superDraw(arg2);
        }
    }

    public void coreOnConfigurationChanged(Configuration arg2) {
        if(this.mWebView != null) {
            this.mWebView.superOnConfigurationChanged(arg2);
        }
    }

    public void coreOnInitializeAccessibilityEvent(AccessibilityEvent arg4) {
        if(this.mWebView != null) {
            arg4.setClassName(WebView.class.getName());
            this.mWebView.invoke(2, new Object[]{arg4});
        }
    }

    public void coreOnInitializeAccessibilityNodeInfo(AccessibilityNodeInfo arg4) {
        if(this.mWebView != null) {
            arg4.setClassName(WebView.class.getName());
            this.mWebView.invoke(1, new Object[]{arg4});
        }
    }

    public void coreOnScrollChanged(int arg2, int arg3, int arg4, int arg5) {
        if(this.mWebView != null) {
            this.mWebView.superOnScrollChanged(arg2, arg3, arg4, arg5);
        }
    }

    public void coreOnVisibilityChanged(View arg2, int arg3) {
        if(this.mWebView != null) {
            this.mWebView.superOnVisibilityChanged(arg2, arg3);
        }
    }

    public boolean coreOverScrollBy(int arg11, int arg12, int arg13, int arg14, int arg15, int arg16, int arg17, int arg18, boolean arg19) {
        boolean v0 = this.mWebView != null ? this.mWebView.superOverScrollBy(arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19) : false;
        return v0;
    }

    public boolean corePerformAccessibilityAction(int arg4, Bundle arg5) {
        boolean v0 = false;
        if(this.mWebView != null) {
            v0 = Boolean.parseBoolean(this.mWebView.invoke(3, new Object[]{Integer.valueOf(arg4), arg5}).toString());
        }

        return v0;
    }

    public void coreRequestLayout() {
        if(this.mWebView != null) {
            this.mWebView.superRequestLayout();
        }
    }

    public void coreSetVisibility(int arg2) {
        if(this.mWebView != null) {
            this.mWebView.superSetVisibility(arg2);
        }
    }

    public WebMessagePort[] createWebMessageChannel() {
        this.a();
        Object v0 = this.mWebView.createWebMessageChannelInner();
        if(!(v0 instanceof WebMessagePort[])) {
            WebMessagePort[] v0_1 = null;
        }

        return ((WebMessagePort[])v0);
    }

    public void destroy() {
        IWebView v4 = null;
        __monitor_enter(this);
        try {
            if(this.i) {
                throw new RuntimeException("destroy() already called.");
            }

            this.i = true;
            this.h = null;
            __monitor_exit(this);
            goto label_16;
        label_9:
            __monitor_exit(this);
        }
        catch(Throwable v0) {
            goto label_9;
        }

        throw v0;
    label_16:
        this.mWebView.destroy();
        this.mWebView = v4;
        this.a = ((WebSettings)v4);
        this.b = ((b)v4);
        this.c = ((CommonExtension)v4);
        this.d = ((UCExtension)v4);
        if(this.f != null) {
            WebView$a v0_1 = this.f;
            Context v1 = v0_1.getBaseContext();
            if(v1 != null) {
                try {
                    Field v2 = ContextWrapper.class.getDeclaredField("mBase");
                    if(v2 == null) {
                        goto label_36;
                    }

                    v2.setAccessible(true);
                    v2.set(v0_1, v1.getApplicationContext());
                }
                catch(IllegalAccessException v0_2) {
                }
                catch(NoSuchFieldException v0_3) {
                }
            }

        label_36:
            this.f = ((WebView$a)v4);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent arg2) {
        this.a();
        return this.mWebView.dispatchKeyEvent(arg2);
    }

    public final boolean dispatchTouchEvent(MotionEvent arg2) {
        return super.dispatchTouchEvent(arg2);
    }

    public void documentHasImages(Message arg2) {
        this.a();
        this.mWebView.documentHasImages(arg2);
    }

    public final void draw(Canvas arg1) {
        super.draw(arg1);
    }

    public static void enableSlowWholeDocumentDraw() {
        SDKFactory.invoke(10055, new Object[0]);
    }

    public void evaluateJavascript(String arg2, ValueCallback arg3) {
        this.a();
        this.mWebView.evaluateJavascript(arg2, arg3);
    }

    @Deprecated public int findAll(String arg2) {
        this.a();
        return this.mWebView.findAll(arg2);
    }

    public void findAllAsync(String arg2) {
        this.a();
        this.mWebView.findAllAsync(arg2);
    }

    public void findNext(boolean arg2) {
        this.a();
        this.mWebView.findNext(arg2);
    }

    public void flingScroll(int arg2, int arg3) {
        this.a();
        this.mWebView.flingScroll(arg2, arg3);
    }

    public CharSequence getAccessibilityClassName() {
        return WebView.class.getName();
    }

    public SslCertificate getCertificate() {
        this.a();
        return this.mWebView.getCertificate();
    }

    public CommonExtension getCommonExtension() {
        return this.c;
    }

    public int getContentHeight() {
        this.a();
        return this.mWebView.getContentHeight();
    }

    public static int getCoreType() {
        return SDKFactory.invoke(10020, new Object[0]).intValue();
    }

    public View getCoreView() {
        this.a();
        return this.mWebView.getView();
    }

    public int getCurrentViewCoreType() {
        return this.g;
    }

    public Bitmap getFavicon() {
        this.a();
        return this.mWebView.getFavicon();
    }

    public WebView$HitTestResult getHitTestResult() {
        this.a();
        WebView$HitTestResult v0 = this.mWebView.getHitTestResultInner() != null ? new WebView$HitTestResult(this, this.mWebView.getHitTestResultInner(), 0) : null;
        return v0;
    }

    public String[] getHttpAuthUsernamePassword(String arg2, String arg3) {
        this.a();
        return this.mWebView.getHttpAuthUsernamePassword(arg2, arg3);
    }

    public String getOriginalUrl() {
        this.a();
        return this.mWebView.getOriginalUrl();
    }

    public int getProgress() {
        this.a();
        return this.mWebView.getProgress();
    }

    @Deprecated public float getScale() {
        this.a();
        return this.mWebView.getScale();
    }

    public WebSettings getSettings() {
        this.a();
        return this.a;
    }

    public String getTitle() {
        this.a();
        return this.mWebView.getTitle();
    }

    public UCExtension getUCExtension() {
        return this.d;
    }

    public String getUrl() {
        this.a();
        return this.mWebView.getUrl();
    }

    public void goBack() {
        this.a();
        this.mWebView.goBack();
    }

    public void goBackOrForward(int arg2) {
        this.a();
        this.mWebView.goBackOrForward(arg2);
    }

    public void goForward() {
        this.a();
        this.mWebView.goForward();
    }

    public void invokeZoomPicker() {
        this.a();
        this.mWebView.invokeZoomPicker();
    }

    public boolean isDestroied() {
        boolean v0 = (this.i) || this.mWebView == null ? true : false;
        return v0;
    }

    public boolean isHorizontalScrollBarEnabled() {
        this.a();
        return this.mWebView.isHorizontalScrollBarEnabled();
    }

    public boolean isPrivateBrowsingEnabled() {
        Object v0 = this.mWebView.invoke(8, null);
        boolean v0_1 = v0 == null || !((Boolean)v0).booleanValue() ? false : true;
        return v0_1;
    }

    public boolean isVerticalScrollBarEnabled() {
        this.a();
        return this.mWebView.isVerticalScrollBarEnabled();
    }

    public void loadData(String arg2, String arg3, String arg4) {
        this.a();
        this.mWebView.loadData(arg2, arg3, arg4);
    }

    public void loadDataWithBaseURL(String arg7, String arg8, String arg9, String arg10, String arg11) {
        this.a();
        this.mWebView.loadDataWithBaseURL(arg7, arg8, arg9, arg10, arg11);
    }

    public void loadUrl(String arg2) {
        this.a();
        this.mWebView.loadUrl(arg2);
    }

    public void loadUrl(String arg2, Map arg3) {
        this.a();
        this.mWebView.loadUrl(arg2, arg3);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(this.mWebView != null && this.b != null) {
            b.a(this.mWebView);
        }
    }

    public final void onConfigurationChanged(Configuration arg1) {
        super.onConfigurationChanged(arg1);
    }

    public InputConnection onCreateInputConnection(EditorInfo arg2) {
        InputConnection v0 = this.mWebView != null ? this.mWebView.onCreateInputConnection(arg2) : null;
        return v0;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(this.mWebView != null && this.b != null) {
            this.b.b(this.mWebView);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent arg4) {
        boolean v0 = super.onInterceptTouchEvent(arg4);
        if(arg4 != null && (arg4.getSource() & 8194) == 8194) {
            v0 = false;
        }

        return v0;
    }

    public void onPause() {
        this.a();
        this.mWebView.onPause();
    }

    public void onResume() {
        this.a();
        this.mWebView.onResume();
    }

    public final void onScrollChanged(int arg1, int arg2, int arg3, int arg4) {
        super.onScrollChanged(arg1, arg2, arg3, arg4);
    }

    protected void onSizeChanged(int arg2, int arg3, int arg4, int arg5) {
        super.onSizeChanged(arg2, arg3, arg4, arg5);
        if(this.mWebView != null && this.b != null) {
            this.b.a(arg2, arg3);
        }
    }

    public final void onVisibilityChanged(View arg1, int arg2) {
        super.onVisibilityChanged(arg1, arg2);
    }

    protected void onWindowVisibilityChanged(int arg3) {
        super.onWindowVisibilityChanged(arg3);
        if(this.mWebView != null && this.b != null) {
            this.b.a(this.mWebView, arg3);
        }
    }

    public boolean overScrollBy(int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, boolean arg10) {
        return super.overScrollBy(arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
    }

    public boolean overlayHorizontalScrollbar() {
        this.a();
        return this.mWebView.overlayHorizontalScrollbar();
    }

    public boolean overlayVerticalScrollbar() {
        this.a();
        return this.mWebView.overlayVerticalScrollbar();
    }

    public boolean pageDown(boolean arg2) {
        this.a();
        return this.mWebView.pageDown(arg2);
    }

    public boolean pageUp(boolean arg2) {
        this.a();
        return this.mWebView.pageUp(arg2);
    }

    public void pauseTimers() {
        this.a();
        this.mWebView.pauseTimers();
    }

    public void postUrl(String arg2, byte[] arg3) {
        this.a();
        this.mWebView.postUrl(arg2, arg3);
    }

    public void postVisualStateCallback(long arg2, WebView$b arg4) {
        this.mWebView.postVisualStateCallback(arg2, arg4);
    }

    public void postWebMessage(WebMessage arg2, Uri arg3) {
        this.a();
        this.mWebView.postWebMessageInner(arg2, arg3);
    }

    public void reload() {
        this.a();
        this.mWebView.reload();
    }

    public void removeJavascriptInterface(String arg2) {
        this.a();
        try {
            this.mWebView.removeJavascriptInterface(arg2);
        }
        catch(Throwable v0) {
        }
    }

    public void requestFocusNodeHref(Message arg2) {
        this.a();
        this.mWebView.requestFocusNodeHref(arg2);
    }

    public void requestImageRef(Message arg2) {
        this.a();
        this.mWebView.requestImageRef(arg2);
    }

    public final void requestLayout() {
        super.requestLayout();
    }

    public WebBackForwardList restoreState(Bundle arg2) {
        this.a();
        return this.mWebView.restoreStateInner(arg2);
    }

    public void resumeTimers() {
        this.a();
        this.mWebView.resumeTimers();
    }

    public WebBackForwardList saveState(Bundle arg2) {
        this.a();
        return this.mWebView.saveStateInner(arg2);
    }

    public void saveWebArchive(String arg2) {
        this.a();
        this.mWebView.saveWebArchive(arg2);
    }

    public void saveWebArchive(String arg2, boolean arg3, ValueCallback arg4) {
        this.a();
        this.mWebView.saveWebArchive(arg2, arg3, arg4);
    }

    public void setBackgroundColor(int arg2) {
        super.setBackgroundColor(arg2);
        if(this.mWebView != null) {
            this.mWebView.setBackgroundColor(arg2);
        }
    }

    public void setDownloadListener(DownloadListener arg2) {
        this.a();
        this.mWebView.setDownloadListener(arg2);
    }

    public void setFindListener(WebView$FindListener arg2) {
        this.a();
        this.mWebView.setFindListener(arg2);
    }

    public void setHorizontalScrollBarEnabled(boolean arg2) {
        this.a();
        this.mWebView.setHorizontalScrollBarEnabled(arg2);
    }

    public void setHttpAuthUsernamePassword(String arg2, String arg3, String arg4, String arg5) {
        this.a();
        this.mWebView.setHttpAuthUsernamePassword(arg2, arg3, arg4, arg5);
    }

    public void setInitialScale(int arg2) {
        this.a();
        this.mWebView.setInitialScale(arg2);
    }

    public void setLayoutParams(ViewGroup$LayoutParams arg3) {
        super.setLayoutParams(arg3);
        if(this.getCoreView() != null && arg3.height < 0) {
            this.getCoreView().setLayoutParams(new FrameLayout$LayoutParams(arg3));
        }
    }

    public void setNetworkAvailable(boolean arg2) {
        this.a();
        this.mWebView.setNetworkAvailable(arg2);
    }

    public void setOnKeyListener(View$OnKeyListener arg3) {
        this.a();
        if(arg3 != null) {
            this.mWebView.setOnKeyListener(new c(this, arg3));
        }
        else {
            this.mWebView.setOnKeyListener(null);
        }
    }

    public void setOnLongClickListener(View$OnLongClickListener arg3) {
        this.a();
        if(arg3 != null) {
            this.mWebView.setOnLongClickListener(new a(this, arg3));
        }
        else {
            this.mWebView.setOnLongClickListener(null);
        }
    }

    public void setOnTouchListener(View$OnTouchListener arg3) {
        this.a();
        if(arg3 != null) {
            this.mWebView.setOnTouchListener(new com.uc.webview.export.b(this, arg3));
        }
        else {
            this.mWebView.setOnTouchListener(null);
        }
    }

    public final void setOverScrollMode(int arg3) {
        if(this.mWebView != null && WebView.getCoreType() != 1) {
            this.mWebView.setOverScrollMode(arg3);
        }
    }

    public void setScrollBarStyle(int arg2) {
        if(this.mWebView != null) {
            this.mWebView.setScrollBarStyle(arg2);
        }

        super.setScrollBarStyle(arg2);
    }

    public void setVerticalScrollBarEnabled(boolean arg2) {
        this.a();
        this.mWebView.setVerticalScrollBarEnabled(arg2);
    }

    public void setVerticalScrollbarOverlay(boolean arg2) {
        this.a();
        this.mWebView.setVerticalScrollbarOverlay(arg2);
    }

    public final void setVisibility(int arg1) {
        super.setVisibility(arg1);
    }

    public void setWebChromeClient(WebChromeClient arg2) {
        this.a();
        this.mWebView.setWebChromeClient(arg2);
    }

    public static void setWebContentsDebuggingEnabled(boolean arg2) {
        UCCore.notifyCoreEvent(100, new Boolean(arg2));
    }

    public void setWebViewClient(WebViewClient arg2) {
        this.a();
        this.mWebView.setWebViewClient(arg2);
    }

    public void stopLoading() {
        this.a();
        this.mWebView.stopLoading();
    }

    public void zoomBy(float arg7) {
        this.a();
        if((((double)arg7)) < 0.01) {
            throw new IllegalArgumentException("zoomFactor must be greater than 0.01.");
        }

        if((((double)arg7)) > 100) {
            throw new IllegalArgumentException("zoomFactor must be less than 100.");
        }

        if(WebView.getCoreType() == 2) {
            if(Build$VERSION.SDK_INT < 21) {
                return;
            }

            try {
                ReflectionUtil.invoke(this.getCoreView(), "zoomBy", new Class[]{Float.TYPE}, new Object[]{Float.valueOf(arg7)});
            }
            catch(Exception v0) {
            }
        }
        else {
            this.mWebView.invoke(7, new Object[]{Float.valueOf(arg7)});
        }
    }

    public boolean zoomIn() {
        this.a();
        return this.mWebView.zoomIn();
    }

    public boolean zoomOut() {
        this.a();
        return this.mWebView.zoomOut();
    }
}

