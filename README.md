# Android Example EkoSDK
EkoSDK allows you to easily present an interactive chat, social and community.

## Installation
#### 1. Chat

https://docs.ekomedia.technology/

You can try out using this API Key: 
```
"b3bee90c39d9a5644831d84e5a0d1688d100ddebef3c6e78"
```
## Goal Project
   - Encourage writing code to **lower than 200 line for each class/file** 
   - Clean Code
   - Custom Component with UI

## Technology Stack
- [X] **Kotlin** Language
- [X] Design pattern is MVVM
- [X] Reactive Programming
- [X] Functional Programming
- [X] Dagger2
- [X] Dynamic feature
- [X] Navigation Component
- [X] ConstraintLayout
- [X] Custom Component

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
      
        - **bindViewModel()** _is auto provide your view model and already to called in onCreate() for activity and onActivityCreated() for fragment so you can used bindViewModel() instead of._

        - **getViewModelClass()** _is required return your view model._
            ###### Example:
            ```
              override fun getViewModelClass(): Class<YourViewModel> {
                 return YourViewModel::class.java
              }
            ```
        - **getLayout()** _is required return your layout._
     
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
1. If you prefer Toolbar you can integrate on this in your layout.
###### Example:
   ```
       <com.ekoapp.sample.core.base.components.toolbar.UIKitAppbar
           android:id="@+id/appbar_main"
           style="@style/AppTheme.Appbar"
           app:layout_constraintStart_toStartOf="parent"
           app:layout_constraintTop_toTopOf="parent" />
   ```
2. In your activity or fragment you can call 

   - If you want back button set **true** in hasBack param. _By default hasBack is **false**_)
   ###### Example:
   ```
   appbar_main.setup(activity = this, true)
   ```
   - If you want set tilte on Toolbar you can call
   ###### Example:
   ```
   appbar_main.setTitle(getString(R.string.your_title))
   ```
   
### RecyclerBuilder
   If you prefer setup adapter you can call
   1. Create your adapter
   ###### Example:
        private lateinit var adapter: MyAdapter
         
        adapter = MyAdapter(itemList)
        
   2. Create RecyclerBuilder for setup adapter
   ###### Example:
          
        RecyclerBuilder(context = requireContext(), recyclerView = recycler_list, spaceCount = spaceNumber)
                             .builder()
                             .build(adapter)
   
   ###### Full Example:
         private lateinit var adapter: MyAdapter
         
         private fun renderList() {
               adapter = MyAdapter(itemList)
               RecyclerBuilder(context = requireContext(), recyclerView = recycler_list, spaceCount = spaceNumber)
                       .builder()
                       .build(adapter)
         }
      

