# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Tomer\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class com.tmd.library.Message
-keepnames class com.tmd.library.Book
-keepnames class com.tmd.library.User
-keepnames public class com.tmd.library.Message

-keep class com.tmd.library.BookView{

  public void StringToBitMap(java.lang.string);
}
-keep class com.tmd.library.Book_Picture{
         private String Pic;
}