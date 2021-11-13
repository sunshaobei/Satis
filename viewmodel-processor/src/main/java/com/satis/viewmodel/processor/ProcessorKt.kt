package com.satis.viewmodel.processor

import com.google.auto.service.AutoService
import com.satis.viewmodel.annotation.Observe
import com.squareup.javapoet.JavaFile
import java.io.IOException
import java.util.*
import javax.annotation.processing.*
import javax.annotation.processing.Processor
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

/**
 * Created by ssb
 * date 2020/3/11
 * 注解处理器
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("Observe")
class ProcessorKt : AbstractProcessor() {
    private var mMassager: Messager? = null
    private var mElementUtils: Elements? = null
    private val mProxyObserveMap: MutableMap<String, ObserveClassCreatorProxyKt> = HashMap()
    private lateinit var mModuleName:String
    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        mMassager = processingEnv.messager
        mElementUtils = processingEnv.elementUtils
        mModuleName = processingEnv.options["module_name"].toString()
        mMassager!!.printMessage(Diagnostic.Kind.ERROR, mModuleName)
    }

    override fun process(set: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        mMassager!!.printMessage(Diagnostic.Kind.NOTE, "processing...")
        mProxyObserveMap.clear()
        val elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(
            Observe::class.java
        )
        for (element in elementsAnnotatedWith) {
            val executableElement = element as ExecutableElement
            val classElement = executableElement.enclosingElement as TypeElement
            val fullClassName = classElement.qualifiedName.toString()
            var observeProxy = mProxyObserveMap[fullClassName]
            if (observeProxy == null) {
                observeProxy = ObserveClassCreatorProxyKt(mElementUtils!!, classElement)
                mProxyObserveMap[fullClassName] = observeProxy
            }
            val observe = executableElement.getAnnotation(Observe::class.java)
            observeProxy.putElement(observe, executableElement)
        }
        //通过javapoet生成

//        ViewModelEnumCreatorPRoxy viewModelEnumCreatorPRoxy = new ViewModelEnumCreatorPRoxy();
        val observeStoreCreatorProxy = ObserveStoreCreatorProxyKt()
        for (key in mProxyObserveMap.keys) {
            try {
                //　生成observer文件
                val proxyInfo = mProxyObserveMap[key]
                //以activity命名的枚举
//                viewModelEnumCreatorPRoxy.addEnum(proxyInfo.getTypeElement());
                observeStoreCreatorProxy.put(proxyInfo!!.typeElement)

                //生成observe 文件
                val javaFile = JavaFile.builder(
                    APT_PACKAGE+mModuleName, proxyInfo.generateJavaCode(
                        mMassager!!
                    )
                ).build()
                javaFile.writeTo(processingEnv.filer)
                //                mMessager.printMessage(Diagnostic.Kind.NOTE,javaFile.toString());
            } catch (e: Exception) {
                e.printStackTrace()
                mMassager!!.printMessage(Diagnostic.Kind.ERROR, "process error!!!..." + e.message)
            }
        }
        observeStoreCreatorProxy.endMethod()
        //生成MTViewModel单例
        try {
            //生成MTViewModel文件
//            ViewModelCreatorProxy viewModelCreatorProxy = new ViewModelCreatorProxy();
//            JavaFile binderFile = JavaFile.builder(APT_PACKAGE, viewModelCreatorProxy.createTypeSpec().build()).build();
//            binderFile.writeTo(processingEnv.getFiler());
//            mMessager.printMessage(Diagnostic.Kind.NOTE,binderFile.toString());
            //生成枚举文件
//            viewModelEnumCreatorPRoxy.addConstructorMethod();
//            JavaFile enumFile = JavaFile.builder(APT_PACKAGE, viewModelEnumCreatorPRoxy.build()).build();
//            enumFile.writeTo(processingEnv.getFiler());

            //生产 observer map
            val storeFile = JavaFile.builder(APT_PACKAGE+mModuleName, observeStoreCreatorProxy.build()).build()
            storeFile.writeTo(processingEnv.filer)
            //            mMessager.printMessage(Diagnostic.Kind.NOTE,enumFile.toString());
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mMassager!!.printMessage(Diagnostic.Kind.NOTE, "process finish ...")
        return true
    }

    companion object {
        var APT_PACKAGE = "com.satis.viewmodel.apt"
    }
}