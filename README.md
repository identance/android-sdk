# Identance

Identance is a intelligible, user-friendly service of user's verification provided as SDK.

- has different level of verification, including basic, address, enhanced verification and corporate (in case of legal entity)
- Easy integration
- Flexible theme customization

## Integration

### Integration via Maven
[Android Maven Plugin v4.0.0](https://simpligility.github.io/android-maven-plugin/) or newer is required.

Open your pom.xml file and add these directives as appropriate:

```xml
<repositories>
    <repository>
        <id>Identance</id>
        <url>https://github.com/...</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.identance</groupId>
        <artifactId>sdk</artifactId>
        <version>latest</version>
</dependency>
```

### Android studio integration

There are 2 ways how to integrate SDK
1) **Directly download** the latest [AAR](https://github.com/...), and insert it into libs folder of app module. Make sure you included libraries from libs folder:

```groovy
dependencies {
    implementation fileTree(include: ['*.aar'], dir: 'libs')
}
```

2) Via **Gradle**

```groovy
android {
    //...

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
maven { url 'http://maven.microblink.com' }
maven {
    url 'https://github.com/...'
}

dependencies {
    api 'com.identance:sdk:{latest}'
}
```

### Initialization of verification client

Add next code to your App class:

```java
public class MyApplication extends SomeOtherApplication {
  
    @Override
    public void onCreate() {
        super.onCreate();
		
        VerificationBuilder builder = new VerificationBuilder(this)
            .setUserId("userId")
            .setEndpoint("https://...")
            .setNightMode()  
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
SDK supports different languages. In case you don't want to use some of them, limit language resources in your application.  Update your **build.gradle**
```groovy
android {
    //...

    defaultConfig {
        ///...
        resConfig "en", "fr"
    }
}
```

### Launching of verification process

In place where your want to start verification, put:

```java
VerificationClient.getInstance().start(activity, REQUEST_CODE);
```
or
```java
VerificationClient.getInstance().start(fragment, REQUEST_CODE);
```

### Interrupting of verification process:

```java
VerificationClient.getInstance().interrupt(context);
```

Caution!!!
----
Because of SDK keeps reference of TokenProvider, VerificationClient should be re-initialized right after a new user has logged in
