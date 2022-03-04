package com.satis.viewmodel.processor.kotlin

import com.satis.viewmodel.annotation.Observe
import com.satis.viewmodel.processor.utils.javaToKotlinType
import com.squareup.kotlinpoet.*
import java.util.HashMap
import javax.annotation.processing.Messager
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName

/**
 * Created by ssb
 * date 2020/3/11
 * 创建Java文件代理类
 */
class ObserveClassCreatorProxyKt(elementUtils: Elements, val typeElement: TypeElement) {
    private val observeClassName: String
    private val mObserveElementMap: MutableMap<Observe, ExecutableElement> = HashMap()
    private val packageName: String

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
    fun generateJavaCode(): TypeSpec {
        val className = ClassName("com.satis.viewmodel.core", "Observer")
        return TypeSpec.classBuilder(observeClassName)
            .addModifiers(KModifier.PUBLIC)
            .addSuperinterface(className)
            .addFunction(generateMethods())
            .build()
    }

    /**
     * 加入Method
     * javapoet
     */
    private fun generateMethods(): FunSpec {
        val host = ClassName.bestGuess(typeElement.qualifiedName.toString())
        val owner = ClassName("androidx.lifecycle", "LifecycleOwner")
        val funSpecBuilder = FunSpec.builder("observe")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("host",owner)
        val baseObserver = ClassName("com.satis.viewmodel.core", "BaseObserver")
        val observer = ClassName("androidx.lifecycle", "Observer")
        //计数形式规避方法重载
        var count = 0
        for ((observe, value) in mObserveElementMap) {
            val method = value.simpleName.toString()
            val parameters = value.parameters
            val className = parameters[0]!!.javaToKotlinType()!!
//            val className = JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(FqName(parameterType.toString()))
//                ?.asSingleFqName()?.asString()

//            val className = ClassName.bestGuess(parameterType)
            //            messager.printMessage(Diagnostic.Kind.NOTE,"process observe.tag():"+observe.tag());
//            messager.printMessage(Diagnostic.Kind.NOTE,"process observe.isSticky():"+observe.isSticky());
//            messager.printMessage(Diagnostic.Kind.NOTE,"process 方法参数名:"+parameterName);
//            messager.printMessage(Diagnostic.Kind.NOTE,"process 方法参数类型:"+parameterType);
            val observeName = method + count
            funSpecBuilder.beginControlFlow(
                "val $observeName = %T<%T>()",
                observer,
                className
            )
                .beginControlFlow("fun onChanged(t:%T)",className)
                .addStatement("(host as %T).$method(t)", host)
                .endControlFlow()
                .endControlFlow()
            funSpecBuilder.addStatement(
                "(host as %T).mViewModel.addObserver(host,\"" + observe.tag + "\" ," + " %T(" + observeName + ")," + observe.sticky + ")",
                host,
                baseObserver
            )
            count++
        }
        return funSpecBuilder.build()
    }

    init {
        val packageElement = elementUtils.getPackageOf(
            typeElement
        )
        packageName = packageElement.qualifiedName.toString()
        val className = typeElement.simpleName.toString()
        observeClassName = "${className}_Observe"
    }
}