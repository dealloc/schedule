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