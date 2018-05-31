# kotlin-monitorfragment
## kotlin-monitorfragment 监视器fragment

通过`monitorfragment`添加到activity or fragment，充当监听器，来实现监听其**生命周期、onActivityResult、requestPermission**，实现：
    
* 动态权限访问；
* 生命周期回调监听；
* startActivityForResult回调监听；


采用kotlin编写，代码比较少，大佬勿喷！

## 修改历史
### 1. 哥们建议
你这代码太不kotlin了，完全就是翻译的Java，缺乏创新，要培养函数式思维；可以尝试改一下；  

* 回调方法采用高阶函数，避免实现多个方法；
* 采用DSL配置回调请求权限；

**1. 将生命周期接口改成高阶函数，更加简洁：**

```kotlin
btn_lifeCycle.setOnClickListener {
        maeLifeCycleListener { state, _ ->
            when (state) {
                MAELifeCycleState.ON_STOP ->
                    Toast.makeText(applicationContext, "onStop", Toast.LENGTH_SHORT).show()
                MAELifeCycleState.ON_DESTROY ->
                    Toast.makeText(applicationContext, "onDestroy", Toast.LENGTH_SHORT).show()
            }
        }
    }
```

**2. 将权限请求改成DSL配置式，更加简洁：**

```kotlin
maeReqPermission(arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION), "摄像头与更好的位置") {
    success {
        Toast.makeText(applicationContext, "获取成功", Toast.LENGTH_SHORT).show()
    }
    failed {
        Toast.makeText(applicationContext, "获取失败！！", Toast.LENGTH_SHORT).show()
    }
    // 用户选择不再询问，执行以下
    shouldShowReqPermission { it ->
        Log.e("better", it.toString())
    }
}
```


## Java 版本
[https://github.com/liyuzero/maeMonitorFragment](https://github.com/liyuzero/maeMonitorFragment)

## 原理 
`monitorfragment`为一个透明的fragment，通过将`monitorfragment`添加到activity or fragment，充当监听器，并监听对应的方法，如上实现；

## 配置

1. 项目根目录build.gradle文件中，引入maven：

	```
	allprojects {
	    repositories {
	        google()
	        jcenter()
	        maven {url 'https://jitpack.io'}
	    }
	}
	```
2. 引入库

    ```
    implementation 'com.github.zhaoyubetter:kotlin-monitorfragment:v1.0.1'
    ```

## 具体使用

### 1.权限请求

```kotlin
btn_sd.setOnClickListener {
    maeReqPermission(arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION), "摄像头与更好的位置") {
        success {
            Toast.makeText(applicationContext, "获取成功", Toast.LENGTH_SHORT).show()
        }
        failed {
            Toast.makeText(applicationContext, "获取失败！！", Toast.LENGTH_SHORT).show()
        }
        // 用户选择不再询问，执行以下
        shouldShowReqPermission { it ->
            Log.e("better", it.toString())
        }
    }
}
```

### 2. 监听生命周期

```kotlin
// lifecycle
btn_lifeCycle.setOnClickListener {
    maeLifeCycle { state, _ ->
        when (state) {
            MAELifeCycleState.ON_STOP ->
                Toast.makeText(applicationContext, "onStop", Toast.LENGTH_SHORT).show()
            MAELifeCycleState.ON_DESTROY ->
                Toast.makeText(applicationContext, "onDestroy", Toast.LENGTH_SHORT).show()
        }
    }
}
```

### 3.onActivityForResult

```kotlin
btn_result.setOnClickListener {
    maeStartForResult(Intent(applicationContext, DemoActivity::class.java), 20) { _, _, data ->
        Toast.makeText(applicationContext, data?.getStringExtra("name"), Toast.LENGTH_SHORT).show()
    }
}
```
