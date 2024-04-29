# Identance

Identance is a intelligible, user-friendly service of user's verification provided as SDK.

- has different level of verification, including basic, address, enhanced verification and
  corporate (in case of legal entity)
- Easy integration
- Flexible theme customization

## Integration

There are 2 ways how to integrate SDK

#### 1) **Directly download**

Download the latest [AAR](https://github.com/identance/android-sdk), and insert it into libs folder
of app module. Make sure you included libraries from libs folder:

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
* After Generating make sure to copy your new personal access token. You cannot see it again! The
  only option is to generate a new key.

##### Step 2: Store your GitHub — Personal Access Token details

* Create a <strong>github.properties</strong> file within your root Android project and add
  properties:

```groovy
   gpr.usr = GITHUB_USERID
gpr.key = PERSONAL_ACCESS_TOKEN
```

* Make sure you add this file to <strong>.gitignore</strong> for keep the token private
* Replace ```GITHUB_USERID``` with Github User ID and ```PERSONAL_ACCESS_TOKEN``` with the token
  generated in **#Step 1**

> Alternatively you can also add the ```gpr.usr``` and ```gpr.key``` values to your environment variables on you local machine or build server to avoid creating a github properties file

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

def githubProperties = new Properties()
githubProperties.load(new FileInputStream(rootProject.file("github.properties")))

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

		List<Stage> descriptions = Arrays.asList(
				new Stage(StageType.IDENTITY, "short text", "full text"),
				new Stage(StageType.ADDRESS, "short text", "full text"),
				new Stage(StageType.ENHANCED, "short text", "full text")
		);

		VerificationBuilder builder = new VerificationBuilder(this)
				.setUserId("userId")
				.setEndpoint("https://...")
				.addTokenProvider(() -> "Java web token, should be provided by merchant")
				.setUIConfig(new UIConfig(descriptions)); // is required

		VerificationClient client = builder.build();
	}
}
```

SDK contains light and dark themes for supporting night mode. The theme will be switched
automatically depending on settings of Android OS.  
In case you need to set theme manually, use

```java
    builder.setNightMode();
```

or

```java
    builder.setLightMode();
```

### Localization

SDK supports different languages (English, Russian, at this moment). In case you don't want to use
some of them, limit language resources in your application. Update your **build.gradle**

```groovy
android {
    //...

    defaultConfig {
        ///...
        resConfig "en"
    }
}
```

**WARNING!**
Because of SDK keeps reference of TokenProvider, VerificationClient should be re-initialized right
after a new user has logged in

### Launching of verification process

In place where your want to start verification, put:

```java
VerificationClient.getInstance().start(activity,verificationMode);
```

where ```verificationMode``` could be one of possible values

| Verification modes | description |
|--------------------|--------------|
| ALL_STAGES         | Verification starts from the list of all available stages |
| SINGLE_STAGE       | Verification starts from the passing of the first available stage
| RESUBMIT_STAGE     | Verification starts from the resubmit screen

In case you need to get a result of verification

```java
    VerificationClient.getInstance().startForResult(activity,REQUEST_CODE,verificationMode);
```

Also, add next code to activity/fragment where you start verification from

```java
    public void onActivityResult(int requestCode,int resultCode,Intent data){
		super.onActivityResult(requestCode,resultCode,data);

		if(requestCode==REQUEST_CODE){
		switch(status){
		case VerificationStatus.CANCELED:
		// your code here
		break;
		case VerificationStatus.SUBMITTED:
		HashMap<String, String> stages;
		stages=(HashMap<String, String>)data.getSerializableExtra(VerificationClient.EXTRAS_RESULT_VERIFICATION);
		// your code here
		break;
		case VerificationStatus.REJECTED:
		// your code here
		break;
default:
		// your code here
		break;
		}
		}

		}
```

| Verification status         | all_stages mode | single_stage mode | resubmit_stage mode
|--------------|--------------|--------------|
| CANCELED| User exited without passing any stage | User exited without passing any stage
| SUBMITTED| User passed some stage with any result or reseted previous results | User passed verification successfully
| REJECTED| User's dossier were completely rejected after passing stage | user failed to pass a stage

In case verification were finished with status ```SUBMITTED```, you also could obtain a results of
verification in ```HashMap<String, String>```, where key is ```stage_id``` and value
is ```stage_status```

**WARNING!**
Do not use results from SDK in all_stages mode for building business logic in your application in
case when manual processing of verification is enabled. For this case the best decision is to
implement notifications of results on backend side and send them to your mobile application

#### Stage types

| type         |
|--------------|
| IDENTITY | 
| ADDRESS |
| ENHANCED|
| CORPORATE |

#### Stage statuses

| Stage status         | Description
|--------------|--------------|
| draft| new stage, user do not provide any information so far
| pending| there is no final decision on application
| correction| user has to provide updated information to the initial application
| accepted| final decision, user has successfully passed verification
| refused| final decision, user has not passed verification
| next_stage_needed| user has to send subsequent stage of verification
| current_stage_needed| user is obliged by CO to provide information on specific stage of verification
| inpogress| user has submitted stage and it is still processing on backend(not saved to db)
