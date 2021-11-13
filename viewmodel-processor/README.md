##### 该组件系对viewmodel 订阅事件的类EventBus处理


使用方式

1. 首先在使用模块module gradle 中添加

```kotlin

 defaultConfig {
       ...
        kapt{
            arguments{
                arg("module_name",project.getName())
            }
        }
    }


dependencies {
   
   ...
    implementation project(':viewmodel-core')
    implementation project(':viewmodel-annotation')
    kapt project(':viewmodel-processor')
}


```

2.使创建的ViewModel类继承 BaseViewModel
```kotlin
class ViewModel(application: Application) :BaseViewModel(application) {


    fun doSomething(){
        setValue("sdfsdfs")
    }
}

```
3.使activity 基础 BaseActivity

```kotlin
lass SatisViewModelActivity :BaseActivity<ViewModel>() {
   
    // 定义需要 在viewModel中订阅的事件 当viewmodel中需要回调时 在viewmodel中调用 setValue（）即可
    @Observe(tag = "test")
    fun onText(s:String){
        Log.e("sunshaobei",s)
    }
}
```


4.最后在application初始化时 将使用的模块添加到 "商店"

```kotlin

//有多少个模块使用就相应添加
  SatisViewModel.addObserverStore(com.satis.viewmodel.apt.模块名称.ObserveStore())
```
