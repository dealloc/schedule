### Dagger inject methods
-keep class be.dealloc.schedule.contracts.* {
    public void inject(**);
}

### Retrolambda
-dontwarn java.lang.invoke.*

### greenDAO 3
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
}

-keep class **$Properties

# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use RxJava:
-dontwarn rx.**

# Biweekly
-dontwarn com.fasterxml.jackson.**
-dontwarn biweekly.io.json.JCalModule

# Remove log calls
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
}
