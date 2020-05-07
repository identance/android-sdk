# Identance

Identance is a intelligible, user-friendly service of user's verification provided as SDK.

- has different level of verification, including basic, address, enhanced verification and corporate (in case of legal entity)
- Easy integration
- Flexible theme customization

## Integration

There are 2 ways how to integrate SDK
#### 1) **Directly download**
Download the latest [AAR](https://github.com/identance/android-sdk), and insert it into libs folder of app module. Make sure you included libraries from libs folder:
```groovy
dependencies {
    implementation fileTree(include: ['*.aar'], dir: 'libs')
}
```

#### 2) Via **Gradle**

##### Step 1 : Generate a Personal Access Token for GitHub
* Inside you GitHub account:
* Settings -> Developer Settings -> Personal Access Tokens -> Generate new token
* Make sure you select the following scopes (“ read:packages”) and Generate a token
* After Generating make sure to copy your new personal access token. You cannot see it again! The only option is to generate a new key.

##### Step 2: Store your GitHub — Personal Access Token details
* Create a <strong>github.properties</strong> file within your root Android project and add properties:
```groovy
   gpr.usr=GITHUB_USERID  
   gpr.key=PERSONAL_ACCESS_TOKEN
```

* Make sure you add this file to <strong>.gitignore</strong> for keep the token private
* Replace ```GITHUB_USERID``` with Github User ID and ```PERSONAL_ACCESS_TOKEN``` with the token generated in **#Step 1**

> Alternatively you can also add the ```GITHUB_USERID``` and ```PERSONAL_ACCESS_TOKEN``` values to your environment variables on you local machine or build server to avoid creating a github properties file

##### Step 3 : Update build.gradle inside the application module
* Add the following code to build.gradle inside the app module

```groovy
android {
    //...

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

def githubProperties = new Properties() githubProperties.load(new FileInputStream(rootProject.file("github.properties")))

repositories {
    maven {
        url 'https://maven.pkg.github.com/identance/android-sdk'

        credentials {
            username = githubProperties['gpr.usr'] ?: System.getenv("GPR_USER")
            password = githubProperties['gpr.key'] ?: System.getenv("GPR_API_KEY")
        }
    }
}

dependencies {
    implementation 'com.identance:sdk:2.0.7'
}
```

### Initialization of verification client

Add next code to your App class:

```java
public class MyApplication extends Application {
  
    @Override
    public void onCreate() {
        super.onCreate();
		
        VerificationBuilder builder = new VerificationBuilder(this)
            .setUserId("userId")
            .setEndpoint("https://...")
            .addTokenProvider(() -> "Java web token, should be provided by merchant");
            
        VerificationClient client = builder.build();
    }
}
```
SDK contains light and dark themes for supporting night mode. The theme will be switched automatically depending on settings of Android OS.  
In case you need to set theme manually, use

```java
    builder.setNightMode();
```
or

```java
    builder.setLightMode();
```

### Localization
SDK supports different languages (English, Russian, at this moment). In case you don't want to use some of them, limit language resources in your application.  Update your **build.gradle**
```groovy
android {
    //...

    defaultConfig {
        ///...
        resConfig "en"
    }
}
```

### Launching of verification process

In place where your want to start verification, put:

```java
VerificationClient.getInstance().start(activity, REQUEST_CODE, verificationMode);

//or
VerificationClient.getInstance().start(activity, REQUEST_CODE, verificationMode);
```

where ```verificationMode``` could be one of possible values

| Verification modes         | description |
|--------------|--------------|
| ALL_STAGES   | Verification starts from the list of all available stages |
| SINGLE_STAGE | Verification starts from the passing of the first available stage


### Interrupting of verification process:

```java
VerificationClient.getInstance().interrupt(context);
```

Caution!!!
----
Because of SDK keeps reference of TokenProvider, VerificationClient should be re-initialized right after a new user has logged in
