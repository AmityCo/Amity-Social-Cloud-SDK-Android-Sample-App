# Example EkoSDK
EkoSDK allows you to easily present an interactive chat, social and community

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
   
   - If you prefer ***MVVM*** Style in your **activity** or **fragment**

      1. First crate your view model class and inject constructor and extend DisposableViewModel
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
       2. For **Activity** You can extend ***SingleViewModelActivity*** and For **fragment** You can extend ***SingleViewModelFragment***
 


