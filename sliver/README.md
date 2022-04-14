# Sliver
[github主页](https://github.com/sunshaobei/Satis)
[文档](https://sunshaobei.gitbook.io/satis/sliver)

使用方式

- 首先在使用模块project gradle 中添加mavencentral() 仓库

 ```groovy
    repositories {
           ...
           mavenCentral()
       }
   ```
- 然后在使用模块module gradle 中添加
 
```kotlin	
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-kapt'
}
dependencies {
   ...
    implementation "io.github.sunshaobei:satis-sliver:1.0.1"
}


```

- 单类型布局
```kotlin
//list 数据集合
//item 列表item数据
//position 列表位置
//holder viewhodler

recyclerView.sliver{
        datas = list
        layoutManager = LinearLayoutManager(this@MainActivity)
        item<数据类型>{
            layoutId = R.layout.item1
            selector = {item,position->
                //多类型使用判断 返回true 即表示使用此类型 item
                retun true
            }
            itemContent = { item,position,viewholder->
                
            }
        }
        
         item<数据类型>{
            layoutId = R.layout.item2
            selector = {item,position->
                //多类型使用判断 返回true 即表示使用此类型 item
                retun true
            }
            itemContent = { item,position,viewholder->
                
            }
        }
  			item<数据类型,ViewDataBinding>{
            selector = {item,position->
                //多类型使用判断 返回true 即表示使用此类型 item
                retun true
            }
            itemContent = { item,position,binding->
                
            }
        }
    }
```
 databinding 使用
```kotlin
recyclerView.satis(list){	
    item<数据类型,ViewDataBinding>{
            selector = {item,position->
                //多类型使用判断 返回true 即表示使用此类型 item
                retun true
            }
            itemContent = { item,position,binding->
                
            }
     }
}
```

selector 作用在于存在多种类型type 时，根据此判断选择是否采用此类型。

-  databinding xml中使用

```kotlin
    <androidx.recyclerview.widget.RecyclerView
        ···
        datas="@{items}"
        layoutId="@{@layout/R.layout.item_single_type}"
        brStr="@{BR ID}"
    />
```
目前在建议在单类型布局的情况使用 databinding xml 方式(仅上面三行代码搞定),多类型的不建议使用databinding xml 实现（我也没实现相关功能，考虑点在：多类型情况下在xml中实现反而更加复杂，不符合我对简洁代码的期望）

- 其他api
```kotlin
recyclerView.satis(list){	
    ...
    layoutManager = ...// 默认为linearLayoutManager
    //添加头部 headerView 头部View
    header = headerView1
    header = headerView2
    //添加尾部 footView
    footer = footView1
    footer = footView2
    
    //点击事件 参数分别为： viewholder、 列表中的实际位置、所在数据集合中的位置
   itemClick = {holder, adapterPosition, listPosition ->
                Toast.makeText(this@SingleTypeActivity, "点击 position -$p2", Toast.LENGTH_SHORT)
                    .show()
            }
    
    //分割线 oriention方向、 size 分割线高度、color 颜色 
    //其中 pading 在水平方向 近 top跟bottom 剩下，垂直方向仅left、right 生效
    divider{
        orientation = VERTICAL_LIST
        size = 1
        color = Color.RED
        paddingLeft = 10
        paddingRight = 10
        paddingTop = 10
        paddingBottom = 10
    }
}
```
## 代码构思架构不易，想做一个体现“好用”开源开发框架，希望各位大佬多多支持，不吝赐教。

求赏。                     有想法欢迎讨论给建议，私信加群
![71649923704_.pic.jpg](https://upload-images.jianshu.io/upload_images/7974572-db197de39302c151.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/720)
