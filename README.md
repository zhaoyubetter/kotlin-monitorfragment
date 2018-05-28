# kotlin-monitorfragment
## kotlin-monitorfragment 监视器fragment

通过`monitorfragment`添加到activity or fragment，充当监听器，来实现监听其**生命周期、onActivityResult、requestPermission**，实现：
    
* 动态权限访问；
* 生命周期回调监听；
* startActivityForResult回调监听；


采用kotlin编写，代码比较少，大佬勿喷！

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
    implementation 'com.github.zhaoyubetter:kotlin-monitorfragment:v1.0.0'
    ```

## 具体使用

### 1.权限请求

```kotlin
 // 权限
btn_sd.setOnClickListener { view ->
    maeRequestPermission(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA), object : MAEPermissionCallback {
        override fun onPermissionApplySuccess() {
            Toast.makeText(applicationContext, "获取成功", Toast.LENGTH_SHORT).show()
        }

        override fun onPermissionApplyFailure(notGrantedPermissions: List<String>, shouldShowRequestPermissions: List<Boolean>) {
            Toast.makeText(applicationContext, "获取失败！！", Toast.LENGTH_SHORT).show()
        }
    })
}
```

### 2. 监听生命周期

```kotlin
// lifecycle
btn_lifeCycle.setOnClickListener {
    MAEMonitorFragment.getInstance(this)?.setLifecycleListener(object : MAELifecycleListener {
        override fun onSaveInstanceState(outState: Bundle) {
            Toast.makeText(applicationContext, "onSaveInstance()", Toast.LENGTH_SHORT).show()
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            Toast.makeText(applicationContext, "onCreate()", Toast.LENGTH_SHORT).show()
        }

        override fun onStop() {
            Toast.makeText(applicationContext, "onStop()", Toast.LENGTH_SHORT).show()
        }
    })
}
```

### 3.onActivityForResult

```kotlin
 maeStartActivityForResult(Intent(applicationContext, DemoActivity::class.java), 20) { _, _, data ->
        Toast.makeText(applicationContext, data?.getStringExtra("name"), Toast.LENGTH_SHORT).show()
}
```
