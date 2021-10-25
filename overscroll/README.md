## SatisOverScroll

![Alt](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9hdmF0YXIuY3Nkbi5uZXQvNy83L0IvMV9yYWxmX2h4MTYzY29tLmpwZw)

> 前言： Android与IOS开发存在的一大差距就是惯性滚动回弹效果，效果确实比较“人性”生动，这也是众多Android开发人员争相效仿的原因，当然我也不例外。

#### 作为一名五年经的猿早已不满足与市面上的各种开源框架，理由有：

- 无一例外需要更多的配置去完成惯性回弹效果
    - 通过嵌套一个具有OverScroll 事件处理机制的Layout
    - 通过自定RecyclerView 等对原“ScrollView”进行事件拦截、滚动动画添加等
    - 需要此效果的地方需要修改layout布局层级
- 对于我来说我不希望去破坏已有的layout层级结构，不希望做过多的配置且用到的地方就要多“几步”操作，
  我更想要的是“原生控件”就自带这个功能。

#### 听起来有点像痴话，其实并不然,我们可以换个角度去思考这个问题,原生组件在我们引用的时候不具备的功能，能否在编译后实现呢？

### 重点来了

[APT （Annotation Processing Tool）]()

[AOP （Aspect Oriented Programming）]()

#### 以上框架使用及原理不做过多介绍，如果不熟悉的可以点击查看介绍（如果想要在Android开发上走的更远的话，强烈建议熟练掌握这两个框架，在架构师领域这两货必不可少）。

#### 今天的主角是[AspectJ](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx),利用该框架在编译成class阶段插入字节码，实现我们要的功能

#### 往哪里插入？

##### 在andorid 开发中事件已有的一套关于手势滑动的事件分发机制，也就是说我们没太必要去重写关于手势（onTouchEvent）上的逻辑,仅监听分发出来的事件做处理，主要涉及两个接口 NestedScrollParent 跟 NestedScrollChild3（extend NestedScrollChild2 extend NestedScrollChild）具体接口如何分发这里也不做过多介绍，想了解原理请参考 [深入了解 Android NesteScroll 原理]()

#### 1.首先对Nested 实现方法进行切面
直接上代码
``` Kotlin 

@Aspect
class AOPOverScroll {
    @Around(Constants.STARTNESTEDSCROLL)
    @Throws(Throwable::class)
    fun startNestedScroll(joinPoint: ProceedingJoinPoint): Any? {
        return NestedScrollAOP.startNestedScroll(joinPoint)
    }

    @Around(Constants.DISPATCHNESTEDPRESCROLL)
    @Throws(Throwable::class)
    fun dispatchNestedPreScroll(joinPoint: ProceedingJoinPoint): Any? {
        return NestedScrollAOP.dispatchNestedPreScroll(joinPoint)
    }

    @Around(Constants.DISPATCHNESTEDSCROLL)
    @Throws(Throwable::class)
    fun dispatchNestedScroll(joinPoint: ProceedingJoinPoint): Any? {
        return NestedScrollAOP.dispatchNestedScroll(joinPoint)
    }

    @Around(Constants.DISPATCHNESTEDPREFLING)
    @Throws(Throwable::class)
    fun dispatchNestedPreFling(joinPoint: ProceedingJoinPoint): Any? {
        return NestedScrollAOP.dispatchNestedPreFling(joinPoint)
    }

    @Around(Constants.STOPNESTEDSCROLL)
    @Throws(Throwable::class)
    fun stopNestedScroll(joinPoint: ProceedingJoinPoint): Any? {
        return NestedScrollAOP.stopNestedScroll(joinPoint)
    }
}

```
###### NestedScrollAOP 内部为插桩的具体逻辑部分

