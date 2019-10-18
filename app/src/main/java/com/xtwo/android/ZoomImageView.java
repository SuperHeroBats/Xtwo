package com.xtwo.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ZoomImageView extends View {

    //定义常量
    public static final int STATUS_INIT = 1;   //初始化状态
    public static final int STATUS_ZOOM_OUT = 2;   //图片放大状态
    public static final int STATUS_ZOOM_IN = 3;   //图片缩小状态
    public static final int STATUS_MOVE = 4;   //图片移动状态

    private Matrix matrix = new Matrix();   //用于对图片进行移动缩放变换的矩阵
    private Bitmap sourceBitmap;   //待展示的Bitmap对象
    private int currentStatus;   //记录当前操作的状态
    private int width;   //ZoomImageView的宽度
    private int height;   //ZoomImageView的高度
    private float currentBitmapWidth;   //记录当前图片的宽度 被缩放时参数同时改变
    private float currentBitmapHeight;   //记录当前图片的高度 被缩放时参数同时改变

    private float centerPointX;   //记录两指放在屏幕上 中心点的横坐标
    private float centerPointY;   //记录两指放在屏幕上 中心点的纵坐标
    private float lastXMove = -1;   //记录上次手指移动时的横坐标
    private float lastYMove = -1;   //记录上次手指移动时的纵坐标
    private float movedDistanceX;   //记录手指在横坐标上的移动距离
    private float movedDistanceY;   //记录手指在纵坐标上的移动距离

    private float totalTranslateX;   //记录图片在矩阵上的横向偏移值
    private float totalTranslateY;   //记录图片在矩阵上的纵向偏移值
    private float totalRatio;   //记录图片在矩阵上的总缩放比例
    private float scaledRatio;   //记录手指移动的距离所造成的缩放比例
    private float initRatio;   //记录图片初始化的缩放比例
    private double lastFingerDis;   //记录上次两指间的距离

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        currentStatus = STATUS_INIT;   //设置当前状态为初始化
    }

    //将待展示的图片设置进来
    public void setImageBitmap(Bitmap bitmap) {
        sourceBitmap = bitmap;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            //获取ZoomImageView的宽高
            width = getWidth();
            height = getHeight();
        }
    }

    //触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:   //多触点模式:
                if (event.getPointerCount() == 2) {
                    //当屏幕中出现两个手指时 计算两指间的距离
                    lastFingerDis = distanceBetweenFingers(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //当屏幕上只有一个手指时 为拖动状态
                if (event.getPointerCount() == 1) {
                    float xMove = event.getX();
                    float yMove = event.getY();
                    if (lastXMove == -1 && lastYMove == -1) {
                        lastXMove = xMove;
                        lastYMove = yMove;
                    }
                    //计算移动距离
                    currentStatus = STATUS_MOVE;
                    movedDistanceX = xMove - lastXMove;
                    movedDistanceY = yMove - lastYMove;
                    //边界检查 防止图片拖出边界
                    if (totalTranslateX + movedDistanceX > 0) {
                        movedDistanceX = 0;
                    } else if (width - (totalTranslateX + movedDistanceX) > currentBitmapWidth) {
                        movedDistanceX = 0;
                    }
                    if (totalTranslateY + movedDistanceY > 0) {
                        movedDistanceY = 0;
                    } else if (height - (totalTranslateY + movedDistanceY) > currentBitmapHeight) {
                        movedDistanceY = 0;
                    }
                    //调用onDraw方法绘制图片
                    invalidate();
                    lastXMove = xMove;
                    lastYMove = yMove;
                } else if (event.getPointerCount() == 2) {
                    //两指在屏幕上时 为缩放状态
                    centerPointBetweenFingers(event);
                    double fingerDis = distanceBetweenFingers(event);
                    //计算两指间距离 判断是处于哪种缩放状态
                    if (fingerDis > lastFingerDis) {
                        currentStatus = STATUS_ZOOM_OUT;
                    }else {
                        currentStatus = STATUS_ZOOM_IN;
                    }
                    //缩放倍数检查 最大放大4倍 最小缩小初始化比例
                    if ((currentStatus == STATUS_ZOOM_OUT && totalRatio < 4 * initRatio) || (currentStatus == STATUS_ZOOM_IN && totalRatio > initRatio)) {
                        scaledRatio = (float) (fingerDis / lastFingerDis);   //计算两指移动的距离所造成的缩放比例
                        totalRatio = totalRatio * scaledRatio;   //计算总缩放比例
                        if (totalRatio > 4 * initRatio) {
                            totalRatio = 4 * initRatio;   //最大放大到4倍
                        } else if (totalRatio < initRatio) {
                            totalRatio = initRatio;   //缩小至初始化比例
                        }
                        //调用onDraw方法绘制图片
                        invalidate();
                        lastFingerDis = fingerDis;
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:   //屏幕上还有手指存在 ACTION_UP是最后个手指离开时触发的
                if (event.getPointerCount() == 2) {
                    //当手指离开屏幕时 还原临时值
                    lastXMove = -1;
                    lastYMove = -1;
                }
                break;
            case MotionEvent.ACTION_UP:
                //当手指离开屏幕时 还原临时值
                lastXMove = -1;
                lastYMove = -1;
                break;
            default:
                break;
        }
        return true;
    }

    //根据currentStatus的状态绘制图片
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (currentStatus) {
            case STATUS_ZOOM_OUT:
            case STATUS_ZOOM_IN:
                zoom(canvas);
                break;
            case STATUS_MOVE:
                move(canvas);
                break;
            case STATUS_INIT:
                initBitmap(canvas);
            default:
                canvas.drawBitmap(sourceBitmap, matrix , null);
                break;
        }
    }

    //缩放处理
    private void zoom(Canvas canvas) {
        matrix.reset();
        matrix.postScale(totalRatio, totalRatio);   //将图片按总比例进行缩放
        //计算缩放后的宽高及初始化偏移量
        float scaledWidth = sourceBitmap.getWidth() * totalRatio;
        float scaledHeight = sourceBitmap.getHeight() * totalRatio;
        float translateX = 0f;
        float translateY = 0f;
        //当图片宽度小于屏幕时 则按屏幕中心的横坐标进行水平缩放 反之按两指间的中心坐标
        if (currentBitmapWidth < width) {
            translateX = (width - scaledWidth) / 2f;   //按屏幕中心的横坐标进行水平缩放
        }else {
            translateX = totalTranslateX * scaledRatio + centerPointX * (1 - scaledRatio);   //按两指间的中心坐标
            //边界检查
            if (translateX > 0) {
                translateX = 0;
            } else if (width - translateX > scaledWidth) {
                translateX = width - scaledWidth;
            }
        }
        //当图片高度小于屏幕时
        if (currentBitmapHeight < height) {
            translateY = (height - scaledHeight) / 2f;
        }else {
            translateY = totalTranslateY * scaledRatio + centerPointY * (1 - scaledRatio);
            //边界检查
            if (translateY > 0) {
                translateY = 0;
            } else if (height - translateY > scaledHeight) {
                translateY = height - scaledHeight;
            }
        }
        //缩放后对图片进行偏移 保证缩放后的中心点不变
        matrix.postTranslate(translateX, translateY);
        //将各个参数传递回去
        totalTranslateX = translateX;
        totalTranslateY = translateY;
        currentBitmapWidth = scaledWidth;
        currentBitmapHeight = scaledHeight;
        canvas.drawBitmap(sourceBitmap, matrix, null);   //绘制图片
    }

    //平移处理
    private void move(Canvas canvas) {
        matrix.reset();
        //根据手指移动距离计算总偏移量
        float translateX = totalTranslateX + movedDistanceX;
        float translateY = totalTranslateY + movedDistanceY;
        matrix.postScale(totalRatio, totalRatio);   //先根据已有比例进行缩放
        matrix.postTranslate(translateX, translateY);   //再根据移动距离进行偏移
        totalTranslateX = translateX;
        totalTranslateY = translateY;
        canvas.drawBitmap(sourceBitmap, matrix, null);
    }

    //初始化操作 包括图片居中及当大于屏幕宽高时进行压缩
    private void initBitmap(Canvas canvas) {
        if (sourceBitmap != null) {
            matrix.reset();
            int bitmapWidth = sourceBitmap.getWidth();
            int bitmapHeight = sourceBitmap.getHeight();
            //当图片宽高大于屏幕时
            if (bitmapWidth > width || bitmapHeight > height) {
                if (bitmapWidth - width > bitmapHeight - height) {
                    //当图片宽度大于屏幕时 进行等比例压缩 并完全显示
                    float ratio = width / (bitmapWidth * 1.0f);
                    matrix.postScale(ratio, ratio);   //等比例缩放
                    float translateY = (height - (bitmapHeight * ratio)) / 2f;
                    //在纵坐标上进行偏移 以保证图片居中显示
                    matrix.postTranslate(0, translateY);
                    totalTranslateY = translateY;
                    totalRatio = initRatio = ratio;
                }else {
                    //当图片高度大于屏幕时
                    float ratio = height / (bitmapHeight * 1.0f);
                    matrix.postScale(ratio, ratio);   //等比例缩放
                    float translateX = (width - (bitmapWidth * ratio)) / 2f;
                    //在横坐标上进行偏移 以保证图片居中显示
                    matrix.postTranslate(translateX, 0);
                    totalTranslateX = translateX;
                    totalRatio = initRatio = ratio;
                }
                //计算当前图片的宽高
                currentBitmapWidth = bitmapWidth * initRatio;
                currentBitmapHeight = bitmapHeight * initRatio;
            }else {
                //当图片宽高都小于屏幕时 直接居中显示
                float translateX = (width - sourceBitmap.getWidth()) / 2f;
                float translateY = (height - sourceBitmap.getHeight()) / 2f;
                matrix.postTranslate(translateX, translateY);   //传入偏移量
                totalTranslateX = translateX;
                totalTranslateY = translateY;
                totalRatio = initRatio = 1f;
                currentBitmapWidth = bitmapWidth;
                currentBitmapHeight = bitmapHeight;
            }
            canvas.drawBitmap(sourceBitmap, matrix, null);   //绘制图片
        }
    }

    //计算两指间距离
    private double distanceBetweenFingers(MotionEvent event) {
        //Math的abs返回的是绝对值 sqrt返回的是一个数的平方根
        float disX = Math.abs(event.getX(0) - event.getX(1));
        float disY = Math.abs(event.getY(0) - event.getY(1));
        return Math.sqrt(disX * disX + disY * disY);
    }

    //计算两指间的中心坐标
    private void centerPointBetweenFingers(MotionEvent event) {
        float xPoint0 = event.getX(0);
        float yPoint0 = event.getY(0);
        float xPoint1 = event.getX(1);
        float yPoint1 = event.getY(1);
        //计算中心坐标
        centerPointX = (xPoint0 + xPoint1) / 2;
        centerPointY = (yPoint0 + yPoint1) / 2;
    }

}
