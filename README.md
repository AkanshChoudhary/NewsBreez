# NewsBreez!

## Quick Overview

This is a news application which has functionality to load live trending news data from **newsapi.org** and show it to users in **Breaking News Section** . The users also have functionality to **Search News** on **newsapi.org** with a keyword eg: Minister. 
All the shown news items in Search News and Breaking News have 2 dedicated buttons to **Read** or **Save** the article. Once clicked on a news item, a **WebView** is opened showing the complete article details. Lets look at everything in detail below.


## Architecture 

This application uses **MVVM** Architecture with **Retrofit** and **Room SQLite Database** for having a clean and scalable code base and folder structure. The Key elements of the architecture are :
### 1. UI 
This contains all the UI elements and fragments defining all the Recycler Views and button functionalities. It also contains the **NewsViewModel** class which acts as the main source of reference for interacting with the backend.

### 2. Adapters
This contains **RecyclerViewAdapter** which handles all layout inflations, interactions and insertion of Article Data to elements for display. It contains **DiffUtil** which helps us update our RecyclerView only by changing new elements and not refreshing the whole list for better optimization.

### 3. API
Here the **GET calls are mapped to functions** and **RetrofitInstance** is created which will be acting as the medium between business logic and URL calling and getting response. 

### 4. Data Access Object (DAO)
Here the **Room Database Classes** and their supporting DAO functionalities like insert/update/delete are defined. The app has 2 Room databases **SavedArticleDatabase** and **CachedArticleDatabase**.

### 5. Model
This contains all our Model classes which define structure for the News API response and help in converting JSON response directly to class objects. The **Article** class also acts as an Entity/Table for the databases mentioned above.

### 6. Repository
 This helps our ViewModel to connect with our Database DAO functions using **Coroutines/ Suspend func**. Acts as medium between ViewModel and DAO by providing relevant functions
 
### 7. Util
Contains basic constant Strings and Resource Handling class for Loading, Success, Error States.


## Positives of the application

* Complete MVVM Architecture.
* Using Room Database for storing Saved Articles.
* Used latest Retrofit for API request handling.
* All loaded data is cached in case the next time app is opened and internet is absent.
* Pagination support in both Breaking News and Search News.
* Highly Scalable, Readable.
* Searching functionality in Saved News based on title.
* Works in both Light and Dark Modes as per phone's settings.
* Using CoRoutines for async tasks on different thread rather than on Main Thread.

## Challenges Faced
* Since **newsapi.org** does not provide full news content in free version making a news detail screen was not possible as relevant content was trimmed. So to present full information to user WebView was implemented which opens the article URL in the fragment as a webpage.
* Rearranging news in Ascending order of dates creates issues with pagination since **newsapi.org** provides data in Descending order of dates, so this functionality was not included.  

### App Photos
<img src="https://github.com/AkanshChoudhary/NewsBreez/blob/master/images/breaking_news.jpeg" width="400" height="800" /> <img src="https://github.com/AkanshChoudhary/NewsBreez/blob/master/images/breaking_news_save.jpeg" width="400" height="800" />
<img src="https://github.com/AkanshChoudhary/NewsBreez/blob/master/images/web_view.jpeg" width="400" height="800" /> <img src="https://github.com/AkanshChoudhary/NewsBreez/blob/master/images/saved_news.jpeg" width="400" height="800" /> 
<img src="https://github.com/AkanshChoudhary/NewsBreez/blob/master/images/save_news_search.jpeg" width="400" height="800" /> <img src="https://github.com/AkanshChoudhary/NewsBreez/blob/master/images/search_news.jpeg" width="400" height="800" /> 
<img src="https://github.com/AkanshChoudhary/NewsBreez/blob/master/images/search_news_save.jpeg" width="400" height="800" />

