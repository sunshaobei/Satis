package com.satis.viewmodel.processor

import com.google.auto.service.AutoService
import com.satis.viewmodel.annotation.Observe
import com.satis.viewmodel.processor.utils.getPackagePath
import com.squareup.kotlinpoet.FileSpec
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
@SupportedAnnotationTypes("com.satis.viewmodel.annotation.Observe")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class Processor : AbstractProcessor() {
    private var mLogger: Messager? = null
    private var mElementUtils: Elements? = null
    private val mProxyObserveMap: MutableMap<String, ObserveClassCreatorProxy> = HashMap()
    private lateinit var mModuleName: String

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        mLogger = processingEnv.messager
        mElementUtils = processingEnv.elementUtils
        mModuleName = processingEnv.options["moduleName"].toString()
        mLogger!!.printMessage(Diagnostic.Kind.NOTE, mModuleName + "：：：：----init")
    }

    override fun process(set: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        mLogger!!.printMessage(Diagnostic.Kind.NOTE, "processing...")
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
                observeProxy = ObserveClassCreatorProxy(mElementUtils!!,mLogger, classElement)
                mProxyObserveMap[fullClassName] = observeProxy
            }
            val observe = executableElement.getAnnotation(Observe::class.java)
            observeProxy.putElement(observe, executableElement)
        }
        //通过javapoet生成

        val createKtx = ObserveKtxCreatorProxy(processingEnv)
        val observeStoreCreatorProxy = ObserveStoreCreatorProxy(mModuleName)
        for (key in mProxyObserveMap.keys) {
            try {
                //　生成observer文件
                val proxyInfo = mProxyObserveMap[key]
                //以activity命名的枚举
                observeStoreCreatorProxy.put(proxyInfo!!.typeElement)
                //生成observe 文件
                val typeSpec = proxyInfo.generateJavaCode{methodName,paramType->
                    createKtx.addFun(proxyInfo.typeElement,methodName,paramType)
                }
                val fileSpec = FileSpec.builder(
                    getPackagePath(proxyInfo.typeElement), typeSpec.name!!)
                    .addType(typeSpec)
                    .build()
                fileSpec.writeTo(processingEnv.filer)
                fileSpec.writeTo(System.out)
            } catch (e: Exception) {
                e.printStackTrace()
                mLogger!!.printMessage(Diagnostic.Kind.ERROR, "process error!!!..." + e.message)
            }
        }

        try {
            val ktxType = createKtx.typeSpec.build()
            val fileSpec = FileSpec.builder("$APT_PACKAGE.$mModuleName",ktxType.name!!).addType(ktxType).build()
            fileSpec.writeTo(processingEnv.filer)
            fileSpec.writeTo(System.out)
        }catch (e:Exception){

        }

        observeStoreCreatorProxy.endMethod()
        try {
            //生产 observer map
            val typeSpec = observeStoreCreatorProxy.build()
            val fileSpec = FileSpec.builder("$APT_PACKAGE.$mModuleName", typeSpec.name!!)
                .addType(typeSpec)
                .build()
            fileSpec.writeTo(processingEnv.filer)
            fileSpec.writeTo(System.out)
        } catch (e: FilerException) {
            e.printStackTrace()
        }
        mLogger!!.printMessage(Diagnostic.Kind.NOTE, "process finish ...")
        return true
    }

    companion object {
        var APT_PACKAGE = "com.satis.viewmodel.apt"
    }
}