```kotlin
object NestedScrollAOP {
    /**
     *  以tag_overscroll_delegate id 作为标识存储代理类 [OverScrollDelegate]
     *  @param target 集成自NestedScrollChild3 接口的实现类（RecyclerView、 NestedScrollView 目前仅针对这两个原生控件进行插桩的消费事件处理 排除自定义控件）
     */
    private fun getOverScrollDelegate(target: Any): OverScrollDelegate? {
        if (target is RecyclerView || target is NestedScrollView) {
            var tag = (target as View).getTag(R.id.tag_overscroll_delegate)
            if (tag == null) {
                tag = createByTarget(target)
                target.setTag(R.id.tag_overscroll_delegate, tag)
            }
            return tag as OverScrollDelegate
        }
        return null
    }

    /**
     *  对应处理[androidx.core.view.NestedScrollingChild2.startNestedScroll] 两个参数的方法
     */

    @Throws(Throwable::class)
    fun startNestedScroll(joinPoint: ProceedingJoinPoint): Any? {
        val args = joinPoint.args
        val target = joinPoint.target
        val overScrollDelegate = getOverScrollDelegate(target)
        if (overScrollDelegate != null) {
            if (overScrollDelegate.onStartNestedScroll(args[0] as Int, args[1] as Int)) {
                overScrollDelegate.onNestedScrollAccepted(
                    (target as View),
                    args[0] as Int,
                    args[1] as Int
                )
                return true
            }
        }
        return joinPoint.proceed()
    }

    /**
     *  对应处理[androidx.core.view.NestedScrollingChild2.dispatchNestedPreScroll]
     */
    @Throws(Throwable::class)
    fun dispatchNestedPreScroll(joinPoint: ProceedingJoinPoint): Any? {
        val args = joinPoint.args
        val target = joinPoint.target
        val overScrollDelegate = getOverScrollDelegate(target)
        if (overScrollDelegate != null) {
            overScrollDelegate.onNestedPreScroll(
                target as View,
                args[0] as Int,
                args[1] as Int,
                (args[2] as IntArray),
                args[4] as Int
            )
            if ((args[2] as IntArray)[1] != 0) {
                return true
            }
        }
        return joinPoint.proceed()
    }

    /**
     *  对应处理[androidx.core.view.NestedScrollingChild2.dispatchNestedScroll]
     */
    @Throws(Throwable::class)
    fun dispatchNestedScroll(joinPoint: ProceedingJoinPoint): Any? {
        val args = joinPoint.args
        val target = joinPoint.target
        val overScrollDelegate = getOverScrollDelegate(target)
        overScrollDelegate?.onNestedScroll(
            target as View,
            args[0] as Int,
            args[1] as Int,
            args[2] as Int,
            args[3] as Int,
            args[5] as Int
        )
        return joinPoint.proceed()
    }


    /**
     *  对应处理[androidx.core.view.NestedScrollingChild2.dispatchNestedPreFling] 
     */
    @Throws(Throwable::class)
    fun dispatchNestedPreFling(joinPoint: ProceedingJoinPoint): Any? {
        val args = joinPoint.args
        val target = joinPoint.target
        val overScrollDelegate = getOverScrollDelegate(target)
        overScrollDelegate?.onNestedPreFling(
            target as View,
            (args[0] as Float),
            (args[1] as Float)
        )
        return joinPoint.proceed()
    }

    /**
     *  对应处理[androidx.core.view.NestedScrollingChild2.stopNestedScroll]
     */
    @Throws(Throwable::class)
    fun stopNestedScroll(joinPoint: ProceedingJoinPoint): Any? {
        val target = joinPoint.target
        val args = joinPoint.args
        val overScrollDelegate = getOverScrollDelegate(target)
        if (overScrollDelegate != null && args.isNotEmpty()) {
            overScrollDelegate.onStopNestedScroll((target as View), args[0] as Int)
        }
        return joinPoint.proceed()
    }
}
```
插桩代码较为固定

#### 2. 代理类OverScrollDelegate 对具体滑动分发事件进行处理即添加“OverScroll”效果


#### 使用 
1. 在project build.gradle 中添加 aspectj plugin 依赖
```
     dependencies {
        classpath 'com.android.tools.build:gradle:7.0.3'
        classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31'
        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.10'  // aspectj plugin 依赖
    }
    
```
2. 在 app build.gradle 中添加 aspectj 依赖
```
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'android-aspectjx' // apply plugin
    id 'kotlin-kapt'
}


 dependencies {
    implementation 'org.aspectj:aspectjrt:1.9.5'
}
```

3. 复制 AOPTest.kt 到目标主项目 app 目录任意（类名也可随意更改）

4. 运行即可赋予 原生 RecyclerView  OverScroll 效果


