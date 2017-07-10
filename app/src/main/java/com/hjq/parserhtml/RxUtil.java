package com.hjq.parserhtml;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.w3c.dom.Document;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by codeest on 2016/8/3.
 */
public class RxUtil {

    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

            }
        };
    }

    public static <Bitmap> Observable.Transformer<ResponseBody, android.graphics.Bitmap> handleResult() {   //compose判断结果
        return new Observable.Transformer<ResponseBody, android.graphics.Bitmap>() {
            @Override
            public Observable<android.graphics.Bitmap> call(Observable<ResponseBody> httpResponseObservable) {
                return httpResponseObservable.flatMap(new Func1<ResponseBody, Observable<android.graphics.Bitmap>>() {
                    @Override
                    public Observable<android.graphics.Bitmap> call(ResponseBody arg0) {
                        android.graphics.Bitmap bitmap=null;
                        try {
                            bitmap=BitmapFactory.decodeStream(arg0.byteStream());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return createData(bitmap);
                    }
                });
            }
        };
    }
    public static <Bitmap> Observable.Transformer<ResponseBody, android.graphics.Bitmap> handleCompressResult() {   //compose判断结果
        return new Observable.Transformer<ResponseBody, android.graphics.Bitmap>() {
            @Override
            public Observable<android.graphics.Bitmap> call(Observable<ResponseBody> httpResponseObservable) {
                return httpResponseObservable.flatMap(new Func1<ResponseBody, Observable<android.graphics.Bitmap>>() {
                    @Override
                    public Observable<android.graphics.Bitmap> call(ResponseBody arg0) {
                        android.graphics.Bitmap bitmap=null;
                        try {
                                BitmapFactory.Options opts = new BitmapFactory.Options();
                                opts.inSampleSize = 2;
                                bitmap = null;
                                bitmap = BitmapFactory.decodeStream(arg0.byteStream(), null, opts);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return createData(bitmap);
                    }
                });
            }
        };
    }
    public static <Document> Observable.Transformer<ResponseBody, org.w3c.dom.Document> handleResultDoc() {   //compose判断结果
        return new Observable.Transformer<ResponseBody, org.w3c.dom.Document>() {
            @Override
            public Observable<org.w3c.dom.Document> call(Observable<ResponseBody> httpResponseObservable) {
                return httpResponseObservable.flatMap(new Func1<ResponseBody, Observable<org.w3c.dom.Document>>() {
                    @Override
                    public Observable<org.w3c.dom.Document> call(ResponseBody arg0) {
                        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = null;
                        org.w3c.dom.Document doc= null;
                        try {
                            builder = builderFactory.newDocumentBuilder();
                            doc = builder.parse(arg0.byteStream());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return createData(doc);
                    }
                });
            }
        };
    }

    public static <T> Observable<String> createHttpObservable(Observable<String> observable) {
        return observable
                .compose(RxUtil.<String>rxSchedulerHelper());
    }

    public static <T> Observable<Bitmap> createBmpObservable(Observable<ResponseBody> observable) {
        return observable
                .compose(RxUtil.<ResponseBody>rxSchedulerHelper())
                .compose(RxUtil.<Bitmap>handleResult());
    }
    public static <T> Observable<Bitmap> createCompressBmpObservable(Observable<ResponseBody> observable) {
        return observable
                .compose(RxUtil.<ResponseBody>rxSchedulerHelper())
                .compose(RxUtil.<Bitmap>handleCompressResult());
    }
    public static <T> Observable<Document> createHtmlObservable(Observable<ResponseBody> observable) {
        return observable
                .compose(RxUtil.<ResponseBody>rxSchedulerHelper())
                .compose(RxUtil.<Document>handleResultDoc());
    }


    public static <T> Observable<T> createBaseObservable(Observable<T> observable) {
        return observable
                .compose(RxUtil.<T>rxSchedulerHelper());
    }

    /**
     * 生成Observable
     *
     * @param <T>
     * @return
     */
    public static <T> Observable<T> createData(final T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


}
