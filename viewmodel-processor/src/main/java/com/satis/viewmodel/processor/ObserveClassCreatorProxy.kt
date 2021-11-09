package com.satis.viewmodel.processor

import com.satis.viewmodel.annotation.Observe
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import java.util.HashMap
import javax.annotation.processing.Messager
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

/**
 * Created by ssb
 * date 2020/3/11
 * 创建Java文件代理类
 */
class ObserveClassCreatorProxy(elementUtils: Elements, val typeElement: TypeElement) {
    val observeClassName: String
    private val mObserveElementMap: MutableMap<Observe, ExecutableElement> = HashMap()
    val packageName: String

    //存储方法信息
    fun putElement(observe: Observe, element: ExecutableElement) {
        mObserveElementMap[observe] = element
    }

    /**
     * 创建Java代码
     * javapoet
     *
     * @return
     */
    fun generateJavaCode(messager: Messager): TypeSpec {
        val className = ClassName.get("com.satis.viewmodel.core", "Observer")
        return TypeSpec.classBuilder(observeClassName)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(className)
            .addMethod(generateMethods(messager))
            .build()
    }

    /**
     * 加入Method
     * javapoet
     */
    private fun generateMethods(messager: Messager): MethodSpec {
        val host = ClassName.bestGuess(typeElement.qualifiedName.toString())
        val owner = ClassName.get("androidx.lifecycle", "LifecycleOwner")
        val methodBuilder = MethodSpec.methodBuilder("observe")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override::class.java)
            .returns(Void.TYPE)
            .addParameter(owner, "host")
        val baseObserver = ClassName.get("com.satis.viewmodel.core", "BaseObserver")
        val observer = ClassName.get("androidx.lifecycle", "Observer")
        //计数形式规避方法重载
        var count = 0
        for ((observe, value) in mObserveElementMap) {
            val method = value.simpleName.toString()
            val parameters = value.parameters
            val parameterName = parameters[0]!!.simpleName.toString()
            val parameterType = parameters[0]!!.asType().toString()
            val className = ClassName.bestGuess(parameterType)
            //            messager.printMessage(Diagnostic.Kind.NOTE,"process observe.tag():"+observe.tag());
//            messager.printMessage(Diagnostic.Kind.NOTE,"process observe.isSticky():"+observe.isSticky());
//            messager.printMessage(Diagnostic.Kind.NOTE,"process 方法参数名:"+parameterName);
//            messager.printMessage(Diagnostic.Kind.NOTE,"process 方法参数类型:"+parameterType);
            val observeName = method + count
            methodBuilder.beginControlFlow(
                "\$T<$parameterType> $observeName = new \$T<$parameterType>()",
                observer,
                observer
            )
                .addCode("@\$T\n", Override::class.java)
                .beginControlFlow("public void onChanged($parameterType t)")
                .addStatement("((\$T)host).$method(t)", host)
                .endControlFlow()
                .endControlFlow()
                .addCode(";\n")
            methodBuilder.addStatement(
                "((\$T)host).mViewModel.addObserver(host,\"" + observe.tag + "\" ," + "new \$T(" + observeName + ")," + observe.sticky + ")",
                host,
                baseObserver
            )
            count++
        }
        return methodBuilder.build()
    }

    init {
        val packageElement = elementUtils.getPackageOf(
            typeElement
        )
        packageName = packageElement.qualifiedName.toString()
        val className = typeElement.simpleName.toString()
        observeClassName = "$className\$Observe"
    }
}