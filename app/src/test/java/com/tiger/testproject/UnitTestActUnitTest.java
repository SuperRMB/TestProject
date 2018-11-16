package com.tiger.testproject;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Created by zhanghe on 2018/8/28.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class UnitTestActUnitTest {


    @Rule
    public ActivityTestRule<UnitTestAct> mActivityRule = new ActivityTestRule<>(UnitTestAct.class);

    @Test
    public void test(){
//        onView(withId(R.id.et_test)).perform(typeText("hahha"));
        onView(withId(R.id.btn_test)).perform(ViewActions.click());
    }
}
