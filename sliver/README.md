### RecyclerView的正确使用姿势

##### 自从事android开发已有五年，一直对android 的适配器模式抱一个怀疑态度，从设计的角度上考虑，确实这样便于扩展，便于高度自定义，但对于绝大多数应用场景的开发人员来说，实际并不友好。

##### 还记的你是怎么使用RecyclerView进行开发的么？

1. ###### 小白使用方式（最原始基础的）
```
 class MyAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>(){
         override fun onCreateViewHolder...
         override fun onBindViewHolder...
         //等等复写一些方法
 }
  
```
2. ###### 稍微有想法点的(创建一个通用类型的adapter，通用的ViewHolder)
```
 abstract class CommonAdapter:RecyclerView.Adapter<ViewHolder>(){
         abstract fun onConvert...
 }
```

##### 可以说目前市面上大家都是这么做的。but 大家有没有思考过一个问题，虽然已经很好的遵循复用原则，但是还是不可避免的在重复造轮子，需要自定义Adapter继承，实现方法等。
##### 说说我个人的理解吧！对于普通开发者而言，这样的代码说好听的遵循设计原则，难听点就是捞（写了五六年的代码还是捞），我觉的我们只需要关注布局跟数据绑定。



##### 正文来了,关注过Compose的小伙伴可能有了解到近年来声明式UI已经体现了在移动开发领域的优越性，无论是性能上还是开发的便捷性上。个人也是被它深深吸引，有过这方法经验后，让我萌发了想要实现一个早就想实现的代码功能。



#### SatisSliver

- 单类型布局
```kotlin
//list 数据集合
//item 列表item数据
//position 列表位置
//holder viewhodler

recyclerView.satis(list){	
    singleTypeItem<String>(R.layout.item){item,position,holder->
        holder.setText(R.id.tv,item)
    }
}
```
databinding 使用
```
recyclerView.satis(list){	
    singleTypeItemBinding<Any,ItemBinding>(R.layout.item){item,position,binding->
       binding.item = item
    }
}
```


- 多类型布局
```kotlin
//list 数据集合
//item 列表item数据
//position 列表位置
//holder viewhodler

recyclerView.satis(list){	
   selector = { item, position ->
        if(...){
            R.layout.item_multi_type1
        }else if(...){
             R.layout.item_multi_type2
        }
    }
    multiTypeItems<String>(R.layout.item_multi_type1) {item,position,holder ->
         h.setText(R.id.tv, "type1 -$position")
    }
     multiTypeItems<String>(R.layout.item_multi_type2) {item, position, holder ->
         h.setText(R.id.tv, "type2 -$position")
    }
    // 有多少type 就加多少。
}
```
databinding 使用
```
recyclerView.satis(list){	
   selector = { item, position ->
        if(...){
            R.layout.item_multi_type1
        }else if(...){
             R.layout.item_multi_type2
        }
    }
    multiTypeItemsBinding<Any,ItemMultiType1Binding>(R.layout.item_multi_type1) {item,position,binding ->
         binding.data1 = item
    }
     multiTypeItemsBinding<Any，ItemMultiType2Binding>(R.layout.item_multi_type2) {item, position, binding ->
        binding.data2 = item
    }
    // 有多少type 就加多少。
}
```

-  databinding xml中使用
```kotlin
    <androidx.recyclerview.widget.RecyclerView
        ···
        app:items="@{items}"
        app:singleTypeItem="@{@layout/R.layout.item_single_type}"
        app:bindingBR="@{全类名BR ID}"
    />
```
目前在建议在单类型布局的情况使用 databinding xml 方式(仅上面三行代码搞定),多类型的不建议使用databinding xml 实现（我也没实现相关功能，考虑点在：多类型情况下在xml中实现反而更加复杂，不符合我对简洁代码的期望）

- 其他api
```
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

- 后续我会在加上刷新逻辑（目前市面上的刷新框架以及自己以前写的都感觉不太合适且各种嵌套破坏结构）

