# Android Example EkoSDK
EkoSDK allows you to easily present an interactive chat, social and community.

## Installation
#### 1. Chat

https://docs.ekomedia.technology/

You can try out using this API Key: 
```
"b3bee90c39d9a5644831d84e5a0d1688d100ddebef3c6e78"
```

## Technology Stack
- [X] Kotlin Language
- [X] Design pattern is MVVM
- [X] Reactive Programming
- [X] Functional Programming
- [X] Dagger2
- [X] Dynamic feature
- [X] Navigation Component

## Architecture 
The ExampleEkoSDK App architecture is a project _design pattern_ based on **MVVM** so you can following code style:
   
   - If you prefer ***MVVM*** Style in your **activity** or **fragment** .

      1. First crate your view model class and inject constructor and extend DisposableViewModel.
      ###### Example:
      ```
        class YourViewModel @Inject constructor() : DisposableViewModel() {
        
        }
      ```
      But if you want to use **ApplicationContext** you can parse context param into constructor. _Dependency Injection will be provide context on this._
      ```
        class YourViewModel @Inject constructor(context: Context) : DisposableViewModel() {
        
        }
      ```
       2. For **Activity** class you can extend **SingleViewModelActivity** .
       ###### Example:
       ```
         class YourActivity : SingleViewModelActivity<YourViewModel>() {
      
         }
       ```
       3. For **Fragment** class you can extend **SingleViewModelFragment** .
       ###### Example:
       ```
         class YourFragment : SingleViewModelFragment<YourViewModel>() {
      
         }
       ```
      4. when you extend SingleViewModelActivity or SingleViewModelFragment It's override function include **bindViewModel(), getViewModelClass()** and **getLayout()** .
      
        - ***bindViewModel()*** is auto provide your view model and already to called in onCreate() for activity and onActivityCreated() for fragment so you can used bindViewModel() instead of.

        - ***getViewModelClass()*** is required return your view model.
            ###### Example:
            ```
              override fun getViewModelClass(): Class<YourViewModel> {
                 return YourViewModel::class.java
              }
            ```
        - ***getLayout()*** is required return your layout.
     
     5. **Don't forgot** override **initDependencyInjection()** in your activity or fragment
     ###### Example:

            override fun initDependencyInjection() {
              DaggerActivityComponent
                      .builder()
                      .coreComponent(coreComponent())
                      .build()
                      .inject(this)
            }

### Toolbar Component


