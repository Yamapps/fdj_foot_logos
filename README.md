Foot Logos - FDJ Test app
=========================

# Architecture

### Design Pattern

This app was built using the MVVM design pattern. It has mulitple advantages, separation of concerns and maintainability, ease of unit testing but also modularity, that made easier to have a architecture with multiple modules.

### Modularization

The app is is implemented using multiple module. Each module is responsible for a specific task

- `app` : contains activities and FootLogosApplication class
- `data` : contains all the classes responsible for fetching or processing the data used in the features.
- `feature.teamList` : home screen that displays the team search by league screen
- `network` : Implements the retrofit API in charge of fetching the data from the remote server
- `common` : Utility classes used by every other modules

# External libraries

### Dagger Hilt
Dependency injection, maintained by Google. We chose Hilt rather than Koin because it throw exception during compilation time when a problem occurs rather that at runtime like Koin.

### Coil
Small, fast and optimized image loading library implementing coroutines. It is also useful because it stores the downloaded images locally.

### Timber
Logging library, makes it easy to log only on debug mode

### Retrofit
Http Client to retrieve data from remote.

### MockK
Useful tool to mock objects for unit test. Easy to use because it provides DSL to mock behaviors.

### Turbine
Useful and easy to use library for testing flows.