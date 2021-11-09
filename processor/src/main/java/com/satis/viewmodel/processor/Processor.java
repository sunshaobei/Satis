package com.satis.viewmodel.processor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;
import com.satis.viewmodel.annotation.Observe;
import com.squareup.javapoet.JavaFile;

/**
 * Created by ssb
 * date 2020/3/11
 * 注解处理器
 */
@AutoService(javax.annotation.processing.Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.satis.viewmodel.annotation.Observe"})
public class Processor extends AbstractProcessor {

    public static String APT_PACKAGE = "com.satis.viewmodel.apt";
    private Messager mMassager;
    private Elements mElementUtils;
    private Map<String, ObserveClassCreatorProxy> mProxyObserveMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMassager = processingEnv.getMessager();
        mElementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mMassager.printMessage(Diagnostic.Kind.NOTE, "processing...");
        mProxyObserveMap.clear();
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(Observe.class);
        for (Element element : elementsAnnotatedWith) {
            ExecutableElement executableElement = (ExecutableElement) element;
            TypeElement classElement = (TypeElement) executableElement.getEnclosingElement();
            String fullClassName = classElement.getQualifiedName().toString();
            ObserveClassCreatorProxy observeProxy = mProxyObserveMap.get(fullClassName);
            if (observeProxy == null) {
                observeProxy = new ObserveClassCreatorProxy(mElementUtils, classElement);
                mProxyObserveMap.put(fullClassName, observeProxy);
            }
            Observe observe = executableElement.getAnnotation(Observe.class);
            observeProxy.putElement(observe, executableElement);
        }
        //通过javapoet生成

//        ViewModelEnumCreatorPRoxy viewModelEnumCreatorPRoxy = new ViewModelEnumCreatorPRoxy();
        ObserveStoreCreatorProxy observeStoreCreatorProxy = new ObserveStoreCreatorProxy();

        for (String key : mProxyObserveMap.keySet()) {
            try {
                //　生成observer文件
                ObserveClassCreatorProxy proxyInfo = mProxyObserveMap.get(key);
                //以activity命名的枚举
//                viewModelEnumCreatorPRoxy.addEnum(proxyInfo.getTypeElement());

                observeStoreCreatorProxy.put(proxyInfo.getTypeElement());

                //生成observe 文件
                JavaFile javaFile = JavaFile.builder(APT_PACKAGE,proxyInfo.generateJavaCode(mMassager)).build();
                javaFile.writeTo(processingEnv.getFiler());
//                mMessager.printMessage(Diagnostic.Kind.NOTE,javaFile.toString());
            } catch (Exception e) {
                e.printStackTrace();
                mMassager.printMessage(Diagnostic.Kind.ERROR,"process error!!!..."+e.getMessage());
            }

        }
        observeStoreCreatorProxy.endMethod();
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
            JavaFile storeFile = JavaFile.builder(APT_PACKAGE, observeStoreCreatorProxy.build()).build();
            storeFile.writeTo(processingEnv.getFiler());
//            mMessager.printMessage(Diagnostic.Kind.NOTE,enumFile.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMassager.printMessage(Diagnostic.Kind.NOTE, "process finish ...");
        return true;
    }

}
