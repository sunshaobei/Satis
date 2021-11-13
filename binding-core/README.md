##### 支持在databinding 布局layout中直接快速构建shape drawable


具体使用方式如下
``` 
  <LinearLayout
        ...
        shape="@{GradientDrawable.RECTANGLE}"   //默认就是方形
        solid="@{color}"    //填充颜色
        

```
其他属性 
"shape", "solid", "strokeWidth", "strokeColor", "radius", "radiusLeftTop", "radiusLeftBottom", "radiusRightTop", "radiusRightBottom"
如上使用
