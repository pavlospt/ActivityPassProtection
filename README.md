ActivityPassProtection
======================

A library that password protects Activities


![Activity Pass Protection](/Screenshots/Screenshot_1.png)

Example
=======
```java
    PasswordProtectionDialog mDialog = new PasswordProtectionDialog(this);
    mDialog.initializeDialog();
```

The first time that you call the dialog it will ask you for a password! 

Although you can manually set it and save it to the SharedPreferences by using the keys from Constants! The library checks if a password has been set and does not ask for one in such case! 

I purposely left the reset and password set functionality out of the Library because every developer wants to do it in his/her way! 

So just use the shared preferences keys and implement your own functionality that fits your needs best :) 

There are a lot of customization for the dialog documented by Javadoc! 

How To Use
==========

It will be on Maven Central in a couple of hours! I will update as soon as it is up! 


Credits
=======
Author : Pavlos-Petros Tournaris (p.tournaris@gmail.com)

Google+ : [+Pavlos-Petros Tournaris](https://plus.google.com/u/0/+PavlosPetrosTournaris/)

Facebook : [Pavlos-Petros Tournaris](https://www.facebook.com/pavlospt)

LinkedIn : [Pavlos-Petros Tournaris](https://www.linkedin.com/pub/pavlos-petros-tournaris/44/abb/218)

License
=======

    Copyright 2014 Pavlos-Petros Tournaris

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
