![Build](https://img.shields.io/badge/Build-Automated-2980b9.svg?style=for-the-badge)
![Latest Version](https://img.shields.io/badge/Latest_Version-v1.0.1-27ae60.svg?style=for-the-badge)
![License](https://img.shields.io/badge/License-Apache_2.0-e74c3c.svg?style=for-the-badge)</br>
![Java CI with Gradle](https://github.com/myConsciousness/zenna-db/workflows/Java%20CI%20with%20Gradle/badge.svg)

# Zenna DB

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**

- [What is it?](#what-is-it)
- [Benefits](#benefits)
- [How To Use](#how-to-use)
  - [1. Add the dependencies](#1-add-the-dependencies)
- [License](#license)
- [More Information](#more-information)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## What is it?

Preparing...

## Benefits

## How To Use

### 1. Add the dependencies

> **_Note:_**</br>
> Replace version you want to use. Check the latest [Packages](https://github.com/myConsciousness/zenna-db/packages).</br>
> Please contact me for a token to download the package.

**_Maven_**

```xml
<dependency>
  <groupId>org.thinkit.zenna</groupId>
  <artifactId>zenna-db</artifactId>
  <version>v1.0.1</version>
</dependency>

<servers>
  <server>
    <id>github</id>
    <username>myConsciousness</username>
    <password>xxxxxxxxxxxxxxxxxx</password>
  </server>
</servers>
```

**_Gradle_**

```gradle
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/myConsciousness/zenna-db")
        credentials {
          username = "myConsciousness"
          password = "xxxxxxxxxxxxxxxxxx"
        }
    }
}

dependencies {
    implementation 'org.thinkit.zenna:zenna-db:v1.0.1'
}
```

## License

```license
Copyright 2021 Kato Shinya.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License
is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
or implied. See the License for the specific language governing permissions and limitations under
the License.
```

## More Information

`Zenna DB` was designed and implemented by Kato Shinya, who works as a freelance developer.

Regardless of the means or content of communication, I would love to hear from you if you have any questions or concerns. I do not check my email box very often so a response may be delayed, anyway thank you for your interest!

- [Creator Profile](https://github.com/myConsciousness)
- [Creator Website](https://myconsciousness.github.io/)
- [License](https://github.com/myConsciousness/zenna-db/blob/master/LICENSE)
- [Release Note](https://github.com/myConsciousness/zenna-db/releases)
- [Package](https://github.com/myConsciousness/zenna-db/packages)
- [File a Bug](https://github.com/myConsciousness/zenna-db/issues)
- [Reference](https://myconsciousness.github.io/zenna-db/)
