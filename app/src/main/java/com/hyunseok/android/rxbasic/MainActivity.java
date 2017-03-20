package com.hyunseok.android.rxbasic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 실제 Task 를 처리하는 객체
        Observable<String> simpleObservable =
                Observable.create((subscriber)  -> { // 발행
                    // 네트워크를 통해서 데이터를 긁어온다
                    // 반복문을 돌면서
                    subscriber.onNext("Hello RxAndroid!!"); // 구독자에게 자료를 뿌려준다.
                    subscriber.onNext("Hello RxAndroid!! 1");
                    subscriber.onNext("Hello RxAndroid!! 2");
                    subscriber.onNext("Hello RxAndroid!! 3");
                    subscriber.onCompleted();
                    }
                );

        // Observer Subscriber를 등록해주는 함수
        simpleObservable
                .subscribe(new Subscriber<String>() { // Observer Subscriber(구독자)
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "[Observer1] complete!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "[Observer1] error: " + e.getMessage());
                    }

                    @Override
                    public void onNext(String text) {
                        Toast.makeText(MainActivity.this, "[Observer1] " + text, Toast.LENGTH_SHORT).show();
                    }
                });

        // Observer 를 등록하는 함수 진화형(각 함수를 하나의 콜백개체에 나눠서 담아준다)
        simpleObservable.subscribe(new Action1<String>() { // onNext 함수와 동일한 역할을 하는 콜백객체
            @Override
            public void call(String s) {
                Toast.makeText(MainActivity.this, "[Observer2] " + s, Toast.LENGTH_SHORT).show();
            }
        }, new Action1<Throwable>() { // onEror 함수와 동일한 역할을 하는 콜백객체
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, "[Observer2] error: " + throwable.getMessage());
            }
        }, new Action0() { // onCompleted 함수와 동일한 역할을 하는 콜백객체
            @Override
            public void call() {
                Log.d(TAG, "[Observer2] complete!");
            }
        });

        // Observer 를 등록하는 함수 - 최종진화형(람다식)
        simpleObservable.subscribe(
                (string) -> { Toast.makeText(MainActivity.this, "[Observer3] " + string, Toast.LENGTH_SHORT).show(); },
                (error) -> { Log.e(TAG, "[Observer3] error: " + error.getMessage()); },
                () -> { Log.d(TAG, "[Observer3] complete!"); }

        );
    }
}